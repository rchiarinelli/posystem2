package br.com.brainyit.posystem2.po.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;

import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.domain.OrderStatus;
import br.com.brainyit.posystem2.domain.POStatus;
import br.com.brainyit.posystem2.domain.ProductionOrder;
import br.com.brainyit.posystem2.domain.QProductionOrder;
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

    @Value("${application.production-order-code-prefix:PO}")
    private String productionOrderCodePrefix;

    @Value("${application.production-order-work-day-hours:8}")
    private int productionOrderWorkDayHours = 8;

    private final ProductionOrderRepository productionOrderRepository;

    private final OrderRepository orderRepository;

    private final SubscriberRepository subscriberRepository;

    @Transactional
    public ProductionOrder createProductionOrder(String subscriberId, CreateProductionOrderParams params) {

        if (params.qtd() <= 0) {
            throw new BusinessException("Quantity must be greater than zero");
        }

        if (params.estimatedTime() <= 0) {
            throw new BusinessException("Estimated time must be greater than zero");
        }

        final Subscriber subscriber = getSubscriber(subscriberId);

        final Order order = orderRepository.findBySubscriberIdAndOrderCode(subscriber.getId(), params.orderCode())
                .orElseThrow(() -> new BusinessException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.EXECUTING) {
            throw new BusinessException(
                    String.format("Order is not in the pending or executing status, it is in the %s status",
                            order.getStatus()));
        }

        order.setStatus(OrderStatus.EXECUTING);

        final String poNumber = String.format("%s%s", productionOrderCodePrefix,
                RandomStringUtils.secureStrong().nextAlphanumeric(5).toUpperCase());

        final LocalDate plannedStartDate = params.plannedStartDate();
        final LocalDate estimatedCompletionDate = plannedStartDate.plus(
                params.estimatedTime().intValue() / productionOrderWorkDayHours,
                ChronoUnit.DAYS);

        return productionOrderRepository.save(ProductionOrder.builder()
                .subscriber(subscriber)
                .order(order)
                .qtd(params.qtd().intValue())
                .estimatedTime(params.estimatedTime())
                .instructions(params.instructions())
                .status(POStatus.PLANNING)
                .priority(params.priority())
                .openDate(LocalDate.now())
                .poNumber(poNumber)
                .plannedStartDate(plannedStartDate)
                .estimatedCompletionDate(estimatedCompletionDate)
                .build());
    }

    public List<ProductionOrder> filterProductionOrders(final String subscriberId,
            final FilterProductionOrdersParams params) {

        var filterPredicate = QProductionOrder.productionOrder.subscriber.id.eq(getSubscriber(subscriberId).getId());

        if (!Strings.isNullOrEmpty(params.poNumber())) {
            filterPredicate = filterPredicate.and(QProductionOrder.productionOrder.poNumber.eq(params.poNumber()));
        }

        if (!Strings.isNullOrEmpty(params.customerName())) {
            filterPredicate = filterPredicate.and(
                    QProductionOrder.productionOrder.order.customer.name.containsIgnoreCase(params.customerName()));
        }

        if (params.statuses().length > 0) {
            filterPredicate = filterPredicate.and(QProductionOrder.productionOrder.status.in(params.statuses()));
        }

        return StreamSupport.stream(productionOrderRepository.findAll(filterPredicate).spliterator(), false)
                .collect(Collectors.toList());
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
    public ProductionOrder cancelProductionOrder(final String subscriberId, final String poNumber,
            final String cancelReason) {
        return productionOrderRepository
                .save(retrieveProductionOrderByPoNumber(subscriberId, poNumber).cancel(cancelReason));
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
        productionOrder.setInstructions(updatedPO.getInstructions());
        productionOrder.setStatus(updatedPO.getStatus());
        productionOrder.setActualCompletionDate(updatedPO.getActualCompletionDate());
        productionOrder.setEstimatedCompletionDate(updatedPO.getEstimatedCompletionDate());
        productionOrder.setPriority(updatedPO.getPriority());

        return productionOrderRepository.save(productionOrder);
    }
}
