package learning_api.workout_api.domain.workoutplan.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Exercise entry on a generated workout sheet")
public record ExerciseSheetDTO(
        @Schema(description = "Exercise name", example = "Squat")
        String exerciseName,

        @Schema(description = "Exercise description", example = "Lower body compound movement")
        String description,

        @Schema(description = "Recommended repetitions based on fitness level", example = "15 repetitions")
        String repetitions
) {
}
