package learning_api.workout_api.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import learning_api.workout_api.domain.user.entity.FitnessLevel;

@Schema(description = "User data returned by the API")
public record UserResponseDTO(
        @Schema(description = "User identifier", example = "1")
        Long id,

        @Schema(description = "User name", example = "Lucas Silva")
        String name,

        @Schema(description = "User email", example = "lucas@email.com")
        String email,

        @Schema(description = "User fitness level")
        FitnessLevel fitnessLevel
) {
}
