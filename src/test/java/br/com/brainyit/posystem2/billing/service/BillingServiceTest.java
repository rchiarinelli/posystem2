package br.com.brainyit.posystem2.billing.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.querydsl.core.types.Predicate;

import br.com.brainyit.posystem2.billing.repository.BillingConfigRepository;
import br.com.brainyit.posystem2.billing.repository.BillingRepository;
import br.com.brainyit.posystem2.domain.Billing;
import br.com.brainyit.posystem2.domain.BillingConfig;
import br.com.brainyit.posystem2.domain.BillingStatus;
import br.com.brainyit.posystem2.domain.Customer;
import br.com.brainyit.posystem2.domain.Individual;
import br.com.brainyit.posystem2.domain.Installment;
import br.com.brainyit.posystem2.domain.InstallmentIntervalUnit;
import br.com.brainyit.posystem2.domain.InstallmentStatus;
import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.domain.OrderStatus;
import br.com.brainyit.posystem2.domain.PaymentType;
import br.com.brainyit.posystem2.domain.Subscriber;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.exception.NotFoundException;
import br.com.brainyit.posystem2.order.service.OrderService;
import br.com.brainyit.posystem2.repository.SubscriberRepository;

@ExtendWith(MockitoExtension.class)
class BillingServiceTest {

        @InjectMocks
        private BillingService billingService;

        @Mock
        private BillingRepository billingRepository;

        @Mock
        private SubscriberRepository subscriberRepository;

        @Mock
        private BillingConfigRepository billingConfigRepository;

        @Mock
        private OrderService orderService;

        private Subscriber subscriber;
        private String subscriberUuid;
        private Billing billing1;
        private Billing billing2;
        private Billing billing3;
        private List<Billing> billings;

        @BeforeEach
        void setUp() {
                subscriberUuid = "test-subscriber-uuid";
                subscriber = Subscriber.builder()
                                .id(1L)
                                .uuid(subscriberUuid)
                                .name("Test Subscriber")
                                .email("test@example.com")
                                .build();

                Customer customer = Individual.builder()
                                .id(1)
                                .name("Test Customer")
                                .cpf("123.456.789-00")
                                .subscriber(subscriber)
                                .build();

                Order order1 = Order.builder()
                                .id(1L)
                                .orderCode("ORDER-001")
                                .description("Test Order 1")
                                .customer(customer)
                                .status(OrderStatus.BILLED)
                                .subscriber(subscriber)
                                .build();

                Order order2 = Order.builder()
                                .id(2L)
                                .orderCode("ORDER-002")
                                .description("Test Order 2")
                                .customer(customer)
                                .status(OrderStatus.BILLED)
                                .subscriber(subscriber)
                                .build();

                billing1 = Billing.builder()
                                .id(1L)
                                .uuid("billing-uuid-1")
                                .order(order1)
                                .calculatedValue(100.0)
                                .finalValue(100.0)
                                .createdAt(LocalDate.now())
                                .status(BillingStatus.OPEN)
                                .subscriber(subscriber)
                                .installments(List.of())
                                .build();

                billing2 = Billing.builder()
                                .id(2L)
                                .uuid("billing-uuid-2")
                                .order(order2)
                                .calculatedValue(200.0)
                                .finalValue(200.0)
                                .createdAt(LocalDate.now())
                                .status(BillingStatus.CLOSED)
                                .subscriber(subscriber)
                                .installments(List.of())
                                .build();
                billing3 = Billing.builder()
                                .id(3L)
                                .uuid("billing-uuid-3")
                                .order(order2)
                                .calculatedValue(200.0)
                                .finalValue(200.0)
                                .createdAt(LocalDate.now())
                                .status(BillingStatus.CLOSED)
                                .subscriber(subscriber)
                                .installments(List.of(Installment.builder()
                                                .id(1L)
                                                .uuid("installment-uuid-1")
                                                .billing(billing3)
                                                .dueValue(100.0)
                                                .dueDate(LocalDate.now())
                                                .status(InstallmentStatus.PENDING)
                                                .build()))
                                .build();

                billings = Arrays.asList(billing1, billing2);
        }

        @Test
        void testGetBillings_WhenNoFilters_ShouldReturnAllBillings() {
                // Arrange
                when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));
                when(billingRepository.findAll(any(Predicate.class))).thenReturn(billings);

                // Act
                List<Billing> result = billingService.getBillings(subscriberUuid, null, null);

                // Assert
                assertNotNull(result);
                assertEquals(2, result.size());
                assertEquals(billing1, result.get(0));
                assertEquals(billing2, result.get(1));
                verify(billingRepository).findAll(any(Predicate.class));
        }

        @Test
        void testGetBillings_WhenSearchTermProvided_ShouldReturnFilteredBillings() {
                // Arrange
                String searchTerm = "Test Customer";
                when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));
                when(billingRepository.findAll(any(Predicate.class)))
                                .thenReturn(Collections.singletonList(billing1));

                // Act
                List<Billing> result = billingService.getBillings(subscriberUuid, searchTerm, null);

                // Assert
                assertNotNull(result);
                assertEquals(1, result.size());
                assertEquals(billing1, result.get(0));
                verify(billingRepository).findAll(any(Predicate.class));
        }

        @Test
        void testGetBillings_WhenStatusFiltersProvided_ShouldReturnFilteredBillings() {
                // Arrange
                List<String> statusFilters = Arrays.asList("OPEN", "CLOSED");
                when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));
                when(billingRepository.findAll(any(Predicate.class))).thenReturn(billings);

                // Act
                List<Billing> result = billingService.getBillings(subscriberUuid, null, statusFilters);

                // Assert
                assertNotNull(result);
                assertEquals(2, result.size());
                verify(billingRepository).findAll(any(Predicate.class));
        }

        @Test
        void testGetBillings_WhenBothFiltersProvided_ShouldReturnFilteredBillings() {
                // Arrange
                String searchTerm = "ORDER-001";
                List<String> statusFilters = Arrays.asList("OPEN");
                when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));
                when(billingRepository.findAll(any(Predicate.class)))
                                .thenReturn(Collections.singletonList(billing1));

                // Act
                List<Billing> result = billingService.getBillings(subscriberUuid, searchTerm, statusFilters);

                // Assert
                assertNotNull(result);
                assertEquals(1, result.size());
                assertEquals(billing1, result.get(0));
                verify(billingRepository).findAll(any(Predicate.class));
        }

        @Test
        void testGetBillings_WhenSubscriberNotFound_ShouldThrowBusinessException() {
                // Arrange
                when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.empty());

                // Act & Assert
                BusinessException exception = assertThrows(BusinessException.class, () -> {
                        billingService.getBillings(subscriberUuid, null, null);
                });

                assertEquals("Subscriber not found", exception.getMessage());
                verify(billingRepository, never()).findAll(any(Predicate.class));
        }

        @Test
        void testGetBillings_WhenInvalidStatus_ShouldThrowBusinessException() {
                // Arrange
                List<String> statusFilters = Arrays.asList("INVALID_STATUS");
                when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));

                // Act & Assert
                BusinessException exception = assertThrows(BusinessException.class, () -> {
                        billingService.getBillings(subscriberUuid, null, statusFilters);
                });

                assertTrue(exception.getMessage().contains("Invalid billing status: INVALID_STATUS"));
                assertTrue(exception.getMessage().contains("Valid statuses are:"));
                verify(billingRepository, never()).findAll(any(Predicate.class));
        }

        @Test
        void testGetBillings_WhenEmptySearchTerm_ShouldTreatAsNull() {
                // Arrange
                String emptySearchTerm = "   ";
                when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));
                when(billingRepository.findAll(any(Predicate.class))).thenReturn(billings);

                // Act
                List<Billing> result = billingService.getBillings(subscriberUuid, emptySearchTerm, null);

                // Assert
                assertNotNull(result);
                assertEquals(2, result.size());
                verify(billingRepository).findAll(any(Predicate.class));
        }

        @Test
        void testGetBillings_WhenEmptyStatusFilters_ShouldTreatAsNull() {
                // Arrange
                List<String> emptyStatusFilters = Collections.emptyList();
                when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));
                when(billingRepository.findAll(any(Predicate.class))).thenReturn(billings);

                // Act
                List<Billing> result = billingService.getBillings(subscriberUuid, null, emptyStatusFilters);

                // Assert
                assertNotNull(result);
                assertEquals(2, result.size());
                verify(billingRepository).findAll(any(Predicate.class));
        }

        @Test
        void testGetBillings_WhenStatusFiltersAreLowercase_ShouldWorkCorrectly() {
                // Arrange
                List<String> statusFilters = Arrays.asList("open", "closed");
                when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));
                when(billingRepository.findAll(any(Predicate.class))).thenReturn(billings);

                // Act
                List<Billing> result = billingService.getBillings(subscriberUuid, null, statusFilters);

                // Assert
                assertNotNull(result);
                assertEquals(2, result.size());
                verify(billingRepository).findAll(any(Predicate.class));
        }

        @Test
        void testGetBillings_WhenMixedCaseStatusFilters_ShouldWorkCorrectly() {
                // Arrange
                List<String> statusFilters = Arrays.asList("Open", "cLOSED");
                when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));
                when(billingRepository.findAll(any(Predicate.class))).thenReturn(billings);

                // Act
                List<Billing> result = billingService.getBillings(subscriberUuid, null, statusFilters);

                // Assert
                assertNotNull(result);
                assertEquals(2, result.size());
                verify(billingRepository).findAll(any(Predicate.class));
        }

        @Test
        void test_createBilling_emptyParams_exceptionIsThrown() {
                BusinessException exception = assertThrows(BusinessException.class, () -> {
                        billingService.createBillings(subscriberUuid, List.of());
                });

                assertEquals("Not allowed to create billings with empty data.", exception.getMessage());
                verify(billingRepository, never()).save(any(Billing.class));
        }

        @Test
        void test_createBillings_orderNotInPendingBilling_exceptionIsThrown() {
                // Arrange
                Order order = Order.builder()
                                .id(1L)
                                .orderCode("ORDER-001")
                                .description("Test Order 1")
                                .status(OrderStatus.EXECUTING)
                                .subscriber(subscriber)
                                .build();

                CreateBillingParams createBillingParams = new CreateBillingParams(
                                order.getOrderCode(),
                                1,
                                LocalDate.now(),
                                100.0);

                when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));
                when(orderService.getOrderByCode(subscriberUuid, order.getOrderCode()))
                                .thenReturn(order);

                // Act & Assert
                BusinessException exception = assertThrows(BusinessException.class, () -> {
                        billingService.createBillings(subscriberUuid, List.of(createBillingParams));
                });

                assertEquals("Order is not in pending billing status", exception.getMessage());
                verify(billingRepository, never()).save(any(Billing.class));
        }

        @Test
        void test_createBillings_billingConfigNotFound_exceptionIsThrown() {
                // Arrange
                CreateBillingParams createBillingParams = new CreateBillingParams(
                                "ORDER-001",
                                1,
                                LocalDate.now(),
                                100.0);

                Order order = Order.builder()
                                .id(1L)
                                .orderCode("ORDER-001")
                                .description("Test Order 1")
                                .status(OrderStatus.PENDING_BILLING)
                                .subscriber(subscriber)
                                .build();

                when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));
                when(orderService.getOrderByCode(subscriberUuid, createBillingParams.orderCode()))
                                .thenReturn(order);
                when(billingConfigRepository.findById(createBillingParams.billingConfigId()))
                                .thenReturn(Optional.empty());

                // Act & Assert
                BusinessException exception = assertThrows(BusinessException.class, () -> {
                        billingService.createBillings(subscriberUuid, List.of(createBillingParams));
                });

                assertEquals("Billing config not found", exception.getMessage());
                verify(billingRepository, never()).save(any(Billing.class));
        }

        @Test
        void test_createBillings_startDateIsInThePast_exceptionIsThrown() {
                // Arrange
                CreateBillingParams createBillingParams = new CreateBillingParams(
                                "ORDER-001",
                                1,
                                LocalDate.now().minusDays(1),
                                100.0);

                Order order = Order.builder()
                                .id(1L)
                                .orderCode("ORDER-001")
                                .description("Test Order 1")
                                .status(OrderStatus.PENDING_BILLING)
                                .subscriber(subscriber)
                                .build();

                BillingConfig billingConfig = BillingConfig.builder()
                                .id(1)
                                .name("Test Billing Config")
                                .subscriber(subscriber)
                                .paymentType(PaymentType.INSTALLMENTS)
                                .installmentsQuantity((short) 1)
                                .installmentsInterval((short) 1)
                                .installmentIntervalUnit(InstallmentIntervalUnit.MONTH)
                                .build();

                when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));
                when(orderService.getOrderByCode(subscriberUuid, createBillingParams.orderCode()))
                                .thenReturn(order);
                when(billingConfigRepository.findById(createBillingParams.billingConfigId()))
                                .thenReturn(Optional.of(billingConfig));

                // Act & Assert
                BusinessException exception = assertThrows(BusinessException.class, () -> {
                        billingService.createBillings(subscriberUuid, List.of(createBillingParams));
                });

                assertEquals("Start date cannot be in the past", exception.getMessage());
                verify(billingRepository, never()).save(any(Billing.class));
        }

        @Test
        void test_createBillings_oneTimePaymentBillingConfigFound_billingIsCreated() {

                final LocalDate startDate = LocalDate.now();

                // Arrange
                CreateBillingParams createBillingParams = new CreateBillingParams(
                                "ORDER-001",
                                1,
                                startDate,
                                100.0);

                Order order = Order.builder()
                                .id(1L)
                                .orderCode("ORDER-001")
                                .description("Test Order 1")
                                .status(OrderStatus.PENDING_BILLING)
                                .subscriber(subscriber)
                                .price(100.0)
                                .build();

                BillingConfig billingConfig = BillingConfig.builder()
                                .id(1)
                                .name("Test Billing Config")
                                .subscriber(subscriber)
                                .paymentType(PaymentType.ONE_TIME)
                                .build();

                when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));
                when(orderService.getOrderByCode(subscriberUuid, createBillingParams.orderCode()))
                                .thenReturn(order);
                when(billingConfigRepository.findById(createBillingParams.billingConfigId()))
                                .thenReturn(Optional.of(billingConfig));

                when(billingRepository.save(any(Billing.class))).thenAnswer(invocation -> invocation.getArgument(0));

                // Act
                List<Billing> result = billingService.createBillings(subscriberUuid, List.of(createBillingParams));

                // Assert
                assertThat(result.size(), is(1));
                assertThat(result.get(0).getUuid(), is(not(nullValue())));
                assertThat(result.get(0).getBillingConfig(), is(billingConfig));
                assertThat(result.get(0).getOrder(), is(order));
                assertThat(result.get(0).getCalculatedValue(), is(100.0));
                assertThat(result.get(0).getFinalValue(), is(100.0));
                assertThat(result.get(0).getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE),
                                is(startDate.format(DateTimeFormatter.ISO_LOCAL_DATE)));
                assertThat(result.get(0).getStatus(), is(BillingStatus.OPEN));
                assertThat(result.get(0).getSubscriber(), is(subscriber));
                assertThat(result.get(0).getInstallments().size(), is(1));

                assertThat(result.get(0).getInstallments().get(0).getDueValue(), is(100.0));
                assertThat(result.get(0).getInstallments().get(0).getDueDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                                is(startDate.format(DateTimeFormatter.ISO_LOCAL_DATE)));
                assertThat(result.get(0).getInstallments().get(0).getUuid(), is(not(nullValue())));
                assertThat(result.get(0).getInstallments().get(0).getStatus(), is(InstallmentStatus.PENDING));

                verify(billingRepository).save(result.get(0));
        }

        @Test
        void test_createBillings_multipleInstallmentsBillingConfigFound_installmentsAreCreated() {

                final LocalDate startDate = LocalDate.now();

                // Arrange
                CreateBillingParams createBillingParams = new CreateBillingParams(
                                "ORDER-001",
                                1,
                                startDate,
                                100.0);

                Order order = Order.builder()
                                .id(1L)
                                .orderCode("ORDER-001")
                                .description("Test Order 1")
                                .status(OrderStatus.PENDING_BILLING)
                                .subscriber(subscriber)
                                .price(100.0)
                                .build();

                BillingConfig billingConfig = BillingConfig.builder()
                                .id(1)
                                .name("Test Billing Config")
                                .subscriber(subscriber)
                                .paymentType(PaymentType.INSTALLMENTS)
                                .installmentsQuantity((short) 2)
                                .installmentsInterval((short) 1)
                                .installmentIntervalUnit(InstallmentIntervalUnit.MONTH)
                                .build();

                when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));
                when(orderService.getOrderByCode(subscriberUuid, createBillingParams.orderCode()))
                                .thenReturn(order);
                when(billingConfigRepository.findById(createBillingParams.billingConfigId()))
                                .thenReturn(Optional.of(billingConfig));

                when(billingRepository.save(any(Billing.class))).thenAnswer(invocation -> invocation.getArgument(0));

                // Act
                List<Billing> result = billingService.createBillings(subscriberUuid, List.of(createBillingParams));

                // Assert
                assertThat(result.size(), is(1));
                assertThat(result.get(0).getUuid(), is(not(nullValue())));
                assertThat(result.get(0).getBillingConfig(), is(billingConfig));
                assertThat(result.get(0).getOrder(), is(order));
                assertThat(result.get(0).getCalculatedValue(), is(100.0));
                assertThat(result.get(0).getFinalValue(), is(100.0));

                assertThat(result.get(0).getInstallments().size(), is(2));
                assertThat(result.get(0).getInstallments().get(0).getDueValue(), is(50.0));
                assertThat(result.get(0).getInstallments().get(0).getDueDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                                is(startDate.format(DateTimeFormatter.ISO_LOCAL_DATE)));
                assertThat(result.get(0).getInstallments().get(0).getUuid(), is(not(nullValue())));
                assertThat(result.get(0).getInstallments().get(0).getStatus(), is(InstallmentStatus.PENDING));

                assertThat(result.get(0).getInstallments().get(1).getDueValue(), is(50.0));
                assertThat(result.get(0).getInstallments().get(1).getDueDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                                is(startDate.plusMonths(1).format(DateTimeFormatter.ISO_LOCAL_DATE)));
                assertThat(result.get(0).getInstallments().get(1).getUuid(), is(not(nullValue())));
                assertThat(result.get(0).getInstallments().get(1).getStatus(), is(InstallmentStatus.PENDING));

                verify(billingRepository).save(result.get(0));
        }

        @Test
        void test_fetchBilling_withInvalidUuid_exceptionIsThrown() {
                // Arrange
                when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));
                when(billingRepository.findBySubscriberIdAndUuid(subscriber.getId(), "invalid-uuid"))
                                .thenReturn(Optional.empty());

                // Act & Assert
                NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                        billingService.fetchBilling(subscriberUuid, "invalid-uuid");
                });

                assertEquals("Billing not found", exception.getMessage());
                verify(billingRepository).findBySubscriberIdAndUuid(subscriber.getId(), "invalid-uuid");
                verify(subscriberRepository).findByUuid(subscriberUuid);
        }

        @Test
        void test_fetchBilling_withValidUuid_billingIsReturned() {
                // Arrange
                when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));
                when(billingRepository.findBySubscriberIdAndUuid(subscriber.getId(), "billing-uuid-1"))
                                .thenReturn(Optional.of(billing1));

                // Act
                Billing result = billingService.fetchBilling(subscriberUuid, "billing-uuid-1");

                // Assert
                assertThat(result, is(billing1));
                verify(billingRepository).findBySubscriberIdAndUuid(subscriber.getId(), "billing-uuid-1");
                verify(subscriberRepository).findByUuid(subscriberUuid);
        }

        @Test
        void test_fetchInstallment_withInvalidInstallmentUuid_exceptionIsThrown() {
                // Arrange
                when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));
                when(billingRepository.findBySubscriberIdAndUuid(subscriber.getId(), "billing-uuid-1"))
                                .thenReturn(Optional.of(billing1));

                // Act & Assert
                NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                        billingService.fetchInstallment(subscriberUuid, "billing-uuid-1", "invalid-installment-uuid");
                });

                assertEquals("Installment not found", exception.getMessage());
                verify(billingRepository).findBySubscriberIdAndUuid(subscriber.getId(), "billing-uuid-1");
                verify(subscriberRepository).findByUuid(subscriberUuid);
        }

        @Test
        void test_fetchInstallment_withValidInstallmentUuid_installmentIsReturned() {
                // Arrange
                when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));
                when(billingRepository.findBySubscriberIdAndUuid(subscriber.getId(), "billing-uuid-3"))
                                .thenReturn(Optional.of(billing3));

                // Act
                Installment result = billingService.fetchInstallment(subscriberUuid, "billing-uuid-3",
                                "installment-uuid-1");

                // Assert
                assertThat(result, is(billing3.getInstallments().get(0)));
                verify(billingRepository).findBySubscriberIdAndUuid(subscriber.getId(), "billing-uuid-3");
                verify(subscriberRepository).findByUuid(subscriberUuid);
        }
}