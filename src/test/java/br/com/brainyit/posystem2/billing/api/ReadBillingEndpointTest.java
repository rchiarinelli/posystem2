package br.com.brainyit.posystem2.billing.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import br.com.brainyit.api.ApiPaths;
import br.com.brainyit.util.TestUtils;

@Transactional
public class ReadBillingEndpointTest extends BaseBillingTest {

        private static Stream<Arguments> getBillingsTestData() {
                return Stream.of(
                                Arguments.of(SUBSCRIBER_3, 15, true, DEFAULT_BILLING_REQUEST_BODY),
                                Arguments.of(SUBSCRIBER_1, 8, false, DEFAULT_BILLING_REQUEST_BODY),
                                Arguments.of(SUBSCRIBER_3, 17, true, DEFAULT_BILLING_REQUEST_BODY));
        }

        @ParameterizedTest
        @MethodSource("getBillingsTestData")
        void test_getBillings_returnsCreatedBilling(final String subscriber, final int customerId,
                        final boolean isBilled, final String billingRequestBody) throws Exception {

                final String orderCode = prepareOrderData(subscriber, customerId, isBilled, billingRequestBody)
                                .getLeft();

                ResultActions results = mockMvc.perform(get(ApiPaths.BILLINGS_ENDPOINT_PATH, subscriber)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Assert

                if (isBilled) {
                        results.andExpect(status().isOk())
                                        .andExpect(jsonPath("$").isArray())
                                        .andExpect(jsonPath(String.format("$[?(@.status=='%s')]", "OPEN")).exists())
                                        .andExpect(jsonPath(String.format("$[?(@.finalValue==%s)]", 100.0)).exists())
                                        .andExpect(jsonPath(String.format("$[?(@.order.orderCode=='%s')]", orderCode))
                                                        .exists())
                                        .andExpect(jsonPath(String.format("$[?(@.billingConfig.name=='%s')]",
                                                        "Pagamento à Vista")).exists())
                                        .andExpect(jsonPath(String.format("$[?(@.billingConfig.billingType=='%s')]",
                                                        "ONE_TIME")).exists())
                                        .andExpect(jsonPath(
                                                        String.format("$[?(@.installments[0].status=='%s')]",
                                                                        "PENDING"))
                                                        .exists());

                } else {
                        results.andExpect(status().isOk())
                                        .andExpect(jsonPath("$").isEmpty());
                }
        }

        @Test
        void test_getBillingByUuid_withInvalidUuid_returnsNotFound() throws Exception {
                mockMvc.perform(get(ApiPaths.BILLING_ENDPOINT_PATH, SUBSCRIBER_1, "invalid-uuid")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()))
                                .andExpect(status().isNotFound());
        }

        @Test
        void test_getBillingByUuid_withValidUuid_returnsBilling() throws Exception {

                final Pair<String, String> billingData = prepareOrderData(SUBSCRIBER_3, 15, true,
                                THREE_INSTALLMENTS_BILLING_REQUEST_BODY);
                final String orderCode = billingData.getKey();
                final String billingUuid = billingData.getValue();

                ResultActions results = mockMvc
                                .perform(get(ApiPaths.BILLING_ENDPOINT_PATH, SUBSCRIBER_3, billingUuid)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                results.andExpect(status().isOk())
                                .andExpect(jsonPath("$.uuid").value(billingUuid))
                                .andExpect(jsonPath("$.status").value("OPEN"))
                                .andExpect(jsonPath("$.finalValue").value(300.0))
                                .andExpect(jsonPath("$.order.orderCode").value(orderCode))
                                .andExpect(jsonPath("$.installments").isArray())
                                .andExpect(jsonPath("$.installments").isNotEmpty());

        }

        @Test
        void test_getBillingInstallments_withInvalidBillingUuid_returnsNotFound() throws Exception {
                mockMvc.perform(get(ApiPaths.BILLING_INSTALLMENTS_ENDPOINT_PATH, SUBSCRIBER_1, "invalid-uuid")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()))
                                .andExpect(status().isNotFound());
        }

        @Test
        void test_getBillingInstallments_withValidBillingUuid_usingDifferentSubscriber_returnsNotFound()
                        throws Exception {

                final Pair<String, String> billingData = prepareOrderData(SUBSCRIBER_3, 15, true,
                                THREE_INSTALLMENTS_BILLING_REQUEST_BODY);
                final String billingUuid = billingData.getValue();

                mockMvc.perform(get(ApiPaths.BILLING_INSTALLMENTS_ENDPOINT_PATH, SUBSCRIBER_1,
                                billingUuid)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()))
                                .andExpect(status().isNotFound());
        }

        @Test
        void test_getBillingInstallments_withValidBillingUuid_returnsBillingInstallments() throws Exception {
                final Pair<String, String> billingData = prepareOrderData(SUBSCRIBER_3, 15, true,
                                THREE_INSTALLMENTS_BILLING_REQUEST_BODY);
                final String billingUuid = billingData.getValue();

                ResultActions results = mockMvc.perform(get(ApiPaths.BILLING_INSTALLMENTS_ENDPOINT_PATH, SUBSCRIBER_3,
                                billingUuid)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                results.andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$").isNotEmpty())
                                .andExpect(jsonPath("$[0].status").value("PENDING"))
                                .andExpect(jsonPath("$[1].status").value("PENDING"))
                                .andExpect(jsonPath("$[2].status").value("PENDING"));
        }

        @Test
        void test_getBillingInstallment_withInvalidInstallmentUuid_returnsNotFound() throws Exception {
                mockMvc.perform(get(ApiPaths.BILLING_INSTALLMENT_ENDPOINT_PATH, SUBSCRIBER_3, "invalid-uuid",
                                "invalid-installment-uuid")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()))
                                .andExpect(status().isNotFound());
        }

        @Test
        void test_getBillingInstallment_withValidInstallmentUuid_returnsInstallment() throws Exception {
                final Pair<String, String> billingData = prepareOrderData(SUBSCRIBER_3, 15, true,
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

                ResultActions installmentResult = mockMvc.perform(get(ApiPaths.BILLING_INSTALLMENT_ENDPOINT_PATH,
                                SUBSCRIBER_3, billingUuid, installmentUuid)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                installmentResult.andExpect(status().isOk())
                                .andExpect(jsonPath("$.uuid").value(installmentUuid))
                                .andExpect(jsonPath("$.status").value("PENDING"))
                                .andExpect(jsonPath("$.dueValue").value(100.0))
                                .andExpect(jsonPath("$.dueDate")
                                                .value(BILLING_START_DATE.format(DateTimeFormatter.ISO_DATE)))
                                .andExpect(jsonPath("$.paidValue").value(0.0));
        }

        @Test
        void test_getBillingInstallments_withNoFilters_returnsOnlyOnePendingInstallment() throws Exception {
                mockMvc.perform(get(ApiPaths.BILLING_INSTALLMENTS_BY_STATUS_PATH, SUBSCRIBER_4)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$").isNotEmpty())
                                .andExpect(jsonPath("$.size()").value(1));
        }

        @Test
        void test_getBillingInstallments_withFromAndToDateAndPendingStatus_returnsAllInstallment() throws Exception {
                mockMvc.perform(get(
                                ApiPaths.BILLING_INSTALLMENTS_BY_STATUS_PATH + "?from="
                                                + BILLING_START_DATE.minusMonths(3).format(DateTimeFormatter.ISO_DATE)
                                                + "&to="
                                                + BILLING_START_DATE.plusMonths(6).format(DateTimeFormatter.ISO_DATE)
                                                + "&status=PENDING",
                                SUBSCRIBER_4)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$").isNotEmpty())
                                .andExpect(jsonPath("$.size()").value(7));
        }

        @Test
        void test_getBillingInstallments_withFromAndToDateAndPaidAndLateStatus_returnsAllInstallment()
                        throws Exception {
                mockMvc.perform(get(
                                ApiPaths.BILLING_INSTALLMENTS_BY_STATUS_PATH + "?from="
                                                + BILLING_START_DATE.minusMonths(3).format(DateTimeFormatter.ISO_DATE)
                                                + "&to="
                                                + BILLING_START_DATE.plusMonths(6).format(DateTimeFormatter.ISO_DATE)
                                                + "&status=PAID,LATE",
                                SUBSCRIBER_4)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$").isEmpty());
        }

}
