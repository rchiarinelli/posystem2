package br.com.brainyit.posystem2.order.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.querydsl.core.types.Predicate;

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
import br.com.brainyit.posystem2.util.MessagesUtil;
import br.com.brainyit.util.TestUtils;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

        private final SimpleDateFormat sdf = new SimpleDateFormat(
                        MessagesUtil.getInstance().getSystemProp("date_pattern"));

        private static final String SUBSCRIBER_UUID = "123e4567-e89b-12d3-a456-426614174000";

        private static final int CUSTOMER_ID = 1;

        private static final String CUSTOMER_DOC = "12345678901";

        private static final String CUSTOMER_NAME = "John Doe";

        private static final String ORDER_DESC = "Test Order";

        private static final Double ORDER_QTY = 1.0;

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

        @Test
        void test_placeOrder_withIndividual_success() {
                // given
                Individual customer = Individual.builder()
                                .id(CUSTOMER_ID)
                                .cpf(CUSTOMER_DOC)
                                .name(CUSTOMER_NAME)
                                .build();

                Subscriber subscriber = Subscriber.builder().uuid(SUBSCRIBER_UUID).build();

                final Date openDate = new Date();

                Order order = TestUtils.createOrder(customer, ORDER_DESC, ORDER_QTY, OrderStatus.PENDING, openDate,
                                UUID.randomUUID().toString());
                order.setSubscriber(subscriber);

                when(subscriberRepository.findByUuid(SUBSCRIBER_UUID)).thenReturn(Optional.of(subscriber));
                when(individualRepository.findByIdAndSubscriberId(customer.getId(), subscriber.getId()))
                                .thenReturn(customer);

                when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

                final Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 1);

                final PlaceOrderParams requestParam = new PlaceOrderParams(customer.getId(), ORDER_DESC,
                                ORDER_QTY, calendar.getTime());

                // when
                Order result = orderService.placeOrder(subscriber.getUuid(), requestParam);

                // then
                assertThat(result, is(notNullValue()));
                assertThat(result.getStatus(), is(OrderStatus.PENDING));

                assertThat(sdf.format(result.getOpenDate()), is(sdf.format(openDate)));
                assertThat(sdf.format(result.getPlannedDeliverDate()),
                                is(sdf.format(requestParam.plannedDeliverDate())));
                assertThat(result.getDescription(), is(ORDER_DESC));
                assertThat(result.getQuantity(), is(ORDER_QTY));
                assertThat(result.getCustomer(), is(customer));
                assertThat(result.getOrderCode(), is(notNullValue()));
                assertThat(result.getSubscriber(), is(notNullValue()));
        }

        @Test
        void test_placeOrder_withCompany_success() {
                Company customer = Company.builder()
                                .id(CUSTOMER_ID)
                                .cnpjCgc(CUSTOMER_DOC)
                                .name(CUSTOMER_NAME)
                                .build();

                Subscriber subscriber = Subscriber.builder().uuid(SUBSCRIBER_UUID).build();

                final Date openDate = new Date();

                Order order = TestUtils.createOrder(customer, ORDER_DESC, ORDER_QTY, OrderStatus.PENDING, openDate,
                                UUID.randomUUID().toString());
                order.setSubscriber(subscriber);

                when(subscriberRepository.findByUuid(SUBSCRIBER_UUID)).thenReturn(Optional.of(subscriber));
                when(companyRepository.findByIdAndSubscriberId(customer.getId(), subscriber.getId()))
                                .thenReturn(customer);

                when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

                // when
                final Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 2);
                final PlaceOrderParams requestParam = new PlaceOrderParams(customer.getId(), ORDER_DESC,
                                ORDER_QTY, calendar.getTime());
                Order result = orderService.placeOrder(subscriber.getUuid(), requestParam);

                // then
                assertThat(result, is(notNullValue()));
                assertThat(result.getStatus(), is(OrderStatus.PENDING));
                assertThat(sdf.format(result.getOpenDate()), is(sdf.format(openDate)));
                assertThat(result.getDescription(), is(ORDER_DESC));
                assertThat(result.getQuantity(), is(ORDER_QTY));
                assertThat(result.getCustomer(), is(customer));
                assertThat(result.getOrderCode(), is(notNullValue()));
                assertThat(result.getSubscriber(), is(notNullValue()));
                assertThat(sdf.format(result.getPlannedDeliverDate()),
                                is(sdf.format(requestParam.plannedDeliverDate())));
        }

        @Test
        void test_placeOrder_whenIndividualCustomerNotFound_throwsBusinessException() {
                Subscriber subscriber = Subscriber.builder().uuid(SUBSCRIBER_UUID).build();

                when(subscriberRepository.findByUuid(SUBSCRIBER_UUID)).thenReturn(Optional.of(subscriber));

                when(individualRepository.findByIdAndSubscriberId(CUSTOMER_ID, subscriber.getId()))
                                .thenReturn(null);
                final PlaceOrderParams requestParam = new PlaceOrderParams(CUSTOMER_ID, ORDER_DESC, ORDER_QTY,
                                new Date());
                assertThrows(BusinessException.class,
                                () -> orderService.placeOrder(subscriber.getUuid(), requestParam));
        }

        @Test
        void test_placeOrder_whenCompanyCustomerNotFound_throwsBusinessException() {
                Subscriber subscriber = Subscriber.builder().uuid(SUBSCRIBER_UUID).build();

                when(subscriberRepository.findByUuid(SUBSCRIBER_UUID)).thenReturn(Optional.of(subscriber));

                when(companyRepository.findByIdAndSubscriberId(CUSTOMER_ID, subscriber.getId()))
                                .thenReturn(null);
                final PlaceOrderParams requestParam = new PlaceOrderParams(CUSTOMER_ID, ORDER_DESC, ORDER_QTY,
                                new Date());
                assertThrows(BusinessException.class,
                                () -> orderService.placeOrder(subscriber.getUuid(), requestParam));
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
                Order order = TestUtils.createOrder(customer, ORDER_DESC, ORDER_QTY, OrderStatus.PENDING, new Date(),
                                orderCode);

                when(subscriberRepository.findByUuid(SUBSCRIBER_UUID)).thenReturn(Optional.of(subscriber));
                when(orderRepository.findBySubscriberIdAndOrderCode(subscriber.getId(), orderCode))
                                .thenReturn(Optional.of(order));

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
                when(orderRepository.findBySubscriberIdAndOrderCode(subscriber.getId(), orderCode))
                                .thenReturn(Optional.empty());

                // when/then
                assertThrows(BusinessException.class,
                                () -> orderService.getOrderByCode(subscriber.getUuid(), orderCode));
        }

        @Test
        void test_getOrderByCode_whenEmptyOrderCode_throwsBusinessException() {
                // given
                Subscriber subscriber = Subscriber.builder().uuid(SUBSCRIBER_UUID).build();

                // when/then
                assertThrows(BusinessException.class, () -> orderService.getOrderByCode(subscriber.getUuid(), ""));
        }

        @Test
        void test_getOrderByCode_whenSubscriberNotFound_throwsBusinessException() {
                // given
                Subscriber subscriber = Subscriber.builder().uuid(SUBSCRIBER_UUID).build();

                when(subscriberRepository.findByUuid(SUBSCRIBER_UUID)).thenReturn(Optional.empty());
                final PlaceOrderParams requestParam = new PlaceOrderParams(CUSTOMER_ID, ORDER_DESC, ORDER_QTY,
                                new Date());
                assertThrows(BusinessException.class,
                                () -> orderService.placeOrder(subscriber.getUuid(), requestParam));
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
                Order order = TestUtils.createOrder(customer, ORDER_DESC, ORDER_QTY, OrderStatus.PENDING, new Date(),
                                orderCode);
                List<Order> orders = List.of(order);

                when(orderRepository.findAll(any(Predicate.class))).thenReturn(orders);

                // when
                when(subscriberRepository.findByUuid(SUBSCRIBER_UUID)).thenReturn(Optional.of(subscriber));
                List<Order> result = orderService.getOrdersBySubscriberId(subscriber.getUuid(), FilterOrderParams.of());

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

                when(orderRepository.findAll(any(Predicate.class))).thenReturn(Collections.emptyList());

                // when
                when(subscriberRepository.findByUuid(SUBSCRIBER_UUID)).thenReturn(Optional.of(subscriber));
                List<Order> result = orderService.getOrdersBySubscriberId(subscriber.getUuid(), FilterOrderParams.of());

                // then
                assertThat(result, Matchers.empty());
        }

        @Test
        void test_getOrdersBySubscriberId_whenSubscriberIdNull_throwsBusinessException() {
                // when/then
                assertThrows(BusinessException.class,
                                () -> orderService.getOrdersBySubscriberId(null, FilterOrderParams.of()));
        }

        @Test
        void test_cancelOrder_orderAlreadyInExecution_throwsBusinessException() {
                // given
                Subscriber subscriber = Subscriber.builder().uuid(SUBSCRIBER_UUID).build();

                Order order = TestUtils.createOrder(null, ORDER_DESC, ORDER_QTY, OrderStatus.EXECUTING, new Date(),
                                UUID.randomUUID().toString());
                order.setSubscriber(subscriber);

                when(subscriberRepository.findByUuid(SUBSCRIBER_UUID)).thenReturn(Optional.of(subscriber));
                when(orderRepository.findBySubscriberIdAndOrderCode(subscriber.getId(), order.getOrderCode()))
                                .thenReturn(Optional.of(order));

                // when/then
                final BusinessException exception = assertThrows(BusinessException.class,
                                () -> orderService.cancelOrder(subscriber.getUuid(), order.getOrderCode(), "test"));
                assertThat(exception.getMessage(), is("Order is already in execution"));
        }

        @Test
        void test_cancelOrder_success() {
                // given
                Subscriber subscriber = Subscriber.builder().uuid(SUBSCRIBER_UUID).build();

                Order order = TestUtils.createOrder(null, ORDER_DESC, ORDER_QTY, OrderStatus.PENDING, new Date(),
                                UUID.randomUUID().toString());
                order.setSubscriber(subscriber);

                when(subscriberRepository.findByUuid(SUBSCRIBER_UUID)).thenReturn(Optional.of(subscriber));
                when(orderRepository.findBySubscriberIdAndOrderCode(subscriber.getId(), order.getOrderCode()))
                                .thenReturn(Optional.of(order));

                when(orderRepository.save(any(Order.class))).thenReturn(order);

                // when
                Order result = orderService.cancelOrder(subscriber.getUuid(), order.getOrderCode(), "test");

                // then
                assertThat(result, is(notNullValue()));
                assertThat(result.getStatus(), is(OrderStatus.CANCELLED));
                assertThat(result.getCloseDate(), is(notNullValue()));
                assertThat(result.getDescription(),
                                is(ORDER_DESC + "\n\n ->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> REASON: test"));
        }

        @Test
        void test_cancelOrder_whenOrderAlreadyCancelled_throwsBusinessException() {
                // given
                Subscriber subscriber = Subscriber.builder().uuid(SUBSCRIBER_UUID).build();

                Order order = TestUtils.createOrder(null, ORDER_DESC, ORDER_QTY, OrderStatus.CANCELLED, new Date(),
                                UUID.randomUUID().toString());
                order.setSubscriber(subscriber);

                when(subscriberRepository.findByUuid(SUBSCRIBER_UUID)).thenReturn(Optional.of(subscriber));
                when(orderRepository.findBySubscriberIdAndOrderCode(subscriber.getId(), order.getOrderCode()))
                                .thenReturn(Optional.of(order));

                // when/then
                final BusinessException exception = assertThrows(BusinessException.class,
                                () -> orderService.cancelOrder(subscriber.getUuid(), order.getOrderCode(), "test"));
                assertThat(exception.getMessage(), is("Order is already cancelled"));
        }

        @Test
        void test_placeOrder_whenPlannedDeliverDateIsInThePast_throwsBusinessException() {
                Subscriber subscriber = Subscriber.builder().uuid(SUBSCRIBER_UUID).build();

                when(subscriberRepository.findByUuid(SUBSCRIBER_UUID)).thenReturn(Optional.of(subscriber));

                when(companyRepository.findByIdAndSubscriberId(CUSTOMER_ID, subscriber.getId()))
                                .thenReturn(Company.builder().id(CUSTOMER_ID).build());

                final PlaceOrderParams requestParam = new PlaceOrderParams(CUSTOMER_ID, ORDER_DESC, ORDER_QTY,
                                new Date(System.currentTimeMillis() - 1000));
                final BusinessException exception = assertThrows(BusinessException.class,
                                () -> orderService.placeOrder(subscriber.getUuid(), requestParam));
                assertThat(exception.getMessage(), is("Planned deliver date cannot be in the past"));
        }

        private static Stream<FilterOrderParams> filterParamsForGetOrdersBySubscriberId() {
                return Stream.of(
                                FilterOrderParams.of(null, null, null),
                                FilterOrderParams.of(null, null, List.of(OrderStatus.PENDING.name())),
                                FilterOrderParams.of(null, null, List.of(OrderStatus.EXECUTING.name())),
                                FilterOrderParams.of(null, null, List.of(OrderStatus.CANCELLED.name())),
                                FilterOrderParams.of(null, null, List.of(OrderStatus.DELIVERED.name())),
                                FilterOrderParams.of(null, "12345678901", null),
                                FilterOrderParams.of(null, "12345678901", List.of(OrderStatus.PENDING.name())),
                                FilterOrderParams.of(null, "12345678901", List.of(OrderStatus.EXECUTING.name())),
                                FilterOrderParams.of(null, "12345678901", List.of(OrderStatus.CANCELLED.name())),
                                FilterOrderParams.of(null, "12345678901", List.of(OrderStatus.DELIVERED.name())),
                                FilterOrderParams.of("John Doe", null, null),
                                FilterOrderParams.of("John Doe", null, List.of(OrderStatus.PENDING.name())),
                                FilterOrderParams.of("John Doe", null, List.of(OrderStatus.EXECUTING.name())),
                                FilterOrderParams.of("John Doe", null, List.of(OrderStatus.CANCELLED.name())),
                                FilterOrderParams.of("John Doe", null, List.of(OrderStatus.DELIVERED.name())),
                                FilterOrderParams.of("John Doe", "12345678901", null),
                                FilterOrderParams.of("John Doe", "12345678901", List.of(OrderStatus.PENDING.name())),
                                FilterOrderParams.of("John Doe", "12345678901", List.of(OrderStatus.EXECUTING.name())),
                                FilterOrderParams.of("John Doe", "12345678901", List.of(OrderStatus.CANCELLED.name())),
                                FilterOrderParams.of("John Doe", "12345678901", List.of(OrderStatus.DELIVERED.name())));
        }

        @ParameterizedTest
        @MethodSource("filterParamsForGetOrdersBySubscriberId")
        void test_getOrdersBySubscriberId_filterByParams_dataReturned(final FilterOrderParams params) {
                // given
                final Subscriber subscriber = Subscriber.builder().uuid(SUBSCRIBER_UUID).build();

                when(orderRepository.findAll(any(Predicate.class))).thenReturn(List.of(TestUtils.createOrder(null,
                                ORDER_DESC, ORDER_QTY, OrderStatus.PENDING, new Date(), UUID.randomUUID().toString())));

                when(subscriberRepository.findByUuid(SUBSCRIBER_UUID)).thenReturn(Optional.of(subscriber));

                // when
                List<Order> result = orderService.getOrdersBySubscriberId(subscriber.getUuid(), params);

                // then
                assertThat(result, Matchers.hasSize(1));
        }
}
