package br.com.brainyit.posystem2.billing.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import br.com.brainyit.api.ApiPaths;
import br.com.brainyit.util.TestUtils;

@Transactional
public class CancelBillingEndpointTest extends BaseBillingTest {

        @Test
        void test_cancelBilling_billingNotInOpenState_internalServerErrorIsReturned() throws Exception {
                // Given billing is paid
                final Pair<String, String> billingData = prepareOrderData(SUBSCRIBER_3, 17, true,
                                DEFAULT_BILLING_REQUEST_BODY);
                final String billingUuid = billingData.getRight();

                ResultActions installmentsResult = mockMvc
                                .perform(get(ApiPaths.BILLING_INSTALLMENTS_ENDPOINT_PATH, SUBSCRIBER_3,
                                                billingUuid)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                final String installmentUuid = TestUtils
                                .convertResponseToList(installmentsResult.andReturn().getResponse()
                                                .getContentAsString())
                                .get(0)
                                .get("uuid").toString();

                final ResultActions results = mockMvc
                                .perform(put(ApiPaths.BILLING_INSTALLMENT_ENDPOINT_PATH, SUBSCRIBER_3,
                                                billingUuid, installmentUuid)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(String.format(DEFAULT_UPDATE_INSTALLMENT_REQUEST_BODY,
                                                                100.0D))
                                                .with(SecurityMockMvcRequestPostProcessors.jwt()));

                results.andExpect(status().isOk())
                                .andExpect(jsonPath("$.paidValue").value(100.0))
                                .andExpect(jsonPath("$.status").value("PAID"));

                // When try to cancel billing
                // Then internal server error is returned
                mockMvc.perform(post(ApiPaths.BILLING_CANCEL_ENDPOINT_PATH, SUBSCRIBER_3, billingUuid)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()))
                                .andExpect(status().isInternalServerError());
        }

        @Test
        void test_cancelBilling_billingInOpenState_billingIsCancelledAndOrderIsInPendingBillingState()
                        throws Exception {
                // Given billing is not paid
                final Pair<String, String> billingData = prepareOrderData(SUBSCRIBER_3, 17, true,
                                DEFAULT_BILLING_REQUEST_BODY);
                final String billingUuid = billingData.getRight();
                // When try to cancel billing
                // Then billing is cancelled and order is in pending billing state
                mockMvc.perform(post(ApiPaths.BILLING_CANCEL_ENDPOINT_PATH, SUBSCRIBER_3, billingUuid)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value("CANCELLED"))
                                .andExpect(jsonPath("$.order.status").value("PENDING_BILLING"))
                                .andExpect(jsonPath("$.installments[0].status").value("CANCELLED"));
        }

}
