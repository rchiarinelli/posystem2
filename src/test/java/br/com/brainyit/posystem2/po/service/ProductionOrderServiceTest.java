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
import br.com.brainyit.posystem2.domain.ProductionOrder;
import br.com.brainyit.posystem2.domain.Subscriber;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.order.repository.OrderRepository;
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

        verify(orderRepository).save(order);
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
            when(orderRepository.findBySubscriberIdAndOrderCode(1L,     "ORDER123")).thenReturn(Optional.empty());

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
        
        assertThat(exception.getMessage(), containsString("Order is not in the pending status, it is in the EXECUTING status"));
    }
}


