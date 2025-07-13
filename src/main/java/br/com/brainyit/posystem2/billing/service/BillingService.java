package br.com.brainyit.posystem2.billing.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.brainyit.posystem2.billing.repository.BillingRepository;
import br.com.brainyit.posystem2.domain.Billing;
import br.com.brainyit.posystem2.domain.BillingStatus;
import br.com.brainyit.posystem2.domain.QBilling;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.repository.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class BillingService {

    private final BillingRepository billingRepository;
    private final SubscriberRepository subscriberRepository;

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
}