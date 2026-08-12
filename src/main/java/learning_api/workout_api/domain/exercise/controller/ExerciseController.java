package learning_api.workout_api.domain.exercise.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import learning_api.workout_api.OpenAPpiConfig;
import learning_api.workout_api.domain.exercise.dto.ExerciseRequestDTO;
import learning_api.workout_api.domain.exercise.dto.ExerciseResponseDTO;
import learning_api.workout_api.domain.exercise.dto.ExerciseUpdateDTO;
import learning_api.workout_api.service.exercise.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Exercises", description = "CRUD operations for the exercise catalog. All endpoints require JWT.")
@RestController
@RequestMapping("/exercises")
@RequiredArgsConstructor
@SecurityRequirement(name = OpenAPpiConfig.BEARER_AUTH)
public class ExerciseController {

    private final ExerciseService exerciseService;

    @Operation(summary = "List all exercises", description = "Returns every exercise stored in the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exercise list",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExerciseResponseDTO.class)))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Not authenticated")
    })
    @GetMapping
    public ResponseEntity<List<ExerciseResponseDTO>> getAllExercises() {
        return ResponseEntity.ok(exerciseService.findAll());
    }

    @Operation(summary = "Get exercise by id", description = "Returns a single exercise by its identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exercise found",
                    content = @Content(schema = @Schema(implementation = ExerciseResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Exercise not found"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Not authenticated")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponseDTO> getById(
            @Parameter(description = "Exercise identifier", example = "1") @PathVariable Long id
    ) {
        return ResponseEntity.ok(exerciseService.findById(id));
    }

    @Operation(summary = "Create exercise", description = "Creates a new exercise in the catalog.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Exercise created",
                    content = @Content(schema = @Schema(implementation = ExerciseResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Not authenticated")
    })
    @PostMapping
    public ResponseEntity<ExerciseResponseDTO> createNewExercise(@RequestBody @Valid ExerciseRequestDTO dto) {
        ExerciseResponseDTO saved = exerciseService.saveExercise(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Update exercise", description = "Updates name and/or description of an existing exercise.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exercise updated",
                    content = @Content(schema = @Schema(implementation = ExerciseResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Exercise not found or invalid data"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Not authenticated")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExerciseResponseDTO> update(
            @Parameter(description = "Exercise identifier", example = "1") @PathVariable Long id,
            @RequestBody @Valid ExerciseUpdateDTO dto
    ) {
        return ResponseEntity.ok(exerciseService.updateExercise(id, dto));
    }

    @Operation(summary = "Delete exercise", description = "Removes an exercise and unlinks it from associated workout plans.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Exercise deleted"),
            @ApiResponse(responseCode = "400", description = "Exercise not found"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Not authenticated")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Exercise identifier", example = "1") @PathVariable Long id
    ) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }
}
