package learning_api.workout_api.domain.workoutplan.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Payload to update a workout plan. Providing exerciseIds replaces the current exercise list.")
public record WorkoutPlanUpdateDTO(
        @Schema(description = "Workout plan title", example = "Leg Day — updated")
        String title,

        @ArraySchema(schema = @Schema(description = "Exercise identifiers", example = "1"))
        List<Long> exerciseIds
) {
}
