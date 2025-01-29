package br.com.brainyit.posystem2.order.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
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
    
    private static final Long SUBSCRIBER_ID = 1L;
    
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

        Subscriber subscriber = Subscriber.builder().id(SUBSCRIBER_ID).build();

        final Date openDate = new Date();

        Order order = TestUtils.createOrder(customer, ORDER_DESC, ORDER_QTY, OrderStatus.PENDING, openDate, UUID.randomUUID().toString());
        order.setSubscriber(subscriber);
                
        when(subscriberRepository.findById(SUBSCRIBER_ID)).thenReturn(Optional.of(subscriber));
        when(individualRepository.findBySubscriberIdAndCpfAndName(SUBSCRIBER_ID, CUSTOMER_DOC, CUSTOMER_NAME))
            .thenReturn(customer);
            
        when(orderRepository.save(any(Order.class))).thenReturn(order);
            
        // when
        Order result = orderService.placeOrder(SUBSCRIBER_ID, CUSTOMER_DOC, CUSTOMER_NAME, ORDER_DESC, ORDER_QTY);
        
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

        Subscriber subscriber = Subscriber.builder().id(SUBSCRIBER_ID).build();

        final Date openDate = new Date();

        Order order = TestUtils.createOrder(customer, ORDER_DESC, ORDER_QTY, OrderStatus.PENDING, openDate, UUID.randomUUID().toString());
        order.setSubscriber(subscriber);

        when(subscriberRepository.findById(SUBSCRIBER_ID)).thenReturn(Optional.of(subscriber));
        when(companyRepository.findBySubscriberIdAndCnpjCgcAndName(SUBSCRIBER_ID, CUSTOMER_DOC,CUSTOMER_NAME))
            .thenReturn(customer);

        when(orderRepository.save(any(Order.class))).thenReturn(order); 

        // when
        Order result = orderService.placeOrder(SUBSCRIBER_ID, CUSTOMER_DOC, CUSTOMER_NAME, ORDER_DESC, ORDER_QTY);
        
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
        when(subscriberRepository.findById(SUBSCRIBER_ID)).thenReturn(Optional.of(Subscriber.builder().id(SUBSCRIBER_ID).build()));

        when(individualRepository.findBySubscriberIdAndCpfAndName(SUBSCRIBER_ID,  CUSTOMER_DOC, CUSTOMER_NAME))
            .thenReturn(null);
        assertThrows(BusinessException.class, () -> 
            orderService.placeOrder(SUBSCRIBER_ID, CUSTOMER_DOC, CUSTOMER_NAME, ORDER_DESC, ORDER_QTY));
    } 

    @Test
    void test_placeOrder_whenCompanyCustomerNotFound_throwsBusinessException() {
        when(subscriberRepository.findById(SUBSCRIBER_ID)).thenReturn(Optional.of(Subscriber.builder().id(SUBSCRIBER_ID).build()));
        
        when(companyRepository.findBySubscriberIdAndCnpjCgcAndName(SUBSCRIBER_ID, CUSTOMER_DOC, CUSTOMER_NAME))
            .thenReturn(null);
        assertThrows(BusinessException.class, () -> 
            orderService.placeOrder(SUBSCRIBER_ID, CUSTOMER_DOC, CUSTOMER_NAME, ORDER_DESC, ORDER_QTY));
    } 

    @Test
    void test_getOrderByCode_success() {
        // given
        Individual customer = Individual.builder()
                .cpf(CUSTOMER_DOC)
                .name(CUSTOMER_NAME)
                .build();

        String orderCode = UUID.randomUUID().toString();
        Order order = TestUtils.createOrder(customer, ORDER_DESC, ORDER_QTY, OrderStatus.PENDING, new Date(), orderCode);

        when(orderRepository.findBySubscriberIdAndOrderCode(SUBSCRIBER_ID, orderCode)).thenReturn(Optional.of(order));

        // when
        Order result = orderService.getOrderByCode(SUBSCRIBER_ID, orderCode);

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
        String orderCode = UUID.randomUUID().toString();
        when(orderRepository.findBySubscriberIdAndOrderCode(SUBSCRIBER_ID, orderCode)).thenReturn(Optional.empty());

        // when/then
        assertThrows(BusinessException.class, () -> 
            orderService.getOrderByCode(SUBSCRIBER_ID, orderCode));
    }


    @Test
    void test_getOrderByCode_whenEmptyOrderCode_throwsBusinessException() {
        // when/then
        assertThrows(BusinessException.class, () -> 
            orderService.getOrderByCode(SUBSCRIBER_ID, ""));
    }

    @Test
    void test_getOrderByCode_whenSubscriberNotFound_throwsBusinessException() {
        when(subscriberRepository.findById(SUBSCRIBER_ID)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> 
            orderService.placeOrder(SUBSCRIBER_ID, CUSTOMER_DOC, CUSTOMER_NAME, ORDER_DESC, ORDER_QTY));
    }

    @Test
    void test_getOrdersBySubscriberId_success() {
        // given
        Individual customer = Individual.builder()
                .cpf(CUSTOMER_DOC)
                .name(CUSTOMER_NAME)
                .build();

        String orderCode = UUID.randomUUID().toString();
        Order order = TestUtils.createOrder(customer, ORDER_DESC, ORDER_QTY, OrderStatus.PENDING, new Date(), orderCode);
        List<Order> orders = List.of(order);

        when(orderRepository.findBySubscriberId(SUBSCRIBER_ID)).thenReturn(orders);

        // when
        List<Order> result = orderService.getOrdersBySubscriberId(SUBSCRIBER_ID);

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
        when(orderRepository.findBySubscriberId(SUBSCRIBER_ID)).thenReturn(Collections.emptyList());

        // when
        List<Order> result = orderService.getOrdersBySubscriberId(SUBSCRIBER_ID);

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





