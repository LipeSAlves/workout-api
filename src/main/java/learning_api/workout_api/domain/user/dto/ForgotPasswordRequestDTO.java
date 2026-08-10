package learning_api.workout_api.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequestDTO(

        @NotBlank(message = "you must input an e-mail")
        @Email(message = "invalid e-mail")
        @Schema(description = "User email", example = "Your@gmail.com")
        String email) {}

