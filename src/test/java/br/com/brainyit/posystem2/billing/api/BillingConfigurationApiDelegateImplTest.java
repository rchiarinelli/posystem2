package br.com.brainyit.posystem2.billing.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.brainyit.posystem2.billing.service.BillingConfigService;
import br.com.brainyit.posystem2.domain.BillingConfig;
import br.com.brainyit.posystem2.domain.InstallmentIntervalUnit;
import br.com.brainyit.posystem2.domain.PaymentType;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.model.BillingConfigModel;

@ExtendWith(MockitoExtension.class)
class BillingConfigurationApiDelegateImplTest {

    @Mock
    private BillingConfigService billingConfigService;

    @InjectMocks
    private BillingConfigurationApiDelegateImpl billingConfigurationApiDelegate;

    private String subscriberUuid;
    private BillingConfig oneTimeConfig;
    private BillingConfig installmentsConfig;
    private List<BillingConfig> billingConfigs;

    @BeforeEach
    void setUp() {
        subscriberUuid = "test-subscriber-uuid";

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
    void testSubscriberBillingConfigsGet_WhenValidSubscriber_ShouldReturnAllConfigs() throws Exception {
        // Arrange
        when(billingConfigService.getBillingConfigs(subscriberUuid)).thenReturn(billingConfigs);

        // Act
        ResponseEntity<List<BillingConfigModel>> response = billingConfigurationApiDelegate
                .subscriberBillingConfigsGet(subscriberUuid);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        // Check first config (ONE_TIME)
        BillingConfigModel firstConfig = response.getBody().get(0);
        assertEquals(1, firstConfig.getId());
        assertEquals("Pagamento à Vista", firstConfig.getName());
        assertEquals("ONE_TIME", firstConfig.getPaymentType());
        assertNull(firstConfig.getInstallmentsQuantity());
        assertNull(firstConfig.getInstallmentsInterval());
        assertNull(firstConfig.getInstallmentsIntervalUnit());

        // Check second config (INSTALLMENTS)
        BillingConfigModel secondConfig = response.getBody().get(1);
        assertEquals(2, secondConfig.getId());
        assertEquals("Parcelado em 12x", secondConfig.getName());
        assertEquals("INSTALLMENTS", secondConfig.getPaymentType());
        assertEquals(12, secondConfig.getInstallmentsQuantity());
        assertEquals(1, secondConfig.getInstallmentsInterval());
        assertEquals("MONTH", secondConfig.getInstallmentsIntervalUnit());

        verify(billingConfigService).getBillingConfigs(subscriberUuid);
    }

    @Test
    void testSubscriberBillingConfigsGet_WhenServiceReturnsEmptyList_ShouldReturnEmptyList() throws Exception {
        // Arrange
        when(billingConfigService.getBillingConfigs(subscriberUuid)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<BillingConfigModel>> response = billingConfigurationApiDelegate
                .subscriberBillingConfigsGet(subscriberUuid);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(billingConfigService).getBillingConfigs(subscriberUuid);
    }

    @Test
    void testSubscriberBillingConfigsGet_WhenServiceThrowsBusinessException_ShouldPropagateException()
            throws Exception {
        // Arrange
        BusinessException businessException = new BusinessException("Subscriber not found");
        when(billingConfigService.getBillingConfigs(subscriberUuid)).thenThrow(businessException);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            billingConfigurationApiDelegate.subscriberBillingConfigsGet(subscriberUuid);
        });

        assertEquals("Subscriber not found", exception.getMessage());
        verify(billingConfigService).getBillingConfigs(subscriberUuid);
    }

    @Test
    void testSubscriberBillingConfigsGet_WhenServiceThrowsRuntimeException_ShouldPropagateException() throws Exception {
        // Arrange
        RuntimeException runtimeException = new RuntimeException("Database error");
        when(billingConfigService.getBillingConfigs(subscriberUuid)).thenThrow(runtimeException);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            billingConfigurationApiDelegate.subscriberBillingConfigsGet(subscriberUuid);
        });

        assertEquals("Database error", exception.getMessage());
        verify(billingConfigService).getBillingConfigs(subscriberUuid);
    }

    @Test
    void testSubscriberBillingConfigsGet_WhenNullSubscriber_ShouldCallServiceWithNull() throws Exception {
        // Arrange
        when(billingConfigService.getBillingConfigs(null)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<BillingConfigModel>> response = billingConfigurationApiDelegate
                .subscriberBillingConfigsGet(null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(billingConfigService).getBillingConfigs(null);
    }

    @Test
    void testSubscriberBillingConfigsGet_WhenEmptySubscriber_ShouldCallServiceWithEmptyString() throws Exception {
        // Arrange
        String emptySubscriber = "";
        when(billingConfigService.getBillingConfigs(emptySubscriber)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<BillingConfigModel>> response = billingConfigurationApiDelegate
                .subscriberBillingConfigsGet(emptySubscriber);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(billingConfigService).getBillingConfigs(emptySubscriber);
    }

    @Test
    void testSubscriberBillingConfigsGet_WhenOnlyOneTimeConfig_ShouldReturnSingleConfig() throws Exception {
        // Arrange
        List<BillingConfig> singleConfig = Arrays.asList(oneTimeConfig);
        when(billingConfigService.getBillingConfigs(subscriberUuid)).thenReturn(singleConfig);

        // Act
        ResponseEntity<List<BillingConfigModel>> response = billingConfigurationApiDelegate
                .subscriberBillingConfigsGet(subscriberUuid);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        BillingConfigModel config = response.getBody().get(0);
        assertEquals(1, config.getId());
        assertEquals("Pagamento à Vista", config.getName());
        assertEquals("ONE_TIME", config.getPaymentType());
        assertNull(config.getInstallmentsQuantity());

        verify(billingConfigService).getBillingConfigs(subscriberUuid);
    }

    @Test
    void testSubscriberBillingConfigsGet_WhenOnlyInstallmentsConfig_ShouldReturnSingleConfig() throws Exception {
        // Arrange
        List<BillingConfig> singleConfig = Arrays.asList(installmentsConfig);
        when(billingConfigService.getBillingConfigs(subscriberUuid)).thenReturn(singleConfig);

        // Act
        ResponseEntity<List<BillingConfigModel>> response = billingConfigurationApiDelegate
                .subscriberBillingConfigsGet(subscriberUuid);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        BillingConfigModel config = response.getBody().get(0);
        assertEquals(2, config.getId());
        assertEquals("Parcelado em 12x", config.getName());
        assertEquals("INSTALLMENTS", config.getPaymentType());
        assertEquals(12, config.getInstallmentsQuantity());
        assertEquals(1, config.getInstallmentsInterval());
        assertEquals("MONTH", config.getInstallmentsIntervalUnit());

        verify(billingConfigService).getBillingConfigs(subscriberUuid);
    }

    @Test
    void testSubscriberBillingConfigsGet_WhenMultipleInstallmentsConfigs_ShouldReturnAllConfigs() throws Exception {
        // Arrange
        BillingConfig config24x = new BillingConfig();
        config24x.setId(3);
        config24x.setName("Parcelado em 24x");
        config24x.setPaymentType(PaymentType.INSTALLMENTS);
        config24x.setInstallmentsQuantity((short) 24);
        config24x.setInstallmentsInterval((short) 1);
        config24x.setInstallmentIntervalUnit(InstallmentIntervalUnit.MONTH);

        BillingConfig configWeekly = new BillingConfig();
        configWeekly.setId(4);
        configWeekly.setName("Parcelado em 8x - Semanal");
        configWeekly.setPaymentType(PaymentType.INSTALLMENTS);
        configWeekly.setInstallmentsQuantity((short) 8);
        configWeekly.setInstallmentsInterval((short) 1);
        configWeekly.setInstallmentIntervalUnit(InstallmentIntervalUnit.WEEK);

        List<BillingConfig> multipleConfigs = Arrays.asList(oneTimeConfig, installmentsConfig, config24x, configWeekly);
        when(billingConfigService.getBillingConfigs(subscriberUuid)).thenReturn(multipleConfigs);

        // Act
        ResponseEntity<List<BillingConfigModel>> response = billingConfigurationApiDelegate
                .subscriberBillingConfigsGet(subscriberUuid);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(4, response.getBody().size());

        // Check all configs are properly mapped
        List<BillingConfigModel> configs = response.getBody();
        assertEquals("ONE_TIME", configs.get(0).getPaymentType());
        assertEquals("INSTALLMENTS", configs.get(1).getPaymentType());
        assertEquals("INSTALLMENTS", configs.get(2).getPaymentType());
        assertEquals("INSTALLMENTS", configs.get(3).getPaymentType());

        // Check specific installments config
        assertEquals(24, configs.get(2).getInstallmentsQuantity());
        assertEquals("MONTH", configs.get(2).getInstallmentsIntervalUnit());

        assertEquals(8, configs.get(3).getInstallmentsQuantity());
        assertEquals("WEEK", configs.get(3).getInstallmentsIntervalUnit());

        verify(billingConfigService).getBillingConfigs(subscriberUuid);
    }

    @Test
    void testSubscriberBillingConfigsGet_WhenConfigWithNullFields_ShouldHandleGracefully() throws Exception {
        // Arrange
        BillingConfig configWithNulls = new BillingConfig();
        configWithNulls.setId(5);
        configWithNulls.setName("Config with nulls");
        configWithNulls.setPaymentType(null);
        configWithNulls.setInstallmentsQuantity(null);
        configWithNulls.setInstallmentsInterval(null);
        configWithNulls.setInstallmentIntervalUnit(null);

        List<BillingConfig> configsWithNulls = Arrays.asList(configWithNulls);
        when(billingConfigService.getBillingConfigs(subscriberUuid)).thenReturn(configsWithNulls);

        // Act
        ResponseEntity<List<BillingConfigModel>> response = billingConfigurationApiDelegate
                .subscriberBillingConfigsGet(subscriberUuid);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        BillingConfigModel config = response.getBody().get(0);
        assertEquals(5, config.getId());
        assertEquals("Config with nulls", config.getName());
        assertNull(config.getPaymentType());
        assertNull(config.getInstallmentsQuantity());
        assertNull(config.getInstallmentsInterval());
        assertNull(config.getInstallmentsIntervalUnit());

        verify(billingConfigService).getBillingConfigs(subscriberUuid);
    }

    @Test
    void testSubscriberBillingConfigsGet_WhenServiceReturnsNull_ShouldHandleGracefully() throws Exception {
        // Arrange
        when(billingConfigService.getBillingConfigs(subscriberUuid)).thenReturn(null);

        // Act
        ResponseEntity<List<BillingConfigModel>> response = billingConfigurationApiDelegate
                .subscriberBillingConfigsGet(subscriberUuid);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(billingConfigService).getBillingConfigs(subscriberUuid);
    }

    @Test
    void testSubscriberBillingConfigsGet_WhenIllegalArgumentExceptionThrown_ShouldPropagateException()
            throws Exception {
        // Arrange
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Invalid argument");
        when(billingConfigService.getBillingConfigs(subscriberUuid)).thenThrow(illegalArgumentException);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            billingConfigurationApiDelegate.subscriberBillingConfigsGet(subscriberUuid);
        });

        assertEquals("Invalid argument", exception.getMessage());
        verify(billingConfigService).getBillingConfigs(subscriberUuid);
    }
}