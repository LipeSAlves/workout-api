package learning_api.workout_api.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JWT authentication response")
public record LoginResponseDTO(

        @Schema(description = "User access token to the system", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0dXJtYXJhcm8yQGdtYWlsLmNvbSIsImlhdCI6MTc1MjgxMjM1NSwiZXhwIjoxNzUyODk4NzU1fQ.QCTCcFcd5Dd8FxvnkKDYopqw2AY64sEukRvS7WISZDg")
        String accessToken) {}
