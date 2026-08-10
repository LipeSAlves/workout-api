package learning_api.workout_api.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import learning_api.workout_api.domain.user.entity.FitnessLevel;

public record UserRequestDTO(
        @NotBlank(message = "You must assign a name to a user.")
        @Schema(description = "User name", example = "Lucas Silva")
        String name,

        @NotBlank
        @Schema(description = "User email", example = "Your@gmail.com")
        @Email(message = "invalid e-mail")
        String email,

        @NotBlank
        @Size(min = 6, message = "The password must contain at least 6 characters")
        @Schema(description = "User password", example = "Secretkey")
        String password,


        @Schema(description = "User's self declared fitness level", example = "Sedentary")
        @NotNull
        FitnessLevel fitnessLevel
) {}