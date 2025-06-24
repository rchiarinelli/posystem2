package br.com.brainyit.posystem2.po.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.domain.OrderStatus;
import br.com.brainyit.posystem2.domain.POStatus;
import br.com.brainyit.posystem2.domain.Subscriber;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.order.repository.OrderRepository;
import br.com.brainyit.posystem2.po.ProductionOrder;
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
    public ProductionOrder createProductionOrder(String subscriberId, String orderCode, int quantity,
            double estimatedTime, String description) {

        if (quantity <= 0) {
            throw new BusinessException("Quantity must be greater than zero");
        }

        if (estimatedTime <= 0) {
            throw new BusinessException("Estimated time must be greater than zero");
        }

        final Subscriber subscriber = getSubscriber(subscriberId);

        final Order order = orderRepository.findBySubscriberIdAndOrderCode(subscriber.getId(), orderCode)
                .orElseThrow(() -> new BusinessException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException(
                    String.format("Order is not in the pending status, it is in the %s status", order.getStatus()));
        }

        order.setStatus(OrderStatus.EXECUTING);

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

    public List<ProductionOrder> retrieveProductionOrders(final String subscriberId, final POStatus... statuses) {
        return productionOrderRepository.findBySubscriberIdAndInStatuses(getSubscriber(subscriberId).getId(), statuses);
    }

    public ProductionOrder retrieveProductionOrderByPoNumber(final String subscriberId, final String poNumber) {
        return productionOrderRepository.findBySubscriberIdAndPoNumber(getSubscriber(subscriberId).getId(), poNumber)
                .orElseThrow(() -> new BusinessException("Production Order not found"));
    }

    private Subscriber getSubscriber(final String subscriberId) {
        return subscriberRepository.findByUuid(subscriberId)
                .orElseThrow(() -> new BusinessException("Subscriber not found"));
    }

    @Transactional
    public ProductionOrder proceedToNextPhase(final String subscriberId, final String poNumber) {
        return productionOrderRepository
                .save(retrieveProductionOrderByPoNumber(subscriberId, poNumber).moveToNextPhase());
    }

    @Transactional
    public ProductionOrder cancelProductionOrder(final String subscriberId, final String poNumber, final String cancelReason) {
        return productionOrderRepository.save(retrieveProductionOrderByPoNumber(subscriberId, poNumber).cancel(cancelReason));
    }

    @Transactional
    public ProductionOrder updateProductionOrder(final String subscriberId, final String poNumber,
            final ProductionOrder updatedPO) {
        
                final ProductionOrder productionOrder = retrieveProductionOrderByPoNumber(subscriberId, poNumber);

        if (updatedPO.getQtd() <= 0) {
            throw new BusinessException("Quantity must be greater than zero");
        }

        if (updatedPO.getEstimatedTime() <= 0) {
            throw new BusinessException("Estimated time must be greater than zero");
        }

        if (productionOrder.getStatus() == POStatus.DONE) {
            throw new BusinessException("Cannot update production order in DONE status");
        }

        if (productionOrder.getStatus() == POStatus.CANCELLED) {
            throw new BusinessException("Cannot update production order in CANCELLED status");
        }

        productionOrder.setQtd(updatedPO.getQtd());
        productionOrder.setEstimatedTime(updatedPO.getEstimatedTime());
        productionOrder.setDescription(updatedPO.getDescription());
        productionOrder.setStatus(updatedPO.getStatus());
        productionOrder.setActualDeliveryDate(updatedPO.getActualDeliveryDate());
        productionOrder.setEstimatedDueDate(updatedPO.getEstimatedDueDate());

        return productionOrderRepository.save(productionOrder);
    }
}
