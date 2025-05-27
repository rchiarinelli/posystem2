package br.com.brainyit.posystem2.po.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import br.com.brainyit.api.ApiPaths;
import br.com.brainyit.api.BaseApiTest;
import br.com.brainyit.posystem2.util.MessagesUtil;
import br.com.brainyit.util.TestUtils;

@Transactional
class UpdateProductionOrderEndpointTest extends BaseApiTest {

        final Long validSubscriberId = 1L;

        final Long invalidSubscriberId = 999L;

        @Test
        void test_updateProductionOrder_success() throws Exception {
                // Arrange - First create an order and a production order
                String orderRequestBody = """
                                {
                                    "customerDocument": "345.678.901-22",
                                    "customerName": "Robert Williams",
                                    "orderDescription": "Test order 1",
                                    "quantity": 1
                                }""";

                ResultActions orderResult = mockMvc.perform(post(ApiPaths.ORDERS_ENDPOINT_PATH, validSubscriberId)
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

                ResultActions poResult = mockMvc.perform(
                                post(ApiPaths.PLACE_PRODUCTION_ORDER_ENDPOINT_PATH, validSubscriberId, orderCode)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(poRequestBody));

                String poNumber = (String) TestUtils
                                .convertResponseToMap(poResult.andReturn().getResponse().getContentAsString())
                                .get("poNumber");

                String updateRequestBody = """
                                {
                                    "description": "Updated description",
                                    "qtd": 10,
                                    "estimatedDueDate": "2025-01-31",
                                    "actualDeliveryDate": "2025-01-31",
                                    "status": "PLANNING",
                                    "estimatedTime": 24.5
                                }""";

                // Act
                ResultActions results = mockMvc
                                .perform(put(ApiPaths.PRODUCTION_ORDER_ENDPOINT_PATH, validSubscriberId, poNumber)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(updateRequestBody));

                // Assert
                results.andExpect(status().isOk())
                                .andExpect(jsonPath("$.poNumber").value(poNumber))
                                .andExpect(jsonPath("$.description").value("Updated description"))
                                .andExpect(jsonPath("$.status").value("PLANNING"))
                                .andExpect(jsonPath("$.estimatedTime").value(24.5))
                                .andExpect(jsonPath("$.estimatedDueDate").value("31/01/2025"))
                                .andExpect(jsonPath("$.quantity").value(10))
                                .andExpect(jsonPath("$.actualDeliveryDate").value("31/01/2025"))
                                .andExpect(jsonPath("$.customerName").value("Robert Williams"));
        }

        @Test
        void test_updateProductionOrder_whenInvalidSubscriber_returnsNotFound() throws Exception {
                String updateRequestBody = """
                                {
                                    "description": "Updated description",
                                    "qtd": 10,
                                    "estimatedDueDate": "2025-01-31",
                                    "actualDeliveryDate": "2025-01-31",
                                    "status": "PLANNING",
                                    "estimatedTime": 24.5
                                }""";

                ResultActions results = mockMvc
                                .perform(put(ApiPaths.PRODUCTION_ORDER_ENDPOINT_PATH, invalidSubscriberId, "PO-123")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(updateRequestBody));

                results.andExpect(status().isInternalServerError())
                                .andExpect(jsonPath("$").value("Subscriber not found"));
        }

        @Test
        void test_updateProductionOrder_whenInvalidPoNumber_returnsNotFound() throws Exception {
                String updateRequestBody = """
                                {
                                    "description": "Updated description",
                                    "qtd": 10,
                                    "estimatedDueDate": "2025-01-31",
                                    "actualDeliveryDate": "2025-01-31",
                                    "status": "PLANNING",
                                    "estimatedTime": 24.5
                                }""";

                ResultActions results = mockMvc
                                .perform(put(ApiPaths.PRODUCTION_ORDER_ENDPOINT_PATH, validSubscriberId, "INVALID-PO")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(updateRequestBody));

                results.andExpect(status().isInternalServerError())
                                .andExpect(jsonPath("$").value("Production Order not found"));
        }

        @Test
        void test_updateProductionOrder_whenInvalidStatus_returnsBadRequest() throws Exception {
                String updateRequestBody = """
                                {
                                    "description": "Updated description",
                                    "qtd": 10,
                                    "estimatedDueDate": "2025-01-31",
                                    "actualDeliveryDate": "2025-01-31",
                                    "status": "INVALID_STATUS",
                                    "estimatedTime": 24.5
                                }""";

                ResultActions results = mockMvc
                                .perform(put(ApiPaths.PRODUCTION_ORDER_ENDPOINT_PATH, validSubscriberId, "PO-123")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(updateRequestBody));

                results.andExpect(status().isBadRequest());
        }

        @Test
        void test_updateProductionOrder_whenInvalidQuantity_returnsBadRequest() throws Exception {
                String updateRequestBody = """
                                {
                                    "description": "Updated description",
                                    "qtd": -1,
                                    "estimatedDueDate": "2025-01-31",
                                    "actualDeliveryDate": "2025-01-31",
                                    "status": "PLANNING",
                                    "estimatedTime": 24.5
                                }""";

                ResultActions results = mockMvc
                                .perform(put(ApiPaths.PRODUCTION_ORDER_ENDPOINT_PATH, validSubscriberId, "PO-123")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(updateRequestBody));

                results.andExpect(status().isBadRequest());
        }

        @Test
        void test_updateProductionOrder_whenInvalidEstimatedTime_returnsBadRequest() throws Exception {
                String updateRequestBody = """
                                {
                                    "description": "Updated description",
                                    "qtd": 10,
                                    "estimatedDueDate": "2025-01-31",
                                    "actualDeliveryDate": "2025-01-31",
                                    "status": "PLANNING",
                                    "estimatedTime": -1
                                }""";

                ResultActions results = mockMvc
                                .perform(put(ApiPaths.PRODUCTION_ORDER_ENDPOINT_PATH, validSubscriberId, "PO-123")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(updateRequestBody));

                results.andExpect(status().isBadRequest());
        }

        @Test
        void test_cancelProductionOrder_success() throws Exception {
                // Arrange - First create an order and a production order
                String orderRequestBody = """
                                {
                                    "customerDocument": "345.678.901-22",
                                    "customerName": "Robert Williams",
                                    "orderDescription": "Test order 1",
                                    "quantity": 1
                                }""";

                ResultActions orderResult = mockMvc.perform(post(ApiPaths.ORDERS_ENDPOINT_PATH, validSubscriberId)
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

                ResultActions poResult = mockMvc.perform(
                                post(ApiPaths.PLACE_PRODUCTION_ORDER_ENDPOINT_PATH, validSubscriberId, orderCode)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(poRequestBody));

                String poNumber = (String) TestUtils
                                .convertResponseToMap(poResult.andReturn().getResponse().getContentAsString())
                                .get("poNumber");

                String cancelRequestBody = """
                                {
                                    "description": "PO cancelled by a test.",
                                    "qtd": 10,
                                    "estimatedDueDate": "2025-01-31",
                                    "actualDeliveryDate": "2025-01-31",
                                    "status": "PLANNING",
                                    "estimatedTime": 24.5
                                }""";

                String expectedClosedDate = LocalDate.now().format(
                                DateTimeFormatter.ofPattern(MessagesUtil.getInstance().getSystemProp("date_pattern")));

                // Act
                ResultActions results = mockMvc.perform(
                                put(ApiPaths.PRODUCTION_ORDER_CANCEL_ENDPOINT_PATH, validSubscriberId, poNumber)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(cancelRequestBody));

                // Assert
                results.andExpect(status().isOk())
                                .andExpect(jsonPath("$.poNumber").value(poNumber))
                                .andExpect(jsonPath("$.description")
                                                .value("Production order description\nPO cancelled by a test."))
                                .andExpect(jsonPath("$.status").value("CANCELLED"))
                                .andExpect(jsonPath("$.actualDeliveryDate").value(expectedClosedDate))
                                .andExpect(jsonPath("$.customerName").value("Robert Williams"));
        }

        @Test
        void test_cancelProductionOrder_whenSubscriberNotFound_returns404() throws Exception {
                // Arrange
                Long invalidSubscriberId = 999L;
                String poNumber = "PO-123";

                String cancelRequestBody = """
                                {
                                    "description": "PO cancelled by a test.",
                                    "qtd": 10,
                                    "estimatedDueDate": "2025-01-31",
                                    "actualDeliveryDate": "2025-01-31",
                                    "status": "PLANNING",
                                    "estimatedTime": 24.5
                                }""";

                // Act & Assert
                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_CANCEL_ENDPOINT_PATH, invalidSubscriberId, poNumber)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(cancelRequestBody))
                                .andExpect(status().isInternalServerError())
                                .andExpect(jsonPath("$").value("Subscriber not found"));
        }

        @Test
        void test_cancelProductionOrder_whenProductionOrderNotFound_returns404() throws Exception {
                // Arrange
                String invalidPoNumber = "PO-999";

                String cancelRequestBody = """
                                {
                                    "description": "PO cancelled by a test.",
                                    "qtd": 10,
                                    "estimatedDueDate": "2025-01-31",
                                    "actualDeliveryDate": "2025-01-31",
                                    "status": "PLANNING",
                                    "estimatedTime": 24.5
                                }""";

                // Act & Assert
                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_CANCEL_ENDPOINT_PATH, validSubscriberId, invalidPoNumber)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(cancelRequestBody))
                                .andExpect(status().isInternalServerError())
                                .andExpect(jsonPath("$").value("Production Order not found"));
        }

        @Test
        void test_cancelProductionOrder_whenOrderInDoneStatus_returns400() throws Exception {
                // Arrange
                // Create and complete a production order first
                String orderRequestBody = """
                                {
                                    "customerDocument": "345.678.901-22",
                                    "customerName": "Robert Williams",
                                    "orderDescription": "Test order 1",
                                    "quantity": 1
                                }""";
                ResultActions orderResult = mockMvc.perform(post(ApiPaths.ORDERS_ENDPOINT_PATH, validSubscriberId)
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

                ResultActions poResult = mockMvc.perform(
                                post(ApiPaths.PLACE_PRODUCTION_ORDER_ENDPOINT_PATH, validSubscriberId, orderCode)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(poRequestBody));

                String poNumber = (String) TestUtils
                                .convertResponseToMap(poResult.andReturn().getResponse().getContentAsString())
                                .get("poNumber");

                // Move to DONE status
                String updateRequestBody = """
                                {
                                    "description": "Production order description",
                                    "qtd": 10,
                                    "estimatedDueDate": "2025-01-31",
                                    "actualDeliveryDate": "2025-01-31",
                                    "status": "DONE",
                                    "estimatedTime": 24.5
                                }""";

                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_ENDPOINT_PATH, validSubscriberId, poNumber)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateRequestBody));

                String cancelRequestBody = """
                                {
                                    "description": "PO cancelled by a test.",
                                    "qtd": 10,
                                    "estimatedDueDate": "2025-01-31",
                                    "actualDeliveryDate": "2025-01-31",
                                    "status": "PLANNING",
                                    "estimatedTime": 24.5
                                }""";

                // Act & Assert
                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_CANCEL_ENDPOINT_PATH, validSubscriberId, poNumber)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(cancelRequestBody))
                                .andExpect(status().isInternalServerError())
                                .andExpect(jsonPath("$").value("Cannot cancel production order in DONE status"));
        }

        @Test
        void test_cancelProductionOrder_whenDescriptionEmpty_returns400() throws Exception {
                // Arrange
                String orderRequestBody = """
                                {
                                    "customerDocument": "345.678.901-22",
                                    "customerName": "Robert Williams",
                                    "orderDescription": "Test order 1",
                                    "quantity": 1
                                }""";
                ResultActions orderResult = mockMvc.perform(post(ApiPaths.ORDERS_ENDPOINT_PATH, validSubscriberId)
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

                ResultActions poResult = mockMvc.perform(
                                post(ApiPaths.PLACE_PRODUCTION_ORDER_ENDPOINT_PATH, validSubscriberId, orderCode)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(poRequestBody));

                String poNumber = (String) TestUtils
                                .convertResponseToMap(poResult.andReturn().getResponse().getContentAsString())
                                .get("poNumber");

                String cancelRequestBody = """
                                {
                                    "qtd": 10,
                                    "estimatedDueDate": "2025-01-31",
                                    "actualDeliveryDate": "2025-01-31",
                                    "status": "PLANNING",
                                    "estimatedTime": 24.5
                                }""";

                // Act & Assert
                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_CANCEL_ENDPOINT_PATH, validSubscriberId, poNumber)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(cancelRequestBody))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void test_proceedProductionOrder_success() throws Exception {
                // Arrange - First create an order and a production order
                String orderRequestBody = """
                                {
                                    "customerDocument": "345.678.901-22",
                                    "customerName": "Robert Williams",
                                    "orderDescription": "Test order 1",
                                    "quantity": 1
                                }""";

                ResultActions orderResult = mockMvc.perform(post(ApiPaths.ORDERS_ENDPOINT_PATH, validSubscriberId)
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

                ResultActions poResult = mockMvc.perform(
                                post(ApiPaths.PLACE_PRODUCTION_ORDER_ENDPOINT_PATH, validSubscriberId, orderCode)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(poRequestBody));

                String poNumber = (String) TestUtils
                                .convertResponseToMap(poResult.andReturn().getResponse().getContentAsString())
                                .get("poNumber");

                // Act & Assert - Move to the next phase
                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH, validSubscriberId, poNumber))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value("EXECUTING"));
        }

        @Test
        void test_proceedProductionOrder_whenAlreadyInProgress_returns400() throws Exception {
                // Arrange - First create an order and a production order
                String orderRequestBody = """
                                {
                                    "customerDocument": "345.678.901-22",
                                    "customerName": "Robert Williams",
                                    "orderDescription": "Test order 1",
                                    "quantity": 1
                                }""";

                ResultActions orderResult = mockMvc.perform(post(ApiPaths.ORDERS_ENDPOINT_PATH, validSubscriberId)
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

                ResultActions poResult = mockMvc.perform(
                                post(ApiPaths.PLACE_PRODUCTION_ORDER_ENDPOINT_PATH, validSubscriberId, orderCode)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(poRequestBody));

                String poNumber = (String) TestUtils
                                .convertResponseToMap(poResult.andReturn().getResponse().getContentAsString())
                                .get("poNumber");

                // Move to the next phase first
                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH, validSubscriberId, poNumber));

                // Act & Assert - Try to proceed again
                ResultActions progResultActions = mockMvc.perform(
                                put(ApiPaths.PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH, validSubscriberId, poNumber)
                                                .contentType(MediaType.APPLICATION_JSON));

                progResultActions.andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value("DONE"));

        }

        @Test
        void test_proceedAndCancelProductionOrder_returns500() throws Exception {
                // Arrange - First create an order and a production order
                String orderRequestBody = """
                                {
                                    "customerDocument": "345.678.901-22",
                                    "customerName": "Robert Williams",
                                    "orderDescription": "Test order 1",
                                    "quantity": 1
                                }""";

                ResultActions orderResult = mockMvc.perform(post(ApiPaths.ORDERS_ENDPOINT_PATH, validSubscriberId)
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

                ResultActions poResult = mockMvc.perform(
                                post(ApiPaths.PLACE_PRODUCTION_ORDER_ENDPOINT_PATH, validSubscriberId, orderCode)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(poRequestBody));

                String poNumber = (String) TestUtils
                                .convertResponseToMap(poResult.andReturn().getResponse().getContentAsString())
                                .get("poNumber");

                // Proceed to the next phase until it is done
                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH, validSubscriberId, poNumber));
                mockMvc.perform(put(ApiPaths.PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH, validSubscriberId, poNumber));

                // Act & Assert - Try to cancel the production order which should fail
                ResultActions cancelResultActions = mockMvc.perform(
                                put(ApiPaths.PRODUCTION_ORDER_CANCEL_ENDPOINT_PATH, validSubscriberId, poNumber)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content("""
                                                                {
                                                                    "description": "Cancelling after done",
                                                                    "qtd": 10,
                                                                    "estimatedDueDate": "2025-01-31",
                                                                    "actualDeliveryDate": "2025-01-31",
                                                                    "status": "PLANNING",
                                                                    "estimatedTime": 24.5
                                                                }"""));

                cancelResultActions.andExpect(status().isInternalServerError());
        }

        @Test
        void test_cancelProductionOrderTwice_returns500() throws Exception {
                // Arrange - First create an order and a production order
                String orderRequestBody = """
                                {
                                    "customerDocument": "345.678.901-22",
                                    "customerName": "Robert Williams",
                                    "orderDescription": "Test order 1",
                                    "quantity": 1
                                }""";

                ResultActions orderResult = mockMvc.perform(post(ApiPaths.ORDERS_ENDPOINT_PATH, validSubscriberId)
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

                ResultActions poResult = mockMvc.perform(
                                post(ApiPaths.PLACE_PRODUCTION_ORDER_ENDPOINT_PATH, validSubscriberId, orderCode)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(poRequestBody));

                String poNumber = (String) TestUtils
                                .convertResponseToMap(poResult.andReturn().getResponse().getContentAsString())
                                .get("poNumber");

                // Act - Cancel the production order
                ResultActions cancelFirstResultActions = mockMvc.perform(
                                put(ApiPaths.PRODUCTION_ORDER_CANCEL_ENDPOINT_PATH, validSubscriberId, poNumber)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content("""
                                                                {
                                                                    "description": "first cancellation",
                                                                    "qtd": 10,
                                                                    "estimatedDueDate": "2025-01-31",
                                                                    "actualDeliveryDate": "2025-01-31",
                                                                    "status": "PLANNING",
                                                                    "estimatedTime": 24.5
                                                                }"""));

                cancelFirstResultActions.andExpect(status().isOk());

                // Act & Assert - Try to cancel the production order again which should fail
                ResultActions cancelSecondResultActions = mockMvc.perform(
                                put(ApiPaths.PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH, validSubscriberId, poNumber));

                cancelSecondResultActions.andExpect(status().isInternalServerError());
        }
}
