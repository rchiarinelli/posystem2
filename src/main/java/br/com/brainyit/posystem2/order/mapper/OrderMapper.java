package br.com.brainyit.posystem2.order.mapper;

import org.apache.commons.lang3.time.DateFormatUtils;

import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.order.api.OrderRestBean;
import br.com.brainyit.posystem2.util.MessagesUtil;
import lombok.Builder;

@Builder
public class OrderMapper {

    private OrderMapper(){}

    public OrderRestBean toRestBean(Order order){
        return OrderRestBean.builder()
                .orderCode(order.getOrderCode())
                .customerName(order.getCustomer() != null ? order.getCustomer().getName() : null)
                .orderDescription(order.getDescription())
                .price(order.getPrice())
                .quantity(order.getQuantity())
                .deliverDate(order.getDeliverDate() != null ? DateFormatUtils.format(order.getDeliverDate(), MessagesUtil.getInstance().getSystemProp("date_pattern")) : null)
                .status(order.getStatus() != null ? order.getStatus().name() : null)
                .build();
    }

}
