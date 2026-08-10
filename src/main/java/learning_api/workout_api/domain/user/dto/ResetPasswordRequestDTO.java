package learning_api.workout_api.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequestDTO(

        @Schema(description = "User password reset token", example = "1c7c34f0-ff2c-40dc-92c5-fdb39c0bd6b7")
        String resetToken,

        @Schema(description = "New user password", example = "NewSecretPassword")
        @NotBlank(message = "You must input a new password.")
        @Size(min = 6, message = "New password must contain at least 6 characters.")
        String newPassword) {}

