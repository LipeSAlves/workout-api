package learning_api.workout_api.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credentials for authentication")
public record LoginRequestDTO(

        @NotBlank(message = "E-mail is required.")
        @Email(message = "Invalid e-mail.")
        @Schema(description = "User email", example = "lucas@email.com")
        String email,

        @NotBlank(message = "Password is required.")
        @Schema(description = "User password", example = "secret123", format = "password")
        String password) {}

