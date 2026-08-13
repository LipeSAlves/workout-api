package learning_api.workout_api.service.exercise;

import learning_api.workout_api.domain.exercise.dto.ExerciseRequestDTO;
import learning_api.workout_api.domain.exercise.dto.ExerciseResponseDTO;
import learning_api.workout_api.domain.exercise.dto.ExerciseUpdateDTO;
import learning_api.workout_api.domain.exercise.entity.Exercise;
import learning_api.workout_api.domain.exercise.mapper.ExerciseMapper;
import learning_api.workout_api.domain.workoutplan.entity.WorkoutPlan;
import learning_api.workout_api.repository.exercise.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;

    public ExerciseResponseDTO saveExercise(ExerciseRequestDTO dto) {
        Exercise exercise = exerciseMapper.toEntity(dto);
        Exercise saved = exerciseRepository.save(exercise);
        return exerciseMapper.toResponseDTO(saved);
    }

    public List<ExerciseResponseDTO> findAll() {
        return exerciseRepository.findAll().stream()
                .map(exerciseMapper::toResponseDTO)
                .toList();
    }

    public ExerciseResponseDTO findById(Long id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));
        return exerciseMapper.toResponseDTO(exercise);
    }

    @Transactional
    public ExerciseResponseDTO updateExercise(Long id, ExerciseUpdateDTO dto) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));

        if (dto.name() != null) exercise.setName(dto.name());
        if (dto.description() != null) exercise.setDescription(dto.description());
        return exerciseMapper.toResponseDTO(exerciseRepository.save(exercise));
    }

    @Transactional
    public void deleteExercise(Long id) {
        Exercise exercise = exerciseRepository.findById(id).orElseThrow(() -> new RuntimeException("Exercise not found"));

        for (WorkoutPlan plan : exercise.getWorkoutPlans()) {
            plan.getExercises().remove(exercise);
        }
        exerciseRepository.delete(exercise);
    }
}
