package br.com.brainyit.posystem2.po.mapper;

import br.com.brainyit.posystem2.domain.ProductionOrder;
import br.com.brainyit.posystem2.po.api.ProductionOrderRestBean;
import lombok.Builder;

/**
 * 
 * @author rafael.chiarinelli
 */
@Builder
public class ProductionOrderMapper {

    private ProductionOrderMapper(){}

    public ProductionOrderRestBean toRestBean(final ProductionOrder po) {
        return ProductionOrderRestBean.builder()
                .poNumber(po.getPoNumber() != null ? po.getPoNumber() : null)
                .description(po.getDescription())
                .status(po.getStatus() != null ? po.getStatus().name() : null)
                .estimatedTime(po.getEstimatedTime())
                .openDate(po.getOpenDate() != null ? po.getOpenDate().toString() : null)
                .estimatedDueDate(po.getEstimatedDueDate() != null ? po.getEstimatedDueDate().toString() : null)
                .quantity(po.getQtd())
                .actualDeliveryDate(po.getActualDeliveryDate() != null ? po.getActualDeliveryDate().toString() : null)
                .customerName(po.getOrder() != null && po.getOrder().getCustomer() != null ? po.getOrder().getCustomer().getName() : null)
                .build();
    }
}