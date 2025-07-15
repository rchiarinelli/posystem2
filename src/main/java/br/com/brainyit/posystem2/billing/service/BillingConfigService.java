package br.com.brainyit.posystem2.billing.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.brainyit.posystem2.billing.repository.BillingConfigRepository;
import br.com.brainyit.posystem2.domain.BillingConfig;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.repository.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class BillingConfigService {

    private final BillingConfigRepository billingConfigRepository;

    private final SubscriberRepository subscriberRepository;

    @Transactional(readOnly = true)
    public List<BillingConfig> getBillingConfigs(String subscriberUuid) {
        log.debug("Getting billing configurations for subscriber: {}", subscriberUuid);

        // Validate subscriber exists
        subscriberRepository.findByUuid(subscriberUuid)
                .orElseThrow(() -> new BusinessException("Subscriber not found"));

        // Return all billing configurations
        return billingConfigRepository.findAll();
    }
}