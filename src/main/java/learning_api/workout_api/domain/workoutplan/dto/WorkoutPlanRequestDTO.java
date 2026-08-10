package learning_api.workout_api.domain.workoutplan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record WorkoutPlanRequestDTO(
        @NotBlank String title,
        @NotNull Long userId,
        List<Long> exerciseIds // optional entry
) {}
