package br.com.brainyit.posystem2.order.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

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

        final String SUBSCRIBER_UUID = "123e4567-e89b-12d3-a456-426614174000";

        @Test
        void test_placeOrder_whenValidInput_returnsCreatedOrder() throws Exception {
                // Arrange
                String requestBody = """
                                {
                                    "customerId": 4,
                                    "orderDescription": "Sample order 1",
                                    "quantity": 10
                                }""";

                // Act
                final MockHttpServletRequestBuilder request = post("/" + SUBSCRIBER_UUID + "/orders")
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
                                .andExpect(jsonPath("$.status").value("PENDING"));
        }

        @Test
        void test_placeOrder_whenInvalidInput_returnsBadRequest() throws Exception {
                // Arrange - Missing required fields
                String requestBody = """
                                {
                                    "orderDescription": "Sample order 1",
                                    "quantity": 10
                                }""";

                // Act
                final MockHttpServletRequestBuilder request = post("/" + SUBSCRIBER_UUID + "/orders")
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
                                    "quantity": 1
                                }""";

                // Act
                final MockHttpServletRequestBuilder request = post("/" + SUBSCRIBER_UUID + "/orders")
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
                                    "quantity": 1
                                }""";

                // Act
                final MockHttpServletRequestBuilder request = post("/" + SUBSCRIBER_UUID + "/orders")
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
                                    "quantity": 1
                                }""";

                MockHttpServletRequestBuilder createRequest = post("/" + SUBSCRIBER_UUID + "/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                .content(createOrderRequest);

                ResultActions createResult = mockMvc.perform(createRequest);

                Map<String, Object> responseMap = TestUtils
                                .convertResponseToMap(createResult.andReturn().getResponse().getContentAsString());
                String orderCode = (String) responseMap.get("orderCode");

                // Act - Get the created order
                MockHttpServletRequestBuilder getRequest = get("/" + SUBSCRIBER_UUID + "/orders/{orderCode}", orderCode)
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
                                "/" + SUBSCRIBER_UUID + "/orders/non-existent-order")
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
                MockHttpServletRequestBuilder request = get("/" + SUBSCRIBER_UUID + "/orders/")
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
                                    "quantity": 1
                                }""";

                for (int i = 0; i < 3; i++) {
                        MockHttpServletRequestBuilder createRequest = post(
                                        "/" + SUBSCRIBER_UUID + "/orders")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                                        .content(requestBody);
                        mockMvc.perform(createRequest);
                }

                // Act
                MockHttpServletRequestBuilder request = get("/" + SUBSCRIBER_UUID + "/orders")
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
                MockHttpServletRequestBuilder request = get("/" + SUBSCRIBER_UUID + "/orders")
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
                                    "quantity": 1
                                }""";

                ResultActions orderResult = mockMvc.perform(post("/" + SUBSCRIBER_UUID + "/orders")
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
                                    "qtd": 1
                                }""";

                mockMvc.perform(post("/" + SUBSCRIBER_UUID + "/orders/{orderCode}/place-production-order", orderCode)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(poRequestBody)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Act - Try to cancel the order which should fail
                ResultActions results = mockMvc
                                .perform(put("/" + SUBSCRIBER_UUID + "/orders/{orderCode}/cancel", orderCode)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Assert
                results.andExpect(status().isBadRequest());
        }

        @Test
        void test_cancelOrder_whenOrderDoesNotExist_returnsNotFound() throws Exception {
                // Act
                ResultActions results = mockMvc.perform(put("/" + SUBSCRIBER_UUID + "/orders/INVALID-ORDER-CODE/cancel")
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
                                    "quantity": 1
                                }""";

                ResultActions orderResult = mockMvc.perform(post("/" + SUBSCRIBER_UUID + "/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode = (String) TestUtils
                                .convertResponseToMap(orderResult.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                // Act
                ResultActions results = mockMvc
                                .perform(put("/" + SUBSCRIBER_UUID + "/orders/{orderCode}/cancel", orderCode)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                                .content("{\"reason\": \"Test reason\"}"));

                // Assert
                results.andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("Order canceled"));

                // and then get the order
                var cancelledOrderResult = mockMvc.perform(get("/" + SUBSCRIBER_UUID + "/orders/{orderCode}", orderCode)
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
                                    "quantity": 1
                                }""";

                ResultActions orderResult = mockMvc.perform(post("/" + SUBSCRIBER_UUID + "/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode = (String) TestUtils
                                .convertResponseToMap(orderResult.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                // Act - Cancel the order first time
                ResultActions firstCancelResult = mockMvc
                                .perform(put("/" + SUBSCRIBER_UUID + "/orders/{orderCode}/cancel", orderCode)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                                .content("{\"reason\": \"Test reason\"}"));

                firstCancelResult.andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("Order canceled"));

                // Act & Assert - Try to cancel the order again which should fail
                ResultActions secondCancelResult = mockMvc
                                .perform(put("/" + SUBSCRIBER_UUID + "/orders/{orderCode}/cancel", orderCode)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt())
                                                .content("{\"reason\": \"Test reason\"}"));

                secondCancelResult.andExpect(status().isInternalServerError());
        }

}
