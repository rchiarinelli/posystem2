package br.com.brainyit.posystem2.po.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.brainyit.posystem2.po.mapper.ProductionOrderMapper;
import br.com.brainyit.posystem2.po.service.ProductionOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/{subscriberId}/orders")
@RequiredArgsConstructor
public class ProductionOrderEndpoint {

    private final ProductionOrderService productionOrderService;

    @PostMapping("/{orderCode}/place-production-order")
    public ResponseEntity<ProductionOrderRestBean> placeProductionOrder(@PathVariable final Long subscriberId,
            @PathVariable final String orderCode, @Valid @RequestBody final ProcutionOrderRequestBody requestBody) {
        return ResponseEntity.ok(ProductionOrderMapper.builder().build()
                .toRestBean(productionOrderService.createProductionOrder(subscriberId, orderCode, requestBody.getQtd(),
                        requestBody.getEstimatedTime(), requestBody.getDescription())));
    }
}
