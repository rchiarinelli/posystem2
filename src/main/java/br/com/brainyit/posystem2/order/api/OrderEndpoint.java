package br.com.brainyit.posystem2.order.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.brainyit.posystem2.order.mapper.OrderMapper;
import br.com.brainyit.posystem2.order.service.OrderService;
import br.com.brainyit.posystem2.po.api.ProcutionOrderRequestBody;
import br.com.brainyit.posystem2.po.api.ProductionOrderRestBean;
import br.com.brainyit.posystem2.po.mapper.ProductionOrderMapper;
import br.com.brainyit.posystem2.po.service.ProductionOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/{subscriberId}/orders")
@RequiredArgsConstructor

public class OrderEndpoint {

    private final OrderService orderService;

    private final ProductionOrderService productionOrderService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderRestBean> placeOrder(@PathVariable String subscriberId, @RequestBody @Valid CreateOrderRequestBody requestBody) {
        return ResponseEntity.ok(OrderMapper.builder().build().toRestBean(
            orderService.placeOrder(Long.parseLong(subscriberId)
            , requestBody.getCustomerDocument()
            , requestBody.getCustomerName()
            , requestBody.getOrderDescription()
            , requestBody.getQuantity())));
    }

    @GetMapping("/{orderCode}")
    public ResponseEntity<OrderRestBean> getOrder(@PathVariable final String subscriberId, @PathVariable String orderCode) {
        return ResponseEntity.ok(OrderMapper.builder().build().toRestBean(orderService.getOrderByCode(Long.parseLong(subscriberId), orderCode)));
    }

    @GetMapping
    public ResponseEntity<List<OrderRestBean>> getOrders(@PathVariable final String subscriberId) {
        return ResponseEntity.ok(orderService.getOrdersBySubscriberId(Long.parseLong(subscriberId)).stream().map(OrderMapper.builder().build()::toRestBean).toList());
    }

    @PostMapping("/{orderCode}/place-production-order")
    public ResponseEntity<ProductionOrderRestBean> placeProductionOrder(@PathVariable final Long subscriberId,
            @PathVariable final String orderCode, @Valid @RequestBody final ProcutionOrderRequestBody requestBody) {
        return ResponseEntity.ok(ProductionOrderMapper.builder().build()
                .toRestBean(productionOrderService.createProductionOrder(subscriberId, orderCode, requestBody.getQtd(),
                        requestBody.getEstimatedTime(), requestBody.getDescription())));
    }
}

