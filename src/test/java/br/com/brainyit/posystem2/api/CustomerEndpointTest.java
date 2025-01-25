package br.com.brainyit.posystem2.api;

import br.com.brainyit.api.BaseApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.google.gson.Gson;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

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
        String responseContent = createResult.andReturn().getResponse().getContentAsString();
        Gson gson = new Gson();
        
        Map<String, Object> responseMap = gson.fromJson(responseContent, Map.class);
        
        String customerId = (String) responseMap.get("id");

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
    


}
