package br.com.brainyit.posystem2.order.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import br.com.brainyit.api.BaseApiTest;
import br.com.brainyit.util.TestUtils;

@Transactional
class OrderEndpointTest extends BaseApiTest {

    private static final String SUBSCRIBER_ONE = "123e4567-e89b-12d3-a456-426614174000";

    private static final String SUBSCRIBER_TWO = "123e4567-e89b-12d3-a456-426614174001";

    private static final LocalDate PLANNED_DELIVER_DATE = LocalDate.now().plusYears(1);

    @Test
    void test_placeOrder_whenValidInput_returnsCreatedOrder() throws Exception {
        // Arrange
        String requestBody = """
                {
                    "customerId": 4,
                    "orderDescription": "Sample order 1",
                    "quantity": 10,
                    "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
                }""";

        // Act
        final MockHttpServletRequestBuilder request = post("/" + SUBSCRIBER_ONE + "/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .content(requestBody);

        final ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isOk())
                .andExpect(jsonPath("$.orderCode").exists())
                .andExpect(jsonPath("$.customerName").isString())
                .andExpect(jsonPath("$.orderDescription").isString())
                .andExpect(jsonPath("$.quantity").isNumber())
                .andExpect(jsonPath("$.status").isString())
                .andExpect(jsonPath("$.openDate").isString())
                .andExpect(jsonPath("$.plannedDeliverDate").isString())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void test_placeOrder_whenInvalidInput_returnsBadRequest() throws Exception {
        // Arrange - Missing required fields
        String requestBody = """
                {
                    "orderDescription": "Sample order 1",
                    "quantity": 10,
                    "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
                }""";

        // Act
        final MockHttpServletRequestBuilder request = post("/" + SUBSCRIBER_ONE + "/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .content(requestBody);

        final ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isBadRequest());
    }

    @Test
    void test_placeOrder_whenIndividualNotFound_returnsInternalServerError() throws Exception {
        // Arrange - Non-existent individual customer
        String requestBody = """
                {
                    "customerId": 1500,
                    "orderDescription": "Test order",
                    "quantity": 1,
                    "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
                }""";

        // Act
        final MockHttpServletRequestBuilder request = post("/" + SUBSCRIBER_ONE + "/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .content(requestBody);

        final ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Customer not found"));
    }

    @Test
    void test_placeOrder_whenCompanyNotFound_returnsInternalServerError() throws Exception {
        // Arrange - Non-existent company customer
        String requestBody = """
                {
                    "customerId": 1000,
                    "orderDescription": "Test order",
                    "quantity": 1,
                    "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
                }""";

        // Act
        final MockHttpServletRequestBuilder request = post("/" + SUBSCRIBER_ONE + "/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .content(requestBody);

        final ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Customer not found"));
    }

    @Test
    void test_getOrder_success() throws Exception {
        // Arrange - First place an order
        String createOrderRequest = """
                {
                    "customerId": 3,
                    "orderDescription": "Test order",
                    "quantity": 1,
                    "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
                }""";

        MockHttpServletRequestBuilder createRequest = post("/" + SUBSCRIBER_ONE + "/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .content(createOrderRequest);

        ResultActions createResult = mockMvc.perform(createRequest);

        Map<String, Object> responseMap = TestUtils
                .convertResponseToMap(createResult.andReturn().getResponse().getContentAsString());
        String orderCode = (String) responseMap.get("orderCode");

        // Act - Get the created order
        MockHttpServletRequestBuilder getRequest = get("/" + SUBSCRIBER_ONE + "/orders/{orderCode}", orderCode)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt());

        ResultActions results = mockMvc.perform(getRequest);

        // Assert
        results.andExpect(status().isOk())
                .andExpect(jsonPath("$.orderCode").value(orderCode))
                .andExpect(jsonPath("$.customerName").value("Robert Williams"))
                .andExpect(jsonPath("$.orderDescription").value("Test order"))
                .andExpect(jsonPath("$.quantity").value(1))
                .andExpect(jsonPath("$.status").isString())
                .andExpect(jsonPath("$.openDate").isString())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void test_getOrder_whenOrderNotFound_returnsInternalServerError() throws Exception {
        // Act
        MockHttpServletRequestBuilder request = get(
                "/" + SUBSCRIBER_ONE + "/orders/non-existent-order")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt());

        ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Order not found"));
    }

    @Test
    void test_getOrder_whenEmptyOrderCode_returnsInternalServerError() throws Exception {
        // Act
        MockHttpServletRequestBuilder request = get("/" + SUBSCRIBER_ONE + "/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt());

        ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isNotFound());
    }

    @Test
    void test_getOrdersBySubscriberId_success() throws Exception {
        // Arrange - Create 3 orders for Robert Williams
        String requestBody = """
                {
                    "customerId": 3,
                    "orderDescription": "Test order 1",
                    "quantity": 1,
                    "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
                }""";

        for (int i = 0; i < 3; i++) {
            MockHttpServletRequestBuilder createRequest = post(
                    "/" + SUBSCRIBER_ONE + "/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(SecurityMockMvcRequestPostProcessors.jwt())
                    .content(requestBody);
            mockMvc.perform(createRequest);
        }

        // Act
        MockHttpServletRequestBuilder request = get("/" + SUBSCRIBER_ONE + "/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt());

        ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void test_getOrdersBySubscriberId_whenNoOrders_returnsEmptyArray() throws Exception {
        // Act
        MockHttpServletRequestBuilder request = get("/" + SUBSCRIBER_TWO + "/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt());

        ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void test_cancelOrder_whenOrderHasProductionOrder_returnsBadRequest() throws Exception {
        // Arrange - First create an order
        String orderRequestBody = """
                {
                    "customerId": 3,
                    "orderDescription": "Test order 1",
                    "quantity": 1,
                    "plannedDeliverDate": """ + PLANNED_DELIVER_DATE.toString() + """
                }""";

        ResultActions orderResult = mockMvc.perform(post("/" + SUBSCRIBER_ONE + "/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderRequestBody)
                .with(SecurityMockMvcRequestPostProcessors.jwt()));

        String orderCode = (String) TestUtils
                .convertResponseToMap(orderResult.andReturn().getResponse().getContentAsString())
                .get("orderCode");

        // Create a production order for this order
        String poRequestBody = """
                {
                    "estimatedTime": 10,
                    "description": "Production order description",
                    "qtd": 1,
                    "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
                }""";

        mockMvc.perform(post("/" + SUBSCRIBER_ONE + "/orders/{orderCode}/place-production-order", orderCode)
                .contentType(MediaType.APPLICATION_JSON)
                .content(poRequestBody)
                .with(SecurityMockMvcRequestPostProcessors.jwt()));

        // Act - Try to cancel the order which should fail
        ResultActions results = mockMvc
                .perform(put("/" + SUBSCRIBER_ONE + "/orders/{orderCode}/cancel", orderCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()));

        // Assert
        results.andExpect(status().isBadRequest());
    }

    @Test
    void test_cancelOrder_whenOrderDoesNotExist_returnsNotFound() throws Exception {
        // Act
        ResultActions results = mockMvc.perform(put("/" + SUBSCRIBER_ONE + "/orders/INVALID-ORDER-CODE/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt()));

        // Assert
        results.andExpect(status().isBadRequest());
    }

    @Test
    void test_cancelOrder_success() throws Exception {
        // Arrange - Create an order
        String orderRequestBody = """
                {
                    "customerId": 3,
                    "orderDescription": "Test order 1",
                    "quantity": 1,
                    "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
                }""";

        ResultActions orderResult = mockMvc.perform(post("/" + SUBSCRIBER_ONE + "/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderRequestBody)
                .with(SecurityMockMvcRequestPostProcessors.jwt()));

        String orderCode = (String) TestUtils
                .convertResponseToMap(orderResult.andReturn().getResponse().getContentAsString())
                .get("orderCode");

        // Act
        ResultActions results = mockMvc
                .perform(put("/" + SUBSCRIBER_ONE + "/orders/{orderCode}/cancel", orderCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .content("{\"reason\": \"Test reason\"}"));

        // Assert
        results.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Order canceled"));

        // and then get the order
        var cancelledOrderResult = mockMvc.perform(get("/" + SUBSCRIBER_ONE + "/orders/{orderCode}", orderCode)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt()));

        // and then assert the order is cancelled
        cancelledOrderResult.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"))
                .andExpect(jsonPath("$.closeDate").exists());
    }

    @Test
    void test_cancelOrderTwice_returnsBadRequest() throws Exception {
        // Arrange - Create an order
        String orderRequestBody = """
                {
                    "customerId": 3,
                    "orderDescription": "Test order 1",
                    "quantity": 1,
                    "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
                }""";

        ResultActions orderResult = mockMvc.perform(post("/" + SUBSCRIBER_ONE + "/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderRequestBody)
                .with(SecurityMockMvcRequestPostProcessors.jwt()));

        String orderCode = (String) TestUtils
                .convertResponseToMap(orderResult.andReturn().getResponse().getContentAsString())
                .get("orderCode");

        // Act - Cancel the order first time
        ResultActions firstCancelResult = mockMvc
                .perform(put("/" + SUBSCRIBER_ONE + "/orders/{orderCode}/cancel", orderCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .content("{\"reason\": \"Test reason\"}"));

        firstCancelResult.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Order canceled"));

        // Act & Assert - Try to cancel the order again which should fail
        ResultActions secondCancelResult = mockMvc
                .perform(put("/" + SUBSCRIBER_ONE + "/orders/{orderCode}/cancel", orderCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .content("{\"reason\": \"Test reason\"}"));

        secondCancelResult.andExpect(status().isInternalServerError());
    }

    @Test
    void test_cancelOrder_whenPlannedDeliverDateIsInThePast_returnsInternalServerError() throws Exception {

        final var plannedDeliverDate = LocalDate.now().minusDays(1);

        // Arrange - Create an order
        String orderRequestBody = """
                {
                    "customerId": 3,
                    "orderDescription": "Test order 1",
                    "quantity": 1,
                    "plannedDeliverDate": """ + "\"" + plannedDeliverDate.toString()
                + "\"" + """
                        }""";

        ResultActions orderResult = mockMvc.perform(post("/" + SUBSCRIBER_ONE + "/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderRequestBody)
                .with(SecurityMockMvcRequestPostProcessors.jwt()));

        // Assert
        orderResult.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Planned deliver date cannot be in the past"));
    }

    @Test
    void test_getOrdersBySubscriberId_filterByStatus_dataReturned() throws Exception {
        // Arrange - Create 3 orders
        String orderRequestBody = """
                {
                    "customerId": 13,
                    "orderDescription": "Test order",
                    "quantity": 1,
                    "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
                }""";

        // Create first order
        ResultActions firstOrderResult = mockMvc.perform(post("/" + SUBSCRIBER_TWO + "/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderRequestBody)
                .with(SecurityMockMvcRequestPostProcessors.jwt()));

        Map<String, Object> firstOrderResponse = TestUtils
                .convertResponseToMap(firstOrderResult.andReturn().getResponse().getContentAsString());
        String firstOrderCode = (String) firstOrderResponse.get("orderCode");

        // Create second order
        ResultActions secondOrderResult = mockMvc.perform(post("/" + SUBSCRIBER_TWO + "/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderRequestBody)
                .with(SecurityMockMvcRequestPostProcessors.jwt()));

        Map<String, Object> secondOrderResponse = TestUtils
                .convertResponseToMap(secondOrderResult.andReturn().getResponse().getContentAsString());
        String secondOrderCode = (String) secondOrderResponse.get("orderCode");

        // Create third order
        mockMvc.perform(post("/" + SUBSCRIBER_TWO + "/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderRequestBody)
                .with(SecurityMockMvcRequestPostProcessors.jwt()));

        // Cancel first order
        mockMvc.perform(put("/" + SUBSCRIBER_TWO + "/orders/{orderCode}/cancel", firstOrderCode)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .content("{\"reason\": \"Test cancellation\"}"));

        // Place production order for second order (changes status to EXECUTING)
        String poRequestBody = """
                {
                    "estimatedTime": 10,
                    "instructions": "Production order description",
                    "qtd": 1,
                    "plannedStartDate": "2026-01-01",
                    "priority": "STANDARD"
                }""";

        mockMvc.perform(
                post("/" + SUBSCRIBER_TWO + "/orders/" + secondOrderCode + "/place-production-order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(poRequestBody)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()));

        // Act & Assert - Filter by CANCELLED status
        ResultActions cancelledOrdersResult = mockMvc
                .perform(get("/" + SUBSCRIBER_TWO + "/orders?status=CANCELLED")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()));

        cancelledOrdersResult.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].status").value("CANCELLED"));

        // Act & Assert - Filter by EXECUTING status
        ResultActions executingOrdersResult = mockMvc
                .perform(get("/" + SUBSCRIBER_TWO + "/orders?status=EXECUTING")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()));

        executingOrdersResult.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].status").value("EXECUTING"));

        // Act & Assert - Filter by PENDING status
        ResultActions pendingOrdersResult = mockMvc
                .perform(get("/" + SUBSCRIBER_TWO + "/orders?status=PENDING")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()));

        pendingOrdersResult.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        // Act & Assert - Filter by ALL status
        ResultActions executingAndPendingOrdersResult = mockMvc
                .perform(get("/" + SUBSCRIBER_TWO + "/orders?status=EXECUTING,PENDING")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()));

        executingAndPendingOrdersResult.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(Matchers.hasSize(2)));

        // Act & Assert - No filter (should return all 3 orders)
        ResultActions allOrdersResult = mockMvc.perform(get("/" + SUBSCRIBER_TWO + "/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt()));

        allOrdersResult.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(Matchers.hasSize(3)));
    }

    @Test
    void test_getOrdersBySubscriberId_filterByName_dataReturned() throws Exception {
        // Arrange - Create 3 orders for Another Customer
        String acmeOrderRequestBody = """
                {
                    "customerId": 11,
                    "orderDescription": "Test order for Another Customer",
                    "quantity": 1,
                    "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
                }""";

        for (int i = 0; i < 3; i++) {
            MockHttpServletRequestBuilder createRequest = post(
                    "/" + SUBSCRIBER_TWO + "/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(SecurityMockMvcRequestPostProcessors.jwt())
                    .content(acmeOrderRequestBody);
            mockMvc.perform(createRequest);
        }

        // Create 2 orders for Patricia Customer
        String patriciaOrderRequestBody = """
                {
                    "customerId": 12,
                    "orderDescription": "Test order for Patricia Customer",
                    "quantity": 1,
                    "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
                }""";

        for (int i = 0; i < 2; i++) {
            MockHttpServletRequestBuilder createRequest = post(
                    "/" + SUBSCRIBER_TWO + "/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(SecurityMockMvcRequestPostProcessors.jwt())
                    .content(patriciaOrderRequestBody);
            mockMvc.perform(createRequest);
        }

        // Act & Assert - Filter by "Patricia"
        ResultActions patriciaOrdersResult = mockMvc
                .perform(get("/" + SUBSCRIBER_TWO + "/orders?name=Patricia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()));

        patriciaOrdersResult.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].customerName").value("Patricia Customer"))
                .andExpect(jsonPath("$[1].customerName").value("Patricia Customer"));

        // Act & Assert - Filter by "cme Corp"
        ResultActions acmeOrdersResult = mockMvc
                .perform(get("/" + SUBSCRIBER_TWO + "/orders?name=another")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()));

        acmeOrdersResult.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].customerName").value("Another Customer"))
                .andExpect(jsonPath("$[1].customerName").value("Another Customer"))
                .andExpect(jsonPath("$[2].customerName").value("Another Customer"));

        // Act & Assert - No filter (should return all 5 orders)
        ResultActions allOrdersResult = mockMvc.perform(get("/" + SUBSCRIBER_TWO + "/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt()));

        allOrdersResult.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(Matchers.hasSize(5)));
    }

    @Test
    void test_getOrdersBySubscriberId_filterByOrderCode_dataReturned() throws Exception {
        // Arrange - Create 4 orders
        String orderRequestBody = """
                {
                    "customerId": 14,
                    "orderDescription": "Test order",
                    "quantity": 1,
                    "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
                }""";

        List<String> orderCodes = new ArrayList<>();

        // Create 4 orders and collect their order codes
        for (int i = 0; i < 4; i++) {
            ResultActions orderResult = mockMvc.perform(post("/" + SUBSCRIBER_TWO + "/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(orderRequestBody)
                    .with(SecurityMockMvcRequestPostProcessors.jwt()));

            String orderCode = (String) TestUtils
                    .convertResponseToMap(
                            orderResult.andReturn().getResponse().getContentAsString())
                    .get("orderCode");
            orderCodes.add(orderCode);
        }

        // Act & Assert - Filter by second order code
        ResultActions secondOrderResult = mockMvc
                .perform(get("/" + SUBSCRIBER_TWO + "/orders?orderCode=" + orderCodes.get(1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()));

        secondOrderResult.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].orderCode").value(orderCodes.get(1)));

        // Act & Assert - Filter by last order code
        ResultActions lastOrderResult = mockMvc
                .perform(get("/" + SUBSCRIBER_TWO + "/orders?orderCode=" + orderCodes.get(3))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()));

        lastOrderResult.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].orderCode").value(orderCodes.get(3)));

        // Act & Assert - No filter (should return all 4 orders)
        ResultActions allOrdersResult = mockMvc.perform(get("/" + SUBSCRIBER_TWO + "/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt()));

        allOrdersResult.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(Matchers.hasSize(4)));
    }

}
