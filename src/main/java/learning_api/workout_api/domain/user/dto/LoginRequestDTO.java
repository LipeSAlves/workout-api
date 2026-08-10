package learning_api.workout_api.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

        @NotBlank(message = "E-mail is required.")
        @Email(message = "Invalid e-mail.")
        @Schema(description = "User email", example = "Your@gmail.com")
        String email,

        @NotBlank(message = "Password is required.")
        @Schema(description = "User password", example = "Secretkey")
        String password) {}

