package br.com.brainyit.posystem2.po.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.brainyit.posystem2.domain.POStatus;
import br.com.brainyit.posystem2.domain.ProductionOrder;
import br.com.brainyit.posystem2.domain.ProductionOrderWorkAllocation;
import br.com.brainyit.posystem2.domain.ProductionOrderWorkAllocation.ProductionOrderWorkAllocationStatus;
import br.com.brainyit.posystem2.domain.ServiceType;
import br.com.brainyit.posystem2.domain.Subscriber;
import br.com.brainyit.posystem2.domain.WorkResource;
import br.com.brainyit.posystem2.domain.WorkSchedule;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.po.repository.ProductionOrderRepository;
import br.com.brainyit.posystem2.po.repository.ProductionOrderWorkAllocationRepository;
import br.com.brainyit.posystem2.po.repository.WorkResourceRepository;
import br.com.brainyit.posystem2.subscriber.service.SubscriberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductionOrderWorkAllocationServices {

    private static final int THRESHOLD_FOR_ALLOCATION_IN_HOURS = 3;

    private static final Comparator<ProductionOrder> PRODUCTION_ALLOCATION_PRIORITY_ORDER_COMPARATOR = Comparator
            .comparing(ProductionOrder::getPlannedStartDate)
            .thenComparing(Comparator.comparing(ProductionOrder::getPriority));

    private final ProductionOrderRepository productionOrderRepository;

    private final SubscriberService subscriberService;

    private final WorkResourceRepository workResourceRepository;

    private final ProductionOrderWorkAllocationRepository productionOrderWorkAllocationRepository;

    @Transactional
    public List<ProductionOrderWorkAllocation> allocateProductionOrder(final String subscriberId,
            final LocalDateTime fromDateTime) {

        // Input validation
        validateInputs(subscriberId, fromDateTime);

        final Subscriber subscriber = subscriberService.getSubscriber(subscriberId);

        final List<ProductionOrder> poReadyToProduce = productionOrderRepository.findBySubscriberIdAndInStatuses(
                subscriber.getId(), POStatus.READY_TO_PRODUCE);

        final Map<ServiceType, List<ProductionOrder>> serviceTypePerProductionOrder = poReadyToProduce.stream()
                .sorted(PRODUCTION_ALLOCATION_PRIORITY_ORDER_COMPARATOR)
                .collect(Collectors.groupingBy(ProductionOrder::getServiceType));

        final Map<ServiceType, List<WorkResource>> freeWorkResources = workResourceRepository
                .retrieveFreeWorkResourceByDate(subscriber.getId(), fromDateTime).stream()
                .filter(workResource -> serviceTypePerProductionOrder.keySet().contains(workResource.getType()))
                .collect(Collectors.groupingBy(WorkResource::getType));

        if (freeWorkResources.isEmpty()) {
            throw new BusinessException(String.format(
                    "There is no available resources to allocate the production order from date %s", fromDateTime));
        }

        final List<ProductionOrderWorkAllocation> workAllocations = new ArrayList<>();

        for (ServiceType serviceType : freeWorkResources.keySet()) {
            final List<ProductionOrder> productionOrders = serviceTypePerProductionOrder.get(serviceType);
            final List<WorkResource> workResources = freeWorkResources.get(serviceType);

            // Allocate each production order to the first available work resource
            LocalDateTime currentAllocationTime = fromDateTime;
            for (final ProductionOrder productionOrder : productionOrders) {
                final WorkResource workResource = workResources.get(0); // Use first available resource

                final int workHoursPerDay = workResource.getWorkSchedule().getShiftDurationInMinutes() / 60;

                LocalDateTime allocationStartDateTime = currentAllocationTime
                        .plusMinutes(workResource.getSetupTimeInMinutes());

                // Check if there's enough time left in the work day
                LocalTime allocationStartTime = allocationStartDateTime.toLocalTime();
                LocalTime workEndTime = workResource.getWorkSchedule().getEndTime();

                if (allocationStartTime.plusHours(THRESHOLD_FOR_ALLOCATION_IN_HOURS).isAfter(workEndTime)) {
                    allocationStartDateTime = getNextWorkDay(allocationStartDateTime, workResource.getWorkSchedule())
                            .withHour(workResource.getWorkSchedule().getStartTime().getHour())
                            .withMinute(workResource.getWorkSchedule().getStartTime().getMinute())
                            .withSecond(0)
                            .withNano(0);
                }

                final double totalEstimatedTime = productionOrder.getEstimatedTime();

                final int todayDaysNeeded = (int) Math.ceil(totalEstimatedTime / workHoursPerDay);

                double remainingHours = totalEstimatedTime;

                LocalDateTime allocationDateTime = allocationStartDateTime;
                for (int day = 0; day < todayDaysNeeded; day++) {

                    ProductionOrderWorkAllocation workAllocation = createWorkAllocation(subscriber, productionOrder,
                            workResource,
                            allocationDateTime, remainingHours);

                    final List<ProductionOrderWorkAllocation> conflictingWorkAllocations = checkForWorkAllocationConflicts(
                            workAllocations, workAllocation);

                    if (!conflictingWorkAllocations.isEmpty()) {
                        // Get the latest end time from conflicting allocations
                        LocalDateTime latestEndTime = conflictingWorkAllocations.stream()
                                .max(Comparator.comparing(ProductionOrderWorkAllocation::getEndDateTime))
                                .get().getEndDateTime();

                        // Add 30 minutes buffer and reschedule
                        allocationDateTime = latestEndTime.plusMinutes(30);

                        workAllocation = createWorkAllocation(subscriber, productionOrder, workResource,
                                allocationDateTime, remainingHours);
                    }

                    workAllocations.add(workAllocation);

                    remainingHours = remainingHours - workAllocation.getHoursPerDay();

                    allocationDateTime = getNextWorkDay(allocationDateTime, workResource.getWorkSchedule());
                }

                // Update current allocation time for next production order
                if (!workAllocations.isEmpty()) {
                    currentAllocationTime = workAllocations.get(workAllocations.size() - 1).getEndDateTime();
                }
            }
        }

        return productionOrderWorkAllocationRepository.saveAll(workAllocations);
    }

    List<ProductionOrderWorkAllocation> checkForWorkAllocationConflicts(
            final List<ProductionOrderWorkAllocation> workAllocations,
            final ProductionOrderWorkAllocation workAllocation) {

        return workAllocations.stream()
                .filter(existingWorkAllocation -> workAllocation.getStartDateTime()
                        .isBefore(existingWorkAllocation.getEndDateTime())
                        && workAllocation.getEndDateTime().isAfter(existingWorkAllocation.getStartDateTime())
                        && workAllocation.getWorkResource().equals(existingWorkAllocation.getWorkResource()))
                .limit(1)
                .toList();
    }

    ProductionOrderWorkAllocation createWorkAllocation(final Subscriber subscriber,
            final ProductionOrder productionOrder,
            final WorkResource workResource,
            final LocalDateTime allocationDate,
            final double remainingHours) {

        final int workHoursPerDay = workResource.getWorkSchedule().getShiftDurationInMinutes() / 60;

        final LocalTime allocationDayStartHour = workResource.getWorkSchedule().getStartTime();
        LocalTime allocationDayEndHour = workResource.getWorkSchedule().getEndTime();

        if (remainingHours < workHoursPerDay) {
            double remainingTimeInMinutes = remainingHours * 60;
            allocationDayEndHour = allocationDayStartHour.plusMinutes((long) remainingTimeInMinutes);
        }

        final LocalDateTime allocationDayEnd = allocationDate
                .withHour(allocationDayEndHour.getHour())
                .withMinute(allocationDayEndHour.getMinute())
                .withSecond(0)
                .withNano(0);

        return ProductionOrderWorkAllocation.builder()
                .subscriber(subscriber)
                .productionOrder(productionOrder)
                .workResource(workResource)
                .startDateTime(allocationDate)
                .endDateTime(allocationDayEnd)
                .hoursPerDay(calculateActualHours(allocationDate.toLocalTime(), allocationDayEndHour))
                .status(ProductionOrderWorkAllocationStatus.PENDING)
                .build();
    }

    private double calculateActualHours(LocalTime start, LocalTime end) {
        return Duration.between(start.atDate(LocalDate.now()), end.atDate(LocalDate.now())).toMinutes() / 60.0;
    }

    LocalDateTime getNextWorkDay(final LocalDateTime fromDateTime, final WorkSchedule workSchedule) {
        LocalDateTime nextAvailableDateTime = fromDateTime.plusDays(1);

        while (!isWorkDay(nextAvailableDateTime, workSchedule)) {
            nextAvailableDateTime = nextAvailableDateTime.plusDays(1);
        }

        return nextAvailableDateTime;
    }

    boolean isWorkDay(LocalDateTime dateTime, WorkSchedule workSchedule) {
        int currentDay = dateTime.getDayOfWeek().getValue();
        int weekStart = workSchedule.getWeekStartDay();
        int weekEnd = workSchedule.getWeekEndDay();

        // Check if the current day falls within the work schedule's defined work days
        if (weekStart <= weekEnd) {
            // Normal case: work days are consecutive (e.g., Monday=2 to Friday=6)
            return currentDay >= weekStart && currentDay <= weekEnd;
        } else {
            // Weekend work case: work days span across weekend (e.g., Friday=6 to Monday=2)
            // This means the work schedule includes weekend days
            return currentDay >= weekStart || currentDay <= weekEnd;
        }
    }

    void validateInputs(String subscriberId, LocalDateTime fromDateTime) {
        if (subscriberId == null || subscriberId.trim().isEmpty()) {
            throw new BusinessException("Subscriber ID cannot be null or empty");
        }
        if (fromDateTime == null || fromDateTime.isBefore(LocalDateTime.now())) {
            throw new BusinessException("From date time must be in the future");
        }
    }

}
