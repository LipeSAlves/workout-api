package learning_api.workout_api.domain.workoutplan.dto;

import java.util.List;

public record WorkoutPlanUpdateDTO(
        String title,
        List<Long> exerciseIds
) {}
