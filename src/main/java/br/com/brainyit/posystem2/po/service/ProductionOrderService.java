package br.com.brainyit.posystem2.po.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.domain.OrderStatus;
import br.com.brainyit.posystem2.domain.POStatus;
import br.com.brainyit.posystem2.domain.ProductionOrder;
import br.com.brainyit.posystem2.domain.Subscriber;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.order.repository.OrderRepository;
import br.com.brainyit.posystem2.po.repository.ProductionOrderRepository;
import br.com.brainyit.posystem2.repository.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductionOrderService {

    private static final String PO_NUMBER_PREFIX = "PO-";

    private final ProductionOrderRepository productionOrderRepository;
    
    private final OrderRepository orderRepository;

    private final SubscriberRepository subscriberRepository;

    @Transactional
    public ProductionOrder createProductionOrder(Long subscriberId, String orderCode, int quantity, double estimatedTime, String description) {

        if (quantity <= 0) {
            throw new BusinessException("Quantity must be greater than zero");
        }

        if (estimatedTime <= 0) {
            throw new BusinessException("Estimated time must be greater than zero");
        }

        final Subscriber subscriber = subscriberRepository.findById(subscriberId).orElseThrow(() -> new BusinessException("Subscriber not found"));

        final Order order = orderRepository.findBySubscriberIdAndOrderCode(subscriberId, orderCode).orElseThrow(() -> new BusinessException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException(String.format("Order is not in the pending status, it is in the %s status", order.getStatus()));
        }

        order.setStatus(OrderStatus.EXECUTING);
        orderRepository.save(order);

        return productionOrderRepository.save(ProductionOrder.builder()
                .subscriber(subscriber)
                .order(order)
                .qtd(quantity)
                .estimatedTime(estimatedTime)
                .description(description)
                .status(POStatus.PLANNING)
                .openDate(LocalDate.now())
                .poNumber(String.format("%s%d", PO_NUMBER_PREFIX, order.getId()))
                .build());
    }
}
