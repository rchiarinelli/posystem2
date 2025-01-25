package br.com.brainyit.posystem2.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.google.gson.Gson;

import br.com.brainyit.api.BaseApiTest;
import br.com.brainyit.util.TestUtils;

class CustomerEndpointTest extends BaseApiTest {

    @Test
    void test_getAllEmployees_employeesRetrievedAndResponsePopulated() throws Exception {
        final MockHttpServletRequestBuilder request = get("/1/customers");
        final ResultActions results = mockMvc.perform(request);
        results.andExpect(status().isOk());
    }

    @Test
    void test_getIndividual_retrieveCustomerById_customerRetrievedAndResponsePopulated() throws Exception {
        final MockHttpServletRequestBuilder request = get("/1/customers/5");
        final ResultActions results = mockMvc.perform(request);
        results.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("Michael Davis"))
                .andExpect(jsonPath("$.details").value("Premium customer"))
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }

    @Test
    void test_createIndividual_whenValidInput_createsAndReturnsCustomer() throws Exception {
        // Arrange
        String requestBody = """
                {
                    "name": "John Doe",
                    "details": "New customer",
                    "status": "ACTIVE",
                    "cpf": "123.456.789-00",
                    "rg": "12.345.678-9"
                }""";

        // Act
        final MockHttpServletRequestBuilder request = post("/1/customers/individuals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        final ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.details").value("New customer"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void test_updateIndividual_whenValidInput_updatesAndReturnsCustomer() throws Exception {

        final String subscriber = "1";

        // Arrange - First create an individual
        String createRequestBody = """
                {
                    "name": "John Doe",
                    "details": "New customer",
                    "status": "ACTIVE", 
                    "cpf": "123.456.789-00",
                    "rg": "12.345.678-9"
                }""";

        MockHttpServletRequestBuilder createRequest = post("/{subscriber}/customers/individuals", subscriber)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestBody);

        ResultActions createResult = mockMvc.perform(createRequest);
        createResult.andExpect(status().isOk());

        // Get the ID from the response for update
        final String customerId = TestUtils.convertResponseToMap(createResult.andReturn().getResponse().getContentAsString()).get("id").toString();

        // Update request
        String updateRequestBody = """
                {
                    "name": "John Smith",
                    "details": "Updated customer details",
                    "status": "INACTIVE",
                    "cpf": "987.654.321-00",
                    "rg": "98.765.432-1",
                    "id": """ + customerId + """
                }""";

        // Act
        MockHttpServletRequestBuilder updateRequest = put("/{subscriber}/customers/individuals/{customerId}", new Object[]{subscriber, customerId})
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequestBody);

        ResultActions updateResult = mockMvc.perform(updateRequest);

        // Assert
        updateResult.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.details").value("Updated customer details"))
                .andExpect(jsonPath("$.status").value("INACTIVE"))
                .andExpect(jsonPath("$.cpf").value("987.654.321-00"))
                .andExpect(jsonPath("$.rg").value("98.765.432-1"));
    }
    
// POST /{subscriber}/customers/companies

    @Test
    void test_createCompany_whenValidInput_returnsCreatedCompany() throws Exception {
        // Arrange
        String subscriber = "1";
        String createRequestBody = """
                {
                    "name": "ACME Corporation",
                    "details": "Large company that makes everything",
                    "status": "ACTIVE", 
                    "cnpj": "12.345.678/0001-90",
                    "ie": "123456789",
                    "im": "987654321"
                }""";

        MockHttpServletRequestBuilder createRequest = post("/{subscriber}/customers/companies", subscriber)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestBody);

        // Act & Assert
        mockMvc.perform(createRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("ACME Corporation"))
                .andExpect(jsonPath("$.details").value("Large company that makes everything"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.cnpj").value("12.345.678/0001-90"))
                .andExpect(jsonPath("$.ie").value("123456789"))
                .andExpect(jsonPath("$.im").value("987654321"));
    }


// PUT /{subscriber}/customers/organizations/{customerId}

    @Test
    void test_updateCompany_whenValidInput_returnsUpdatedCompany() throws Exception {
        // Arrange
        String subscriber = "1";
        
        // First create a company
        String createRequestBody = """
                {
                    "name": "ACME Corporation",
                    "details": "Large company",
                    "status": "ACTIVE",
                    "cnpj": "12.345.678/0001-90", 
                    "ie": "123456789",
                    "im": "987654321"
                }""";

        MockHttpServletRequestBuilder createRequest = post("/{subscriber}/customers/companies", subscriber)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestBody);

        // Get the ID from create response
        ResultActions createResult = mockMvc.perform(createRequest);
        createResult.andExpect(status().isOk());

        // Get the ID from the response for update
        final String customerId = TestUtils.convertResponseToMap(createResult.andReturn().getResponse().getContentAsString()).get("id").toString();

        // Update the company
        String updateRequestBody = """
                {
                    "id": "%s",
                    "name": "ACME Corporation Updated",
                    "details": "Updated company details",
                    "status": "INACTIVE",
                    "cnpj": "12.345.678/0001-90",
                    "ie": "987654321",
                    "im": "123456789"
                }""".formatted(customerId);

        MockHttpServletRequestBuilder updateRequest = put("/{subscriber}/customers/companies/{customerId}", subscriber, customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequestBody);

        // Act & Assert
        mockMvc.perform(updateRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.name").value("ACME Corporation Updated"))
                .andExpect(jsonPath("$.details").value("Updated company details"))
                .andExpect(jsonPath("$.status").value("INACTIVE"))
                .andExpect(jsonPath("$.cnpj").value("12.345.678/0001-90"))
                .andExpect(jsonPath("$.ie").value("987654321"))
                .andExpect(jsonPath("$.im").value("123456789"));
    }


}
