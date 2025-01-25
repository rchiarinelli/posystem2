package br.com.brainyit.posystem2.api;

import br.com.brainyit.api.BaseApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    


}
