package learning_api.workout_api.domain.workoutplan.dto;

import learning_api.workout_api.domain.exercise.dto.ExerciseResponseDTO;

import java.util.List;

public record WorkoutPlanResponseDTO (Long id, String title, List<ExerciseResponseDTO> exercises) { }
