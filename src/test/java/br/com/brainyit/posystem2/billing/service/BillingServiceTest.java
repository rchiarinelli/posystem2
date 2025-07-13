package br.com.brainyit.posystem2.billing.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
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

import br.com.brainyit.posystem2.billing.repository.BillingRepository;
import br.com.brainyit.posystem2.domain.Billing;
import br.com.brainyit.posystem2.domain.BillingStatus;
import br.com.brainyit.posystem2.domain.Customer;
import br.com.brainyit.posystem2.domain.Individual;
import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.domain.OrderStatus;
import br.com.brainyit.posystem2.domain.Subscriber;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.repository.SubscriberRepository;

@ExtendWith(MockitoExtension.class)
class BillingServiceTest {

    @Mock
    private BillingRepository billingRepository;

    @Mock
    private SubscriberRepository subscriberRepository;

    @InjectMocks
    private BillingService billingService;

    private Subscriber subscriber;
    private String subscriberUuid;
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
}