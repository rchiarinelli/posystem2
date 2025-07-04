package br.com.brainyit.posystem2.po.api;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import br.com.brainyit.posystem2.api.ProductionOrderApiDelegate;
import br.com.brainyit.posystem2.domain.POPriority;
import br.com.brainyit.posystem2.domain.POStatus;
import br.com.brainyit.posystem2.model.ProductionOrderRestModel;
import br.com.brainyit.posystem2.model.UpdateProductionOrderRequestBody;
import br.com.brainyit.posystem2.po.ProductionOrder;
import br.com.brainyit.posystem2.po.mapper.ProductionOrderMapper;
import br.com.brainyit.posystem2.po.service.FilterProductionOrdersParams;
import br.com.brainyit.posystem2.po.service.ProductionOrderService;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ProductionOrderApiDelegateImpl implements ProductionOrderApiDelegate {

        private final ProductionOrderService productionOrderService;

        @Override
        public ResponseEntity<List<ProductionOrderRestModel>> subscriberProductionOrdersGet(String subscriber,
                        String poNumber, String customerName, List<String> status) throws Exception {
                POStatus[] statuses = null;
                if (status == null || status.isEmpty()) {
                        statuses = new POStatus[] { POStatus.EXECUTING, POStatus.PLANNING };
                } else if (status.contains("ALL") || status.contains("all")) {
                        statuses = POStatus.values();
                } else {
                        statuses = status.stream()
                                        .map(POStatus::valueOf)
                                        .toArray(POStatus[]::new);
                }

                final ProductionOrderMapper mapper = ProductionOrderMapper.builder().build();
                return ResponseEntity
                                .ok(productionOrderService.filterProductionOrders(subscriber,
                                                new FilterProductionOrdersParams(poNumber, customerName, statuses))
                                                .stream()
                                                .map(mapper::toRestBean).toList());

        }

        @Override
        public ResponseEntity<ProductionOrderRestModel> subscriberProductionOrdersPoNumberCancelPut(String subscriber,
                        String poNumber, UpdateProductionOrderRequestBody requestBody)
                        throws Exception {
                if (StringUtils.isBlank(requestBody.getInstructions())) {
                        return ResponseEntity.badRequest().build();
                }
                return ResponseEntity.ok(ProductionOrderMapper.builder().build()
                                .toRestBean(productionOrderService.cancelProductionOrder(subscriber,
                                                poNumber,
                                                requestBody.getInstructions())));
        }

        @Override
        public ResponseEntity<ProductionOrderRestModel> subscriberProductionOrdersPoNumberGet(String subscriber,
                        String poNumber) throws Exception {
                return ResponseEntity.ok(ProductionOrderMapper.builder().build()
                                .toRestBean(
                                                productionOrderService.retrieveProductionOrderByPoNumber(
                                                                subscriber, poNumber)));

        }

        @Override
        public ResponseEntity<ProductionOrderRestModel> subscriberProductionOrdersPoNumberProceedPut(String subscriber,
                        String poNumber) throws Exception {
                return ResponseEntity.ok(ProductionOrderMapper.builder().build()
                                .toRestBean(productionOrderService.proceedToNextPhase(subscriber,
                                                poNumber)));

        }

        @Override
        public ResponseEntity<ProductionOrderRestModel> subscriberProductionOrdersPoNumberPut(String subscriber,
                        String poNumber, UpdateProductionOrderRequestBody requestBody)
                        throws Exception {
                final ProductionOrder productionOrder = ProductionOrder.builder().build();
                productionOrder.setQtd(requestBody.getQtd().intValue());
                productionOrder.setEstimatedTime(requestBody.getEstimatedTime());
                productionOrder.setInstructions(requestBody.getInstructions());
                productionOrder.setStatus(POStatus.valueOf(requestBody.getStatus()));
                productionOrder.setPlannedStartDate(requestBody.getPlannedStartDate());
                productionOrder.setEstimatedCompletionDate(requestBody.getEstimatedCompletionDate());
                productionOrder.setPriority(POPriority.valueOf(requestBody.getPriority().name()));

                return ResponseEntity.ok(ProductionOrderMapper.builder().build()
                                .toRestBean(productionOrderService.updateProductionOrder(subscriber,
                                                poNumber,
                                                productionOrder)));
        }

}
