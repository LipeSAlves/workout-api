package learning_api.workout_api.domain.exercise.dto;

import jakarta.validation.constraints.NotBlank;

public record ExerciseRequestDTO(
        @NotBlank String name,
        @NotBlank String description) {
}
