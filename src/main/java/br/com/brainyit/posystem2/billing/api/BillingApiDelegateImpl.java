package br.com.brainyit.posystem2.billing.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import br.com.brainyit.posystem2.api.BillingApiDelegate;
import br.com.brainyit.posystem2.billing.mapper.BillingMapper;
import br.com.brainyit.posystem2.billing.service.BillingService;
import br.com.brainyit.posystem2.domain.Billing;
import br.com.brainyit.posystem2.model.BillingModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class BillingApiDelegateImpl implements BillingApiDelegate {

    private final BillingService billingService;

    @Override
    public ResponseEntity<List<BillingModel>> subscriberBillingsGet(String subscriber, String searchTerm,
            List<String> status) throws Exception {
        log.debug("Getting billings for subscriber: {}, searchTerm: {}, status: {}", subscriber, searchTerm, status);

        try {
            List<Billing> billings = billingService.getBillings(subscriber, searchTerm, status);

            BillingMapper mapper = BillingMapper.builder().build();
            List<BillingModel> billingModels = mapper.toRestModelList(billings);

            return ResponseEntity.ok(billingModels);
        } catch (Exception e) {
            log.error("Error getting billings for subscriber: {}", subscriber, e);
            throw e;
        }
    }
}