package learning_api.workout_api.repository.workoutplan;

import learning_api.workout_api.domain.workoutplan.entity.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {

    List<WorkoutPlan> findByUserId(Long userId);
}
