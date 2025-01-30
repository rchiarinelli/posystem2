package br.com.brainyit.posystem2.order.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import br.com.brainyit.api.BaseApiTest;
import br.com.brainyit.util.TestUtils;

class OrderEndpointTest extends BaseApiTest {

    @Test
    void test_placeOrder_whenValidInput_returnsCreatedOrder() throws Exception {
        // Arrange
        String requestBody = """
                {
                    "customerDocument": "456.789.012-33",
                    "customerName": "Patricia Brown", 
                    "orderDescription": "Sample order 1",
                    "quantity": 10
                }""";

        // Act
        final MockHttpServletRequestBuilder request = post("/1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        final ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isOk())
                .andExpect(jsonPath("$.orderCode").exists())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void test_placeOrder_whenInvalidInput_returnsBadRequest() throws Exception {
        // Arrange - Missing required fields
        String requestBody = """
                {
                    "customerDocument": "123.456.789-00",
                    "customerName": "John Doe"
                }""";

        // Act
        final MockHttpServletRequestBuilder request = post("/1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        final ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isBadRequest());
    }

    @Test
    void test_placeOrder_whenUserNotFound_returnsInternalServerError() throws Exception {
        // Arrange - Invalid quantity that may cause server error
        String requestBody = """
                {
                    "customerDocument": "998.888.999-99",
                    "customerName": "John Doe",
                    "orderDescription": "Sample order 1",
                    "quantity": -1
                }""";

        // Act
        final MockHttpServletRequestBuilder request = post("/1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        final ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isInternalServerError());
    }

    @Test
    void test_placeOrder_whenIndividualNotFound_returnsInternalServerError() throws Exception {
        // Arrange - Non-existent individual customer
        String requestBody = """
                {
                    "customerDocument": "999.999.999-99",
                    "customerName": "Non Existent Individual",
                    "orderDescription": "Test order",
                    "quantity": 1
                }""";

        // Act
        final MockHttpServletRequestBuilder request = post("/1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        final ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("Customer not found"));
    }

    @Test
    void test_placeOrder_whenCompanyNotFound_returnsInternalServerError() throws Exception {
        // Arrange - Non-existent company customer
        String requestBody = """
                {
                    "customerDocument": "99.999.999/9999-99",
                    "customerName": "Non Existent Company",
                    "orderDescription": "Test order",
                    "quantity": 1
                }""";

        // Act
        final MockHttpServletRequestBuilder request = post("/1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        final ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("Customer not found"));
    }

    @Test
    void test_getOrder_success() throws Exception {
        // Arrange - First place an order
        String createOrderRequest = """
                {
                    "customerDocument": "345.678.901-22",
                    "customerName": "Robert Williams",
                    "orderDescription": "Test order",
                    "quantity": 1
                }""";

        MockHttpServletRequestBuilder createRequest = post("/1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createOrderRequest);

        ResultActions createResult = mockMvc.perform(createRequest);
        
        Map<String, Object> responseMap = TestUtils.convertResponseToMap(createResult.andReturn().getResponse().getContentAsString());
        String orderCode = (String) responseMap.get("orderCode");

        // Act - Get the created order
        MockHttpServletRequestBuilder getRequest = get("/1/orders/{orderCode}", orderCode)
                .contentType(MediaType.APPLICATION_JSON);

        ResultActions results = mockMvc.perform(getRequest);

        // Assert
        results.andExpect(status().isOk())
                .andExpect(jsonPath("$.orderCode").value(orderCode))
                .andExpect(jsonPath("$.customerName").value("Robert Williams"))
                .andExpect(jsonPath("$.orderDescription").value("Test order"))
                .andExpect(jsonPath("$.quantity").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void test_getOrder_whenOrderNotFound_returnsInternalServerError() throws Exception {
        // Act
        MockHttpServletRequestBuilder request = get("/1/orders/non-existent-order")
                .contentType(MediaType.APPLICATION_JSON);

        ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("Order not found"));
    }

    @Test
    void test_getOrder_whenEmptyOrderCode_returnsInternalServerError() throws Exception {
        // Act
        MockHttpServletRequestBuilder request = get("/1/orders/")
                .contentType(MediaType.APPLICATION_JSON);

        ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isNotFound());
    }


    @Test
    void test_getOrdersBySubscriberId_success() throws Exception {
        // Arrange - Create 3 orders for Robert Williams
        String requestBody = """
                {
                    "customerDocument": "345.678.901-22",
                    "customerName": "Robert Williams",
                    "orderDescription": "Test order 1", 
                    "quantity": 1
                }""";

        for (int i = 0; i < 3; i++) {
            MockHttpServletRequestBuilder createRequest = post("/1/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody);
            mockMvc.perform(createRequest);
        }

        // Act
        MockHttpServletRequestBuilder request = get("/1/orders")
                .contentType(MediaType.APPLICATION_JSON);

        ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void test_getOrdersBySubscriberId_whenNoOrders_returnsEmptyArray() throws Exception {
        // Act
        MockHttpServletRequestBuilder request = get("/2/orders")
                .contentType(MediaType.APPLICATION_JSON);

        ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}
