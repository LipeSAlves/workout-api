package learning_api.workout_api.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequestDTO(

        @Schema(description = "User email", example = "Your@gmail.com")
        String email,

        @Schema(description = "User password", example = "Secretkey")
        String password) {}

