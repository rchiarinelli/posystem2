package br.com.brainyit.posystem2.po.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import br.com.brainyit.api.BaseApiTest;
import br.com.brainyit.util.TestUtils;

@Transactional
class ReadProductionOrderEndpointTest extends BaseApiTest {
 
        final String validSubscriberId = "123e4567-e89b-12d3-a456-426614174000";

        final String invalidSubscriberId = "123e4567-e89b-12d3-a456-426614174003";

        final String subscriberIdWithoutData = "123e4567-e89b-12d3-a456-426614174001";

        @Test
        void test_getProductionOrders_whenNoProductionOrdersExist_returnsEmptyList() throws Exception {
                // Act
                ResultActions results = mockMvc.perform(get("/{subscriberIdWithoutData}/production-orders", subscriberIdWithoutData)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Assert
                results.andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        void test_getProductionOrders_whenProductionOrdersExist_returnsProductionOrders() throws Exception {
                // Arrange - First create an order and a production order
                String orderRequestBody = """
                                {
                                        "customerDocument": "567.890.123-44",
                                        "customerName": "Michael Davis",
                                        "orderDescription": "Test order x",
                                        "quantity": 1
                                }""";

                ResultActions orderResult = mockMvc.perform(post("/{validSubscriberId}/orders", validSubscriberId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode = (String) TestUtils
                                .convertResponseToMap(orderResult.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                String poRequestBody = """
                                {
                                        "estimatedTime": 10,
                                        "description": "Production order description",
                                        "qtd": 1
                                }""";

                mockMvc.perform(post("/123e4567-e89b-12d3-a456-426614174000/orders/" + orderCode + "/place-production-order")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(poRequestBody)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Act
                ResultActions results = mockMvc.perform(get("/123e4567-e89b-12d3-a456-426614174000/production-orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Assert
                results.andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$").isNotEmpty());
        }

        @Test
        void test_getProductionOrdersByStatus_whenInvalidStatus_returnsBadRequest() throws Exception {
                // Act
                ResultActions results = mockMvc.perform(get("/123e4567-e89b-12d3-a456-426614174000/production-orders?status=INVALID_STATUS")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Assert
                results.andExpect(status().isBadRequest());
        }

        @Test
        void test_getProductionOrdersByStatus_whenValidStatus_returnsFilteredOrders() throws Exception {
                // Arrange - First create an order and a production order
                String orderRequestBody = """
                                {
                                        "customerDocument": "345.678.901-22",
                                        "customerName": "Robert Williams",
                                        "orderDescription": "Test order 1",
                                        "quantity": 1
                                }""";

                ResultActions orderResult = mockMvc.perform(post("/123e4567-e89b-12d3-a456-426614174000/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode = (String) TestUtils
                                .convertResponseToMap(orderResult.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                String poRequestBody = """
                                {
                                        "estimatedTime": 10,
                                        "description": "Production order description",
                                        "qtd": 1
                                }""";

                mockMvc.perform(post("/123e4567-e89b-12d3-a456-426614174000/orders/" + orderCode + "/place-production-order")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(poRequestBody)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));


                // Act
                ResultActions results = mockMvc.perform(get("/123e4567-e89b-12d3-a456-426614174000/production-orders?status=PLANNING")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Assert
                results.andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$").isNotEmpty())
                                .andExpect(jsonPath("$[0].status").value("PLANNING"));
        }

        @Test
        void test_getProductionOrdersByStatus_whenNoOrdersInStatus_returnsEmptyList() throws Exception {
                // Act
                ResultActions results = mockMvc.perform(get("/123e4567-e89b-12d3-a456-426614174000/production-orders?status=DONE")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Assert
                results.andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        void test_getProductionOrderByNumber_success() throws Exception {
                // Arrange - First create an order and a production order
                String orderRequestBody = """
                                {
                                        "customerDocument": "345.678.901-22",
                                        "customerName": "Robert Williams",
                                        "orderDescription": "Test order 1",
                                        "quantity": 1
                                }""";

                ResultActions orderResult = mockMvc.perform(post("/{subscriberId}/orders", validSubscriberId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode = (String) TestUtils
                                .convertResponseToMap(orderResult.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                String poRequestBody = """
                                {
                                        "estimatedTime": 10,
                                        "description": "Production order description",
                                        "qtd": 1
                                }""";

                ResultActions poResult = mockMvc.perform(post("/{validSubscriberId}/orders/{orderCode}/place-production-order", validSubscriberId, orderCode)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(poRequestBody)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String poNumber = (String) TestUtils
                                .convertResponseToMap(poResult.andReturn().getResponse().getContentAsString())
                                .get("poNumber");

                // Act
                ResultActions results = mockMvc.perform(get("/{validSubscriberId}/production-orders/{poNumber}", validSubscriberId, poNumber)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Assert
                results.andExpect(status().isOk())
                                .andExpect(jsonPath("$.poNumber").value(poNumber))
                                .andExpect(jsonPath("$.description").value("Production order description"))
                                .andExpect(jsonPath("$.status").value("PLANNING"))
                                .andExpect(jsonPath("$.estimatedTime").value(10.0))
                                .andExpect(jsonPath("$.openDate").exists())
                                .andExpect(jsonPath("$.quantity").value(1))
                                .andExpect(jsonPath("$.customerName").value("Robert Williams"))
                                .andExpect(jsonPath("$.actualDeliveryDate").doesNotExist());
        }

        @Test
        void test_getProductionOrderByNumber_whenInvalidNumber_returnsNotFound() throws Exception {
                // Act
                ResultActions results = mockMvc.perform(get("/{validSubscriberId}/production-orders/{poNumber}", validSubscriberId, "INVALID-PO-NUMBER")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Assert
                results.andExpect(status().isInternalServerError())
                                .andExpect(jsonPath("$").value("Production Order not found"));
        }

        @Test
        void test_getProductionOrderByNumber_whenInvalidSubscriber_returnsNotFound() throws Exception {
                // Act
                ResultActions results = mockMvc.perform(get("/{invalidSubscriberId}/production-orders/{poNumber}", invalidSubscriberId, "PO-123")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Assert
                results.andExpect(status().isInternalServerError())
                                .andExpect(jsonPath("$").value("Subscriber not found"));
        }

}
