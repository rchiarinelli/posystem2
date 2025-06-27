package br.com.brainyit.posystem2.order.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import br.com.brainyit.posystem2.api.OrderApiDelegate;
import br.com.brainyit.posystem2.model.CreateOrderRequestBody;
import br.com.brainyit.posystem2.model.Message;
import br.com.brainyit.posystem2.model.OrderRestModel;
import br.com.brainyit.posystem2.model.ProcutionOrderRequestBody;
import br.com.brainyit.posystem2.model.ProductionOrderRestModel;
import br.com.brainyit.posystem2.model.SubscriberOrdersOrderCodeCancelPutRequest;
import br.com.brainyit.posystem2.order.mapper.OrderMapper;
import br.com.brainyit.posystem2.order.service.OrderService;
import br.com.brainyit.posystem2.po.mapper.ProductionOrderMapper;
import br.com.brainyit.posystem2.po.service.ProductionOrderService;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OrderApiDelegateImpl implements OrderApiDelegate {

        private final OrderService orderService;

        private final ProductionOrderService productionOrderService;

        @Override
        public ResponseEntity<OrderRestModel> subscriberOrdersOrderCodeGet(String subscriber, String orderCode)
                        throws Exception {
                return ResponseEntity
                                .ok(OrderMapper.builder().build()
                                                .toRestBean(orderService.getOrderByCode(subscriber, orderCode)));
        }

        @Override
        public ResponseEntity<List<OrderRestModel>> subscriberOrdersGet(String subscriberId) throws Exception {
                return ResponseEntity.ok(orderService.getOrdersBySubscriberId(
                                subscriberId).stream().map(OrderMapper.builder().build()::toRestBean).toList());
        }

        @Override
        public ResponseEntity<ProductionOrderRestModel> subscriberOrdersOrderCodePlaceProductionOrderPost(
                        String subscriber,
                        String orderCode, ProcutionOrderRequestBody requestBody) throws Exception {
                return ResponseEntity.ok(ProductionOrderMapper.builder().build()
                                .toRestBean(productionOrderService.createProductionOrder(subscriber, orderCode,
                                                requestBody.getQtd().intValue(),
                                                requestBody.getEstimatedTime(), requestBody.getDescription())));
        }

        @Override
        public ResponseEntity<OrderRestModel> subscriberOrdersPost(String subscriber,
                        CreateOrderRequestBody requestBody) throws Exception {
                return ResponseEntity.ok(OrderMapper.builder().build().toRestBean(
                                orderService.placeOrder(subscriber, requestBody.getCustomerId(),
                                                requestBody.getOrderDescription(), requestBody.getQuantity())));
        }

        @Override
        public ResponseEntity<Message> subscriberOrdersOrderCodeCancelPut(String subscriber, String orderCode,
                        SubscriberOrdersOrderCodeCancelPutRequest subscriberOrdersOrderCodeCancelPutRequest)
                        throws Exception {
                orderService.cancelOrder(subscriber, orderCode,
                                subscriberOrdersOrderCodeCancelPutRequest.getReason());
                return ResponseEntity.ok(new Message().message("Order canceled"));
        }

}
