package br.com.brainyit.posystem2.order.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

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

    private final OrderRepository orderRepository;

    private final IndividualRepository individualRepository;

    private final CompanyRepository companyRepository;

    private final SubscriberRepository subscriberRepository;

    public Order placeOrder(final String subscriberId, final String customerDocument, final String customerName, final String orderDescription, final Double orderQuantity) {

        final var subscriber = subscriberRepository.findByUuid(subscriberId).orElseThrow(() -> new BusinessException("Subscriber not found"));

        final var individual = individualRepository.findBySubscriberIdAndCpfAndName(subscriber.getId(), customerDocument, customerName);

        final var company = companyRepository.findBySubscriberIdAndCnpjCgcAndName(subscriber.getId(), customerDocument, customerName);
        
        if (individual == null && company == null) {
            throw new BusinessException("Customer not found");
        }
        final var order = Order.builder()
                .customer(individual == null ? company : individual)
                .description(orderDescription)
                .quantity(orderQuantity)
                .status(OrderStatus.PENDING)
                .openDate(new Date())
                .orderCode(UUID.randomUUID().toString())
                .subscriber(subscriber)
                .build();
        return orderRepository.save(order);
    }

    public Order getOrderByCode(final String subscriberId, final String orderCode) {
        if (orderCode == null || orderCode.isEmpty()) {
            throw new BusinessException("Order code is required");
        }
        final var subscriber = subscriberRepository.findByUuid(subscriberId).orElseThrow(() -> new BusinessException("Subscriber not found"));
        return orderRepository.findBySubscriberIdAndOrderCode(subscriber.getId(), orderCode).orElseThrow(() -> new BusinessException("Order not found"));
    }

    public List<Order> getOrdersBySubscriberId(final String subscriberId) {
        if (subscriberId == null) {
            throw new BusinessException("Subscriber ID is required");
        }
        final var subscriber = subscriberRepository.findByUuid(subscriberId).orElseThrow(() -> new BusinessException("Subscriber not found"));
        return orderRepository.findBySubscriberId(subscriber.getId());
    }   

}
