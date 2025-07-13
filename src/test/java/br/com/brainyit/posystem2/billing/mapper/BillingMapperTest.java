package br.com.brainyit.posystem2.billing.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.brainyit.posystem2.domain.Billing;
import br.com.brainyit.posystem2.domain.BillingStatus;
import br.com.brainyit.posystem2.domain.Customer;
import br.com.brainyit.posystem2.domain.Individual;
import br.com.brainyit.posystem2.domain.Installment;
import br.com.brainyit.posystem2.domain.InstallmentStatus;
import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.domain.OrderStatus;
import br.com.brainyit.posystem2.domain.Subscriber;
import br.com.brainyit.posystem2.model.BillingModel;
import br.com.brainyit.posystem2.model.InstallmentModel;

class BillingMapperTest {

    private BillingMapper billingMapper;
    private Subscriber subscriber;
    private Customer customer;
    private Order order;
    private Billing billing;
    private Installment installment1;
    private Installment installment2;
    private Set<Installment> installments;

    @BeforeEach
    void setUp() {
        billingMapper = BillingMapper.builder().build();

        subscriber = Subscriber.builder()
                .id(1L)
                .uuid("subscriber-uuid")
                .name("Test Subscriber")
                .email("test@example.com")
                .build();

        customer = Individual.builder()
                .id(1)
                .name("Test Customer")
                .cpf("123.456.789-00")
                .subscriber(subscriber)
                .build();

        order = Order.builder()
                .id(1L)
                .orderCode("ORDER-001")
                .description("Test Order")
                .customer(customer)
                .status(OrderStatus.BILLED)
                .subscriber(subscriber)
                .openDate(new Date())
                .quantity(1.0)
                .build();

        installment1 = Installment.builder()
                .id(1L)
                .uuid("installment-uuid-1")
                .dueValue(50.0)
                .date(LocalDate.now().plusDays(30))
                .createdAt(LocalDate.now())
                .paidValue(null)
                .paymentDate(null)
                .status(InstallmentStatus.PENDING)
                .subscriber(subscriber)
                .build();

        installment2 = Installment.builder()
                .id(2L)
                .uuid("installment-uuid-2")
                .dueValue(50.0)
                .date(LocalDate.now().plusDays(60))
                .createdAt(LocalDate.now())
                .paidValue(50.0)
                .paymentDate(LocalDate.now().minusDays(1))
                .status(InstallmentStatus.PAID)
                .subscriber(subscriber)
                .build();

        installments = new HashSet<>(Arrays.asList(installment1, installment2));

        billing = Billing.builder()
                .id(1L)
                .uuid("billing-uuid")
                .order(order)
                .calculatedValue(100.0)
                .finalValue(100.0)
                .createdAt(LocalDate.now())
                .closedAt(null)
                .status(BillingStatus.OPEN)
                .subscriber(subscriber)
                .installments(installments)
                .build();
    }

    @Test
    void testToRestModel_WhenBillingIsNull_ShouldReturnNull() {
        // Act
        BillingModel result = billingMapper.toRestModel(null);

        // Assert
        assertNull(result);
    }

    @Test
    void testToRestModel_WhenBillingIsValid_ShouldReturnCorrectModel() {
        // Act
        BillingModel result = billingMapper.toRestModel(billing);

        // Assert
        assertNotNull(result);
        assertEquals("billing-uuid", result.getUuid());
        assertEquals(LocalDate.now().toString(), result.getCreatedAt());
        assertNull(result.getClosedAt());
        assertEquals("OPEN", result.getStatus());
        assertEquals(100.0, result.getCalculatedValue());
        assertEquals(100.0, result.getFinalValue());

        // Check installments
        assertNotNull(result.getInstallments());
        assertEquals(2, result.getInstallments().size());

        // Check order
        assertNotNull(result.getOrder());
        assertEquals("ORDER-001", result.getOrder().getOrderCode());
        assertEquals("Test Order", result.getOrder().getOrderDescription());
    }

    @Test
    void testToRestModel_WhenBillingHasClosedAt_ShouldIncludeClosedDate() {
        // Arrange
        LocalDate closedDate = LocalDate.now().minusDays(1);
        billing.setClosedAt(closedDate);

        // Act
        BillingModel result = billingMapper.toRestModel(billing);

        // Assert
        assertNotNull(result);
        assertEquals(closedDate.toString(), result.getClosedAt());
    }

    @Test
    void testToRestModel_WhenBillingHasNoInstallments_ShouldHaveEmptyInstallments() {
        // Arrange
        billing.setInstallments(null);

        // Act
        BillingModel result = billingMapper.toRestModel(billing);

        // Assert
        assertNotNull(result);
        assertNull(result.getInstallments());
    }

    @Test
    void testToRestModel_WhenBillingHasNoOrder_ShouldHaveNullOrder() {
        // Arrange
        billing.setOrder(null);

        // Act
        BillingModel result = billingMapper.toRestModel(billing);

        // Assert
        assertNotNull(result);
        assertNull(result.getOrder());
    }

    @Test
    void testToRestModel_WhenInstallmentMappingIsCorrect_ShouldReturnCorrectInstallmentModels() {
        // Act
        BillingModel result = billingMapper.toRestModel(billing);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getInstallments());
        assertEquals(2, result.getInstallments().size());

        // Find the paid installment
        InstallmentModel paidInstallment = result.getInstallments().stream()
                .filter(i -> "PAID".equals(i.getStatus()))
                .findFirst()
                .orElse(null);

        assertNotNull(paidInstallment);
        assertEquals("installment-uuid-2", paidInstallment.getUuid());
        assertEquals(50.0, paidInstallment.getDueValue());
        assertEquals(50.0, paidInstallment.getPaidValue());
        assertNotNull(paidInstallment.getPaymentDate());
        assertEquals("PAID", paidInstallment.getStatus());

        // Find the pending installment
        InstallmentModel pendingInstallment = result.getInstallments().stream()
                .filter(i -> "PENDING".equals(i.getStatus()))
                .findFirst()
                .orElse(null);

        assertNotNull(pendingInstallment);
        assertEquals("installment-uuid-1", pendingInstallment.getUuid());
        assertEquals(50.0, pendingInstallment.getDueValue());
        assertNull(pendingInstallment.getPaidValue());
        assertNull(pendingInstallment.getPaymentDate());
        assertEquals("PENDING", pendingInstallment.getStatus());
    }

    @Test
    void testToRestModelList_WhenBillingsIsNull_ShouldReturnEmptyList() {
        // Act
        List<BillingModel> result = billingMapper.toRestModelList(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testToRestModelList_WhenBillingsIsEmpty_ShouldReturnEmptyList() {
        // Act
        List<BillingModel> result = billingMapper.toRestModelList(Collections.emptyList());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testToRestModelList_WhenBillingsIsValid_ShouldReturnCorrectList() {
        // Arrange
        Billing billing2 = Billing.builder()
                .id(2L)
                .uuid("billing-uuid-2")
                .order(order)
                .calculatedValue(200.0)
                .finalValue(200.0)
                .createdAt(LocalDate.now())
                .closedAt(LocalDate.now().minusDays(1))
                .status(BillingStatus.CLOSED)
                .subscriber(subscriber)
                .installments(new HashSet<>())
                .build();

        List<Billing> billings = Arrays.asList(billing, billing2);

        // Act
        List<BillingModel> result = billingMapper.toRestModelList(billings);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        BillingModel firstModel = result.get(0);
        assertEquals("billing-uuid", firstModel.getUuid());
        assertEquals("OPEN", firstModel.getStatus());
        assertEquals(100.0, firstModel.getCalculatedValue());

        BillingModel secondModel = result.get(1);
        assertEquals("billing-uuid-2", secondModel.getUuid());
        assertEquals("CLOSED", secondModel.getStatus());
        assertEquals(200.0, secondModel.getCalculatedValue());
        assertNotNull(secondModel.getClosedAt());
    }

    @Test
    void testToRestModel_WhenInstallmentHasNullFields_ShouldHandleGracefully() {
        // Arrange
        Installment installmentWithNulls = Installment.builder()
                .id(3L)
                .uuid("installment-uuid-3")
                .dueValue(25.0)
                .date(null)
                .createdAt(null)
                .paidValue(null)
                .paymentDate(null)
                .status(null)
                .subscriber(subscriber)
                .build();

        billing.setInstallments(Collections.singleton(installmentWithNulls));

        // Act
        BillingModel result = billingMapper.toRestModel(billing);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getInstallments());
        assertEquals(1, result.getInstallments().size());

        InstallmentModel installmentModel = result.getInstallments().get(0);
        assertEquals("installment-uuid-3", installmentModel.getUuid());
        assertEquals(25.0, installmentModel.getDueValue());
        assertNull(installmentModel.getDueDate());
        assertNull(installmentModel.getCreatedAt());
        assertNull(installmentModel.getPaidValue());
        assertNull(installmentModel.getPaymentDate());
        assertNull(installmentModel.getStatus());
    }

    @Test
    void testToRestModel_WhenBillingHasNullFields_ShouldHandleGracefully() {
        // Arrange
        billing.setCreatedAt(null);
        billing.setClosedAt(null);
        billing.setStatus(null);

        // Act
        BillingModel result = billingMapper.toRestModel(billing);

        // Assert
        assertNotNull(result);
        assertNull(result.getCreatedAt());
        assertNull(result.getClosedAt());
        assertNull(result.getStatus());
        assertEquals("billing-uuid", result.getUuid());
        assertEquals(100.0, result.getCalculatedValue());
        assertEquals(100.0, result.getFinalValue());
    }
}