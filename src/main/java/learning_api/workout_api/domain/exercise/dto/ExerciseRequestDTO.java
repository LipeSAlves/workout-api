package learning_api.workout_api.domain.exercise.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload to create a new exercise")
public record ExerciseRequestDTO(
        @NotBlank
        @Schema(description = "Exercise name", example = "Squat")
        String name,

        @NotBlank
        @Schema(description = "Exercise description", example = "Lower body compound movement")
        String description) {
}
