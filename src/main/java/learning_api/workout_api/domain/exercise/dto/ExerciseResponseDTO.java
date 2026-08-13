package learning_api.workout_api.domain.exercise.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Exercise data returned by the API")
public record ExerciseResponseDTO(
        @Schema(description = "Exercise identifier", example = "1")
        Long id,

        @Schema(description = "Exercise name", example = "Squat")
        String name,

        @Schema(description = "Exercise description", example = "Lower body compound movement")
        String description) {
}
