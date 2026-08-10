package learning_api.workout_api.domain.exercise.dto;

public record ExerciseResponseDTO (
        Long id,
        String name,
        String description) {
}
