package br.com.brainyit.posystem2.po.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.domain.OrderStatus;
import br.com.brainyit.posystem2.domain.POStatus;
import br.com.brainyit.posystem2.domain.Subscriber;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.order.repository.OrderRepository;
import br.com.brainyit.posystem2.po.ProductionOrder;
import br.com.brainyit.posystem2.po.repository.ProductionOrderRepository;
import br.com.brainyit.posystem2.repository.SubscriberRepository;

@ExtendWith(MockitoExtension.class)
class ProductionOrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private SubscriberRepository subscriberRepository;

    @Mock
    private ProductionOrderRepository productionOrderRepository;

    @InjectMocks
    private ProductionOrderService productionOrderService;

    private Order order;
    private Subscriber subscriber;

    @BeforeEach
    void setUp() {
        subscriber = Subscriber.builder()
                .id(1L)
                .build();

        order = Order.builder()
                .id(1L)
                .orderCode("ORDER123")
                .status(OrderStatus.PENDING)
                .build();
    }

    @Test
    void test_createProductionOrder_success() {

        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(subscriber));
        when(orderRepository.findBySubscriberIdAndOrderCode(1L, "ORDER123")).thenReturn(Optional.of(order));
        when(productionOrderRepository.save(any(ProductionOrder.class))).thenAnswer(i -> i.getArguments()[0]);

        // when
        ProductionOrder result = productionOrderService.createProductionOrder(1L, "ORDER123", 10, 2.5, "Test PO");

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.getQtd(), is(10));
        assertThat(result.getEstimatedTime(), is(2.5));
        assertThat(result.getDescription(), is("Test PO"));
        assertThat(result.getStatus(), is(POStatus.PLANNING));
        assertThat(result.getOpenDate(), is(LocalDate.now()));
        assertThat(result.getOrder().getStatus(), is(OrderStatus.EXECUTING));
        assertThat(result.getSubscriber(), is(subscriber));
        assertThat(result.getPoNumber(), is(notNullValue()));

        verify(productionOrderRepository).save(any(ProductionOrder.class));
    }

    @Test
    void test_createProductionOrder_subscriberNotFound() {
        // given
        when(subscriberRepository.findById(1L)).thenReturn(Optional.empty());

        // when/then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productionOrderService.createProductionOrder(1L, "ORDER123", 10, 2.5, "Test PO"));

        assertThat(exception.getMessage(), containsString("Subscriber not found"));
    }

    @Test
    void test_createProductionOrder_orderNotFound() {
        // given
        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(subscriber));
        when(orderRepository.findBySubscriberIdAndOrderCode(1L, "ORDER123")).thenReturn(Optional.empty());

        // when/then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productionOrderService.createProductionOrder(1L, "ORDER123", 10, 2.5, "Test PO"));

        assertThat(exception.getMessage(), containsString("Order not found"));
    }

    @Test
    void test_createProductionOrder_zeroQuantity() {
        // given

        // when/then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productionOrderService.createProductionOrder(1L, "ORDER123", 0, 2.5, "Test PO"));

        assertThat(exception.getMessage(), containsString("Quantity must be greater than zero"));
    }

    @Test
    void test_createProductionOrder_zeroEstimatedTime() {
        // given

        // when/then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productionOrderService.createProductionOrder(1L, "ORDER123", 10, 0.0, "Test PO"));

        assertThat(exception.getMessage(), containsString("Estimated time must be greater than zero"));
    }

    @Test
    void test_createProductionOrder_orderNotInPendingStatus() {
        // given
        order.setStatus(OrderStatus.EXECUTING);
        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(subscriber));
        when(orderRepository.findBySubscriberIdAndOrderCode(1L, "ORDER123")).thenReturn(Optional.of(order));

        // when/then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productionOrderService.createProductionOrder(1L, "ORDER123", 10, 2.5, "Test PO"));

        assertThat(exception.getMessage(),
                containsString("Order is not in the pending status, it is in the EXECUTING status"));
    }

    @Test
    void test_retrieveProductionOrders_whenNoProductionOrdersExist_returnsEmptyList() {
        // given
        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(subscriber));
        when(productionOrderRepository.findBySubscriberIdAndInStatuses(1L, POStatus.EXECUTING, POStatus.PLANNING))
                .thenReturn(List.of());

        // when
        List<ProductionOrder> result = productionOrderService.retrieveProductionOrders(1L, POStatus.EXECUTING,
                POStatus.PLANNING);

        // then
        assertThat(result, is(Collections.emptyList()));
    }

    @Test
    void test_retrieveProductionOrders_whenSubscriberNotFound_throwsBusinessException() {
        // given
        when(subscriberRepository.findById(1L)).thenReturn(Optional.empty());

        // when/then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productionOrderService.retrieveProductionOrders(1L, POStatus.EXECUTING, POStatus.PLANNING));

        assertThat(exception.getMessage(), containsString("Subscriber not found"));
    }

    @Test
    void test_retrieveProductionOrders_whenProductionOrdersExist_returnsOrders() {
        // given
        ProductionOrder productionOrder = ProductionOrder.builder()
                .subscriber(subscriber)
                .order(order)
                .qtd(10)
                .estimatedTime(2.5)
                .description("Test PO")
                .status(POStatus.PLANNING)
                .build();

        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(subscriber));
        when(productionOrderRepository.findBySubscriberIdAndInStatuses(1L, POStatus.EXECUTING, POStatus.PLANNING))
                .thenReturn(List.of(productionOrder));

        // when
        List<ProductionOrder> result = productionOrderService.retrieveProductionOrders(1L, POStatus.EXECUTING,
                POStatus.PLANNING);

        // then
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getQtd(), is(10));
        assertThat(result.get(0).getEstimatedTime(), is(2.5));
        assertThat(result.get(0).getDescription(), is("Test PO"));
        assertThat(result.get(0).getStatus(), is(POStatus.PLANNING));
    }

    @Test
    void test_retrieveProductionOrderByPoNumber_whenExists_returnsOrder() {
        // given
        String poNumber = "PO-123";
        ProductionOrder productionOrder = ProductionOrder.builder()
                .subscriber(subscriber)
                .order(order)
                .qtd(10)
                .estimatedTime(2.5)
                .description("Test PO")
                .status(POStatus.PLANNING)
                .poNumber(poNumber)
                .build();

        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(subscriber));
        when(productionOrderRepository.findBySubscriberIdAndPoNumber(1L, poNumber))
                .thenReturn(Optional.of(productionOrder));

        // when
        ProductionOrder result = productionOrderService.retrieveProductionOrderByPoNumber(1L, poNumber);

        // then
        assertThat(result.getPoNumber(), is(poNumber));
        assertThat(result.getQtd(), is(10));
        assertThat(result.getEstimatedTime(), is(2.5));
        assertThat(result.getDescription(), is("Test PO"));
        assertThat(result.getStatus(), is(POStatus.PLANNING));
    }

    @Test
    void test_retrieveProductionOrderByPoNumber_whenSubscriberNotFound_throwsBusinessException() {
        // given
        when(subscriberRepository.findById(1L)).thenReturn(Optional.empty());

        // when/then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productionOrderService.retrieveProductionOrderByPoNumber(1L, "PO-123"));

        assertThat(exception.getMessage(), containsString("Subscriber not found"));
    }

    @Test
    void test_retrieveProductionOrderByPoNumber_whenOrderNotFound_throwsBusinessException() {
        // given
        String poNumber = "PO-123";
        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(subscriber));
        when(productionOrderRepository.findBySubscriberIdAndPoNumber(1L, poNumber))
                .thenReturn(Optional.empty());

        // when/then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productionOrderService.retrieveProductionOrderByPoNumber(1L, poNumber));

        assertThat(exception.getMessage(), containsString("Production Order not found"));
    }

    @Test
    void test_proceedToNextPhase_whenOrderInPlanning_movesToExecuting() {
        // given
        String poNumber = "PO-123";
        ProductionOrder productionOrder = ProductionOrder.builder()
                .subscriber(subscriber)
                .order(order)
                .qtd(10)
                .estimatedTime(2.5)
                .description("Test PO")
                .status(POStatus.PLANNING)
                .poNumber(poNumber)
                .build();

        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(subscriber));
        when(productionOrderRepository.findBySubscriberIdAndPoNumber(1L, poNumber))
                .thenReturn(Optional.of(productionOrder));
        when(productionOrderRepository.save(any(ProductionOrder.class)))
                .thenReturn(productionOrder);

        // when
        ProductionOrder result = productionOrderService.proceedToNextPhase(1L, poNumber);

        // then
        assertThat(result.getStatus(), is(POStatus.EXECUTING));
        verify(productionOrderRepository).save(productionOrder);
    }

    @Test
    void test_proceedToNextPhase_whenOrderInExecuting_movesToDone() {
        // given
        String poNumber = "PO-123";
        ProductionOrder productionOrder = ProductionOrder.builder()
                .subscriber(subscriber)
                .order(order)
                .qtd(10)
                .estimatedTime(2.5)
                .description("Test PO")
                .status(POStatus.EXECUTING)
                .poNumber(poNumber)
                .build();

        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(subscriber));
        when(productionOrderRepository.findBySubscriberIdAndPoNumber(1L, poNumber))
                .thenReturn(Optional.of(productionOrder));
        when(productionOrderRepository.save(any(ProductionOrder.class)))
                .thenReturn(productionOrder);

        // when
        ProductionOrder result = productionOrderService.proceedToNextPhase(1L, poNumber);

        // then
        assertThat(result.getStatus(), is(POStatus.DONE));
        verify(productionOrderRepository).save(productionOrder);
    }

    @Test
    void test_proceedToNextPhase_whenSubscriberNotFound_throwsBusinessException() {
        // given
        when(subscriberRepository.findById(1L)).thenReturn(Optional.empty());

        // when/then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productionOrderService.proceedToNextPhase(1L, "PO-123"));

        assertThat(exception.getMessage(), containsString("Subscriber not found"));
    }

    @Test
    void test_proceedToNextPhase_whenOrderNotFound_throwsBusinessException() {
        // given
        String poNumber = "PO-123";
        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(subscriber));
        when(productionOrderRepository.findBySubscriberIdAndPoNumber(1L, poNumber))
                .thenReturn(Optional.empty());

        // when/then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productionOrderService.proceedToNextPhase(1L, poNumber));

        assertThat(exception.getMessage(), containsString("Production Order not found"));
    }

    @Test
    void test_proceedToNextPhase_whenOrderAlreadyDone_throwsBusinessException() {
        // given
        String poNumber = "PO-123";
        ProductionOrder productionOrder = ProductionOrder.builder()
                .subscriber(subscriber)
                .order(order)
                .qtd(10)
                .estimatedTime(2.5)
                .description("Test PO")
                .status(POStatus.DONE)
                .poNumber(poNumber)
                .build();

        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(subscriber));
        when(productionOrderRepository.findBySubscriberIdAndPoNumber(1L, poNumber))
                .thenReturn(Optional.of(productionOrder));

        // when/then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productionOrderService.proceedToNextPhase(1L, poNumber));

        assertThat(exception.getMessage(), containsString("Cannot move production order from DONE status"));
    }

    @Test
    void test_cancelProductionOrder_whenOrderInPlanningStatus_cancelsOrder() {
        // given
        String poNumber = "PO-123";
        ProductionOrder productionOrder = ProductionOrder.builder()
                .subscriber(subscriber)
                .order(order)
                .qtd(10)
                .estimatedTime(2.5)
                .description("Test PO")
                .status(POStatus.PLANNING)
                .poNumber(poNumber)
                .build();

        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(subscriber));
        when(productionOrderRepository.findBySubscriberIdAndPoNumber(1L, poNumber))
                .thenReturn(Optional.of(productionOrder));
        when(productionOrderRepository.save(any(ProductionOrder.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        ProductionOrder result = productionOrderService.cancelProductionOrder(1L, poNumber, "Canceled by a test.");

        // then
        assertThat(result.getStatus(), is(POStatus.CANCELLED));
        assertThat(result.getDescription(), is("Test PO\nCanceled by a test."));
        assertThat(result.getActualDeliveryDate(), is(LocalDate.now()));
        verify(productionOrderRepository).save(productionOrder);
    }

    @Test
    void test_cancelProductionOrder_whenOrderInExecutingStatus_cancelsOrder() {
        // given
        String poNumber = "PO-123";
        ProductionOrder productionOrder = ProductionOrder.builder()
                .subscriber(subscriber)
                .order(order)
                .qtd(10)
                .estimatedTime(2.5)
                .description("Test PO")
                .status(POStatus.EXECUTING)
                .poNumber(poNumber)
                .build();

        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(subscriber));
        when(productionOrderRepository.findBySubscriberIdAndPoNumber(1L, poNumber))
                .thenReturn(Optional.of(productionOrder));
        when(productionOrderRepository.save(any(ProductionOrder.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        ProductionOrder result = productionOrderService.cancelProductionOrder(1L, poNumber, "Canceled by a test.");

        // then
        assertThat(result.getStatus(), is(POStatus.CANCELLED));
        assertThat(result.getDescription(), is("Test PO\nCanceled by a test."));
        assertThat(result.getActualDeliveryDate(), is(LocalDate.now()));
        verify(productionOrderRepository).save(productionOrder);
    }

    @Test
    void test_cancelProductionOrder_whenSubscriberNotFound_throwsBusinessException() {
        // given
        when(subscriberRepository.findById(1L)).thenReturn(Optional.empty());

        // when/then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productionOrderService.cancelProductionOrder(1L, "PO-123", "Test PO"));

        assertThat(exception.getMessage(), containsString("Subscriber not found"));
    }

    @Test
    void test_cancelProductionOrder_whenOrderNotFound_throwsBusinessException() {
        // given
        String poNumber = "PO-123";
        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(subscriber));
        when(productionOrderRepository.findBySubscriberIdAndPoNumber(1L, poNumber))
                .thenReturn(Optional.empty());

        // when/then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productionOrderService.cancelProductionOrder(1L, poNumber, "Test PO"));

        assertThat(exception.getMessage(), containsString("Production Order not found"));
    }

    @Test
    void test_cancelProductionOrder_whenOrderAlreadyDone_throwsBusinessException() {
        // given
        String poNumber = "PO-123";
        ProductionOrder productionOrder = ProductionOrder.builder()
                .subscriber(subscriber)
                .order(order)
                .qtd(10)
                .estimatedTime(2.5)
                .description("Test PO")
                .status(POStatus.DONE)
                .poNumber(poNumber)
                .build();

        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(subscriber));
        when(productionOrderRepository.findBySubscriberIdAndPoNumber(1L, poNumber))
                .thenReturn(Optional.of(productionOrder));

        // when/then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productionOrderService.cancelProductionOrder(1L, poNumber, "Test PO"));

        assertThat(exception.getMessage(), containsString("Cannot cancel production order in DONE status"));
    }

    @Test
    void test_updateProductionOrder_success() {
        // given
        String poNumber = "PO-123";
        ProductionOrder existingPO = ProductionOrder.builder()
                .subscriber(subscriber)
                .order(order)
                .qtd(10)
                .estimatedTime(2.5)
                .description("Original description")
                .status(POStatus.PLANNING)
                .poNumber(poNumber)
                .build();

        ProductionOrder updatedPO = ProductionOrder.builder()
                .subscriber(subscriber)
                .order(order)
                .qtd(20)
                .estimatedTime(5.0)
                .description("Updated description")
                .status(POStatus.PLANNING)
                .poNumber(poNumber)
                .build();

        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(subscriber));
        when(productionOrderRepository.findBySubscriberIdAndPoNumber(1L, poNumber))
                .thenReturn(Optional.of(existingPO));
        when(productionOrderRepository.save(any(ProductionOrder.class))).thenReturn(updatedPO);

        // when
        ProductionOrder result = productionOrderService.updateProductionOrder(1L, poNumber, updatedPO);

        // then
        assertThat(result.getQtd(), is(20));
        assertThat(result.getEstimatedTime(), is(5.0));
        assertThat(result.getDescription(), is("Updated description"));
        verify(productionOrderRepository).save(any(ProductionOrder.class));
    }

    @Test
    void test_updateProductionOrder_whenSubscriberNotFound_throwsBusinessException() {
        // given
        String poNumber = "PO-123";
        ProductionOrder updatedPO = ProductionOrder.builder()
                .subscriber(subscriber)
                .order(order)
                .qtd(20)
                .estimatedTime(5.0)
                .description("Updated description")
                .status(POStatus.PLANNING)
                .poNumber(poNumber)
                .build();
        when(subscriberRepository.findById(1L)).thenReturn(Optional.empty());

        // when/then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productionOrderService.updateProductionOrder(1L, poNumber, updatedPO));

        assertThat(exception.getMessage(), containsString("Subscriber not found"));
    }

    @Test
    void test_updateProductionOrder_whenProductionOrderNotFound_throwsBusinessException() {
        // given
        String poNumber = "PO-123";
        ProductionOrder updatedPO = ProductionOrder.builder()
            .subscriber(subscriber)
            .order(order)
            .qtd(20)
            .estimatedTime(5.0)
            .description("Updated description") 
            .status(POStatus.PLANNING)
            .poNumber(poNumber)
            .build();
        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(subscriber));
        when(productionOrderRepository.findBySubscriberIdAndPoNumber(1L, poNumber))
                .thenReturn(Optional.empty());

        // when/then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productionOrderService.updateProductionOrder(1L, poNumber, updatedPO));

        assertThat(exception.getMessage(), containsString("Production Order not found"));
    }

    @Test
    void test_updateProductionOrder_whenInvalidQuantity_throwsBusinessException() {
        // given
        String poNumber = "PO-123";

        ProductionOrder updatedPO = ProductionOrder.builder()
                .subscriber(subscriber)
                .order(order)
                .qtd(-1) // Invalid quantity
                .estimatedTime(5.0)
                .description("Updated description")
                .status(POStatus.PLANNING)
                .poNumber(poNumber)
                .build();

        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(subscriber));
        when(productionOrderRepository.findBySubscriberIdAndPoNumber(1L, poNumber))
                .thenReturn(Optional.of(ProductionOrder.builder().build()));

        // when/then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productionOrderService.updateProductionOrder(1L, poNumber, updatedPO));

        assertThat(exception.getMessage(), containsString("Quantity must be greater than zero"));
    }

    @Test
    void test_updateProductionOrder_whenInvalidEstimatedTime_throwsBusinessException() {
        // given
        String poNumber = "PO-123";
        ProductionOrder existingPO = ProductionOrder.builder()
                .subscriber(subscriber)
                .order(order)
                .qtd(10)
                .estimatedTime(2.5)
                .description("Original description")
                .status(POStatus.PLANNING)
                .poNumber(poNumber)
                .build();

        ProductionOrder updatedPO = ProductionOrder.builder()
                .subscriber(subscriber)
                .order(order)
                .qtd(20)
                .estimatedTime(-1.0) // Invalid estimated time
                .description("Updated description")
                .status(POStatus.PLANNING)
                .poNumber(poNumber)
                .build();

        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(subscriber));
        when(productionOrderRepository.findBySubscriberIdAndPoNumber(1L, poNumber))
                .thenReturn(Optional.of(existingPO));

        // when/then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productionOrderService.updateProductionOrder(1L, poNumber, updatedPO));

        assertThat(exception.getMessage(), containsString("Estimated time must be greater than zero"));
    }

    @Test
    void test_updateProductionOrder_whenProductionOrderInDoneStatus_throwsBusinessException() {
        // given
        String poNumber = "PO-123";
        ProductionOrder existingPO = ProductionOrder.builder()
                .subscriber(subscriber)
                .order(order)
                .qtd(10)
                .estimatedTime(2.5)
                .description("Original description")
                .status(POStatus.DONE)
                .poNumber(poNumber)
                .build();

        ProductionOrder updatedPO = ProductionOrder.builder()
                .subscriber(subscriber)
                .order(order)
                .qtd(20)
                .estimatedTime(5.0)
                .description("Updated description")
                .status(POStatus.PLANNING)
                .poNumber(poNumber)
                .build();

        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(subscriber));
        when(productionOrderRepository.findBySubscriberIdAndPoNumber(1L, poNumber))
                .thenReturn(Optional.of(existingPO));

        // when/then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productionOrderService.updateProductionOrder(1L, poNumber, updatedPO));

        assertThat(exception.getMessage(), containsString("Cannot update production order in DONE status"));
    }

    @Test
    void test_updateProductionOrder_whenProductionOrderInCancelledStatus_throwsBusinessException() {
        // given
        String poNumber = "PO-123";
        ProductionOrder existingPO = ProductionOrder.builder()
                .subscriber(subscriber)
                .order(order)
                .qtd(10)
                .estimatedTime(2.5)
                .description("Original description")
                .status(POStatus.CANCELLED)
                .poNumber(poNumber)
                .build();

        ProductionOrder updatedPO = ProductionOrder.builder()
                .subscriber(subscriber)
                .order(order)
                .qtd(20)
                .estimatedTime(5.0)
                .description("Updated description")
                .status(POStatus.PLANNING)
                .poNumber(poNumber)
                .build();

        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(subscriber));
        when(productionOrderRepository.findBySubscriberIdAndPoNumber(1L, poNumber))
                .thenReturn(Optional.of(existingPO));

        // when/then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productionOrderService.updateProductionOrder(1L, poNumber, updatedPO));

        assertThat(exception.getMessage(), containsString("Cannot update production order in CANCELLED status"));
    }   
    
    

}
