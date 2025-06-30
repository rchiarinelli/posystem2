package br.com.brainyit.posystem2.po.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import br.com.brainyit.api.BaseApiTest;
import br.com.brainyit.util.TestUtils;

@Transactional
class CreateProductionOrderEndpointTest extends BaseApiTest {

    private static final LocalDate PLANNED_DELIVER_DATE = LocalDate.now().plusYears(1);

    @Test
    void test_placeProductionOrder_success() throws Exception {

        // Arrange - First create an order
        String orderRequestBody = """
                {
                    "customerId": 3,
                    "orderDescription": "Test order 1",
                    "quantity": 10,
                    "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
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
                    "qtd": 10,
                    "plannedDeliverDate": "2026-01-01"
                }""";

        // Act
        MockHttpServletRequestBuilder request = post(
                "/123e4567-e89b-12d3-a456-426614174000/orders/" + orderCode + "/place-production-order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(poRequestBody)
                .with(SecurityMockMvcRequestPostProcessors.jwt());

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
        MockHttpServletRequestBuilder request = post(
                "/123e4567-e89b-12d3-a456-426614174000/orders/INVALID_ORDER/place-production-order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(poRequestBody)
                .with(SecurityMockMvcRequestPostProcessors.jwt());

        ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Order not found"));
    }

    @Test
    void test_placeProductionOrder_whenInvalidInput_returnsBadRequest() throws Exception {
        // Arrange - First create an order
        String orderRequestBody = """
                {
                    "customerId": 3,
                    "orderDescription": "Test order 1",
                    "quantity": 1,
                    "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
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
                    "estimatedTime": 0,
                    "description": "Production order description",
                    "qtd": -1
                }""";

        // Act
        MockHttpServletRequestBuilder request = post(
                "/123e4567-e89b-12d3-a456-426614174000/orders/" + orderCode + "/place-production-order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(poRequestBody)
                .with(SecurityMockMvcRequestPostProcessors.jwt());

        ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isBadRequest());
    }

    @Test
    void test_placeProductionOrder_whenOrderNotInPendingStatus_returnsBadRequest() throws Exception {
        // Arrange - First create an order and a production order
        String orderRequestBody = """
                {
                    "customerId": 3,
                    "orderDescription": "Test order 1",
                    "quantity": 1,
                    "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
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
                    "qtd": 10
                }""";

        // Create first production order to change order status
        mockMvc.perform(post(
                "/123e4567-e89b-12d3-a456-426614174000/orders/" + orderCode + "/place-production-order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(poRequestBody)
                .with(SecurityMockMvcRequestPostProcessors.jwt()));

        // Act - Try to create another production order
        MockHttpServletRequestBuilder request = post(
                "/123e4567-e89b-12d3-a456-426614174000/orders/" + orderCode + "/place-production-order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(poRequestBody)
                .with(SecurityMockMvcRequestPostProcessors.jwt());

        ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(
                        "Order is not in the pending status, it is in the EXECUTING status"));
    }

}
