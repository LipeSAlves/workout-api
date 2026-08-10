package learning_api.workout_api.domain.user.dto;

import learning_api.workout_api.domain.user.entity.FitnessLevel;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        FitnessLevel fitnessLevel
) {}
