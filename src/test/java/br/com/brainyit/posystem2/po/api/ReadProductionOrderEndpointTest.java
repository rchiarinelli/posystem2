package br.com.brainyit.posystem2.po.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import br.com.brainyit.api.BaseApiTest;
import br.com.brainyit.util.TestUtils;

@Transactional
class ReadProductionOrderEndpointTest extends BaseApiTest {
 
        final Long validSubscriberId = 1L;

        final Long invalidSubscriberId = 999L;

        final Long subscriberIdWithoutData = 2L;

        @Test
        void test_getProductionOrders_whenNoProductionOrdersExist_returnsEmptyList() throws Exception {
                // Act
                ResultActions results = mockMvc.perform(get("/{subscriberIdWithoutData}/production-orders", subscriberIdWithoutData));

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
                                .content(orderRequestBody));

                String orderCode = (String) TestUtils
                                .convertResponseToMap(orderResult.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                String poRequestBody = """
                                {
                                        "estimatedTime": 10,
                                        "description": "Production order description",
                                        "qtd": 1
                                }""";

                mockMvc.perform(post("/1/orders/" + orderCode + "/place-production-order")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(poRequestBody));

                // Act
                ResultActions results = mockMvc.perform(get("/1/production-orders"));

                // Assert
                results.andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$").isNotEmpty());
        }

        @Test
        void test_getProductionOrdersByStatus_whenInvalidStatus_returnsBadRequest() throws Exception {
                // Act
                ResultActions results = mockMvc.perform(get("/1/production-orders?status=INVALID_STATUS"));

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

                ResultActions orderResult = mockMvc.perform(post("/1/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody));

                String orderCode = (String) TestUtils
                                .convertResponseToMap(orderResult.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                String poRequestBody = """
                                {
                                        "estimatedTime": 10,
                                        "description": "Production order description",
                                        "qtd": 1
                                }""";

                mockMvc.perform(post("/1/orders/" + orderCode + "/place-production-order")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(poRequestBody));


                // Act
                ResultActions results = mockMvc.perform(get("/1/production-orders?status=PLANNING"));

                // Assert
                results.andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$").isNotEmpty())
                                .andExpect(jsonPath("$[0].status").value("PLANNING"));
        }

        @Test
        void test_getProductionOrdersByStatus_whenNoOrdersInStatus_returnsEmptyList() throws Exception {
                // Act
                ResultActions results = mockMvc.perform(get("/1/production-orders?status=DONE"));

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
                                .content(orderRequestBody));

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
                                .content(poRequestBody));

                String poNumber = (String) TestUtils
                                .convertResponseToMap(poResult.andReturn().getResponse().getContentAsString())
                                .get("poNumber");

                // Act
                ResultActions results = mockMvc.perform(get("/{validSubscriberId}/production-orders/{poNumber}", validSubscriberId, poNumber));

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
                ResultActions results = mockMvc.perform(get("/{validSubscriberId}/production-orders/{poNumber}", validSubscriberId, "INVALID-PO-NUMBER"));

                // Assert
                results.andExpect(status().isInternalServerError())
                                .andExpect(jsonPath("$").value("Production Order not found"));
        }

        @Test
        void test_getProductionOrderByNumber_whenInvalidSubscriber_returnsNotFound() throws Exception {
                // Act
                ResultActions results = mockMvc.perform(get("/{invalidSubscriberId}/production-orders/{poNumber}", invalidSubscriberId, "PO-123"));

                // Assert
                results.andExpect(status().isInternalServerError())
                                .andExpect(jsonPath("$").value("Subscriber not found"));
        }

}
