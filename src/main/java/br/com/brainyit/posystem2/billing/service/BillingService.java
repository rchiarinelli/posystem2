package br.com.brainyit.posystem2.billing.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import br.com.brainyit.posystem2.billing.repository.BillingConfigRepository;
import br.com.brainyit.posystem2.billing.repository.BillingRepository;
import br.com.brainyit.posystem2.domain.Billing;
import br.com.brainyit.posystem2.domain.BillingConfig;
import br.com.brainyit.posystem2.domain.BillingStatus;
import br.com.brainyit.posystem2.domain.Installment;
import br.com.brainyit.posystem2.domain.InstallmentIntervalUnit;
import br.com.brainyit.posystem2.domain.InstallmentStatus;
import br.com.brainyit.posystem2.domain.OrderStatus;
import br.com.brainyit.posystem2.domain.PaymentType;
import br.com.brainyit.posystem2.domain.QBilling;
import br.com.brainyit.posystem2.domain.Subscriber;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.exception.NotFoundException;
import br.com.brainyit.posystem2.order.service.OrderService;
import br.com.brainyit.posystem2.repository.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class BillingService {

    private final BillingRepository billingRepository;

    private final SubscriberRepository subscriberRepository;

    private final BillingConfigRepository billingConfigRepository;

    private final OrderService orderService;

    @Transactional(readOnly = true)
    public List<Billing> getBillings(String subscriberUuid, String searchTerm, List<String> statusFilters) {
        log.debug("Getting billings for subscriber: {}, searchTerm: {}, statusFilters: {}",
                subscriberUuid, searchTerm, statusFilters);

        // Validate subscriber exists
        subscriberRepository.findByUuid(subscriberUuid)
                .orElseThrow(() -> new BusinessException("Subscriber not found"));

        // Parse and validate status filters
        List<BillingStatus> statuses = parseAndValidateStatuses(statusFilters);

        // Execute query based on provided filters

        var billingPredicate = QBilling.billing.subscriber.uuid.eq(subscriberUuid);

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            billingPredicate = billingPredicate
                    .and(QBilling.billing.order.customer.name.containsIgnoreCase(searchTerm.trim())
                            .or(QBilling.billing.order.orderCode.containsIgnoreCase(searchTerm.trim())));
        }

        if (statuses != null && !statuses.isEmpty()) {
            billingPredicate = billingPredicate.and(QBilling.billing.status.in(statuses));
        }

        return StreamSupport.stream(billingRepository.findAll(billingPredicate).spliterator(), false)
                .collect(Collectors.toList());

    }

    private List<BillingStatus> parseAndValidateStatuses(List<String> statusFilters) {
        if (statusFilters == null || statusFilters.isEmpty()) {
            return null;
        }

        return statusFilters.stream()
                .map(String::toUpperCase)
                .map(status -> {
                    try {
                        return BillingStatus.valueOf(status);
                    } catch (IllegalArgumentException e) {
                        String validStatuses = Arrays.toString(BillingStatus.values());
                        throw new BusinessException(
                                String.format("Invalid billing status: %s. Valid statuses are: %s",
                                        status, validStatuses));
                    }
                })
                .toList();
    }

    @Transactional
    public List<Billing> createBillings(String subscriberUuid, List<CreateBillingParams> createBillingParams) {

        if (CollectionUtils.isEmpty(createBillingParams)) {
            throw new BusinessException("Not allowed to create billings with empty data.");
        }

        // Validate subscriber exists
        var subscriber = subscriberRepository.findByUuid(subscriberUuid)
                .orElseThrow(() -> new BusinessException("Subscriber not found"));

        return createBillingParams.stream().map(param -> createBilling(subscriber, param))
                .toList();
    }

    private Billing createBilling(final Subscriber subscriber, final CreateBillingParams createBillingParams) {
        // Validate order exists
        var order = orderService.getOrderByCode(subscriber.getUuid(), createBillingParams.orderCode());

        if (order.getStatus() != OrderStatus.PENDING_BILLING) {
            throw new BusinessException("Order is not in pending billing status");
        }

        // Validate billing config exists
        var billingConfig = billingConfigRepository.findById(createBillingParams.billingConfigId())
                .orElseThrow(() -> new BusinessException("Billing config not found"));

        if (createBillingParams.startDate().isBefore(LocalDate.now())) {
            throw new BusinessException("Start date cannot be in the past");
        }

        var billing = Billing.builder()
                .order(order)
                .billingConfig(billingConfig)
                .startDate(createBillingParams.startDate())
                .calculatedValue(order.getPrice())
                .finalValue(createBillingParams.value())
                .status(BillingStatus.OPEN)
                .subscriber(subscriber)
                .createdAt(LocalDate.now())
                .build();

        billing.setInstallments(generateInstallments(billing, billingConfig));

        return billingRepository.save(billing);
    }

    private List<Installment> generateInstallments(final Billing billing, final BillingConfig billingConfig) {
        final List<Installment> installments = new ArrayList<>();
        if (billingConfig.getPaymentType() == PaymentType.ONE_TIME) {
            installments.add(Installment.builder()
                    .billing(billing)
                    .dueValue(billing.getFinalValue())
                    .dueDate(billing.getStartDate())
                    .status(InstallmentStatus.PENDING)
                    .subscriber(billing.getSubscriber())
                    .build());
        } else {
            final var installmentValue = billing.getFinalValue() / billingConfig.getInstallmentsQuantity();
            var installmentDate = billing.getStartDate();
            for (int i = 0; i < billingConfig.getInstallmentsQuantity(); i++) {
                installments.add(Installment.builder()
                        .billing(billing)
                        .dueValue(installmentValue)
                        .dueDate(installmentDate)
                        .status(InstallmentStatus.PENDING)
                        .subscriber(billing.getSubscriber())
                        .build());
                installmentDate = calculateInstallmentDate(installmentDate, billingConfig);
            }
        }
        return installments;
    }

    private LocalDate calculateInstallmentDate(final LocalDate startDate, final BillingConfig billingConfig) {
        if (billingConfig.getInstallmentIntervalUnit() == InstallmentIntervalUnit.DAY) {
            return startDate.plusDays(billingConfig.getInstallmentsInterval());
        } else if (billingConfig.getInstallmentIntervalUnit() == InstallmentIntervalUnit.WEEK) {
            return startDate.plusWeeks(billingConfig.getInstallmentsInterval());
        } else if (billingConfig.getInstallmentIntervalUnit() == InstallmentIntervalUnit.MONTH) {
            return startDate.plusMonths(billingConfig.getInstallmentsInterval());
        }
        return startDate;
    }

    @Transactional(readOnly = true)
    public Billing fetchBilling(final String subscriberUuid, final String billingUuid) {
        var subscriber = subscriberRepository.findByUuid(subscriberUuid)
                .orElseThrow(() -> new BusinessException("Subscriber not found"));

        return billingRepository.findBySubscriberIdAndUuid(subscriber.getId(), billingUuid)
                .orElseThrow(() -> new NotFoundException("Billing not found"));
    }

    @Transactional(readOnly = true)
    public Installment fetchInstallment(final String subscriberUuid, final String billingUuid,
            final String installmentUuid) {
        var billing = fetchBilling(subscriberUuid, billingUuid);
        return billing.getInstallments().stream()
                .filter(installment -> installment.getUuid().equals(installmentUuid))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Installment not found"));
    }
}