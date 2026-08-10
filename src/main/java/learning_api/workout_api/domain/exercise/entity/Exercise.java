package learning_api.workout_api.domain.exercise.entity;


import jakarta.persistence.*;
import learning_api.workout_api.domain.workoutplan.entity.WorkoutPlan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exercises")
@EqualsAndHashCode(of = "id")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 150)
    private String name;
    @Column(length = 1000)
    private String description;

    @ManyToMany(mappedBy = "exercises")
    private List<WorkoutPlan> workoutPlans = new ArrayList<>();
}

