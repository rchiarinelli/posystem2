package br.com.brainyit.posystem2.billing.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import br.com.brainyit.api.ApiPaths;
import br.com.brainyit.util.TestUtils;

@Transactional
public class UpdateBillingInstallmentTest extends BaseBillingTest {

        @Test
        void test_updateBillingInstallment_withInvalidInstallmentUuid_returnsNotFound() throws Exception {
                // Arrange
                final Pair<String, String> billingData = prepareOrderData(SUBSCRIBER_3, 17, true,
                                THREE_INSTALLMENTS_BILLING_REQUEST_BODY);
                final String billingUuid = billingData.getRight();

                // Act
                final ResultActions results = mockMvc
                                .perform(put(ApiPaths.BILLING_INSTALLMENT_ENDPOINT_PATH, SUBSCRIBER_3,
                                                billingUuid, "invalid-installment-uuid")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(String.format(DEFAULT_UPDATE_INSTALLMENT_REQUEST_BODY, 100.0D))
                                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Assert
                results.andExpect(status().isNotFound());

        }

        @Test
        void test_updateBillingInstallment_withValidInstallmentUuid_returnsInstallment() throws Exception {
                // Arrange
                final Pair<String, String> billingData = prepareOrderData(SUBSCRIBER_3, 17, true,
                                THREE_INSTALLMENTS_BILLING_REQUEST_BODY);
                final String billingUuid = billingData.getRight();

                ResultActions installmentsResult = mockMvc
                                .perform(get(ApiPaths.BILLING_INSTALLMENTS_ENDPOINT_PATH, SUBSCRIBER_3,
                                                billingUuid)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                final String installmentUuid = TestUtils
                                .convertResponseToList(installmentsResult.andReturn().getResponse()
                                                .getContentAsString())
                                .get(0)
                                .get("uuid").toString();

                // Act
                final ResultActions results = mockMvc
                                .perform(put(ApiPaths.BILLING_INSTALLMENT_ENDPOINT_PATH, SUBSCRIBER_3,
                                                billingUuid, installmentUuid)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(String.format(DEFAULT_UPDATE_INSTALLMENT_REQUEST_BODY, 100.0D))
                                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Assert
                results.andExpect(status().isOk())
                                .andExpect(jsonPath("$.paidValue").value(100.0))
                                .andExpect(jsonPath("$.status").value("PAID"));

                final ResultActions billingResult = mockMvc
                                .perform(get(ApiPaths.BILLING_ENDPOINT_PATH, SUBSCRIBER_3,
                                                billingUuid)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                billingResult.andExpect(jsonPath("$.totalPaidValue").value(100.0));
        }

        @Test
        void test_updateBillingInstallment_payAllInstallments_returnsInstallmentAndBillingIsClosed() throws Exception {
                // Arrange
                final Pair<String, String> billingData = prepareOrderData(SUBSCRIBER_3, 17, true,
                                THREE_INSTALLMENTS_BILLING_REQUEST_BODY);
                final String billingUuid = billingData.getRight();

                ResultActions installmentsResult = mockMvc
                                .perform(get(ApiPaths.BILLING_INSTALLMENTS_ENDPOINT_PATH, SUBSCRIBER_3,
                                                billingUuid)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Act
                for (int i = 0; i < 3; i++) {
                        final String installmentUuid = TestUtils
                                        .convertResponseToList(installmentsResult.andReturn().getResponse()
                                                        .getContentAsString())
                                        .get(i)
                                        .get("uuid").toString();

                        final ResultActions results = mockMvc
                                        .perform(put(ApiPaths.BILLING_INSTALLMENT_ENDPOINT_PATH, SUBSCRIBER_3,
                                                        billingUuid, installmentUuid)
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(String.format(DEFAULT_UPDATE_INSTALLMENT_REQUEST_BODY,
                                                                        100.0D))
                                                        .with(SecurityMockMvcRequestPostProcessors.jwt()));

                        // Assert
                        results.andExpect(status().isOk())
                                        .andExpect(jsonPath("$.paidValue").value(100.0))
                                        .andExpect(jsonPath("$.status").value("PAID"));

                }

                final ResultActions billingResult = mockMvc
                                .perform(get(ApiPaths.BILLING_ENDPOINT_PATH, SUBSCRIBER_3,
                                                billingUuid)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                billingResult.andExpect(jsonPath("$.totalPaidValue").value(300.0));
                billingResult.andExpect(jsonPath("$.status").value("CLOSED"));
                billingResult.andExpect(jsonPath("$.closedAt").isNotEmpty());
        }

        @Test
        void test_updateBillingInstallment_payAllInstallmentsWithLowerPaidValue_returnsInstallmentAndBillingIsNotClosed()
                        throws Exception {
                // Arrange
                final Pair<String, String> billingData = prepareOrderData(SUBSCRIBER_3, 17, true,
                                THREE_INSTALLMENTS_BILLING_REQUEST_BODY);
                final String billingUuid = billingData.getRight();

                ResultActions installmentsResult = mockMvc
                                .perform(get(ApiPaths.BILLING_INSTALLMENTS_ENDPOINT_PATH, SUBSCRIBER_3,
                                                billingUuid)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Act
                for (int i = 0; i < 3; i++) {
                        final String installmentUuid = TestUtils
                                        .convertResponseToList(installmentsResult.andReturn().getResponse()
                                                        .getContentAsString())
                                        .get(i)
                                        .get("uuid").toString();

                        final ResultActions results = mockMvc
                                        .perform(put(ApiPaths.BILLING_INSTALLMENT_ENDPOINT_PATH, SUBSCRIBER_3,
                                                        billingUuid, installmentUuid)
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(String.format(DEFAULT_UPDATE_INSTALLMENT_REQUEST_BODY,
                                                                        50.0D))
                                                        .with(SecurityMockMvcRequestPostProcessors.jwt()));

                        // Assert
                        results.andExpect(status().isOk())
                                        .andExpect(jsonPath("$.paidValue").value(50.0))
                                        .andExpect(jsonPath("$.status").value("PAID"));

                }

                final ResultActions billingResult = mockMvc
                                .perform(get(ApiPaths.BILLING_ENDPOINT_PATH, SUBSCRIBER_3,
                                                billingUuid)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                billingResult.andExpect(jsonPath("$.totalPaidValue").value(150.0));
                billingResult.andExpect(jsonPath("$.status").value("OPEN"));
                billingResult.andExpect(jsonPath("$.closedAt").doesNotExist());
        }

}
