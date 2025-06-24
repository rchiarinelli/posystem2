package br.com.brainyit.posystem2.user.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import br.com.brainyit.api.BaseApiTest;

public class AboutMeApiTest extends BaseApiTest {

    private static final String JWT_TOKEN = "token";

    private final static String USER_NAME = "janesmith@brainyit.com.br";

    private final static String SUBSCRIBER_UUID = "123e4567-e89b-12d3-a456-426614174000";

    @Test
    void test_aboutMeGet_authenticated_baseInfoReturned() throws Exception {
        mockMvc.perform(get("/about-me")
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(Jwt.withTokenValue(JWT_TOKEN).header("Authorization", "Bearer ")
                                .claim("preferred_username", USER_NAME).build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("janesmith@brainyit.com.br"))
                .andExpect(jsonPath("$.subscriber.uuid").value(SUBSCRIBER_UUID))
                .andExpect(jsonPath("$.subscriber.name").value("BrainyIT"))
                .andExpect(jsonPath("$.subscriber.email").value("johnsmith@brainyit.com.br"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty());
    }

    @Test
    void test_aboutMeGet_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/about-me"))
                .andExpect(status().isUnauthorized());
    }
}
