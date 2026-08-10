package learning_api.workout_api.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import learning_api.workout_api.domain.user.dto.*;
import learning_api.workout_api.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users", description = "Manage http requests that call for creating, updating and deleting users, as well as operations meant to safely disclose critical user information.")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Create a new user", description = "Saves a new user to the database")
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO dto) {
        UserResponseDTO response = userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Generate new password change token", description = "Uses provided request data to check for a user with a matching e-mail in the database, in which case a password change token is either created or overwritten with a new one. The token is valid for 15 minutes, after which it expires and is no longer serviceable. The operation returns a generic statement regardless of a match being found in the database in order to prevent disclosure of user information")
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequestDTO dto) {
        userService.generateNewToken(dto.email());

        return ResponseEntity.ok("If that e-mail is registered, a new token has been generated and can be used to change your password. Remember: your token lasts 15 minutes.");
    }

    @Operation(summary = "Change a password", description = "Checks if a matching change password token is found in the database and if its 15-minute expiration limit hasn't been breached, in which case the user's password field is overwritten with the new password ")
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequestDTO dto) {
        userService.updatePassword(dto.resetToken(), dto.newPassword());
        return ResponseEntity.ok("Password successfully altered.");
    }

    @Operation(summary = "Update a user", description = "Overwrites user information saved in the database with the new entries provided in the request data")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @Operation(summary = "Delete a user", description = "Uses provided request data to delete a user saved in the database")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); //
    }

}
