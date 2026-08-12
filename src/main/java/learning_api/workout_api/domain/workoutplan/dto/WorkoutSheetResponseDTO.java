package learning_api.workout_api.domain.workoutplan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import learning_api.workout_api.domain.user.entity.FitnessLevel;

import java.util.List;

@Schema(description = "Personalized workout sheet generated from a plan")
public record WorkoutSheetResponseDTO(
        @Schema(description = "Workout plan title", example = "Leg Day")
        String planTitle,

        @Schema(description = "Plan owner name", example = "Lucas Silva")
        String userName,

        @Schema(description = "Owner fitness level")
        FitnessLevel userLevel,

        @Schema(description = "Exercises with calculated repetitions")
        List<ExerciseSheetDTO> exercises
) {
}
