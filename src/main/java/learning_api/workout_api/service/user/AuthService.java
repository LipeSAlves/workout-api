package learning_api.workout_api.service.user;

import learning_api.workout_api.domain.user.dto.LoginRequestDTO;
import learning_api.workout_api.domain.user.dto.LoginResponseDTO;
import learning_api.workout_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );

        String token = jwtService.generateToken(dto.email());
        return new LoginResponseDTO(token);
    }
}
