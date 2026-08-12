package learning_api.workout_api.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import learning_api.workout_api.OpenAPpiConfig;
import learning_api.workout_api.domain.user.dto.*;
import learning_api.workout_api.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users", description = "User registration, profile updates, password recovery and account deletion.")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Create a new user", description = "Registers a new user. Password is stored with BCrypt. Public endpoint.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or duplicate e-mail")
    })
    @SecurityRequirements
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO dto) {
        UserResponseDTO response = userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Request password reset", description = """
            Sends a password reset e-mail when the address is registered.
            Always returns the same generic message to avoid user enumeration.
            Token validity: 15 minutes.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Generic success message",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid e-mail format")
    })
    @SecurityRequirements
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequestDTO dto) {
        userService.generateNewToken(dto.email());
        return ResponseEntity.ok("If that e-mail is registered, a password reset e-mail has been sent. The reset token is valid for 15 minutes.");
    }

    @Operation(summary = "Reset password", description = "Updates the password when a valid, non-expired reset token is provided. Public endpoint.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password updated",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid or expired token")
    })
    @SecurityRequirements
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequestDTO dto) {
        userService.updatePassword(dto.resetToken(), dto.newPassword());
        return ResponseEntity.ok("Password successfully altered.");
    }

    @Operation(summary = "Update user", description = "Updates user profile fields. Requires JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "User not found or invalid data"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Not authenticated")
    })
    @SecurityRequirement(name = OpenAPpiConfig.BEARER_AUTH)
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "User identifier", example = "1") @PathVariable Long id,
            @RequestBody @Valid UserUpdateDTO dto
    ) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @Operation(summary = "Delete user", description = "Permanently removes a user. Requires JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "400", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Not authenticated")
    })
    @SecurityRequirement(name = OpenAPpiConfig.BEARER_AUTH)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User identifier", example = "1") @PathVariable Long id
    ) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
