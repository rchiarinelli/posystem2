package br.com.brainyit.posystem2.po.mapper;

import br.com.brainyit.posystem2.model.ProductionOrderRestModel;
import br.com.brainyit.posystem2.order.mapper.OrderMapper;
import br.com.brainyit.posystem2.po.ProductionOrder;
import lombok.Builder;

/**
 * 
 * @author rafael.chiarinelli
 */
@Builder
public class ProductionOrderMapper {

        private ProductionOrderMapper() {
        }

        public ProductionOrderRestModel toRestBean(final ProductionOrder po) {
                final ProductionOrderRestModel productionOrderRestModel = new ProductionOrderRestModel();

                return productionOrderRestModel
                                .order(OrderMapper.builder().build().toRestBean(po.getOrder()))
                                .poNumber(po.getPoNumber() != null ? po.getPoNumber() : null)
                                .instructions(po.getInstructions())
                                .status(po.getStatus() != null ? po.getStatus().name() : null)
                                .estimatedTime(po.getEstimatedTime())
                                .openDate(po.getOpenDate())
                                .estimatedCompletionDate(po.getEstimatedCompletionDate())
                                .quantity(po.getQtd())
                                .actualCompletionDate(po.getActualCompletionDate())
                                .customerName(po.getOrder() != null && po.getOrder().getCustomer() != null
                                                ? po.getOrder().getCustomer().getName()
                                                : null)
                                .priority(po.getPriority() != null ? po.getPriority().name() : null)
                                .plannedStartDate(po.getPlannedStartDate());
        }
}