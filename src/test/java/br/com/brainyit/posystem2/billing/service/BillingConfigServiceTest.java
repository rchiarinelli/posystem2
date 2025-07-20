package br.com.brainyit.posystem2.billing.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

import br.com.brainyit.posystem2.billing.repository.BillingConfigRepository;
import br.com.brainyit.posystem2.domain.BillingConfig;
import br.com.brainyit.posystem2.domain.InstallmentIntervalUnit;
import br.com.brainyit.posystem2.domain.PaymentType;
import br.com.brainyit.posystem2.domain.Subscriber;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.repository.SubscriberRepository;

@ExtendWith(MockitoExtension.class)
class BillingConfigServiceTest {

    @Mock
    private BillingConfigRepository billingConfigRepository;

    @Mock
    private SubscriberRepository subscriberRepository;

    @InjectMocks
    private BillingConfigService billingConfigService;

    private String subscriberUuid;
    private Subscriber subscriber;
    private BillingConfig oneTimeConfig;
    private BillingConfig installmentsConfig;
    private List<BillingConfig> billingConfigs;

    @BeforeEach
    void setUp() {
        subscriberUuid = "test-subscriber-uuid";

        subscriber = Subscriber.builder()
                .id(1L)
                .uuid(subscriberUuid)
                .name("Test Subscriber")
                .email("test@example.com")
                .build();

        oneTimeConfig = new BillingConfig();
        oneTimeConfig.setId(1);
        oneTimeConfig.setName("Pagamento à Vista");
        oneTimeConfig.setPaymentType(PaymentType.ONE_TIME);
        oneTimeConfig.setInstallmentsQuantity(null);
        oneTimeConfig.setInstallmentsInterval(null);
        oneTimeConfig.setInstallmentIntervalUnit(null);

        installmentsConfig = new BillingConfig();
        installmentsConfig.setId(2);
        installmentsConfig.setName("Parcelado em 12x");
        installmentsConfig.setPaymentType(PaymentType.INSTALLMENTS);
        installmentsConfig.setInstallmentsQuantity((short) 12);
        installmentsConfig.setInstallmentsInterval((short) 1);
        installmentsConfig.setInstallmentIntervalUnit(InstallmentIntervalUnit.MONTH);

        billingConfigs = Arrays.asList(oneTimeConfig, installmentsConfig);
    }

    @Test
    void testGetBillingConfigs_WhenValidSubscriber_ShouldReturnAllConfigs() {
        // Arrange
        when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));
        when(billingConfigRepository.findAll()).thenReturn(billingConfigs);

        // Act
        List<BillingConfig> result = billingConfigService.getBillingConfigs(subscriberUuid);

        // Assert
        assertEquals(2, result.size());
        assertEquals(oneTimeConfig, result.get(0));
        assertEquals(installmentsConfig, result.get(1));

        verify(subscriberRepository).findByUuid(subscriberUuid);
        verify(billingConfigRepository).findAll();
    }

    @Test
    void testGetBillingConfigs_WhenSubscriberNotFound_ShouldThrowBusinessException() {
        // Arrange
        when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            billingConfigService.getBillingConfigs(subscriberUuid);
        });

        assertEquals("Subscriber not found", exception.getMessage());
        verify(subscriberRepository).findByUuid(subscriberUuid);
        verify(billingConfigRepository, never()).findAll();
    }

    @Test
    void testGetBillingConfigs_WhenNoConfigsExist_ShouldReturnEmptyList() {
        // Arrange
        when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));
        when(billingConfigRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<BillingConfig> result = billingConfigService.getBillingConfigs(subscriberUuid);

        // Assert
        assertTrue(result.isEmpty());
        verify(subscriberRepository).findByUuid(subscriberUuid);
        verify(billingConfigRepository).findAll();
    }

    @Test
    void testGetBillingConfigs_WhenNullSubscriberUuid_ShouldThrowBusinessException() {
        // Arrange
        when(subscriberRepository.findByUuid(null)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            billingConfigService.getBillingConfigs(null);
        });

        assertEquals("Subscriber not found", exception.getMessage());
        verify(subscriberRepository).findByUuid(null);
        verify(billingConfigRepository, never()).findAll();
    }

    @Test
    void testGetBillingConfigs_WhenEmptySubscriberUuid_ShouldThrowBusinessException() {
        // Arrange
        String emptyUuid = "";
        when(subscriberRepository.findByUuid(emptyUuid)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            billingConfigService.getBillingConfigs(emptyUuid);
        });

        assertEquals("Subscriber not found", exception.getMessage());
        verify(subscriberRepository).findByUuid(emptyUuid);
        verify(billingConfigRepository, never()).findAll();
    }

    @Test
    void testGetBillingConfigs_WhenOnlyOneTimeConfig_ShouldReturnSingleConfig() {
        // Arrange
        List<BillingConfig> singleConfig = Arrays.asList(oneTimeConfig);
        when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));
        when(billingConfigRepository.findAll()).thenReturn(singleConfig);

        // Act
        List<BillingConfig> result = billingConfigService.getBillingConfigs(subscriberUuid);

        // Assert
        assertEquals(1, result.size());
        assertEquals(oneTimeConfig, result.get(0));
        assertEquals(PaymentType.ONE_TIME, result.get(0).getPaymentType());
        assertNull(result.get(0).getInstallmentsQuantity());

        verify(subscriberRepository).findByUuid(subscriberUuid);
        verify(billingConfigRepository).findAll();
    }

    @Test
    void testGetBillingConfigs_WhenOnlyInstallmentsConfig_ShouldReturnSingleConfig() {
        // Arrange
        List<BillingConfig> singleConfig = Arrays.asList(installmentsConfig);
        when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));
        when(billingConfigRepository.findAll()).thenReturn(singleConfig);

        // Act
        List<BillingConfig> result = billingConfigService.getBillingConfigs(subscriberUuid);

        // Assert
        assertEquals(1, result.size());
        assertEquals(installmentsConfig, result.get(0));
        assertEquals(PaymentType.INSTALLMENTS, result.get(0).getPaymentType());
        assertEquals((short) 12, result.get(0).getInstallmentsQuantity());
        assertEquals((short) 1, result.get(0).getInstallmentsInterval());
        assertEquals(InstallmentIntervalUnit.MONTH, result.get(0).getInstallmentIntervalUnit());

        verify(subscriberRepository).findByUuid(subscriberUuid);
        verify(billingConfigRepository).findAll();
    }

    @Test
    void testGetBillingConfigs_WhenRepositoryThrowsException_ShouldPropagateException() {
        // Arrange
        when(subscriberRepository.findByUuid(subscriberUuid)).thenReturn(Optional.of(subscriber));
        when(billingConfigRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            billingConfigService.getBillingConfigs(subscriberUuid);
        });

        assertEquals("Database error", exception.getMessage());
        verify(subscriberRepository).findByUuid(subscriberUuid);
        verify(billingConfigRepository).findAll();
    }

    @Test
    void testGetBillingConfigs_WhenSubscriberRepositoryThrowsException_ShouldPropagateException() {
        // Arrange
        when(subscriberRepository.findByUuid(subscriberUuid))
                .thenThrow(new RuntimeException("Database connection error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            billingConfigService.getBillingConfigs(subscriberUuid);
        });

        assertEquals("Database connection error", exception.getMessage());
        verify(subscriberRepository).findByUuid(subscriberUuid);
        verify(billingConfigRepository, never()).findAll();
    }
}