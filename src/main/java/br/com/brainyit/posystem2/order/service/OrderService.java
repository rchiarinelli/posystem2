package br.com.brainyit.posystem2.order.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.brainyit.posystem2.customer.repository.CompanyRepository;
import br.com.brainyit.posystem2.customer.repository.IndividualRepository;
import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.domain.OrderStatus;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.order.repository.OrderRepository;
import br.com.brainyit.posystem2.repository.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    @Value("${application.order-code-prefix:PED-}")
    private String orderCodePrefix;

    @Value("${application.order-code-length:5}")
    private int orderCodeLength;

    private final OrderRepository orderRepository;

    private final IndividualRepository individualRepository;

    private final CompanyRepository companyRepository;

    private final SubscriberRepository subscriberRepository;

    @Transactional
    public Order placeOrder(final String subscriberId, final PlaceOrderParams params) {

        final var openDate = new Date();

        final var subscriber = subscriberRepository.findByUuid(subscriberId)
                .orElseThrow(() -> new BusinessException("Subscriber not found"));

        final var individual = individualRepository.findByIdAndSubscriberId(params.customerId(), subscriber.getId());

        final var company = companyRepository.findByIdAndSubscriberId(params.customerId(), subscriber.getId());

        if (individual == null && company == null) {
            throw new BusinessException("Customer not found");
        }
        if (params.plannedDeliverDate().before(openDate)) {
            throw new BusinessException("Planned deliver date cannot be in the past");
        }
        final var order = Order.builder()
                .customer(individual == null ? company : individual)
                .description(params.orderDescription())
                .quantity(params.orderQuantity())
                .status(OrderStatus.PENDING)
                .openDate(openDate)
                .orderCode(orderCodePrefix
                        + RandomStringUtils.secureStrong().nextAlphanumeric(orderCodeLength).toUpperCase())
                .subscriber(subscriber)
                .plannedDeliverDate(params.plannedDeliverDate())
                .build();
        return orderRepository.save(order);
    }

    public Order getOrderByCode(final String subscriberId, final String orderCode) {
        if (orderCode == null || orderCode.isEmpty()) {
            throw new BusinessException("Order code is required");
        }
        final var subscriber = subscriberRepository.findByUuid(subscriberId)
                .orElseThrow(() -> new BusinessException("Subscriber not found"));
        return orderRepository.findBySubscriberIdAndOrderCode(subscriber.getId(), orderCode)
                .orElseThrow(() -> new BusinessException("Order not found"));
    }

    public List<Order> getOrdersBySubscriberId(final String subscriberId) {
        if (subscriberId == null) {
            throw new BusinessException("Subscriber ID is required");
        }
        final var subscriber = subscriberRepository.findByUuid(subscriberId)
                .orElseThrow(() -> new BusinessException("Subscriber not found"));
        return orderRepository.findBySubscriberId(subscriber.getId());
    }

    @Transactional
    public Order cancelOrder(final String subscriberId, final String orderCode, final String reason) {
        final var order = getOrderByCode(subscriberId, orderCode);
        if (order.getStatus() == OrderStatus.EXECUTING) {
            throw new BusinessException("Order is already in execution");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessException("Order is already cancelled");
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.setCloseDate(new Date());
        order.setDescription(order.getDescription() + "\n\n ->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> REASON: " + reason);
        return orderRepository.save(order);
    }
}
