package learning_api.workout_api.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateUserRequestDTO(

        @Schema(description = "User name", example = "Lucas Silva")
        String name,

        @Schema(description = "User's self-declared fitness level", example = "Athlete")
        String fitnessLevel,

        @Schema(description = "User old password", example = "Oldsecretkey")
        String oldPassword,

        @Schema(description = "User new password", example = "Newsecretkey")
        String newPassword
) {}