package learning_api.workout_api.domain.user.mapper;

import learning_api.workout_api.domain.user.dto.UserRequestDTO;
import learning_api.workout_api.domain.user.dto.UserResponseDTO;
import org.springframework.stereotype.Component;
import learning_api.workout_api.domain.user.entity.User;

@Component
public class UserMapper {
    public User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setFitnessLevel(dto.fitnessLevel());
        return user;
    }

    public UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getFitnessLevel()
        );
    }
}
