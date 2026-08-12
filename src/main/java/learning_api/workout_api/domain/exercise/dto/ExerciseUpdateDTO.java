package learning_api.workout_api.domain.exercise.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payload to update an exercise. All fields are optional.")
public record ExerciseUpdateDTO(
        @Schema(description = "Exercise name", example = "Barbell squat")
        String name,

        @Schema(description = "Exercise description", example = "Squat with barbell on upper back")
        String description
) {
}
