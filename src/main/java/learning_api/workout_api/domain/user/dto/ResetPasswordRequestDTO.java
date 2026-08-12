package learning_api.workout_api.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload to reset a password using a token from e-mail")
public record ResetPasswordRequestDTO(
        @NotBlank(message = "Reset token is required.")
        @Schema(description = "Password reset token received by e-mail", example = "1c7c34f0-ff2c-40dc-92c5-fdb39c0bd6b7")
        String resetToken,

        @NotBlank(message = "You must input a new password.")
        @Size(min = 6, message = "New password must contain at least 6 characters.")
        @Schema(description = "New password", example = "newpass456", format = "password")
        String newPassword
) {
}
