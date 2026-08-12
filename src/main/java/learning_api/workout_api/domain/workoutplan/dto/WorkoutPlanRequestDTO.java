package learning_api.workout_api.domain.workoutplan.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Payload to create a workout plan")
public record WorkoutPlanRequestDTO(
        @NotBlank
        @Schema(description = "Workout plan title", example = "Leg Day")
        String title,

        @NotNull
        @Schema(description = "Owner user identifier", example = "1")
        Long userId,

        @ArraySchema(schema = @Schema(description = "Exercise identifiers to attach on creation", example = "1"))
        List<Long> exerciseIds
) {
}
