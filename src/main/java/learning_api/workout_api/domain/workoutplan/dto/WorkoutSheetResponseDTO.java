package learning_api.workout_api.domain.workoutplan.dto;

import learning_api.workout_api.domain.user.entity.FitnessLevel;

import java.util.List;

public record WorkoutSheetResponseDTO (
        String planTitle,
        String userName,
        FitnessLevel userLevel,
        List<ExerciseSheetDTO> exercises
) {}
