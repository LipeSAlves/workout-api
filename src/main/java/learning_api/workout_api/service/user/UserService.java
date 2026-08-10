package learning_api.workout_api.service.user;

import learning_api.workout_api.domain.user.dto.UserRequestDTO;
import learning_api.workout_api.domain.user.dto.UserResponseDTO;
import learning_api.workout_api.domain.user.dto.UserUpdateDTO;
import learning_api.workout_api.domain.user.entity.User;
import learning_api.workout_api.domain.user.mapper.UserMapper;
import learning_api.workout_api.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public UserResponseDTO registerUser(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("That e-mail is already assigned to an existing user.");
        }

        User user = userMapper.toEntity(dto);

        user.setPassword(dto.password());

        User savedUser = userRepository.save(user);
        return userMapper.toResponseDTO(savedUser);
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Atualiza apenas o que foi enviado
        if (dto.name() != null) user.setName(dto.name());
        if (dto.email() != null) user.setEmail(dto.email());
        if (dto.fitnessLevel() != null) user.setFitnessLevel(dto.fitnessLevel());

        return userMapper.toResponseDTO(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void generateNewToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));

        user.setResetPasswordToken(UUID.randomUUID().toString());
        user.setTokenCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        // For ease of use, we'll print the token to the terminal in place of having it be sent to an e-mail (intended future feature) for now.
        System.out.println("\n--- NEW TOKEN GENERATED ---");
        System.out.println("E-mail: " + email);
        System.out.println("Token: " + user.getResetPasswordToken());
        System.out.println("Expires within: 15 minutes");
        System.out.println("-------------------------\n");
    }

    public boolean isTokenExpired(User user) {
        if (user.getResetPasswordToken() == null || user.getTokenCreatedAt() == null) {
            return true;
        }
        LocalDateTime now = LocalDateTime.now();
        long secondsBetween = Duration.between(user.getTokenCreatedAt(), now).toSeconds();
        return secondsBetween > 60;
    }

    @Transactional
    public void updatePassword(String token, String newPassword) {
        System.out.println("Looking up user with token: [" + token + "]");
        User user = userRepository.findByResetPasswordToken(token).orElseThrow(() -> {
            System.out.println("Error: token did not match any user.");
            return new RuntimeException("Invalid token");
        });

        if (isTokenExpired(user)) {
            throw new RuntimeException("Token expired, 15 minutes have passed since it has been created.");
        }
        user.setPassword(newPassword);
        user.setResetPasswordToken(null);
        userRepository.save(user);

    }
}
