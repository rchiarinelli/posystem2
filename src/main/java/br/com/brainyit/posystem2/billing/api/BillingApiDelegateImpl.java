package br.com.brainyit.posystem2.billing.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import br.com.brainyit.posystem2.api.BillingApiDelegate;
import br.com.brainyit.posystem2.billing.mapper.BillingMapper;
import br.com.brainyit.posystem2.billing.service.BillingService;
import br.com.brainyit.posystem2.billing.service.CreateBillingParams;
import br.com.brainyit.posystem2.model.BillingModel;
import br.com.brainyit.posystem2.model.CreateBillingRequestBody;
import br.com.brainyit.posystem2.model.InstallmentModel;
import br.com.brainyit.posystem2.model.UpdateBillingInstallmentRequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class BillingApiDelegateImpl implements BillingApiDelegate {

        private final BillingService billingService;

        @Override
        public ResponseEntity<List<BillingModel>> subscriberBillingsGet(final String subscriber,
                        final String searchTerm,
                        final List<String> status) throws Exception {
                log.debug("Getting billings for subscriber: {}, searchTerm: {}, status: {}", subscriber, searchTerm,
                                status);

                final var billings = billingService.getBillings(subscriber, searchTerm, status);

                return ResponseEntity.ok(BillingMapper.builder().build().toRestModelList(billings));
        }

        @Override
        public ResponseEntity<List<BillingModel>> subscriberBillingsPost(String subscriber,
                        List<@Valid CreateBillingRequestBody> createBillingRequestBody) throws Exception {
                log.debug("Creating billings for subscriber: {}, createBillingRequestBody: {}", subscriber,
                                createBillingRequestBody);

                final var createBillingParams = createBillingRequestBody.stream()
                                .map(body -> new CreateBillingParams(
                                                body.getOrderCode(),
                                                body.getBillingConfigId(),
                                                body.getStartDate(),
                                                body.getFinalValue()))
                                .toList();

                final var billings = billingService.createBillings(subscriber, createBillingParams);

                return ResponseEntity.ok(BillingMapper.builder().build().toRestModelList(billings));
        }

        @Override
        public ResponseEntity<BillingModel> subscriberBillingsBillingUuidGet(String subscriber, String billingUuid)
                        throws Exception {

                return ResponseEntity.ok(BillingMapper.builder().build()
                                .toRestModel(billingService.fetchBilling(subscriber, billingUuid)));
        }

        @Override
        public ResponseEntity<List<InstallmentModel>> subscriberBillingsBillingUuidInstallmentsGet(String subscriber,
                        String billingUuid) throws Exception {

                final var billing = billingService.fetchBilling(subscriber, billingUuid);

                return ResponseEntity.ok(billing.getInstallments().stream()
                                .map(BillingMapper.builder().build()::toInstallmentModel)
                                .toList());
        }

        @Override
        public ResponseEntity<InstallmentModel> subscriberBillingsBillingUuidInstallmentsInstallmentUuidGet(
                        String subscriber, String billingUuid, String installmentUuid) throws Exception {

                final var installment = billingService.fetchInstallment(subscriber, billingUuid, installmentUuid);

                return ResponseEntity.ok(BillingMapper.builder().build()
                                .toInstallmentModel(installment));
        }

        @Override
        public ResponseEntity<InstallmentModel> subscriberBillingsBillingUuidInstallmentsInstallmentUuidPut(
                        String subscriber, String billingUuid, String installmentUuid,
                        UpdateBillingInstallmentRequestBody updateBillingInstallmentRequestBody) throws Exception {

                final var installment = billingService.payInstallment(subscriber, billingUuid, installmentUuid,
                                updateBillingInstallmentRequestBody.getPaidValue());

                return ResponseEntity.ok(BillingMapper.builder().build()
                                .toInstallmentModel(installment));
        }

}