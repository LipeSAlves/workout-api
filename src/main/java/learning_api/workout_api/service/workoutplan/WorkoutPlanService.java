package learning_api.workout_api.service.workoutplan;

import jakarta.transaction.Transactional;
import learning_api.workout_api.domain.exercise.entity.Exercise;
import learning_api.workout_api.domain.user.entity.FitnessLevel;
import learning_api.workout_api.domain.user.entity.User;
import learning_api.workout_api.domain.workoutplan.dto.*;
import learning_api.workout_api.domain.workoutplan.entity.WorkoutPlan;
import learning_api.workout_api.domain.workoutplan.mapper.WorkoutPlanMapper;
import learning_api.workout_api.repository.exercise.ExerciseRepository;
import learning_api.workout_api.repository.user.UserRepository;
import learning_api.workout_api.repository.workoutplan.WorkoutPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutPlanService {

    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private WorkoutPlanMapper mapper;

    public List<WorkoutPlanResponseDTO> findAll() {
        return workoutPlanRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public WorkoutPlanResponseDTO findById(Long id) {
        WorkoutPlan plan = workoutPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
        return mapper.toDTO(plan);
    }

    @Transactional
    public WorkoutPlanResponseDTO createPlan (WorkoutPlanRequestDTO dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        WorkoutPlan plan = new WorkoutPlan();
        plan.setTitle(dto.title());
        plan.setUser(user);

        if (dto.exerciseIds() != null && !dto.exerciseIds().isEmpty()) {
            List<Exercise> exercises = exerciseRepository.findAllById(dto.exerciseIds());
            plan.setExercises(exercises);
        }

        WorkoutPlan savedPlan = workoutPlanRepository.save(plan);
        return mapper.toDTO(savedPlan);
    }

    // Method needs an existing Workout Plan it can add Exercises to. It A) looks up exercises that match the given Id; B) adds them to an existing workout plan; C) saves the workout plan with the newly added exercises.
    @Transactional
    public WorkoutPlanResponseDTO addExercises(Long planId, List<Long> exerciseIds) {
        WorkoutPlan plan = workoutPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Existing Plan under the given Id not found"));

        List<Exercise> exercisesToAdd = exerciseRepository.findAllById(exerciseIds);

        plan.getExercises().addAll(exercisesToAdd);

        return mapper.toDTO(plan);
    }

    @Transactional
    public WorkoutPlanResponseDTO updatePlan(Long id, WorkoutPlanUpdateDTO dto) {
        WorkoutPlan plan = workoutPlanRepository.findById(id).orElseThrow(() -> new RuntimeException("Workout Plan not found."));

        if (dto.title() != null) {
            plan.setTitle(dto.title());
        }

        if(dto.exerciseIds() != null) {
            List<Exercise> updatedExercises = exerciseRepository.findAllById(dto.exerciseIds());
            plan.getExercises().clear();
            plan.getExercises().addAll(updatedExercises);
        }
        return mapper.toDTO(workoutPlanRepository.save(plan));
    }

    public WorkoutSheetResponseDTO generateSheet(Long planId) {
        WorkoutPlan plan = workoutPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        User user = plan.getUser();
        FitnessLevel level = user.getFitnessLevel();

        // Transforma os exercícios do plano em detalhes com repetições calculadas
        List<ExerciseSheetDTO> exerciseSheets = plan.getExercises().stream()
                .map(exercise -> new ExerciseSheetDTO(
                        exercise.getName(),
                        exercise.getDescription(),
                        calculateReps(level) + " repetitions"
                )).toList();

        return new WorkoutSheetResponseDTO(
                plan.getTitle(),
                user.getName(),
                level,
                exerciseSheets
        );
    }

    @Transactional
    public void deletePlan(Long id) {
        if (!workoutPlanRepository.existsById(id)) {
            throw new RuntimeException("Workout Plan not found with id: " + id);
        }
        workoutPlanRepository.deleteById(id);
    }

    private int calculateReps(FitnessLevel level) {
        return switch (level) {
            case SEDENTARY -> 10;
            case FIT -> 15;
            case ATHLETE -> 20;
        };
    }
}