package br.com.brainyit.posystem2.billing.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import br.com.brainyit.posystem2.api.BillingConfigurationApiDelegate;
import br.com.brainyit.posystem2.billing.mapper.BillingConfigMapper;
import br.com.brainyit.posystem2.billing.service.BillingConfigService;
import br.com.brainyit.posystem2.domain.BillingConfig;
import br.com.brainyit.posystem2.model.BillingConfigModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class BillingConfigurationApiDelegateImpl implements BillingConfigurationApiDelegate {

    private final BillingConfigService billingConfigService;

    @Override
    public ResponseEntity<List<BillingConfigModel>> subscriberBillingConfigsGet(String subscriber) throws Exception {
        log.debug("Getting billing configurations for subscriber: {}", subscriber);

        try {
            List<BillingConfig> billingConfigs = billingConfigService.getBillingConfigs(subscriber);

            BillingConfigMapper mapper = BillingConfigMapper.builder().build();
            List<BillingConfigModel> billingConfigModels = mapper.toRestModelList(billingConfigs);

            return ResponseEntity.ok(billingConfigModels);
        } catch (Exception e) {
            log.error("Error getting billing configurations for subscriber: {}", subscriber, e);
            throw e;
        }
    }
}