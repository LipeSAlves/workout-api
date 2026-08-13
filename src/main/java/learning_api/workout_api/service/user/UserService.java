package learning_api.workout_api.service.user;

import learning_api.workout_api.domain.user.dto.UserRequestDTO;
import learning_api.workout_api.domain.user.dto.UserResponseDTO;
import learning_api.workout_api.domain.user.dto.UserUpdateDTO;
import learning_api.workout_api.domain.user.entity.User;
import learning_api.workout_api.domain.user.mapper.UserMapper;
import learning_api.workout_api.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private static final long RESET_TOKEN_VALIDITY_MINUTES = 15;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Transactional
    public UserResponseDTO registerUser(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("That e-mail is already assigned to an existing user.");
        }

        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));

        User savedUser = userRepository.save(user);
        return userMapper.toResponseDTO(savedUser);
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

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
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setResetPasswordToken(UUID.randomUUID().toString());
            user.setTokenCreatedAt(LocalDateTime.now());
            userRepository.save(user);

            try {
                emailService.sendPasswordResetEmail(email, user.getResetPasswordToken());
            } catch (Exception ex) {
                log.error("Failed to send password reset e-mail to {}", email, ex);
            }
        });
    }

    public boolean isTokenExpired(User user) {
        if (user.getResetPasswordToken() == null || user.getTokenCreatedAt() == null) {
            return true;
        }
        long minutesBetween = Duration.between(user.getTokenCreatedAt(), LocalDateTime.now()).toMinutes();
        return minutesBetween >= RESET_TOKEN_VALIDITY_MINUTES;
    }

    @Transactional
    public void updatePassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (isTokenExpired(user)) {
            throw new RuntimeException("Token expired, 15 minutes have passed since it has been created.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setTokenCreatedAt(null);
        userRepository.save(user);
    }
}
