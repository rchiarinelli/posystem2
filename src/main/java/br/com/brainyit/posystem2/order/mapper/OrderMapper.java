package br.com.brainyit.posystem2.order.mapper;

import java.text.SimpleDateFormat;

import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.model.OrderRestModel;
import br.com.brainyit.posystem2.util.MessagesUtil;
import lombok.Builder;

@Builder
public class OrderMapper {

        private OrderMapper() {
        }

        public OrderRestModel toRestBean(Order order) {
                final SimpleDateFormat dateFormat = new SimpleDateFormat(
                                MessagesUtil.getInstance().getSystemProp("date_pattern"));

                final OrderRestModel orderRestModel = new OrderRestModel();
                return orderRestModel
                                .orderCode(order.getOrderCode())
                                .customerName(order.getCustomer() != null ? order.getCustomer().getName() : null)
                                .orderDescription(order.getDescription())
                                .openDate(
                                                order.getOpenDate() != null
                                                                ? dateFormat.format(order.getOpenDate())
                                                                : null)
                                .price(order.getPrice())
                                .quantity(order.getQuantity())
                                .deliverDate(order.getDeliverDate() != null
                                                ? dateFormat.format(order.getDeliverDate())
                                                : null)
                                .closeDate(
                                                order.getCloseDate() != null
                                                                ? dateFormat.format(order.getCloseDate())
                                                                : null)
                                .status(order.getStatus() != null ? order.getStatus().name() : null)
                                .plannedDeliverDate(order.getPlannedDeliverDate() != null
                                                ? dateFormat.format(order.getPlannedDeliverDate())
                                                : null);
        }

}
