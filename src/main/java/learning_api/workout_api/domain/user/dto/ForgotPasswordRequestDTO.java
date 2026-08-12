package learning_api.workout_api.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload to request a password reset e-mail")
public record ForgotPasswordRequestDTO(
        @NotBlank(message = "you must input an e-mail")
        @Email(message = "invalid e-mail")
        @Schema(description = "Registered user email", example = "lucas@email.com")
        String email
) {
}
