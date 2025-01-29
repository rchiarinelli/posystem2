package br.com.brainyit.posystem2.order.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.brainyit.posystem2.customer.repository.CompanyRepository;
import br.com.brainyit.posystem2.customer.repository.IndividualRepository;
import br.com.brainyit.posystem2.domain.Company;
import br.com.brainyit.posystem2.domain.Individual;
import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.domain.OrderStatus;
import br.com.brainyit.posystem2.domain.Subscriber;
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

    public Order placeOrder(final Long subscriberId, final String customerDocument, final String customerName, final String orderDescription, final Double orderQuantity) {

        final Subscriber subscriber = subscriberRepository.findById(subscriberId).orElseThrow(() -> new BusinessException("Subscriber not found"));

        final Individual individual = individualRepository.findBySubscriberIdAndCpfAndName(subscriberId, customerDocument, customerName);

        final Company company = companyRepository.findBySubscriberIdAndCnpjCgcAndName(subscriberId, customerDocument, customerName);
        
        if (individual == null && company == null) {
            throw new BusinessException("Customer not found");
        }
        final Order order = Order.builder()
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

    public Order getOrderByCode(final Long subscriberId, final String orderCode) {
        if (orderCode == null || orderCode.isEmpty()) {
            throw new BusinessException("Order code is required");
        }
        return orderRepository.findBySubscriberIdAndOrderCode(subscriberId, orderCode).orElseThrow(() -> new BusinessException("Order not found"));
    }

    public List<Order> getOrdersBySubscriberId(final Long subscriberId) {
        if (subscriberId == null) {
            throw new BusinessException("Subscriber ID is required");
        }
        return orderRepository.findBySubscriberId(subscriberId);
    }   

}
