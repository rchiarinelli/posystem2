package br.com.brainyit.posystem2.order.api;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import br.com.brainyit.posystem2.api.OrderApiDelegate;
import br.com.brainyit.posystem2.domain.OrderStatus;
import br.com.brainyit.posystem2.domain.POPriority;
import br.com.brainyit.posystem2.model.CreateOrderRequestBody;
import br.com.brainyit.posystem2.model.Message;
import br.com.brainyit.posystem2.model.OrderRestModel;
import br.com.brainyit.posystem2.model.ProcutionOrderRequestBody;
import br.com.brainyit.posystem2.model.ProductionOrderRestModel;
import br.com.brainyit.posystem2.model.SubscriberOrdersOrderCodeCancelPutRequest;
import br.com.brainyit.posystem2.order.mapper.OrderMapper;
import br.com.brainyit.posystem2.order.service.FilterOrderParams;
import br.com.brainyit.posystem2.order.service.OrderService;
import br.com.brainyit.posystem2.order.service.PlaceOrderParams;
import br.com.brainyit.posystem2.po.mapper.ProductionOrderMapper;
import br.com.brainyit.posystem2.po.service.CreateProductionOrderParams;
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
        public ResponseEntity<List<OrderRestModel>> subscriberOrdersGet(String subscriber, String name,
                        String orderCode, List<String> status) throws Exception {
                List<String> statuses = null;
                if (status == null || status.isEmpty() || status.contains("ALL") || status.contains("all")) {
                        statuses = Arrays.stream(OrderStatus.values()).map(OrderStatus::name).toList();
                } else {
                        statuses = status.stream().map(String::toUpperCase).map(OrderStatus::valueOf)
                                        .map(OrderStatus::name).toList();
                }
                return ResponseEntity.ok(orderService.getOrdersBySubscriberId(
                                subscriber, FilterOrderParams.of(name, orderCode, statuses)).stream()
                                .map(OrderMapper.builder().build()::toRestBean).toList());
        }

        @Override
        public ResponseEntity<ProductionOrderRestModel> subscriberOrdersOrderCodePlaceProductionOrderPost(
                        String subscriber,
                        String orderCode, ProcutionOrderRequestBody requestBody) throws Exception {
                final CreateProductionOrderParams params = new CreateProductionOrderParams(
                                orderCode,
                                requestBody.getInstructions(),
                                requestBody.getQtd().doubleValue(),
                                requestBody.getEstimatedTime(),
                                requestBody.getPlannedStartDate(),
                                POPriority.valueOf(requestBody.getPriority().name()));
                return ResponseEntity.ok(ProductionOrderMapper.builder().build()
                                .toRestBean(productionOrderService.createProductionOrder(subscriber, params)));
        }

        @Override
        public ResponseEntity<OrderRestModel> subscriberOrdersPost(String subscriber,
                        CreateOrderRequestBody requestBody) throws Exception {
                return ResponseEntity.ok(OrderMapper.builder().build().toRestBean(
                                orderService.placeOrder(subscriber, new PlaceOrderParams(requestBody.getCustomerId(),
                                                requestBody.getOrderDescription(), requestBody.getQuantity(),
                                                Date
                                                                .from(requestBody.getPlannedDeliverDate()
                                                                                .atStartOfDay(ZoneId.systemDefault())
                                                                                .toInstant())))));
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
