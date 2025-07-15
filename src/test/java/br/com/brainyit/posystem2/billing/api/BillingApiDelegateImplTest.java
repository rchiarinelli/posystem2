package br.com.brainyit.posystem2.billing.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.brainyit.posystem2.billing.service.BillingService;
import br.com.brainyit.posystem2.domain.Billing;
import br.com.brainyit.posystem2.domain.BillingStatus;
import br.com.brainyit.posystem2.domain.Customer;
import br.com.brainyit.posystem2.domain.Individual;
import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.domain.OrderStatus;
import br.com.brainyit.posystem2.domain.Subscriber;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.model.BillingModel;

@ExtendWith(MockitoExtension.class)
class BillingApiDelegateImplTest {

        @Mock
        private BillingService billingService;

        @InjectMocks
        private BillingApiDelegateImpl billingApiDelegate;

        private String subscriberUuid;
        private Subscriber subscriber;
        private Customer customer;
        private Order order1;
        private Order order2;
        private Billing billing1;
        private Billing billing2;
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

                customer = Individual.builder()
                                .id(1)
                                .name("Test Customer")
                                .cpf("123.456.789-00")
                                .subscriber(subscriber)
                                .build();

                order1 = Order.builder()
                                .id(1L)
                                .orderCode("ORDER-001")
                                .description("Test Order 1")
                                .customer(customer)
                                .status(OrderStatus.BILLED)
                                .subscriber(subscriber)
                                .openDate(new Date())
                                .quantity(1.0)
                                .build();

                order2 = Order.builder()
                                .id(2L)
                                .orderCode("ORDER-002")
                                .description("Test Order 2")
                                .customer(customer)
                                .status(OrderStatus.BILLED)
                                .subscriber(subscriber)
                                .openDate(new Date())
                                .quantity(2.0)
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
                                .build();

                billings = Arrays.asList(billing1, billing2);
        }

        @Test
        void testSubscriberBillingsGet_WhenNoFilters_ShouldReturnAllBillings() throws Exception {
                // Arrange
                when(billingService.getBillings(subscriberUuid, null, null)).thenReturn(billings);

                // Act
                ResponseEntity<List<BillingModel>> response = billingApiDelegate.subscriberBillingsGet(subscriberUuid,
                                null,
                                null);

                // Assert
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertEquals(2, response.getBody().size());

                BillingModel firstBilling = response.getBody().get(0);
                assertEquals("billing-uuid-1", firstBilling.getUuid());
                assertEquals("OPEN", firstBilling.getStatus());
                assertEquals(100.0, firstBilling.getCalculatedValue());

                BillingModel secondBilling = response.getBody().get(1);
                assertEquals("billing-uuid-2", secondBilling.getUuid());
                assertEquals("CLOSED", secondBilling.getStatus());
                assertEquals(200.0, secondBilling.getCalculatedValue());

                verify(billingService).getBillings(subscriberUuid, null, null);
        }

        @Test
        void testSubscriberBillingsGet_WhenSearchTermProvided_ShouldReturnFilteredBillings() throws Exception {
                // Arrange
                String searchTerm = "Test Order";
                when(billingService.getBillings(subscriberUuid, searchTerm, null))
                                .thenReturn(Collections.singletonList(billing1));

                // Act
                ResponseEntity<List<BillingModel>> response = billingApiDelegate.subscriberBillingsGet(subscriberUuid,
                                searchTerm, null);

                // Assert
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertEquals(1, response.getBody().size());

                BillingModel billingModel = response.getBody().get(0);
                assertEquals("billing-uuid-1", billingModel.getUuid());
                assertEquals("OPEN", billingModel.getStatus());
                assertEquals(100.0, billingModel.getCalculatedValue());

                verify(billingService).getBillings(subscriberUuid, searchTerm, null);
        }

        @Test
        void testSubscriberBillingsGet_WhenStatusFiltersProvided_ShouldReturnFilteredBillings() throws Exception {
                // Arrange
                List<String> statusFilters = Arrays.asList("OPEN", "CLOSED");
                when(billingService.getBillings(subscriberUuid, null, statusFilters)).thenReturn(billings);

                // Act
                ResponseEntity<List<BillingModel>> response = billingApiDelegate.subscriberBillingsGet(subscriberUuid,
                                null,
                                statusFilters);

                // Assert
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertEquals(2, response.getBody().size());

                verify(billingService).getBillings(subscriberUuid, null, statusFilters);
        }

        @Test
        void testSubscriberBillingsGet_WhenBothFiltersProvided_ShouldReturnFilteredBillings() throws Exception {
                // Arrange
                String searchTerm = "Test Order";
                List<String> statusFilters = Arrays.asList("OPEN");
                when(billingService.getBillings(subscriberUuid, searchTerm, statusFilters))
                                .thenReturn(Collections.singletonList(billing1));

                // Act
                ResponseEntity<List<BillingModel>> response = billingApiDelegate.subscriberBillingsGet(subscriberUuid,
                                searchTerm, statusFilters);

                // Assert
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertEquals(1, response.getBody().size());

                BillingModel billingModel = response.getBody().get(0);
                assertEquals("billing-uuid-1", billingModel.getUuid());
                assertEquals("OPEN", billingModel.getStatus());

                verify(billingService).getBillings(subscriberUuid, searchTerm, statusFilters);
        }

        @Test
        void testSubscriberBillingsGet_WhenEmptyResult_ShouldReturnEmptyList() throws Exception {
                // Arrange
                when(billingService.getBillings(subscriberUuid, null, null)).thenReturn(Collections.emptyList());

                // Act
                ResponseEntity<List<BillingModel>> response = billingApiDelegate.subscriberBillingsGet(subscriberUuid,
                                null,
                                null);

                // Assert
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertTrue(response.getBody().isEmpty());

                verify(billingService).getBillings(subscriberUuid, null, null);
        }

        @Test
        void testSubscriberBillingsGet_WhenServiceThrowsBusinessException_ShouldPropagateException() throws Exception {
                // Arrange
                BusinessException exception = new BusinessException("Subscriber not found");
                when(billingService.getBillings(subscriberUuid, null, null)).thenThrow(exception);

                // Act & Assert
                BusinessException thrownException = assertThrows(BusinessException.class, () -> {
                        billingApiDelegate.subscriberBillingsGet(subscriberUuid, null, null);
                });

                assertEquals("Subscriber not found", thrownException.getMessage());
                verify(billingService).getBillings(subscriberUuid, null, null);
        }

        @Test
        void testSubscriberBillingsGet_WhenServiceThrowsGenericException_ShouldPropagateException() throws Exception {
                // Arrange
                RuntimeException exception = new RuntimeException("Database error");
                when(billingService.getBillings(subscriberUuid, null, null)).thenThrow(exception);

                // Act & Assert
                RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
                        billingApiDelegate.subscriberBillingsGet(subscriberUuid, null, null);
                });

                assertEquals("Database error", thrownException.getMessage());
                verify(billingService).getBillings(subscriberUuid, null, null);
        }

        @Test
        void testSubscriberBillingsGet_WhenBillingHasInstallments_ShouldIncludeInstallments() throws Exception {
                // Arrange
                billing1.setInstallments(Collections.emptyList()); // Set empty for this test
                when(billingService.getBillings(subscriberUuid, null, null))
                                .thenReturn(Collections.singletonList(billing1));

                // Act
                ResponseEntity<List<BillingModel>> response = billingApiDelegate.subscriberBillingsGet(subscriberUuid,
                                null,
                                null);

                // Assert
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertEquals(1, response.getBody().size());

                BillingModel billingModel = response.getBody().get(0);
                assertEquals("billing-uuid-1", billingModel.getUuid());
                assertNotNull(billingModel.getOrder());
                assertEquals("ORDER-001", billingModel.getOrder().getOrderCode());
                assertEquals("Test Order 1", billingModel.getOrder().getOrderDescription());

                verify(billingService).getBillings(subscriberUuid, null, null);
        }

        @Test
        void testSubscriberBillingsGet_WhenBillingHasNullOrder_ShouldHandleGracefully() throws Exception {
                // Arrange
                billing1.setOrder(null);
                when(billingService.getBillings(subscriberUuid, null, null))
                                .thenReturn(Collections.singletonList(billing1));

                // Act
                ResponseEntity<List<BillingModel>> response = billingApiDelegate.subscriberBillingsGet(subscriberUuid,
                                null,
                                null);

                // Assert
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertEquals(1, response.getBody().size());

                BillingModel billingModel = response.getBody().get(0);
                assertEquals("billing-uuid-1", billingModel.getUuid());
                assertNull(billingModel.getOrder());

                verify(billingService).getBillings(subscriberUuid, null, null);
        }

        @Test
        void testSubscriberBillingsGet_WhenMultipleStatusFilters_ShouldPassCorrectly() throws Exception {
                // Arrange
                List<String> statusFilters = Arrays.asList("OPEN", "CLOSED", "CANCELLED");
                when(billingService.getBillings(subscriberUuid, null, statusFilters)).thenReturn(billings);

                // Act
                ResponseEntity<List<BillingModel>> response = billingApiDelegate.subscriberBillingsGet(subscriberUuid,
                                null,
                                statusFilters);

                // Assert
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertEquals(2, response.getBody().size());

                verify(billingService).getBillings(subscriberUuid, null, statusFilters);
        }

        @Test
        void testSubscriberBillingsGet_WhenEmptySearchTerm_ShouldPassToService() throws Exception {
                // Arrange
                String emptySearchTerm = "";
                when(billingService.getBillings(subscriberUuid, emptySearchTerm, null)).thenReturn(billings);

                // Act
                ResponseEntity<List<BillingModel>> response = billingApiDelegate.subscriberBillingsGet(subscriberUuid,
                                emptySearchTerm, null);

                // Assert
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertEquals(2, response.getBody().size());

                verify(billingService).getBillings(subscriberUuid, emptySearchTerm, null);
        }

        @Test
        void testSubscriberBillingsGet_WhenEmptyStatusFilters_ShouldPassToService() throws Exception {
                // Arrange
                List<String> emptyStatusFilters = Collections.emptyList();
                when(billingService.getBillings(subscriberUuid, null, emptyStatusFilters)).thenReturn(billings);

                // Act
                ResponseEntity<List<BillingModel>> response = billingApiDelegate.subscriberBillingsGet(subscriberUuid,
                                null,
                                emptyStatusFilters);

                // Assert
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertEquals(2, response.getBody().size());

                verify(billingService).getBillings(subscriberUuid, null, emptyStatusFilters);
        }

        @Test
        void testSubscriberBillingsGet_WhenBillingHasClosedDate_ShouldIncludeClosedDate() throws Exception {
                // Arrange
                billing1.setClosedAt(LocalDate.now().minusDays(1));
                when(billingService.getBillings(subscriberUuid, null, null))
                                .thenReturn(Collections.singletonList(billing1));

                // Act
                ResponseEntity<List<BillingModel>> response = billingApiDelegate.subscriberBillingsGet(subscriberUuid,
                                null,
                                null);

                // Assert
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertEquals(1, response.getBody().size());

                BillingModel billingModel = response.getBody().get(0);
                assertEquals("billing-uuid-1", billingModel.getUuid());
                assertNotNull(billingModel.getClosedAt());

                verify(billingService).getBillings(subscriberUuid, null, null);
        }
}