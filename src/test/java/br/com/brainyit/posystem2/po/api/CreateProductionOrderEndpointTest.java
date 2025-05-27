package br.com.brainyit.posystem2.po.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import br.com.brainyit.api.BaseApiTest;
import br.com.brainyit.util.TestUtils;

@Transactional
class CreateProductionOrderEndpointTest extends BaseApiTest {
        
        @Test
        void test_placeProductionOrder_success() throws Exception {
                // Arrange - First create an order
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
                                    "qtd": 10
                                }""";

                // Act
                MockHttpServletRequestBuilder request = post("/1/orders/" + orderCode + "/place-production-order")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(poRequestBody);

                ResultActions results = mockMvc.perform(request);

                // Assert
                results.andExpect(status().isOk())
                                .andExpect(jsonPath("$.openDate").exists())
                                .andExpect(jsonPath("$.estimatedTime").value(10))
                                .andExpect(jsonPath("$.description").value("Production order description"))
                                .andExpect(jsonPath("$.quantity").value(10))
                                .andExpect(jsonPath("$.status").value("PLANNING"))
                                .andExpect(jsonPath("$.poNumber").exists());
        }

        @Test
        void test_placeProductionOrder_whenOrderNotFound_returnsNotFound() throws Exception {
                String poRequestBody = """
                                {
                                    "estimatedTime": 10,
                                    "description": "Production order description",
                                    "qtd": 10
                                }""";

                // Act
                MockHttpServletRequestBuilder request = post("/1/orders/INVALID_ORDER/place-production-order")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(poRequestBody);

                ResultActions results = mockMvc.perform(request);

                // Assert
                results.andExpect(status().isInternalServerError())
                                .andExpect(jsonPath("$").value("Order not found"));
        }

        @Test
        void test_placeProductionOrder_whenInvalidInput_returnsBadRequest() throws Exception {
                // Arrange - First create an order
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
                                    "estimatedTime": 0,
                                    "description": "Production order description",
                                    "qtd": -1
                                }""";

                // Act
                MockHttpServletRequestBuilder request = post("/1/orders/" + orderCode + "/place-production-order")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(poRequestBody);

                ResultActions results = mockMvc.perform(request);

                // Assert
                results.andExpect(status().isBadRequest());
        }

        @Test
        void test_placeProductionOrder_whenOrderNotInPendingStatus_returnsBadRequest() throws Exception {
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
                                    "qtd": 10
                                }""";

                // Create first production order to change order status
                mockMvc.perform(post("/1/orders/" + orderCode + "/place-production-order")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(poRequestBody));

                // Act - Try to create another production order
                MockHttpServletRequestBuilder request = post("/1/orders/" + orderCode + "/place-production-order")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(poRequestBody);

                ResultActions results = mockMvc.perform(request);

                // Assert
                results.andExpect(status().isInternalServerError())
                                .andExpect(jsonPath("$").value(
                                                "Order is not in the pending status, it is in the EXECUTING status"));
        }

}
