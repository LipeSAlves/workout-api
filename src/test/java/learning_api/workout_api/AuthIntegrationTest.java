package learning_api.workout_api;

import learning_api.workout_api.domain.user.entity.FitnessLevel;
import learning_api.workout_api.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthIntegrationTest extends IntegrationTestSupport {

    @Test
    void shouldLoginWithValidCredentials() throws Exception {
        String email = uniqueEmail();
        String password = "secret123";
        registerUser("Lucas", email, password, FitnessLevel.FIT);

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", email,
                                "password", password
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    void shouldRejectInvalidCredentials() throws Exception {
        String email = uniqueEmail();
        registerUser("Lucas", email, "secret123", FitnessLevel.FIT);

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", email,
                                "password", "wrong-password"
                        ))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid e-mail or password."));
    }

    @Test
    void shouldRejectProtectedEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/exercises"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowAccessWithValidToken() throws Exception {
        String email = uniqueEmail();
        String token = registerAndLogin(email, "secret123", FitnessLevel.FIT);

        mockMvc.perform(get("/exercises")
                        .header("Authorization", bearerToken(token)))
                .andExpect(status().isOk());
    }
}
