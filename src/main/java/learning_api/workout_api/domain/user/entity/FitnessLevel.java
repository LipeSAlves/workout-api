package learning_api.workout_api.domain.user.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User fitness level used to calculate workout repetitions", example = "FIT")
public enum FitnessLevel {
    @Schema(description = "Sedentary — 10 repetitions per exercise")
    SEDENTARY,
    @Schema(description = "Fit — 15 repetitions per exercise")
    FIT,
    @Schema(description = "Athlete — 20 repetitions per exercise")
    ATHLETE,
}
