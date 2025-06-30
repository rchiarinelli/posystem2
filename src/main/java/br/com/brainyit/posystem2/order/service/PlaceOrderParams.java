package br.com.brainyit.posystem2.order.service;

import java.util.Date;

public record PlaceOrderParams(
                int customerId,
                String orderDescription,
                Double orderQuantity,
                Date plannedDeliverDate) {
}
