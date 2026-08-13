package learning_api.workout_api.domain.workoutplan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import learning_api.workout_api.domain.exercise.dto.ExerciseResponseDTO;

import java.util.List;

@Schema(description = "Workout plan data returned by the API")
public record WorkoutPlanResponseDTO(
        @Schema(description = "Workout plan identifier", example = "1")
        Long id,

        @Schema(description = "Workout plan title", example = "Leg Day")
        String title,

        @Schema(description = "Exercises linked to the plan")
        List<ExerciseResponseDTO> exercises
) {
}
