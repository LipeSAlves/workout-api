package learning_api.workout_api.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import learning_api.workout_api.domain.user.entity.FitnessLevel;

@Schema(description = "Payload to update a user. All fields are optional.")
public record UserUpdateDTO(
        @Schema(description = "User name", example = "Lucas Silva")
        String name,

        @Schema(description = "User email", example = "lucas@email.com")
        String email,

        @Schema(description = "User fitness level", example = "ATHLETE")
        FitnessLevel fitnessLevel
) {
}
