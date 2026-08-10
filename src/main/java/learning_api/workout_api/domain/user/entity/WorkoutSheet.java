package learning_api.workout_api.domain.user.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class WorkoutSheet {

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}
