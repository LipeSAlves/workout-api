package learning_api.workout_api;

import learning_api.workout_api.domain.user.entity.FitnessLevel;
import learning_api.workout_api.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserIntegrationTest extends IntegrationTestSupport {

    private static final String GENERIC_FORGOT_PASSWORD_MESSAGE =
            "If that e-mail is registered, a password reset e-mail has been sent. The reset token is valid for 15 minutes.";

    @Test
    void shouldRegisterUser() throws Exception {
        String email = uniqueEmail();

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", "Lucas Silva",
                                "email", email,
                                "password", "secret123",
                                "fitnessLevel", "FIT"
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Lucas Silva"))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.fitnessLevel").value("FIT"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void shouldStorePasswordHashed() throws Exception {
        String email = uniqueEmail();
        String plainPassword = "secret123";

        registerUser("Lucas", email, plainPassword, FitnessLevel.FIT);

        var user = userRepository.findByEmail(email).orElseThrow();
        assertThat(user.getPassword()).isNotEqualTo(plainPassword);
        assertThat(new BCryptPasswordEncoder().matches(plainPassword, user.getPassword())).isTrue();
    }

    @Test
    void forgotPassword_shouldReturnGenericMessageForUnknownEmail() throws Exception {
        mockMvc.perform(post("/users/forgot-password")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "unknown@test.com"
                        ))))
                .andExpect(status().isOk())
                .andExpect(content().string(GENERIC_FORGOT_PASSWORD_MESSAGE));

        verify(emailService, never()).sendPasswordResetEmail(anyString(), anyString());
    }

    @Test
    void forgotPassword_shouldReturnGenericMessageForKnownEmail() throws Exception {
        String email = uniqueEmail();
        registerUser("Lucas", email, "secret123", FitnessLevel.FIT);

        mockMvc.perform(post("/users/forgot-password")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", email
                        ))))
                .andExpect(status().isOk())
                .andExpect(content().string(GENERIC_FORGOT_PASSWORD_MESSAGE));
    }

    @Test
    void forgotPassword_shouldGenerateTokenForKnownEmail() throws Exception {
        String email = uniqueEmail();
        registerUser("Lucas", email, "secret123", FitnessLevel.FIT);

        mockMvc.perform(post("/users/forgot-password")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", email
                        ))))
                .andExpect(status().isOk());

        var user = userRepository.findByEmail(email).orElseThrow();
        assertThat(user.getResetPasswordToken()).isNotBlank();
        assertThat(user.getTokenCreatedAt()).isNotNull();

        verify(emailService).sendPasswordResetEmail(eq(email), eq(user.getResetPasswordToken()));
    }

    @Test
    void resetPassword_shouldAllowLoginWithNewPassword() throws Exception {
        String email = uniqueEmail();
        String oldPassword = "secret123";
        String newPassword = "newpass456";

        registerUser("Lucas", email, oldPassword, FitnessLevel.FIT);

        mockMvc.perform(post("/users/forgot-password")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Map.of("email", email))))
                .andExpect(status().isOk());

        String resetToken = userRepository.findByEmail(email).orElseThrow().getResetPasswordToken();

        mockMvc.perform(post("/users/reset-password")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Map.of(
                                "resetToken", resetToken,
                                "newPassword", newPassword
                        ))))
                .andExpect(status().isOk())
                .andExpect(content().string("Password successfully altered."));

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", email,
                                "password", oldPassword
                        ))))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", email,
                                "password", newPassword
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    void resetPassword_shouldRejectInvalidToken() throws Exception {
        mockMvc.perform(post("/users/reset-password")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Map.of(
                                "resetToken", "invalid-token",
                                "newPassword", "newpass456"
                        ))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid token"));
    }
}
