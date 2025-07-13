package br.com.brainyit.posystem2.po.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import br.com.brainyit.api.BaseApiTest;
import br.com.brainyit.util.TestUtils;

@Transactional
class ReadProductionOrderEndpointTest extends BaseApiTest {

        private static final LocalDate PLANNED_DELIVER_DATE = LocalDate.now().plusYears(1);

        final String validSubscriberId = "123e4567-e89b-12d3-a456-426614174000";

        final String invalidSubscriberId = "123e4567-e89b-12d3-a456-426614174003";

        final String subscriberIdWithoutData = "123e4567-e89b-12d3-a456-426614174001";

        @Test
        void test_getProductionOrders_whenNoProductionOrdersExist_returnsEmptyList() throws Exception {
                // Act
                ResultActions results = mockMvc
                                .perform(get("/{subscriberIdWithoutData}/production-orders", subscriberIdWithoutData)
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
                                        "customerId": 5,
                                        "orderDescription": "Test order x",
                                        "quantity": 1,
                                        "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
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
                                        "instructions": "Production order description",
                                        "qtd": 1,
                                        "plannedStartDate": "2026-01-01",
                                        "priority": "STANDARD"
                                }""";

                mockMvc.perform(post(
                                "/123e4567-e89b-12d3-a456-426614174000/orders/" + orderCode + "/place-production-order")
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
                ResultActions results = mockMvc.perform(
                                get("/123e4567-e89b-12d3-a456-426614174000/production-orders?status=INVALID_STATUS")
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
                                        "instructions": "Production order description",
                                        "qtd": 1,
                                        "plannedStartDate": "2026-01-01",
                                        "priority": "STANDARD"
                                }""";

                mockMvc.perform(post(
                                "/123e4567-e89b-12d3-a456-426614174000/orders/" + orderCode + "/place-production-order")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(poRequestBody)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Act
                ResultActions results = mockMvc
                                .perform(get("/123e4567-e89b-12d3-a456-426614174000/production-orders?status=PLANNING")
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
                ResultActions results = mockMvc
                                .perform(get("/123e4567-e89b-12d3-a456-426614174000/production-orders?status=DONE")
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
                                        "customerId": 3,
                                        "orderDescription": "Test order 1",
                                        "quantity": 1,
                                        "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
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
                                        "instructions": "Production order description",
                                        "qtd": 1,
                                        "plannedStartDate": "2026-01-01",
                                        "priority": "STANDARD"
                                }""";

                ResultActions poResult = mockMvc
                                .perform(post("/{validSubscriberId}/orders/{orderCode}/place-production-order",
                                                validSubscriberId, orderCode)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(poRequestBody)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String poNumber = (String) TestUtils
                                .convertResponseToMap(poResult.andReturn().getResponse().getContentAsString())
                                .get("poNumber");

                // Act
                ResultActions results = mockMvc.perform(
                                get("/{validSubscriberId}/production-orders/{poNumber}", validSubscriberId, poNumber)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Assert
                results.andExpect(status().isOk())
                                .andExpect(jsonPath("$.poNumber").value(poNumber))
                                .andExpect(jsonPath("$.instructions").value("Production order description"))
                                .andExpect(jsonPath("$.status").value("PLANNING"))
                                .andExpect(jsonPath("$.estimatedTime").value(10.0))
                                .andExpect(jsonPath("$.openDate").exists())
                                .andExpect(jsonPath("$.quantity").value(1))
                                .andExpect(jsonPath("$.customerName").value("Robert Williams"))
                                .andExpect(jsonPath("$.actualDeliveryDate").doesNotExist())
                                .andExpect(jsonPath("$.order.orderCode").value(orderCode));
        }

        @Test
        void test_getProductionOrderByNumber_whenInvalidNumber_returnsNotFound() throws Exception {
                // Act
                ResultActions results = mockMvc.perform(get("/{validSubscriberId}/production-orders/{poNumber}",
                                validSubscriberId, "INVALID-PO-NUMBER")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Assert
                results.andExpect(status().isInternalServerError())
                                .andExpect(jsonPath("$.message").value("Production Order not found"));
        }

        @Test
        void test_getProductionOrderByNumber_whenInvalidSubscriber_returnsNotFound() throws Exception {
                // Act
                ResultActions results = mockMvc.perform(get("/{invalidSubscriberId}/production-orders/{poNumber}",
                                invalidSubscriberId, "PO-123")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Assert
                results.andExpect(status().isInternalServerError())
                                .andExpect(jsonPath("$.message").value("Subscriber not found"));
        }

        @Test
        void test_getProductionOrders_whenMultipleProductionOrdersExist_returnsAllOrders() throws Exception {
                // Arrange - Create 3 production orders
                String orderRequestBody1 = """
                                {
                                        "customerId": 6,
                                        "orderDescription": "Test order for ACME Corporation",
                                        "quantity": 2,
                                        "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
                                }""";

                String orderRequestBody2 = """
                                {
                                        "customerId": 4,
                                        "orderDescription": "Test order for Patricia Brown",
                                        "quantity": 1,
                                        "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
                                }""";

                String orderRequestBody3 = """
                                {
                                        "customerId": 6,
                                        "orderDescription": "Another test order for ACME Corporation",
                                        "quantity": 3,
                                        "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
                                }""";

                // Create first order and production order
                ResultActions orderResult1 = mockMvc.perform(post("/{validSubscriberId}/orders", validSubscriberId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody1)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode1 = (String) TestUtils
                                .convertResponseToMap(orderResult1.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                String poRequestBody1 = """
                                {
                                        "estimatedTime": 15,
                                        "instructions": "First production order",
                                        "qtd": 2,
                                        "plannedStartDate": "2026-01-01",
                                        "priority": "STANDARD"
                                }""";

                ResultActions poResult1 = mockMvc.perform(post(
                                "/{validSubscriberId}/orders/{orderCode}/place-production-order", validSubscriberId,
                                orderCode1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(poRequestBody1)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String poNumber1 = (String) TestUtils
                                .convertResponseToMap(poResult1.andReturn().getResponse().getContentAsString())
                                .get("poNumber");

                // Create second order and production order
                ResultActions orderResult2 = mockMvc.perform(post("/{validSubscriberId}/orders", validSubscriberId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody2)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode2 = (String) TestUtils
                                .convertResponseToMap(orderResult2.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                String poRequestBody2 = """
                                {
                                        "estimatedTime": 8,
                                        "instructions": "Second production order",
                                        "qtd": 1,
                                        "plannedStartDate": "2026-01-02",
                                        "priority": "HIGH"
                                }""";

                ResultActions poResult2 = mockMvc.perform(post(
                                "/{validSubscriberId}/orders/{orderCode}/place-production-order", validSubscriberId,
                                orderCode2)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(poRequestBody2)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String poNumber2 = (String) TestUtils
                                .convertResponseToMap(poResult2.andReturn().getResponse().getContentAsString())
                                .get("poNumber");

                // Create third order and production order
                ResultActions orderResult3 = mockMvc.perform(post("/{validSubscriberId}/orders", validSubscriberId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody3)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode3 = (String) TestUtils
                                .convertResponseToMap(orderResult3.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                String poRequestBody3 = """
                                {
                                        "estimatedTime": 20,
                                        "instructions": "Third production order",
                                        "qtd": 3,
                                        "plannedStartDate": "2026-01-03",
                                        "priority": "LOW"
                                }""";

                mockMvc.perform(post(
                                "/{validSubscriberId}/orders/{orderCode}/place-production-order", validSubscriberId,
                                orderCode3)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(poRequestBody3)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Act - Search for the second production order by poNumber
                ResultActions searchResult = mockMvc.perform(
                                get("/{validSubscriberId}/production-orders?poNumber={poNumber}", validSubscriberId,
                                                poNumber2)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Assert - Should return 1 result
                searchResult.andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(1)))
                                .andExpect(jsonPath("$[0].poNumber").value(poNumber2))
                                .andExpect(jsonPath("$[0].instructions").value("Second production order"))
                                .andExpect(jsonPath("$[0].status").value("PLANNING"))
                                .andExpect(jsonPath("$[0].estimatedTime").value(8.0))
                                .andExpect(jsonPath("$[0].quantity").value(1))
                                .andExpect(jsonPath("$[0].customerName").value("Patricia Brown"));

                // Act - Search without any parameters (should return all 3)
                ResultActions allResults = mockMvc.perform(
                                get("/{validSubscriberId}/production-orders", validSubscriberId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Assert - Should return 3 results
                allResults.andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(3)))
                                .andExpect(jsonPath("$[0].poNumber").exists())
                                .andExpect(jsonPath("$[1].poNumber").exists())
                                .andExpect(jsonPath("$[2].poNumber").exists());
        }

        @Test
        void test_getProductionOrders_whenSearchingByCustomerName_returnsFilteredResults() throws Exception {
                // Arrange - Create orders for ACME Corporation (customer id 6)
                String orderRequestBody1 = """
                                {
                                        "customerId": 6,
                                        "orderDescription": "ACME order 1",
                                        "quantity": 1,
                                        "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
                                }""";

                ResultActions orderResult1 = mockMvc.perform(post("/{validSubscriberId}/orders", validSubscriberId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody1)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode1 = (String) TestUtils
                                .convertResponseToMap(orderResult1.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                String orderRequestBody2 = """
                                {
                                        "customerId": 6,
                                        "orderDescription": "ACME order 2",
                                        "quantity": 1,
                                        "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
                                }""";

                ResultActions orderResult2 = mockMvc.perform(post("/{validSubscriberId}/orders", validSubscriberId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody2)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode2 = (String) TestUtils
                                .convertResponseToMap(orderResult2.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                // Create production orders for ACME Corporation
                String poRequestBody1 = """
                                {
                                        "estimatedTime": 10,
                                        "instructions": "ACME production order 1",
                                        "qtd": 1,
                                        "plannedStartDate": "2026-01-01",
                                        "priority": "STANDARD"
                                }""";

                mockMvc.perform(post(
                                "/{validSubscriberId}/orders/{orderCode}/place-production-order", validSubscriberId,
                                orderCode1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(poRequestBody1)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String poRequestBody2 = """
                                {
                                        "estimatedTime": 15,
                                        "instructions": "ACME production order 2",
                                        "qtd": 2,
                                        "plannedStartDate": "2026-01-02",
                                        "priority": "HIGH"
                                }""";

                mockMvc.perform(post(
                                "/{validSubscriberId}/orders/{orderCode}/place-production-order", validSubscriberId,
                                orderCode2)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(poRequestBody2)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Arrange - Create orders for Patricia Brown (customer id 4)
                String orderRequestBody3 = """
                                {
                                        "customerId": 4,
                                        "orderDescription": "Patricia order 1",
                                        "quantity": 1,
                                        "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
                                }""";

                ResultActions orderResult3 = mockMvc.perform(post("/{validSubscriberId}/orders", validSubscriberId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody3)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode3 = (String) TestUtils
                                .convertResponseToMap(orderResult3.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                String orderRequestBody4 = """
                                {
                                        "customerId": 4,
                                        "orderDescription": "Patricia order 2",
                                        "quantity": 1,
                                        "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
                                }""";

                ResultActions orderResult4 = mockMvc.perform(post("/{validSubscriberId}/orders", validSubscriberId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody4)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode4 = (String) TestUtils
                                .convertResponseToMap(orderResult4.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                String orderRequestBody5 = """
                                {
                                        "customerId": 4,
                                        "orderDescription": "Patricia order 3",
                                        "quantity": 1,
                                        "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
                                }""";

                ResultActions orderResult5 = mockMvc.perform(post("/{validSubscriberId}/orders", validSubscriberId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody5)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode5 = (String) TestUtils
                                .convertResponseToMap(orderResult5.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                String orderRequestBody6 = """
                                {
                                        "customerId": 4,
                                        "orderDescription": "Patricia order 4",
                                        "quantity": 1,
                                        "plannedDeliverDate": """ + "\"" + PLANNED_DELIVER_DATE.toString() + "\"" + """
                                }""";

                ResultActions orderResult6 = mockMvc.perform(post("/{validSubscriberId}/orders", validSubscriberId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestBody6)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String orderCode6 = (String) TestUtils
                                .convertResponseToMap(orderResult6.andReturn().getResponse().getContentAsString())
                                .get("orderCode");

                // Create production orders for Patricia Brown
                String poRequestBody3 = """
                                {
                                        "estimatedTime": 5,
                                        "instructions": "Patricia production order 1",
                                        "qtd": 1,
                                        "plannedStartDate": "2026-01-03",
                                        "priority": "LOW"
                                }""";

                mockMvc.perform(post(
                                "/{validSubscriberId}/orders/{orderCode}/place-production-order", validSubscriberId,
                                orderCode3)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(poRequestBody3)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String poRequestBody4 = """
                                {
                                        "estimatedTime": 8,
                                        "instructions": "Patricia production order 2",
                                        "qtd": 1,
                                        "plannedStartDate": "2026-01-04",
                                        "priority": "STANDARD"
                                }""";

                mockMvc.perform(post(
                                "/{validSubscriberId}/orders/{orderCode}/place-production-order", validSubscriberId,
                                orderCode4)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(poRequestBody4)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String poRequestBody5 = """
                                {
                                        "estimatedTime": 12,
                                        "instructions": "Patricia production order 3",
                                        "qtd": 1,
                                        "plannedStartDate": "2026-01-05",
                                        "priority": "HIGH"
                                }""";

                mockMvc.perform(post(
                                "/{validSubscriberId}/orders/{orderCode}/place-production-order", validSubscriberId,
                                orderCode5)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(poRequestBody5)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                String poRequestBody6 = """
                                {
                                        "estimatedTime": 6,
                                        "instructions": "Patricia production order 4",
                                        "qtd": 1,
                                        "plannedStartDate": "2026-01-06",
                                        "priority": "LOW"
                                }""";

                mockMvc.perform(post(
                                "/{validSubscriberId}/orders/{orderCode}/place-production-order", validSubscriberId,
                                orderCode6)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(poRequestBody6)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Act - Search for customer name containing "Corp" (should return ACME
                // Corporation orders)
                ResultActions searchResult = mockMvc.perform(
                                get("/{validSubscriberId}/production-orders?customerName=Corp", validSubscriberId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                // Assert - Should return 2 results for ACME Corporation
                searchResult.andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(2)))
                                .andExpect(jsonPath("$[0].poNumber").exists())
                                .andExpect(jsonPath("$[1].poNumber").exists());
        }
}
