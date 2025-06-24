package br.com.brainyit.posystem2.order.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
import br.com.brainyit.util.TestUtils;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;
    
    @Mock
    private OrderRepository orderRepository;
    
    @Mock
    private IndividualRepository individualRepository;
    
    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private SubscriberRepository subscriberRepository;
    
    private static final String SUBSCRIBER_UUID = "123e4567-e89b-12d3-a456-426614174000";
    
    private static final String CUSTOMER_DOC = "12345678901";
    
    private static final String CUSTOMER_NAME = "John Doe";
    
    private static final String ORDER_DESC = "Test Order";
    
    private static final Double ORDER_QTY = 1.0;
    
    @Test
    void test_placeOrder_withIndividual_success() {
        // given
        Individual customer = Individual.builder()
                .cpf(CUSTOMER_DOC)
                .name(CUSTOMER_NAME)
                .build();

        Subscriber subscriber = Subscriber.builder().uuid(SUBSCRIBER_UUID).build();

        final Date openDate = new Date();

        Order order = TestUtils.createOrder(customer, ORDER_DESC, ORDER_QTY, OrderStatus.PENDING, openDate, UUID.randomUUID().toString());
        order.setSubscriber(subscriber);
                
        when(subscriberRepository.findByUuid(SUBSCRIBER_UUID)).thenReturn(Optional.of(subscriber));
        when(individualRepository.findBySubscriberIdAndCpfAndName(subscriber.getId(), CUSTOMER_DOC, CUSTOMER_NAME))
            .thenReturn(customer);
            
        when(orderRepository.save(any(Order.class))).thenReturn(order);
            
        // when
        Order result = orderService.placeOrder(subscriber.getUuid(), CUSTOMER_DOC, CUSTOMER_NAME, ORDER_DESC, ORDER_QTY);
        
        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.getStatus(), is(OrderStatus.PENDING));
        assertThat(result.getOpenDate(), is(openDate));
        assertThat(result.getDescription(), is(ORDER_DESC));
        assertThat(result.getQuantity(), is(ORDER_QTY));
        assertThat(result.getCustomer(), is(customer));
        assertThat(result.getOrderCode(), is(notNullValue()));
        assertThat(result.getSubscriber(), is(notNullValue()));
    }

    @Test
    void test_placeOrder_withCompany_success() {
        Company customer = Company.builder()
                .cnpjCgc(CUSTOMER_DOC)
                .name(CUSTOMER_NAME)
                .build();

        Subscriber subscriber = Subscriber.builder().uuid(SUBSCRIBER_UUID).build();

        final Date openDate = new Date();

        Order order = TestUtils.createOrder(customer, ORDER_DESC, ORDER_QTY, OrderStatus.PENDING, openDate, UUID.randomUUID().toString());
        order.setSubscriber(subscriber);

        when(subscriberRepository.findByUuid(SUBSCRIBER_UUID)).thenReturn(Optional.of(subscriber));
        when(companyRepository.findBySubscriberIdAndCnpjCgcAndName(subscriber.getId(), CUSTOMER_DOC,CUSTOMER_NAME))
            .thenReturn(customer);

        when(orderRepository.save(any(Order.class))).thenReturn(order); 

        // when
        Order result = orderService.placeOrder(subscriber.getUuid(), CUSTOMER_DOC, CUSTOMER_NAME, ORDER_DESC, ORDER_QTY);
        
        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.getStatus(), is(OrderStatus.PENDING));
        assertThat(result.getOpenDate(), is(openDate));
        assertThat(result.getDescription(), is(ORDER_DESC));
        assertThat(result.getQuantity(), is(ORDER_QTY));
        assertThat(result.getCustomer(), is(customer));
        assertThat(result.getOrderCode(), is(notNullValue()));
        assertThat(result.getSubscriber(), is(notNullValue()));
    }

    @Test
    void test_placeOrder_whenIndividualCustomerNotFound_throwsBusinessException() {
        Subscriber subscriber = Subscriber.builder().uuid(SUBSCRIBER_UUID).build();

        when(subscriberRepository.findByUuid(SUBSCRIBER_UUID)).thenReturn(Optional.of(subscriber));

        when(individualRepository.findBySubscriberIdAndCpfAndName(subscriber.getId(),  CUSTOMER_DOC, CUSTOMER_NAME))
            .thenReturn(null);
        assertThrows(BusinessException.class, () -> 
            orderService.placeOrder(subscriber.getUuid(), CUSTOMER_DOC, CUSTOMER_NAME, ORDER_DESC, ORDER_QTY));
    } 

    @Test
    void test_placeOrder_whenCompanyCustomerNotFound_throwsBusinessException() {
        Subscriber subscriber = Subscriber.builder().uuid(SUBSCRIBER_UUID).build();
        
        when(subscriberRepository.findByUuid(SUBSCRIBER_UUID)).thenReturn(Optional.of(subscriber));
        
        when(companyRepository.findBySubscriberIdAndCnpjCgcAndName(subscriber.getId(), CUSTOMER_DOC, CUSTOMER_NAME))
            .thenReturn(null);
        assertThrows(BusinessException.class, () -> 
            orderService.placeOrder(subscriber.getUuid(), CUSTOMER_DOC, CUSTOMER_NAME, ORDER_DESC, ORDER_QTY));
    } 

    @Test
    void test_getOrderByCode_success() {
        // given
        Subscriber subscriber = Subscriber.builder().uuid(SUBSCRIBER_UUID).build();

        Individual customer = Individual.builder()
                .cpf(CUSTOMER_DOC)
                .name(CUSTOMER_NAME)
                .build();

        String orderCode = UUID.randomUUID().toString();
        Order order = TestUtils.createOrder(customer, ORDER_DESC, ORDER_QTY, OrderStatus.PENDING, new Date(), orderCode);

        when(subscriberRepository.findByUuid(SUBSCRIBER_UUID)).thenReturn(Optional.of(subscriber));
        when(orderRepository.findBySubscriberIdAndOrderCode(subscriber.getId(), orderCode)).thenReturn(Optional.of(order));

        // when
        Order result = orderService.getOrderByCode(subscriber.getUuid(), orderCode);

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.getCustomer(), is(customer));
        assertThat(result.getDescription(), is(ORDER_DESC));
        assertThat(result.getQuantity(), is(ORDER_QTY));
        assertThat(result.getStatus(), is(OrderStatus.PENDING));
        assertThat(result.getOrderCode(), is(orderCode));
    }

    @Test
    void test_getOrderByCode_whenOrderNotFound_throwsBusinessException() {
        // given
        Subscriber subscriber = Subscriber.builder().uuid(SUBSCRIBER_UUID).build();

        String orderCode = UUID.randomUUID().toString();
        when(subscriberRepository.findByUuid(SUBSCRIBER_UUID)).thenReturn(Optional.of(subscriber));
        when(orderRepository.findBySubscriberIdAndOrderCode(subscriber.getId(), orderCode)).thenReturn(Optional.empty());

        // when/then
        assertThrows(BusinessException.class, () -> 
            orderService.getOrderByCode(subscriber.getUuid(), orderCode));
    }


    @Test
    void test_getOrderByCode_whenEmptyOrderCode_throwsBusinessException() {
        // given
        Subscriber subscriber = Subscriber.builder().uuid(SUBSCRIBER_UUID).build();

        // when/then
        assertThrows(BusinessException.class, () -> 
            orderService.getOrderByCode(subscriber.getUuid(), ""));
    }

    @Test
    void test_getOrderByCode_whenSubscriberNotFound_throwsBusinessException() {
        // given
        Subscriber subscriber = Subscriber.builder().uuid(SUBSCRIBER_UUID).build();

        when(subscriberRepository.findByUuid(SUBSCRIBER_UUID)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> 
            orderService.placeOrder(subscriber.getUuid(), CUSTOMER_DOC, CUSTOMER_NAME, ORDER_DESC, ORDER_QTY));
    }

    @Test
    void test_getOrdersBySubscriberId_success() {
        // given
        Subscriber subscriber = Subscriber.builder().uuid(SUBSCRIBER_UUID).build();

        Individual customer = Individual.builder()
                .cpf(CUSTOMER_DOC)
                .name(CUSTOMER_NAME)
                .build();

        String orderCode = UUID.randomUUID().toString();
        Order order = TestUtils.createOrder(customer, ORDER_DESC, ORDER_QTY, OrderStatus.PENDING, new Date(), orderCode);
        List<Order> orders = List.of(order);

        when(orderRepository.findBySubscriberId(subscriber.getId())).thenReturn(orders);

        // when
        when(subscriberRepository.findByUuid(SUBSCRIBER_UUID)).thenReturn(Optional.of(subscriber));
        List<Order> result = orderService.getOrdersBySubscriberId(subscriber.getUuid());

        // then
        assertThat(result, Matchers.hasSize(1));
        assertThat(result.get(0).getCustomer(), is(customer));
        assertThat(result.get(0).getDescription(), is(ORDER_DESC));
        assertThat(result.get(0).getQuantity(), is(ORDER_QTY));
        assertThat(result.get(0).getStatus(), is(OrderStatus.PENDING));
        assertThat(result.get(0).getOrderCode(), is(orderCode));
    }

    @Test
    void test_getOrdersBySubscriberId_whenNoOrders_returnsEmptyList() {
        // given
        Subscriber subscriber = Subscriber.builder().uuid(SUBSCRIBER_UUID).build();

        when(orderRepository.findBySubscriberId(subscriber.getId())).thenReturn(Collections.emptyList());

        // when
        when(subscriberRepository.findByUuid(SUBSCRIBER_UUID)).thenReturn(Optional.of(subscriber));
        List<Order> result = orderService.getOrdersBySubscriberId(subscriber.getUuid());

        // then
        assertThat(result, Matchers.empty());
    }

    @Test
    void test_getOrdersBySubscriberId_whenSubscriberIdNull_throwsBusinessException() {
        // when/then
        assertThrows(BusinessException.class, () -> 
            orderService.getOrdersBySubscriberId(null));
    }

}





