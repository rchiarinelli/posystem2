package br.com.brainyit.posystem2.po.api;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import br.com.brainyit.posystem2.api.ProductionOrderApiDelegate;
import br.com.brainyit.posystem2.domain.POStatus;
import br.com.brainyit.posystem2.model.ProductionOrderRestModel;
import br.com.brainyit.posystem2.model.UpdateProductionOrderRequestBody;
import br.com.brainyit.posystem2.po.ProductionOrder;
import br.com.brainyit.posystem2.po.mapper.ProductionOrderMapper;
import br.com.brainyit.posystem2.po.service.ProductionOrderService;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ProductionOrderApiDelegateImpl implements ProductionOrderApiDelegate {

        private final ProductionOrderService productionOrderService;

        @Override
        public ResponseEntity<List<ProductionOrderRestModel>> subscriberProductionOrdersGet(String subscriber,
                        String status)
                        throws Exception {
                POStatus[] statuses = null;
                if (StringUtils.isBlank(status)) {
                        statuses = new POStatus[] { POStatus.EXECUTING, POStatus.PLANNING };
                } else {
                        statuses = new POStatus[] { POStatus.valueOf(status) };
                }

                final ProductionOrderMapper mapper = ProductionOrderMapper.builder().build();
                return ResponseEntity
                                .ok(productionOrderService.retrieveProductionOrders(subscriber, statuses)
                                                .stream()
                                                .map(mapper::toRestBean).collect(Collectors.toList()));

        }

        @Override
        public ResponseEntity<ProductionOrderRestModel> subscriberProductionOrdersPoNumberCancelPut(String subscriber,
                        String poNumber, UpdateProductionOrderRequestBody requestBody)
                        throws Exception {
                if (StringUtils.isBlank(requestBody.getDescription())) {
                        return ResponseEntity.badRequest().build();
                }
                return ResponseEntity.ok(ProductionOrderMapper.builder().build()
                                .toRestBean(productionOrderService.cancelProductionOrder(subscriber,
                                                poNumber,
                                                requestBody.getDescription())));
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
                productionOrder.setDescription(requestBody.getDescription());
                productionOrder.setStatus(POStatus.valueOf(requestBody.getStatus()));
                productionOrder.setActualDeliveryDate(requestBody.getActualDeliveryDate());
                productionOrder.setEstimatedDueDate(requestBody.getEstimatedDueDate());

                return ResponseEntity.ok(ProductionOrderMapper.builder().build()
                                .toRestBean(productionOrderService.updateProductionOrder(subscriber,
                                                poNumber,
                                                productionOrder)));
        }

}
