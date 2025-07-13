package br.com.brainyit.posystem2.billing.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.brainyit.posystem2.domain.BillingConfig;
import br.com.brainyit.posystem2.domain.InstallmentIntervalUnit;
import br.com.brainyit.posystem2.domain.PaymentType;
import br.com.brainyit.posystem2.model.BillingConfigModel;

class BillingConfigMapperTest {

    private BillingConfigMapper billingConfigMapper;
    private BillingConfig oneTimeConfig;
    private BillingConfig installmentsConfig;
    private BillingConfig fullInstallmentsConfig;

    @BeforeEach
    void setUp() {
        billingConfigMapper = BillingConfigMapper.builder().build();

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

        fullInstallmentsConfig = new BillingConfig();
        fullInstallmentsConfig.setId(3);
        fullInstallmentsConfig.setName("Parcelado em 24x - Semanal");
        fullInstallmentsConfig.setPaymentType(PaymentType.INSTALLMENTS);
        fullInstallmentsConfig.setInstallmentsQuantity((short) 24);
        fullInstallmentsConfig.setInstallmentsInterval((short) 1);
        fullInstallmentsConfig.setInstallmentIntervalUnit(InstallmentIntervalUnit.WEEK);
    }

    @Test
    void testToRestModel_WhenOneTimeConfig_ShouldMapCorrectly() {
        // Act
        BillingConfigModel result = billingConfigMapper.toRestModel(oneTimeConfig);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Pagamento à Vista", result.getName());
        assertEquals("ONE_TIME", result.getPaymentType());
        assertNull(result.getInstallmentsQuantity());
        assertNull(result.getInstallmentsInterval());
        assertNull(result.getInstallmentsIntervalUnit());
    }

    @Test
    void testToRestModel_WhenInstallmentsConfig_ShouldMapCorrectly() {
        // Act
        BillingConfigModel result = billingConfigMapper.toRestModel(installmentsConfig);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getId());
        assertEquals("Parcelado em 12x", result.getName());
        assertEquals("INSTALLMENTS", result.getPaymentType());
        assertEquals(12, result.getInstallmentsQuantity());
        assertEquals(1, result.getInstallmentsInterval());
        assertEquals("MONTH", result.getInstallmentsIntervalUnit());
    }

    @Test
    void testToRestModel_WhenFullInstallmentsConfig_ShouldMapCorrectly() {
        // Act
        BillingConfigModel result = billingConfigMapper.toRestModel(fullInstallmentsConfig);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getId());
        assertEquals("Parcelado em 24x - Semanal", result.getName());
        assertEquals("INSTALLMENTS", result.getPaymentType());
        assertEquals(24, result.getInstallmentsQuantity());
        assertEquals(1, result.getInstallmentsInterval());
        assertEquals("WEEK", result.getInstallmentsIntervalUnit());
    }

    @Test
    void testToRestModel_WhenNullInput_ShouldReturnNull() {
        // Act
        BillingConfigModel result = billingConfigMapper.toRestModel(null);

        // Assert
        assertNull(result);
    }

    @Test
    void testToRestModel_WhenNullPaymentType_ShouldHandleGracefully() {
        // Arrange
        BillingConfig configWithNullPaymentType = new BillingConfig();
        configWithNullPaymentType.setId(4);
        configWithNullPaymentType.setName("Test Config");
        configWithNullPaymentType.setPaymentType(null);

        // Act
        BillingConfigModel result = billingConfigMapper.toRestModel(configWithNullPaymentType);

        // Assert
        assertNotNull(result);
        assertEquals(4, result.getId());
        assertEquals("Test Config", result.getName());
        assertNull(result.getPaymentType());
    }

    @Test
    void testToRestModel_WhenNullInstallmentIntervalUnit_ShouldHandleGracefully() {
        // Arrange
        BillingConfig configWithNullIntervalUnit = new BillingConfig();
        configWithNullIntervalUnit.setId(5);
        configWithNullIntervalUnit.setName("Test Config");
        configWithNullIntervalUnit.setPaymentType(PaymentType.INSTALLMENTS);
        configWithNullIntervalUnit.setInstallmentsQuantity((short) 6);
        configWithNullIntervalUnit.setInstallmentsInterval((short) 2);
        configWithNullIntervalUnit.setInstallmentIntervalUnit(null);

        // Act
        BillingConfigModel result = billingConfigMapper.toRestModel(configWithNullIntervalUnit);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.getId());
        assertEquals("Test Config", result.getName());
        assertEquals("INSTALLMENTS", result.getPaymentType());
        assertEquals(6, result.getInstallmentsQuantity());
        assertEquals(2, result.getInstallmentsInterval());
        assertNull(result.getInstallmentsIntervalUnit());
    }

    @Test
    void testToRestModelList_WhenValidList_ShouldMapAllItems() {
        // Arrange
        List<BillingConfig> configs = Arrays.asList(oneTimeConfig, installmentsConfig, fullInstallmentsConfig);

        // Act
        List<BillingConfigModel> result = billingConfigMapper.toRestModelList(configs);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());

        // Check first item (ONE_TIME)
        assertEquals(1, result.get(0).getId());
        assertEquals("ONE_TIME", result.get(0).getPaymentType());
        assertNull(result.get(0).getInstallmentsQuantity());

        // Check second item (INSTALLMENTS - Monthly)
        assertEquals(2, result.get(1).getId());
        assertEquals("INSTALLMENTS", result.get(1).getPaymentType());
        assertEquals(12, result.get(1).getInstallmentsQuantity());
        assertEquals("MONTH", result.get(1).getInstallmentsIntervalUnit());

        // Check third item (INSTALLMENTS - Weekly)
        assertEquals(3, result.get(2).getId());
        assertEquals("INSTALLMENTS", result.get(2).getPaymentType());
        assertEquals(24, result.get(2).getInstallmentsQuantity());
        assertEquals("WEEK", result.get(2).getInstallmentsIntervalUnit());
    }

    @Test
    void testToRestModelList_WhenEmptyList_ShouldReturnEmptyList() {
        // Act
        List<BillingConfigModel> result = billingConfigMapper.toRestModelList(Collections.emptyList());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testToRestModelList_WhenNullList_ShouldReturnEmptyList() {
        // Act
        List<BillingConfigModel> result = billingConfigMapper.toRestModelList(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testToRestModelList_WhenListWithNullElements_ShouldIncludeNulls() {
        // Arrange
        List<BillingConfig> configsWithNull = Arrays.asList(oneTimeConfig, null, installmentsConfig);

        // Act
        List<BillingConfigModel> result = billingConfigMapper.toRestModelList(configsWithNull);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size()); // null elements should be included as null
        assertEquals(1, result.get(0).getId());
        assertNull(result.get(1)); // null input produces null output
        assertEquals(2, result.get(2).getId());
    }

    @Test
    void testToRestModelList_WhenSingleItemList_ShouldReturnSingleItem() {
        // Arrange
        List<BillingConfig> singleConfig = Arrays.asList(oneTimeConfig);

        // Act
        List<BillingConfigModel> result = billingConfigMapper.toRestModelList(singleConfig);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Pagamento à Vista", result.get(0).getName());
        assertEquals("ONE_TIME", result.get(0).getPaymentType());
    }

    @Test
    void testToRestModel_WhenAllInstallmentFieldsNull_ShouldHandleGracefully() {
        // Arrange
        BillingConfig configWithNullInstallmentFields = new BillingConfig();
        configWithNullInstallmentFields.setId(6);
        configWithNullInstallmentFields.setName("Test Config");
        configWithNullInstallmentFields.setPaymentType(PaymentType.INSTALLMENTS);
        configWithNullInstallmentFields.setInstallmentsQuantity(null);
        configWithNullInstallmentFields.setInstallmentsInterval(null);
        configWithNullInstallmentFields.setInstallmentIntervalUnit(null);

        // Act
        BillingConfigModel result = billingConfigMapper.toRestModel(configWithNullInstallmentFields);

        // Assert
        assertNotNull(result);
        assertEquals(6, result.getId());
        assertEquals("Test Config", result.getName());
        assertEquals("INSTALLMENTS", result.getPaymentType());
        assertNull(result.getInstallmentsQuantity());
        assertNull(result.getInstallmentsInterval());
        assertNull(result.getInstallmentsIntervalUnit());
    }

    @Test
    void testToRestModel_WhenDayIntervalUnit_ShouldMapCorrectly() {
        // Arrange
        BillingConfig configWithDayInterval = new BillingConfig();
        configWithDayInterval.setId(7);
        configWithDayInterval.setName("Parcelado em 30x - Diário");
        configWithDayInterval.setPaymentType(PaymentType.INSTALLMENTS);
        configWithDayInterval.setInstallmentsQuantity((short) 30);
        configWithDayInterval.setInstallmentsInterval((short) 1);
        configWithDayInterval.setInstallmentIntervalUnit(InstallmentIntervalUnit.DAY);

        // Act
        BillingConfigModel result = billingConfigMapper.toRestModel(configWithDayInterval);

        // Assert
        assertNotNull(result);
        assertEquals(7, result.getId());
        assertEquals("Parcelado em 30x - Diário", result.getName());
        assertEquals("INSTALLMENTS", result.getPaymentType());
        assertEquals(30, result.getInstallmentsQuantity());
        assertEquals(1, result.getInstallmentsInterval());
        assertEquals("DAY", result.getInstallmentsIntervalUnit());
    }
}