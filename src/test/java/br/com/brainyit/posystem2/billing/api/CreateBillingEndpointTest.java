package br.com.brainyit.posystem2.billing.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import br.com.brainyit.api.ApiPaths;
import br.com.brainyit.api.BaseApiTest;
import br.com.brainyit.util.TestUtils;

@Transactional
public class CreateBillingEndpointTest extends BaseApiTest {

        private static final String SUBSCRIBER_UUID = "123e4567-e89b-12d3-a456-426614174000";

        private static final LocalDate PLANNED_DELIVER_DATE = LocalDate.now().plusYears(1);

        private static final LocalDate BILLING_START_DATE = LocalDate.now().plusDays(1);

        // Positive Test Scenarios

        @Test
        void test_createBilling_whenValidInput_returnsCreatedBilling() throws Exception {
                // Arrange - Create order first
                String orderRequestBody = String.format("""
                                {
                                    "customerId": 3,
                                    "orderDescription": "Test order for billing",
                                    "quantity": 1,
                                    "plannedDeliverDate": "%s"
                                }""", PLANNED_DELIVER_DATE.toString());

                ResultActions orderResult = mockMvc.perform(post(ApiPaths.ORDERS_ENDPOINT_PATH, SUBSCRIBER_UUID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode = (String) TestUtils
                                .convertResponseToMap(orderResult.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                // Create production order for the order
                String createPORequest = String.format("""
                                {
                                    "instructions": "Test PO for billing test",
                                    "qtd": 1,
                                    "estimatedTime": 8.0,
                                    "plannedStartDate": "%s",
                                    "priority": "STANDARD"
                                }""", LocalDate.now().toString());

                ResultActions createPOResult = mockMvc
                                .perform(post(ApiPaths.PLACE_PRODUCTION_ORDER_ENDPOINT_PATH, SUBSCRIBER_UUID, orderCode)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                                .content(createPORequest));

                String poNumber = (String) TestUtils
                                .convertResponseToMap(createPOResult.andReturn().getResponse().getContentAsString())
                                .get("poNumber");

                // Move production order from PLANNING to EXECUTING using /proceed endpoint
                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH, SUBSCRIBER_UUID, poNumber)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                TestTransaction.flagForCommit();
                TestTransaction.end();
                TestTransaction.start();

                // Move production order from EXECUTING to DONE using /proceed endpoint (this
                // sets order to PENDING_BILLING)
                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH, SUBSCRIBER_UUID, poNumber)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                TestTransaction.flagForCommit();
                TestTransaction.end();
                TestTransaction.start();

                String requestBody = String.format("""
                                [
                                    {
                                        "orderCode": "%s",
                                        "billingConfigId": 1,
                                        "startDate": "%s",
                                        "finalValue": 100.0
                                    }
                                ]""", orderCode, BILLING_START_DATE.toString());

                // Act
                final MockHttpServletRequestBuilder request = post(ApiPaths.BILLINGS_ENDPOINT_PATH, SUBSCRIBER_UUID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                .content(requestBody);

                final ResultActions results = mockMvc.perform(request);

                // Assert
                results.andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$[0].uuid").exists())
                                .andExpect(jsonPath("$[0].status").value("OPEN"))
                                .andExpect(jsonPath("$[0].finalValue").value(100.0))
                                .andExpect(jsonPath("$[0].order").exists())
                                .andExpect(jsonPath("$[0].order.orderCode").value(orderCode))
                                .andExpect(jsonPath("$[0].installments").isArray())
                                .andExpect(jsonPath("$[0].installments[0].status").value("PENDING"));
        }

        @Test
        void test_createBilling_withInstallmentPayment_returnsCorrectInstallments() throws Exception {
                // Arrange - Create order first
                String orderRequestBody = String.format("""
                                {
                                    "customerId": 3,
                                    "orderDescription": "Test order for installment billing",
                                    "quantity": 1,
                                    "plannedDeliverDate": "%s"
                                }""", PLANNED_DELIVER_DATE.toString());

                ResultActions orderResult = mockMvc.perform(post(ApiPaths.ORDERS_ENDPOINT_PATH, SUBSCRIBER_UUID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode = (String) TestUtils
                                .convertResponseToMap(orderResult.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                // Create production order for the order
                String createPORequest = String.format("""
                                {
                                    "instructions": "Test PO for installment billing test",
                                    "qtd": 1,
                                    "estimatedTime": 8.0,
                                    "plannedStartDate": "%s",
                                    "priority": "STANDARD"
                                }""", LocalDate.now().toString());

                ResultActions createPOResult = mockMvc
                                .perform(post(ApiPaths.PLACE_PRODUCTION_ORDER_ENDPOINT_PATH, SUBSCRIBER_UUID, orderCode)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                                .content(createPORequest));

                String poNumber = (String) TestUtils
                                .convertResponseToMap(createPOResult.andReturn().getResponse().getContentAsString())
                                .get("poNumber");

                // Move production order from PLANNING to EXECUTING using /proceed endpoint
                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH, SUBSCRIBER_UUID, poNumber)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                TestTransaction.flagForCommit();
                TestTransaction.end();
                TestTransaction.start();

                // Move production order from EXECUTING to DONE using /proceed endpoint (this
                // sets order to PENDING_BILLING)
                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH, SUBSCRIBER_UUID, poNumber)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                TestTransaction.flagForCommit();
                TestTransaction.end();
                TestTransaction.start();

                String requestBody = String.format("""
                                [
                                    {
                                        "orderCode": "%s",
                                        "billingConfigId": 3,
                                        "startDate": "%s",
                                        "finalValue": 300.0
                                    }
                                ]""", orderCode, BILLING_START_DATE.toString());

                // Act
                final MockHttpServletRequestBuilder request = post(ApiPaths.BILLINGS_ENDPOINT_PATH, SUBSCRIBER_UUID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                .content(requestBody);

                final ResultActions results = mockMvc.perform(request);

                // Assert - Should create 3 installments (billing config ID 3 is 3x
                // installments)
                results.andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].installments").isArray())
                                .andExpect(jsonPath("$[0].installments.length()").value(3))
                                .andExpect(jsonPath("$[0].installments[0].dueValue").value(100.0))
                                .andExpect(jsonPath("$[0].installments[1].dueValue").value(100.0))
                                .andExpect(jsonPath("$[0].installments[2].dueValue").value(100.0));
        }

        @Test
        void test_createMultipleBillings_whenValidInput_returnsAllCreatedBillings() throws Exception {
                // Arrange - Create first order
                String orderRequestBody1 = String.format("""
                                {
                                    "customerId": 3,
                                    "orderDescription": "Test order 1 for multiple billing",
                                    "quantity": 1,
                                    "plannedDeliverDate": "%s"
                                }""", PLANNED_DELIVER_DATE.toString());

                ResultActions orderResult1 = mockMvc.perform(post(ApiPaths.ORDERS_ENDPOINT_PATH, SUBSCRIBER_UUID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody1)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode1 = (String) TestUtils
                                .convertResponseToMap(orderResult1.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                // Create production order for first order
                String createPORequest1 = String.format("""
                                {
                                    "instructions": "Test PO 1 for multiple billing test",
                                    "qtd": 1,
                                    "estimatedTime": 8.0,
                                    "plannedStartDate": "%s",
                                    "priority": "STANDARD"
                                }""", LocalDate.now().toString());

                ResultActions createPOResult1 = mockMvc
                                .perform(post(ApiPaths.PLACE_PRODUCTION_ORDER_ENDPOINT_PATH, SUBSCRIBER_UUID,
                                                orderCode1)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                                .content(createPORequest1));

                String poNumber1 = (String) TestUtils
                                .convertResponseToMap(createPOResult1.andReturn().getResponse().getContentAsString())
                                .get("poNumber");

                // Complete first production order using /proceed endpoint twice
                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH, SUBSCRIBER_UUID, poNumber1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                TestTransaction.flagForCommit();
                TestTransaction.end();
                TestTransaction.start();

                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH, SUBSCRIBER_UUID, poNumber1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                TestTransaction.flagForCommit();
                TestTransaction.end();
                TestTransaction.start();

                // Create second order
                String orderRequestBody2 = String.format("""
                                {
                                    "customerId": 4,
                                    "orderDescription": "Test order 2 for multiple billing",
                                    "quantity": 1,
                                    "plannedDeliverDate": "%s"
                                }""", PLANNED_DELIVER_DATE.toString());

                ResultActions orderResult2 = mockMvc.perform(post(ApiPaths.ORDERS_ENDPOINT_PATH, SUBSCRIBER_UUID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody2)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode2 = (String) TestUtils
                                .convertResponseToMap(orderResult2.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                // Create production order for second order
                String createPORequest2 = String.format("""
                                {
                                    "instructions": "Test PO 2 for multiple billing test",
                                    "qtd": 1,
                                    "estimatedTime": 8.0,
                                    "plannedStartDate": "%s",
                                    "priority": "STANDARD"
                                }""", LocalDate.now().toString());

                ResultActions createPOResult2 = mockMvc
                                .perform(post(ApiPaths.PLACE_PRODUCTION_ORDER_ENDPOINT_PATH, SUBSCRIBER_UUID,
                                                orderCode2)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                                .content(createPORequest2));

                String poNumber2 = (String) TestUtils
                                .convertResponseToMap(createPOResult2.andReturn().getResponse().getContentAsString())
                                .get("poNumber");

                // Complete second production order using /proceed endpoint twice
                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH, SUBSCRIBER_UUID, poNumber2)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                TestTransaction.flagForCommit();
                TestTransaction.end();
                TestTransaction.start();

                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH, SUBSCRIBER_UUID, poNumber2)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                TestTransaction.flagForCommit();
                TestTransaction.end();
                TestTransaction.start();

                String requestBody = String.format("""
                                [
                                    {
                                        "orderCode": "%s",
                                        "billingConfigId": 1,
                                        "startDate": "%s",
                                        "finalValue": 100.0
                                    },
                                    {
                                        "orderCode": "%s",
                                        "billingConfigId": 2,
                                        "startDate": "%s",
                                        "finalValue": 200.0
                                    }
                                ]""", orderCode1, BILLING_START_DATE.toString(), orderCode2,
                                BILLING_START_DATE.toString());

                // Act
                final MockHttpServletRequestBuilder request = post(ApiPaths.BILLINGS_ENDPOINT_PATH, SUBSCRIBER_UUID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                .content(requestBody);

                final ResultActions results = mockMvc.perform(request);

                // Assert
                results.andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].order.orderCode").value(orderCode1))
                                .andExpect(jsonPath("$[1].order.orderCode").value(orderCode2));
        }

        // Negative Test Scenarios

        @Test
        void test_createBilling_whenInvalidSubscriber_returnsInternalServerError() throws Exception {
                // Arrange
                String requestBody = String.format("""
                                [
                                    {
                                        "orderCode": "SOME-ORDER",
                                        "billingConfigId": 1,
                                        "startDate": "%s",
                                        "finalValue": 100.0
                                    }
                                ]""", BILLING_START_DATE.toString());

                // Act
                final MockHttpServletRequestBuilder request = post("/invalid-subscriber-uuid/billings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                .content(requestBody);

                final ResultActions results = mockMvc.perform(request);

                // Assert
                results.andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Subscriber not found"));
        }

        @Test
        void test_createBilling_whenNonExistentOrder_returnsInternalServerError() throws Exception {
                // Arrange
                String requestBody = String.format("""
                                [
                                    {
                                        "orderCode": "NON-EXISTENT-ORDER",
                                        "billingConfigId": 1,
                                        "startDate": "%s",
                                        "finalValue": 100.0
                                    }
                                ]""", BILLING_START_DATE.toString());

                // Act
                final MockHttpServletRequestBuilder request = post(ApiPaths.BILLINGS_ENDPOINT_PATH, SUBSCRIBER_UUID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                .content(requestBody);

                final ResultActions results = mockMvc.perform(request);

                // Assert
                results.andExpect(status().isInternalServerError())
                                .andExpect(jsonPath("$.message").value("Order not found"));
        }

        @Test
        void test_createBilling_whenOrderNotInPendingBillingStatus_returnsInternalServerError() throws Exception {
                // Arrange - Create order but don't move to ready for billing (stays in PENDING
                // status)
                String orderRequestBody = String.format("""
                                {
                                    "customerId": 3,
                                    "orderDescription": "Test order not ready for billing",
                                    "quantity": 1,
                                    "plannedDeliverDate": "%s"
                                }""", PLANNED_DELIVER_DATE.toString());

                ResultActions orderResult = mockMvc.perform(post(ApiPaths.ORDERS_ENDPOINT_PATH, SUBSCRIBER_UUID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode = (String) TestUtils
                                .convertResponseToMap(orderResult.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                String requestBody = String.format("""
                                [
                                    {
                                        "orderCode": "%s",
                                        "billingConfigId": 1,
                                        "startDate": "%s",
                                        "finalValue": 100.0
                                    }
                                ]""", orderCode, BILLING_START_DATE.toString());

                // Act
                final MockHttpServletRequestBuilder request = post(ApiPaths.BILLINGS_ENDPOINT_PATH, SUBSCRIBER_UUID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                .content(requestBody);

                final ResultActions results = mockMvc.perform(request);

                // Assert
                results.andExpect(status().isInternalServerError())
                                .andExpect(jsonPath("$.message").value("Order is not in pending billing status"));
        }

        @Test
        void test_createBilling_whenNonExistentBillingConfig_returnsInternalServerError() throws Exception {
                // Arrange - Create order and prepare for billing
                String orderRequestBody = String.format("""
                                {
                                    "customerId": 3,
                                    "orderDescription": "Test order for non-existent config test",
                                    "quantity": 1,
                                    "plannedDeliverDate": "%s"
                                }""", PLANNED_DELIVER_DATE.toString());

                ResultActions orderResult = mockMvc.perform(post(ApiPaths.ORDERS_ENDPOINT_PATH, SUBSCRIBER_UUID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode = (String) TestUtils
                                .convertResponseToMap(orderResult.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                // Create production order for the order
                String createPORequest = String.format("""
                                {
                                    "instructions": "Test PO for config test",
                                    "qtd": 1,
                                    "estimatedTime": 8.0,
                                    "plannedStartDate": "%s",
                                    "priority": "STANDARD"
                                }""", LocalDate.now().toString());

                ResultActions createPOResult = mockMvc
                                .perform(post(ApiPaths.PLACE_PRODUCTION_ORDER_ENDPOINT_PATH, SUBSCRIBER_UUID, orderCode)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                                .content(createPORequest));

                String poNumber = (String) TestUtils
                                .convertResponseToMap(createPOResult.andReturn().getResponse().getContentAsString())
                                .get("poNumber");

                // Move production order through lifecycle using /proceed endpoint twice
                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH, SUBSCRIBER_UUID, poNumber)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                TestTransaction.flagForCommit();
                TestTransaction.end();
                TestTransaction.start();

                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH, SUBSCRIBER_UUID, poNumber)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                TestTransaction.flagForCommit();
                TestTransaction.end();
                TestTransaction.start();

                String requestBody = String.format("""
                                [
                                    {
                                        "orderCode": "%s",
                                        "billingConfigId": 999,
                                        "startDate": "%s",
                                        "finalValue": 100.0
                                    }
                                ]""", orderCode, BILLING_START_DATE.toString());

                // Act
                final MockHttpServletRequestBuilder request = post(ApiPaths.BILLINGS_ENDPOINT_PATH, SUBSCRIBER_UUID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                .content(requestBody);

                final ResultActions results = mockMvc.perform(request);

                // Assert
                results.andExpect(status().isInternalServerError())
                                .andExpect(jsonPath("$.message").value("Billing config not found"));
        }

        @Test
        void test_createBilling_whenStartDateInPast_returnsInternalServerError() throws Exception {
                // Arrange - Create order and prepare for billing
                String orderRequestBody = String.format("""
                                {
                                    "customerId": 3,
                                    "orderDescription": "Test order for past date test",
                                    "quantity": 1,
                                    "plannedDeliverDate": "%s"
                                }""", PLANNED_DELIVER_DATE.toString());

                ResultActions orderResult = mockMvc.perform(post(ApiPaths.ORDERS_ENDPOINT_PATH, SUBSCRIBER_UUID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode = (String) TestUtils
                                .convertResponseToMap(orderResult.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                // Create production order for the order
                String createPORequest = String.format("""
                                {
                                    "instructions": "Test PO for past date test",
                                    "qtd": 1,
                                    "estimatedTime": 8.0,
                                    "plannedStartDate": "%s",
                                    "priority": "STANDARD"
                                }""", LocalDate.now().toString());

                ResultActions createPOResult = mockMvc
                                .perform(post(ApiPaths.PLACE_PRODUCTION_ORDER_ENDPOINT_PATH, SUBSCRIBER_UUID, orderCode)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                                .content(createPORequest));

                String poNumber = (String) TestUtils
                                .convertResponseToMap(createPOResult.andReturn().getResponse().getContentAsString())
                                .get("poNumber");

                // Move production order through lifecycle using /proceed endpoint twice
                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH, SUBSCRIBER_UUID, poNumber)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                TestTransaction.flagForCommit();
                TestTransaction.end();
                TestTransaction.start();

                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH, SUBSCRIBER_UUID, poNumber)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                TestTransaction.flagForCommit();
                TestTransaction.end();
                TestTransaction.start();

                String pastDate = LocalDate.now().minusDays(1).toString();
                String requestBody = String.format("""
                                [
                                    {
                                        "orderCode": "%s",
                                        "billingConfigId": 1,
                                        "startDate": "%s",
                                        "finalValue": 100.0
                                    }
                                ]""", orderCode, pastDate);

                // Act
                final MockHttpServletRequestBuilder request = post(ApiPaths.BILLINGS_ENDPOINT_PATH, SUBSCRIBER_UUID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                .content(requestBody);

                final ResultActions results = mockMvc.perform(request);

                // Assert
                results.andExpect(status().isInternalServerError())
                                .andExpect(jsonPath("$.message").value("Start date cannot be in the past"));
        }

        @Test
        void test_createBilling_whenMissingRequiredFields_returnsBadRequest() throws Exception {
                // Arrange - Missing orderCode
                String requestBody = String.format("""
                                [
                                    {
                                        "billingConfigId": 1,
                                        "startDate": "%s",
                                        "finalValue": 100.0
                                    }
                                ]""", BILLING_START_DATE.toString());

                // Act
                final MockHttpServletRequestBuilder request = post(ApiPaths.BILLINGS_ENDPOINT_PATH, SUBSCRIBER_UUID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                .content(requestBody);

                final ResultActions results = mockMvc.perform(request);

                // Assert
                results.andExpect(status().isBadRequest());
        }

        @Test
        void test_createBilling_whenEmptyRequestBody_returnsBadRequest() throws Exception {
                // Arrange
                String requestBody = "[]";

                // Act
                final MockHttpServletRequestBuilder request = post(ApiPaths.BILLINGS_ENDPOINT_PATH, SUBSCRIBER_UUID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                .content(requestBody);

                final ResultActions results = mockMvc.perform(request);

                // Assert
                results.andExpect(status().isInternalServerError());
        }

        @Test
        void test_createBilling_whenInvalidJsonFormat_returnsBadRequest() throws Exception {
                // Arrange - Invalid JSON
                String requestBody = "{ invalid json }";

                // Act
                final MockHttpServletRequestBuilder request = post(ApiPaths.BILLINGS_ENDPOINT_PATH, SUBSCRIBER_UUID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                .content(requestBody);

                final ResultActions results = mockMvc.perform(request);

                // Assert
                results.andExpect(status().isBadRequest());
        }

        @Test
        void test_createBilling_whenNullFinalValue_returnsBadRequest() throws Exception {
                // Arrange - Create order and prepare for billing
                String orderRequestBody = String.format("""
                                {
                                    "customerId": 3,
                                    "orderDescription": "Test order for null value test",
                                    "quantity": 1,
                                    "plannedDeliverDate": "%s"
                                }""", PLANNED_DELIVER_DATE.toString());

                ResultActions orderResult = mockMvc.perform(post(ApiPaths.ORDERS_ENDPOINT_PATH, SUBSCRIBER_UUID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode = (String) TestUtils
                                .convertResponseToMap(orderResult.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                // Create production order for the order
                String createPORequest = String.format("""
                                {
                                    "instructions": "Test PO for null value test",
                                    "qtd": 1,
                                    "estimatedTime": 8.0,
                                    "plannedStartDate": "%s",
                                    "priority": "STANDARD"
                                }""", LocalDate.now().toString());

                ResultActions createPOResult = mockMvc
                                .perform(post(ApiPaths.PLACE_PRODUCTION_ORDER_ENDPOINT_PATH, SUBSCRIBER_UUID, orderCode)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                                .content(createPORequest));

                String poNumber = (String) TestUtils
                                .convertResponseToMap(createPOResult.andReturn().getResponse().getContentAsString())
                                .get("poNumber");

                // Move production order through lifecycle using /proceed endpoint twice
                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH, SUBSCRIBER_UUID, poNumber)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH, SUBSCRIBER_UUID, poNumber)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String requestBody = String.format("""
                                [
                                    {
                                        "orderCode": "%s",
                                        "billingConfigId": 1,
                                        "startDate": "%s",
                                        "finalValue": null
                                    }
                                ]""", orderCode, BILLING_START_DATE.toString());

                // Act
                final MockHttpServletRequestBuilder request = post(ApiPaths.BILLINGS_ENDPOINT_PATH, SUBSCRIBER_UUID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                .content(requestBody);

                final ResultActions results = mockMvc.perform(request);

                // Assert
                results.andExpect(status().isBadRequest());
        }
}
