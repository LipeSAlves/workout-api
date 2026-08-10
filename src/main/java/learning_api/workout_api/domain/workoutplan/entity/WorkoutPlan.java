package learning_api.workout_api.domain.workoutplan.entity;

import jakarta.persistence.*;
import learning_api.workout_api.domain.exercise.entity.Exercise;
import learning_api.workout_api.domain.user.entity.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "workout_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; //e.g. "leg day"

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "workout_plan_exercises",
            joinColumns = @JoinColumn(name = "workout_plan_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id")
    )
    private List<Exercise> exercises = new ArrayList<>();
}
