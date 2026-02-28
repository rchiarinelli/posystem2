package br.com.brainyit.posystem2.po.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.brainyit.posystem2.domain.POStatus;
import br.com.brainyit.posystem2.domain.POPriority;
import br.com.brainyit.posystem2.domain.ProductionOrder;
import br.com.brainyit.posystem2.domain.ProductionOrderWorkAllocation;
import br.com.brainyit.posystem2.domain.ProductionOrderWorkAllocation.ProductionOrderWorkAllocationStatus;
import br.com.brainyit.posystem2.domain.ServiceType;
import br.com.brainyit.posystem2.domain.Subscriber;
import br.com.brainyit.posystem2.domain.WorkResource;
import br.com.brainyit.posystem2.domain.WorkResource.WorkResourceStatus;
import br.com.brainyit.posystem2.domain.WorkSchedule;
import br.com.brainyit.posystem2.domain.WorkSchedule.WorkScheduleStatus;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.po.repository.ProductionOrderRepository;
import br.com.brainyit.posystem2.po.repository.ProductionOrderWorkAllocationRepository;
import br.com.brainyit.posystem2.po.repository.WorkResourceRepository;
import br.com.brainyit.posystem2.subscriber.service.SubscriberService;

@ExtendWith(MockitoExtension.class)
class ProductionOrderWorkAllocationServicesTest {

    @InjectMocks
    private ProductionOrderWorkAllocationServices productionOrderWorkAllocationServices;

    @Mock
    private ProductionOrderWorkAllocationRepository productionOrderWorkAllocationRepository;

    @Mock
    private SubscriberService subscriberService;

    @Mock
    private WorkResourceRepository workResourceRepository;

    @Mock
    private ProductionOrderRepository productionOrderRepository;

    public static Stream<Arguments> createWorkAllocationTestData() {
        return Stream.of(
                Arguments.of(LocalDateTime.of(2025, 1, 3, 8, 0), LocalDateTime.of(2025, 1, 3, 10, 30), 2.5, 2.5),
                Arguments.of(LocalDateTime.of(2025, 1, 3, 8, 0), LocalDateTime.of(2025, 1, 3, 17, 00), 9.5, 9),
                Arguments.of(LocalDateTime.of(2025, 1, 3, 8, 0), LocalDateTime.of(2025, 1, 3, 17, 00), 9.0, 9));
    }

    @Test
    void test_validateInputs_whenSubscriberIdIsNull_throwsBusinessException() {
        assertThrows(BusinessException.class,
                () -> productionOrderWorkAllocationServices.allocateProductionOrder(null, LocalDateTime.now()));
    }

    @Test
    void test_validateInputs_whenFromDateTimeIsNull_throwsBusinessException() {
        assertThrows(BusinessException.class,
                () -> productionOrderWorkAllocationServices.allocateProductionOrder("123", null));
    }

    @Test
    void test_validateInputs_whenFromDateTimeIsInTheFuture_throwsBusinessException() {
        assertThrows(BusinessException.class, () -> productionOrderWorkAllocationServices.allocateProductionOrder("123",
                LocalDateTime.now().minusDays(1)));
    }

    @Test
    void test_isWorkDay_whenWeekStartDayIsLessThanWeekEndDay_returnsTrue() {
        assertThat(productionOrderWorkAllocationServices.isWorkDay(LocalDateTime.of(2025, 1, 1, 0, 0),
                WorkSchedule.builder().weekStartDay(1).weekEndDay(5).build()), is(true));
    }

    @Test
    void test_isWorkDay_whenWorkScheduleIsWeekend_startMondayAndEndSaturday_returnsFalse() {
        assertThat(productionOrderWorkAllocationServices.isWorkDay(LocalDateTime.of(2025, 1, 5, 0, 0),
                WorkSchedule.builder().weekStartDay(1).weekEndDay(6).build()), is(false));
    }

    @Test
    void test_isWorkDay_whenWorkScheduleIsWeekend_startSaturdayAndEndMonday_returnsTrue() {
        assertThat(productionOrderWorkAllocationServices.isWorkDay(LocalDateTime.of(2025, 1, 6, 0, 0),
                WorkSchedule.builder().weekStartDay(6).weekEndDay(1).build()), is(true));
    }

    @Test
    void test_getNextWorkDay_whenWorkScheduleIsWeekend_startMondayAndEndSaturday_returnsNextMonday() {
        assertThat(productionOrderWorkAllocationServices.getNextWorkDay(LocalDateTime.of(2025, 1, 5, 0, 0),
                WorkSchedule.builder().weekStartDay(1).weekEndDay(6).build()), is(LocalDateTime.of(2025, 1, 6, 0, 0)));
    }

    @Test
    void test_getNextWorkDay_whenWorkScheduleIsWeekend_startSaturdayAndEndMonday_returnsNextMonday() {
        assertThat(productionOrderWorkAllocationServices.getNextWorkDay(LocalDateTime.of(2025, 1, 7, 0, 0),
                WorkSchedule.builder().weekStartDay(6).weekEndDay(1).build()), is(LocalDateTime.of(2025, 1, 11, 0, 0)));
    }

    @ParameterizedTest
    @MethodSource("createWorkAllocationTestData")
    void test_createWorkAllocation_parameters_returnsWorkAllocationWithCorrectEndDateTime(
            final LocalDateTime allocationDate, final LocalDateTime expectedEndDateTime, final double remainingHours,
            final double expectedHoursPerDay) {

        final WorkResource workResource = WorkResource.builder().id(1L)
                .workSchedule(WorkSchedule.builder().weekStartDay(1).weekEndDay(5).startTime(LocalTime.of(8, 0))
                        .endTime(LocalTime.of(17, 0))
                        .shiftDurationInMinutes((short) 480).build())
                .build();
        final Subscriber subscriber = Subscriber.builder().id(1L).build();
        final ProductionOrder productionOrder = ProductionOrder.builder().subscriber(subscriber).id(1L).build();

        assertThat(productionOrderWorkAllocationServices.createWorkAllocation(subscriber,
                productionOrder,
                workResource,
                allocationDate,
                remainingHours),
                is(ProductionOrderWorkAllocation.builder()
                        .startDateTime(allocationDate)
                        .subscriber(subscriber)
                        .productionOrder(productionOrder)
                        .workResource(workResource)
                        .endDateTime(expectedEndDateTime)
                        .hoursPerDay(expectedHoursPerDay)
                        .status(ProductionOrderWorkAllocationStatus.PENDING).build()));
    }

    @ParameterizedTest
    @MethodSource("checkForWorkAllocationConflictsTestData")
    void test_checkForWorkAllocationConflicts_whenThereAreConflicts_returnsConflicts(
            final List<ProductionOrderWorkAllocation> workAllocations,
            final ProductionOrderWorkAllocation conflictingWorkAllocation,
            final List<ProductionOrderWorkAllocation> expectedConflicts) {
        assertThat(productionOrderWorkAllocationServices.checkForWorkAllocationConflicts(workAllocations,
                conflictingWorkAllocation), is(expectedConflicts));
    }

    public static Stream<Arguments> checkForWorkAllocationConflictsTestData() {
        final WorkResource workResource = WorkResource.builder().id(1L).build();
        return Stream.of(
                Arguments.of(List.of(),
                        ProductionOrderWorkAllocation.builder().startDateTime(LocalDateTime.of(2025, 1, 3, 8, 0))
                                .endDateTime(LocalDateTime.of(2025, 1, 3, 10, 30)).workResource(workResource).build(),
                        List.of()),
                Arguments.of(
                        List.of(ProductionOrderWorkAllocation.builder()
                                .startDateTime(LocalDateTime.of(2025, 1, 3, 8, 0))
                                .endDateTime(LocalDateTime.of(2025, 1, 3, 10, 30)).workResource(workResource).build()),
                        ProductionOrderWorkAllocation.builder().startDateTime(LocalDateTime.of(2025, 1, 3, 10, 00))
                                .endDateTime(LocalDateTime.of(2025, 1, 3, 12, 00)).workResource(workResource).build(),
                        List.of(ProductionOrderWorkAllocation.builder()
                                .startDateTime(LocalDateTime.of(2025, 1, 3, 8, 00))
                                .endDateTime(LocalDateTime.of(2025, 1, 3, 10, 30)).workResource(workResource)
                                .build())));
    }

    // ==================== allocateProductionOrder() Tests ====================

    @Test
    void test_allocateProductionOrder_whenNoReadyProductionOrders_throwsBusinessException() {
        // Given
        final String subscriberId = "123";
        final LocalDateTime fromDateTime = LocalDateTime.now().plusDays(1);
        final Subscriber subscriber = Subscriber.builder().id(1L).build();

        when(subscriberService.getSubscriber(subscriberId)).thenReturn(subscriber);
        when(productionOrderRepository.findBySubscriberIdAndInStatuses(anyLong(), any(POStatus.class)))
                .thenReturn(new ArrayList<>());

        // When & Then
        assertThrows(BusinessException.class,
                () -> productionOrderWorkAllocationServices.allocateProductionOrder(subscriberId, fromDateTime));
    }

    @Test
    void test_allocateProductionOrder_whenNoAvailableResources_throwsBusinessException() {
        // Given
        final String subscriberId = "123";
        final LocalDateTime fromDateTime = LocalDateTime.now().plusDays(1);
        final Subscriber subscriber = Subscriber.builder().id(1L).build();
        final ProductionOrder productionOrder = createTestProductionOrder("PO001", ServiceType.MACHINE, 8.0);

        when(subscriberService.getSubscriber(subscriberId)).thenReturn(subscriber);
        when(productionOrderRepository.findBySubscriberIdAndInStatuses(anyLong(), any(POStatus.class)))
                .thenReturn(List.of(productionOrder));
        when(workResourceRepository.retrieveFreeWorkResourceByDate(anyLong(), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());

        // When & Then
        assertThrows(BusinessException.class, () -> productionOrderWorkAllocationServices
                .allocateProductionOrder(subscriberId, fromDateTime));
    }

    @Test
    void test_allocateProductionOrder_whenSingleProductionOrderAndResource_createsAllocation() {
        // Given
        final String subscriberId = "123";
        final LocalDateTime fromDateTime = LocalDateTime.now().plusDays(1);
        final Subscriber subscriber = Subscriber.builder().id(1L).build();
        final ProductionOrder productionOrder = createTestProductionOrder("PO001", ServiceType.MACHINE, 8.0);
        final WorkResource workResource = createTestWorkResource(ServiceType.MACHINE);
        final List<ProductionOrderWorkAllocation> savedAllocations = new ArrayList<>();

        when(subscriberService.getSubscriber(subscriberId)).thenReturn(subscriber);
        when(productionOrderRepository.findBySubscriberIdAndInStatuses(anyLong(), any(POStatus.class)))
                .thenReturn(List.of(productionOrder));
        when(workResourceRepository.retrieveFreeWorkResourceByDate(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(workResource));
        when(productionOrderWorkAllocationRepository.saveAll(any()))
                .thenAnswer(invocation -> {
                    List<ProductionOrderWorkAllocation> allocations = invocation.getArgument(0);
                    savedAllocations.addAll(allocations);
                    return allocations;
                });

        // When
        final List<ProductionOrderWorkAllocation> result = productionOrderWorkAllocationServices
                .allocateProductionOrder(subscriberId, fromDateTime);

        // Then
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getProductionOrder(), is(productionOrder));
        assertThat(result.get(0).getWorkResource(), is(workResource));
        assertThat(result.get(0).getSubscriber(), is(subscriber));
        assertThat(result.get(0).getStatus(), is(ProductionOrderWorkAllocationStatus.PENDING));
    }

    @Test
    void test_allocateProductionOrder_whenMultipleProductionOrders_allocatesByPriority() {
        // Given
        final String subscriberId = "123";
        final LocalDateTime fromDateTime = LocalDateTime.now().plusDays(1);
        final Subscriber subscriber = Subscriber.builder().id(1L).build();

        final ProductionOrder highPriorityPO = createTestProductionOrder("PO001", ServiceType.MACHINE, 4.0);
        highPriorityPO.setPriority(POPriority.HIGH);
        highPriorityPO.setPlannedStartDate(LocalDate.now().plusDays(1));

        final ProductionOrder standardPriorityPO = createTestProductionOrder("PO002", ServiceType.MACHINE, 4.0);
        standardPriorityPO.setPriority(POPriority.STANDARD);
        standardPriorityPO.setPlannedStartDate(LocalDate.now().plusDays(2));

        final WorkResource workResource = createTestWorkResource(ServiceType.MACHINE);
        final List<ProductionOrderWorkAllocation> savedAllocations = new ArrayList<>();

        when(subscriberService.getSubscriber(subscriberId)).thenReturn(subscriber);
        when(productionOrderRepository.findBySubscriberIdAndInStatuses(anyLong(), any(POStatus.class)))
                .thenReturn(List.of(standardPriorityPO, highPriorityPO));
        when(workResourceRepository.retrieveFreeWorkResourceByDate(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(workResource));
        when(productionOrderWorkAllocationRepository.saveAll(any()))
                .thenAnswer(invocation -> {
                    List<ProductionOrderWorkAllocation> allocations = invocation.getArgument(0);
                    savedAllocations.addAll(allocations);
                    return allocations;
                });

        // When
        final List<ProductionOrderWorkAllocation> result = productionOrderWorkAllocationServices
                .allocateProductionOrder(subscriberId, fromDateTime);

        // Then
        assertThat(result.size(), is(2));
        // High priority should be allocated first (earlier start time)
        assertThat(result.get(0).getProductionOrder().getPoNumber(), is("PO001"));
        assertThat(result.get(1).getProductionOrder().getPoNumber(), is("PO002"));
    }

    @Test
    void test_allocateProductionOrder_whenInsufficientTimeInDay_movesToNextDay() {
        // Given
        final String subscriberId = "123";
        final LocalDateTime fromDateTime = LocalDateTime.now().plusDays(1).withHour(15).withMinute(0); // 3 PM tomorrow
        final Subscriber subscriber = Subscriber.builder().id(1L).build();
        final ProductionOrder productionOrder = createTestProductionOrder("PO001", ServiceType.MACHINE, 8.0);
        final WorkResource workResource = createTestWorkResource(ServiceType.MACHINE);
        final List<ProductionOrderWorkAllocation> savedAllocations = new ArrayList<>();

        when(subscriberService.getSubscriber(subscriberId)).thenReturn(subscriber);
        when(productionOrderRepository.findBySubscriberIdAndInStatuses(anyLong(), any(POStatus.class)))
                .thenReturn(List.of(productionOrder));
        when(workResourceRepository.retrieveFreeWorkResourceByDate(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(workResource));
        when(productionOrderWorkAllocationRepository.saveAll(any()))
                .thenAnswer(invocation -> {
                    List<ProductionOrderWorkAllocation> allocations = invocation.getArgument(0);
                    savedAllocations.addAll(allocations);
                    return allocations;
                });

        // When
        final List<ProductionOrderWorkAllocation> result = productionOrderWorkAllocationServices
                .allocateProductionOrder(subscriberId, fromDateTime);

        // Then
        assertThat(result.size(), is(1));
        // Should start at 8 AM next day (work start time)
        assertThat(result.get(0).getStartDateTime().isAfter(fromDateTime.plusDays(1)), is(true));
        assertThat(result.get(0).getStartDateTime().getHour(), is(8));
    }

    @Test
    void test_allocateProductionOrder_whenConflictingAllocations_resolvesConflicts() {
        // Given
        final String subscriberId = "123";
        final LocalDateTime fromDateTime = LocalDateTime.now().plusDays(1);
        final Subscriber subscriber = Subscriber.builder().id(1L).build();

        final ProductionOrder productionOrder1 = createTestProductionOrder("PO001", ServiceType.MACHINE, 4.0);
        final ProductionOrder productionOrder2 = createTestProductionOrder("PO002", ServiceType.MACHINE, 4.0);
        final WorkResource workResource = createTestWorkResource(ServiceType.MACHINE);
        final List<ProductionOrderWorkAllocation> savedAllocations = new ArrayList<>();

        when(subscriberService.getSubscriber(subscriberId)).thenReturn(subscriber);
        when(productionOrderRepository.findBySubscriberIdAndInStatuses(anyLong(), any(POStatus.class)))
                .thenReturn(List.of(productionOrder1, productionOrder2));
        when(workResourceRepository.retrieveFreeWorkResourceByDate(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(workResource));
        when(productionOrderWorkAllocationRepository.saveAll(any()))
                .thenAnswer(invocation -> {
                    List<ProductionOrderWorkAllocation> allocations = invocation.getArgument(0);
                    savedAllocations.addAll(allocations);
                    return allocations;
                });

        // When
        final List<ProductionOrderWorkAllocation> result = productionOrderWorkAllocationServices
                .allocateProductionOrder(subscriberId, fromDateTime);

        // Then
        assertThat(result.size(), is(2));
        // Second allocation should start after first one ends (with 30min buffer)
        final LocalDateTime firstEnd = result.get(0).getEndDateTime();
        final LocalDateTime secondStart = result.get(1).getStartDateTime();
        assertThat(secondStart.isAfter(firstEnd), is(true));
    }

    @Test
    void test_allocateProductionOrder_whenMultiDayAllocation_createsMultipleAllocations() {
        // Given
        final String subscriberId = "123";
        final LocalDateTime fromDateTime = LocalDateTime.now().plusDays(1);
        final Subscriber subscriber = Subscriber.builder().id(1L).build();
        final ProductionOrder productionOrder = createTestProductionOrder("PO001", ServiceType.MACHINE, 16.0); // 2 days
        final WorkResource workResource = createTestWorkResource(ServiceType.MACHINE);
        final List<ProductionOrderWorkAllocation> savedAllocations = new ArrayList<>();

        when(subscriberService.getSubscriber(subscriberId)).thenReturn(subscriber);
        when(productionOrderRepository.findBySubscriberIdAndInStatuses(anyLong(), any(POStatus.class)))
                .thenReturn(List.of(productionOrder));
        when(workResourceRepository.retrieveFreeWorkResourceByDate(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(workResource));
        when(productionOrderWorkAllocationRepository.saveAll(any()))
                .thenAnswer(invocation -> {
                    List<ProductionOrderWorkAllocation> allocations = invocation.getArgument(0);
                    savedAllocations.addAll(allocations);
                    return allocations;
                });

        // When
        final List<ProductionOrderWorkAllocation> result = productionOrderWorkAllocationServices
                .allocateProductionOrder(subscriberId, fromDateTime);

        // Then
        assertThat(result.size(), is(2)); // Should create 2 allocations for 2 days
        assertThat(result.get(0).getStartDateTime().isAfter(fromDateTime), is(true));
        assertThat(result.get(1).getStartDateTime().isAfter(result.get(0).getStartDateTime()), is(true));
    }

    @Test
    void test_allocateProductionOrder_whenDifferentServiceTypes_allocatesToCorrectResources() {
        // Given
        final String subscriberId = "123";
        final LocalDateTime fromDateTime = LocalDateTime.now().plusDays(1);
        final Subscriber subscriber = Subscriber.builder().id(1L).build();

        final ProductionOrder machinePO = createTestProductionOrder("PO001", ServiceType.MACHINE, 4.0);
        final ProductionOrder humanPO = createTestProductionOrder("PO002", ServiceType.HUMAN, 4.0);

        final WorkResource machineResource = createTestWorkResource(ServiceType.MACHINE);
        final WorkResource humanResource = createTestWorkResource(ServiceType.HUMAN);
        final List<ProductionOrderWorkAllocation> savedAllocations = new ArrayList<>();

        when(subscriberService.getSubscriber(subscriberId)).thenReturn(subscriber);
        when(productionOrderRepository.findBySubscriberIdAndInStatuses(anyLong(), any(POStatus.class)))
                .thenReturn(List.of(machinePO, humanPO));
        when(workResourceRepository.retrieveFreeWorkResourceByDate(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(machineResource, humanResource));
        when(productionOrderWorkAllocationRepository.saveAll(any()))
                .thenAnswer(invocation -> {
                    List<ProductionOrderWorkAllocation> allocations = invocation.getArgument(0);
                    savedAllocations.addAll(allocations);
                    return allocations;
                });

        // When
        final List<ProductionOrderWorkAllocation> result = productionOrderWorkAllocationServices
                .allocateProductionOrder(subscriberId, fromDateTime);

        // Then
        assertThat(result.size(), is(2));
        // Each PO should be allocated to its matching service type resource
        assertThat(result.stream().anyMatch(wa -> wa.getProductionOrder().getServiceType() == ServiceType.MACHINE &&
                wa.getWorkResource().getType() == ServiceType.MACHINE), is(true));
        assertThat(result.stream().anyMatch(wa -> wa.getProductionOrder().getServiceType() == ServiceType.HUMAN &&
                wa.getWorkResource().getType() == ServiceType.HUMAN), is(true));
    }

    // ==================== Helper Methods ====================

    private ProductionOrder createTestProductionOrder(String poNumber, ServiceType serviceType, Double estimatedTime) {
        return ProductionOrder.builder()
                .poNumber(poNumber)
                .serviceType(serviceType)
                .estimatedTime(estimatedTime)
                .status(POStatus.READY_TO_PRODUCE)
                .priority(POPriority.STANDARD)
                .plannedStartDate(LocalDate.now().plusDays(1))
                .openDate(LocalDate.now())
                .build();
    }

    private WorkResource createTestWorkResource(ServiceType serviceType) {
        return WorkResource.builder()
                .id(1L)
                .name("Test Resource")
                .type(serviceType)
                .status(WorkResourceStatus.ACTIVE)
                .setupTimeInMinutes((short) 30)
                .costPerHour(50.0)
                .workSchedule(WorkSchedule.builder()
                        .weekStartDay(2) // Monday
                        .weekEndDay(6) // Friday
                        .startTime(LocalTime.of(8, 0))
                        .endTime(LocalTime.of(17, 0))
                        .shiftDurationInMinutes((short) 480) // 8 hours
                        .status(WorkScheduleStatus.ACTIVE)
                        .build())
                .build();
    }

}
