package br.com.brainyit.posystem2.po.api;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.brainyit.posystem2.domain.POStatus;
import br.com.brainyit.posystem2.po.ProductionOrder;
import br.com.brainyit.posystem2.po.mapper.ProductionOrderMapper;
import br.com.brainyit.posystem2.po.service.ProductionOrderService;
import br.com.brainyit.posystem2.util.MessagesUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/{subscriberId}/production-orders")
@RequiredArgsConstructor
public class ProductionOrderEndpoint {

    private final ProductionOrderService productionOrderService;

    @GetMapping
    public ResponseEntity<List<ProductionOrderRestBean>> getProductionOrdersByStatus(
            @PathVariable final Long subscriberId,
            @RequestParam(required = false) final String status) {
        POStatus[] statuses = null;
        if (StringUtils.isBlank(status)) {
            statuses = new POStatus[] { POStatus.EXECUTING, POStatus.PLANNING };
        } else {
            statuses = new POStatus[] { POStatus.valueOf(status) };
        }

        final ProductionOrderMapper mapper = ProductionOrderMapper.builder().build();
        return ResponseEntity.ok(productionOrderService.retrieveProductionOrders(subscriberId, statuses).stream()
                .map(mapper::toRestBean).collect(Collectors.toList()));
    }

    @GetMapping("/{poNumber}")
    public ResponseEntity<ProductionOrderRestBean> getProductionOrder(@PathVariable final Long subscriberId,
            @PathVariable final String poNumber) {
        return ResponseEntity.ok(ProductionOrderMapper.builder().build()
                .toRestBean(productionOrderService.retrieveProductionOrderByPoNumber(subscriberId, poNumber)));
    }

    @PutMapping("/{poNumber}")
    public ResponseEntity<ProductionOrderRestBean> updateProductionOrder(@PathVariable final Long subscriberId,
            @PathVariable final String poNumber, @Valid @RequestBody final UpdateProductionOrderRequestBody requestBody) {

        final ProductionOrder productionOrder = ProductionOrder.builder().build();
        productionOrder.setQtd(requestBody.getQtd());
        productionOrder.setEstimatedTime(requestBody.getEstimatedTime());
        productionOrder.setDescription(requestBody.getDescription());
        productionOrder.setStatus(POStatus.valueOf(requestBody.getStatus()));
        productionOrder.setActualDeliveryDate(LocalDate.parse(requestBody.getActualDeliveryDate(),
                DateTimeFormatter.ofPattern(MessagesUtil.getInstance().getSystemProp("date_pattern"))));
        productionOrder.setEstimatedDueDate(LocalDate.parse(requestBody.getEstimatedDueDate(),
                DateTimeFormatter.ofPattern(MessagesUtil.getInstance().getSystemProp("date_pattern"))));

        return ResponseEntity.ok(ProductionOrderMapper.builder().build()
                .toRestBean(productionOrderService.updateProductionOrder(subscriberId, poNumber, productionOrder)));
    }

    @PutMapping("/{poNumber}/cancel")
    public ResponseEntity<?> cancelProductionOrder(@PathVariable final Long subscriberId,
            @PathVariable final String poNumber, @RequestBody final UpdateProductionOrderRequestBody requestBody) {
        
        if (StringUtils.isBlank(requestBody.getDescription())) {
            return ResponseEntity.badRequest().body("Description cannot be empty");
        }

        return ResponseEntity.ok(ProductionOrderMapper.builder().build()
                .toRestBean(productionOrderService.cancelProductionOrder(subscriberId, poNumber, requestBody.getDescription())));
    }

    @PutMapping("/{poNumber}/proceed")
    public ResponseEntity<?> putMethodName(@PathVariable final String subscriberId, @PathVariable final String poNumber) {
        return ResponseEntity.ok(ProductionOrderMapper.builder().build().toRestBean(productionOrderService.proceedToNextPhase(Long.valueOf(subscriberId), poNumber)));
    }
}
