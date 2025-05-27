package br.com.brainyit.posystem2.po.mapper;

import java.time.format.DateTimeFormatter;

import br.com.brainyit.posystem2.model.ProductionOrderRestModel;
import br.com.brainyit.posystem2.po.ProductionOrder;
import br.com.brainyit.posystem2.util.MessagesUtil;
import lombok.Builder;

/**
 * 
 * @author rafael.chiarinelli
 */
@Builder
public class ProductionOrderMapper {

    private ProductionOrderMapper(){}

    public ProductionOrderRestModel toRestBean(final ProductionOrder po) {
        final ProductionOrderRestModel productionOrderRestModel = new ProductionOrderRestModel();
        return productionOrderRestModel
                .poNumber(po.getPoNumber() != null ? po.getPoNumber() : null)
                .description(po.getDescription())
                .status(po.getStatus() != null ? po.getStatus().name() : null)
                .estimatedTime(po.getEstimatedTime())
                .openDate(po.getOpenDate() != null ? po.getOpenDate().format(DateTimeFormatter.ofPattern(MessagesUtil.getInstance().getSystemProp("date_pattern"))) : null)
                .estimatedDueDate(po.getEstimatedDueDate() != null ? po.getEstimatedDueDate().format(DateTimeFormatter.ofPattern(MessagesUtil.getInstance().getSystemProp("date_pattern"))) : null)
                .quantity(po.getQtd())
                .actualDeliveryDate(po.getActualDeliveryDate() != null ? po.getActualDeliveryDate().format(DateTimeFormatter.ofPattern(MessagesUtil.getInstance().getSystemProp("date_pattern"))) : null)
                .customerName(po.getOrder() != null && po.getOrder().getCustomer() != null ? po.getOrder().getCustomer().getName() : null);
    }
}