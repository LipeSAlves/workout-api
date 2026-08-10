package learning_api.workout_api.domain.workoutplan.mapper;

import learning_api.workout_api.domain.exercise.dto.ExerciseResponseDTO;
import learning_api.workout_api.domain.exercise.mapper.ExerciseMapper;
import learning_api.workout_api.domain.workoutplan.dto.WorkoutPlanResponseDTO;
import learning_api.workout_api.domain.workoutplan.entity.WorkoutPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkoutPlanMapper {

    @Autowired
    private ExerciseMapper exerciseMapper;

    public WorkoutPlanResponseDTO toDTO(WorkoutPlan entity) {
        if (entity == null) return null;

        List<ExerciseResponseDTO> exerciseDTOs = entity.getExercises()
                .stream()
                .map(exerciseMapper::toResponseDTO)
                .toList();
        return new WorkoutPlanResponseDTO(
                entity.getId(),
                entity.getTitle(),
                exerciseDTOs
        );
    }
}
