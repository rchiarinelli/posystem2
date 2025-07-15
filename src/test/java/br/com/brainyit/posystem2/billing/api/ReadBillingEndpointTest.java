package br.com.brainyit.posystem2.billing.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import br.com.brainyit.api.ApiPaths;
import br.com.brainyit.api.BaseApiTest;
import br.com.brainyit.util.TestUtils;

@Transactional
public class ReadBillingEndpointTest extends BaseApiTest {

        private static final String SUBSCRIBER_1 = "123e4567-e89b-12d3-a456-426614174000";

        private static final String SUBSCRIBER_3 = "123e4567-e89b-12d3-a456-426614174002";

        private static final LocalDate PLANNED_DELIVER_DATE = LocalDate.now().plusYears(1);

        private static final LocalDate BILLING_START_DATE = LocalDate.now().plusDays(1);

        private static final String DEFAULT_ORDER_REQUEST_BODY = """
                        {
                        "customerId": %d,
                        "orderDescription": "Test order for billing",
                        "quantity": 1,
                        "plannedDeliverDate": "%s"
                        }""";

        private static final String DEFAULT_PO_REQUEST_BODY = """
                        {
                        "estimatedTime": 10,
                        "instructions": "Production order description",
                        "qtd": 10,
                        "plannedStartDate": "2026-01-01",
                        "priority": "STANDARD"
                        }""";

        private static final String DEFAULT_BILLING_REQUEST_BODY = """
                        [{
                        "orderCode": "%s",
                        "billingConfigId": 1,
                        "startDate": "%s",
                        "finalValue": 100.0
                        }]""";

        private static final String THREE_INSTALLMENTS_BILLING_REQUEST_BODY = """
                        [{
                        "orderCode": "%s",
                        "billingConfigId": 3,
                        "startDate": "%s",
                        "finalValue": 300.0
                        }]""";

        private static final String DEFAULT_UPDATE_INSTALLMENT_REQUEST_BODY = """
                        {
                        "paidValue": 100.0
                        }""";

        private static Stream<Arguments> getBillingsTestData() {
                return Stream.of(
                                Arguments.of(SUBSCRIBER_3, 15, true, DEFAULT_BILLING_REQUEST_BODY),
                                Arguments.of(SUBSCRIBER_1, 8, false, DEFAULT_BILLING_REQUEST_BODY),
                                Arguments.of(SUBSCRIBER_3, 17, true, DEFAULT_BILLING_REQUEST_BODY));
        }

        private Pair<String, String> prepareOrderData(final String subscriber, final int customerId,
                        final boolean isBilled,
                        final String billingRequestBody)
                        throws Exception {

                // Customer One - Order ready to be billed
                String orderRequestBody = String.format(DEFAULT_ORDER_REQUEST_BODY, customerId,
                                PLANNED_DELIVER_DATE.toString());

                ResultActions customerOneResult = mockMvc.perform(post(ApiPaths.ORDERS_ENDPOINT_PATH, subscriber)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode = (String) TestUtils
                                .convertResponseToMap(customerOneResult.andReturn().getResponse()
                                                .getContentAsString())
                                .get("orderCode");

                // Act
                ResultActions poResult = mockMvc
                                .perform(post(ApiPaths.PLACE_PRODUCTION_ORDER_ENDPOINT_PATH, subscriber,
                                                orderCode)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(DEFAULT_PO_REQUEST_BODY)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String billingUuid = null;

                if (isBilled) {
                        TestTransaction.flagForCommit();
                        TestTransaction.end();
                        TestTransaction.start();

                        String poNumber = (String) TestUtils
                                        .convertResponseToMap(
                                                        poResult.andReturn().getResponse()
                                                                        .getContentAsString())
                                        .get("poNumber");

                        // Move production order from PLANNING to EXECUTING using /proceed endpoint
                        mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH, subscriber, poNumber)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .with(SecurityMockMvcRequestPostProcessors.jwt()));

                        // Move production order from EXECUTING to DONE using /proceed endpoint (this
                        // sets order to PENDING_BILLING)
                        mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH, subscriber, poNumber)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .with(SecurityMockMvcRequestPostProcessors.jwt()));

                        TestTransaction.flagForCommit();
                        TestTransaction.end();
                        TestTransaction.start();

                        ResultActions billingResult = mockMvc.perform(post(ApiPaths.BILLINGS_ENDPOINT_PATH, subscriber)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                                        .content(String.format(billingRequestBody, orderCode,
                                                        BILLING_START_DATE.format(DateTimeFormatter.ISO_LOCAL_DATE))));

                        billingResult.andExpect(status().isOk());

                        billingUuid = (String) TestUtils
                                        .convertResponseToList(billingResult.andReturn().getResponse()
                                                        .getContentAsString())
                                        .get(0)
                                        .get("uuid");

                }

                TestTransaction.flagForCommit();
                TestTransaction.end();

                return Pair.of(orderCode, billingUuid);
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

}
