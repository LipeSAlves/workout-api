package learning_api.workout_api.repository.exercise;

import learning_api.workout_api.domain.exercise.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
}
