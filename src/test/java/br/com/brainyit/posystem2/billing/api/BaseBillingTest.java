package br.com.brainyit.posystem2.billing.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import br.com.brainyit.api.ApiPaths;
import br.com.brainyit.api.BaseApiTest;
import br.com.brainyit.util.TestUtils;

@Transactional
public abstract class BaseBillingTest extends BaseApiTest {

        static final String SUBSCRIBER_1 = "123e4567-e89b-12d3-a456-426614174000";

        static final String SUBSCRIBER_3 = "123e4567-e89b-12d3-a456-426614174002";

        static final LocalDate PLANNED_DELIVER_DATE = LocalDate.now().plusYears(1);

        static final LocalDate BILLING_START_DATE = LocalDate.now().plusDays(1);

        static final String DEFAULT_ORDER_REQUEST_BODY = """
                        {
                        "customerId": %d,
                        "orderDescription": "Test order for billing",
                        "quantity": 1,
                        "plannedDeliverDate": "%s"
                        }""";

        static final String DEFAULT_PO_REQUEST_BODY = """
                        {
                        "estimatedTime": 10,
                        "instructions": "Production order description",
                        "qtd": 10,
                        "plannedStartDate": "2026-01-01",
                        "priority": "STANDARD"
                        }""";

        static final String DEFAULT_BILLING_REQUEST_BODY = """
                        [{
                        "orderCode": "%s",
                        "billingConfigId": 1,
                        "startDate": "%s",
                        "finalValue": 100.0
                        }]""";

        static final String THREE_INSTALLMENTS_BILLING_REQUEST_BODY = """
                        [{
                        "orderCode": "%s",
                        "billingConfigId": 3,
                        "startDate": "%s",
                        "finalValue": 300.0
                        }]""";

        static final String DEFAULT_UPDATE_INSTALLMENT_REQUEST_BODY = """
                        {
                        "paidValue": %f
                        }""";

        Pair<String, String> prepareOrderData(final String subscriber, final int customerId,
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

}
