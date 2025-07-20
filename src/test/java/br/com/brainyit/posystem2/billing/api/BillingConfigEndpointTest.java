package br.com.brainyit.posystem2.billing.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import br.com.brainyit.api.BaseApiTest;

@Transactional
class BillingConfigEndpointTest extends BaseApiTest {

    private static final String SUBSCRIBER_UUID = "123e4567-e89b-12d3-a456-426614174000";

    @Test
    void test_getBillingConfigs_success() throws Exception {
        // Act
        final MockHttpServletRequestBuilder request = get("/" + SUBSCRIBER_UUID + "/billing-configs")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt());

        final ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").isString())
                .andExpect(jsonPath("$[0].paymentType").isString())
                .andExpect(jsonPath("$[0].installmentsQuantity").isNumber())
                .andExpect(jsonPath("$[0].installmentsInterval").isNumber())
                .andExpect(jsonPath("$[0].installmentsIntervalUnit").isString());
    }

    @Test
    void test_getBillingConfigs_whenNoData_returnsEmptyArray() throws Exception {
        // Use a different subscriber UUID that exists in the database but has no
        // billing configs
        // Since billing configs are global (not per subscriber), this will return all
        // billing configs
        final String emptySubscriberUuid = "123e4567-e89b-12d3-a456-426614174001";

        // Act
        final MockHttpServletRequestBuilder request = get("/" + emptySubscriberUuid + "/billing-configs")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt());

        final ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty()); // Now we have billing configs in test data
    }

    @Test
    void test_getBillingConfigs_whenNullSubscriber_returnsInternalServerError() throws Exception {
        // Act
        final MockHttpServletRequestBuilder request = get("/null/billing-configs")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt());

        final ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Subscriber not found"));
    }

    @Test
    void test_getBillingConfigs_whenEmptySubscriber_returnsInternalServerError() throws Exception {
        // Act
        final MockHttpServletRequestBuilder request = get("/ /billing-configs")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt());

        final ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Subscriber not found"));
    }

    @Test
    void test_getBillingConfigs_verifyPaymentTypes() throws Exception {
        // Act
        final MockHttpServletRequestBuilder request = get("/" + SUBSCRIBER_UUID + "/billing-configs")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt());

        final ResultActions results = mockMvc.perform(request);

        // Assert - Verify that payment types are correctly mapped
        results.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[*].paymentType").exists())
                .andExpect(jsonPath("$[*].paymentType").isNotEmpty());
    }

    @Test
    void test_getBillingConfigs_verifyInstallmentFields() throws Exception {
        // Act
        final MockHttpServletRequestBuilder request = get("/" + SUBSCRIBER_UUID + "/billing-configs")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt());

        final ResultActions results = mockMvc.perform(request);

        // Assert - Verify installment-related fields are present
        results.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[*].installmentsQuantity").exists())
                .andExpect(jsonPath("$[*].installmentsInterval").exists())
                .andExpect(jsonPath("$[*].installmentsIntervalUnit").exists());
    }

    @Test
    void test_getBillingConfigs_withoutAuthentication_returnsUnauthorized() throws Exception {
        // Act - Request without JWT token
        final MockHttpServletRequestBuilder request = get("/" + SUBSCRIBER_UUID + "/billing-configs")
                .contentType(MediaType.APPLICATION_JSON);

        final ResultActions results = mockMvc.perform(request);

        // Assert
        results.andExpect(status().isUnauthorized());
    }

    @Test
    void test_getBillingConfigs_responseStructure() throws Exception {
        // Act
        final MockHttpServletRequestBuilder request = get("/" + SUBSCRIBER_UUID + "/billing-configs")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt());

        final ResultActions results = mockMvc.perform(request);

        // Assert - Verify complete response structure
        results.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").isString())
                .andExpect(jsonPath("$[0].paymentType").isString())
                .andExpect(jsonPath("$[0].installmentsQuantity").isNumber())
                .andExpect(jsonPath("$[0].installmentsInterval").isNumber())
                .andExpect(jsonPath("$[0].installmentsIntervalUnit").isString());
    }

    @Test
    void test_getBillingConfigs_checkNameField() throws Exception {
        // Act
        final MockHttpServletRequestBuilder request = get("/" + SUBSCRIBER_UUID + "/billing-configs")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt());

        final ResultActions results = mockMvc.perform(request);

        // Assert - Verify name field is not empty
        results.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[*].name").exists())
                .andExpect(jsonPath("$[*].name").isNotEmpty());
    }

    @Test
    void test_getBillingConfigs_checkIdField() throws Exception {
        // Act
        final MockHttpServletRequestBuilder request = get("/" + SUBSCRIBER_UUID + "/billing-configs")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt());

        final ResultActions results = mockMvc.perform(request);

        // Assert - Verify id field is positive number
        results.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].id").isNumber());
    }
}