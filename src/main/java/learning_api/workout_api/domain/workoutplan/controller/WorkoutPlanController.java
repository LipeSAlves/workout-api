package learning_api.workout_api.domain.workoutplan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import learning_api.workout_api.OpenAPpiConfig;
import learning_api.workout_api.domain.workoutplan.dto.WorkoutPlanRequestDTO;
import learning_api.workout_api.domain.workoutplan.dto.WorkoutPlanResponseDTO;
import learning_api.workout_api.domain.workoutplan.dto.WorkoutPlanUpdateDTO;
import learning_api.workout_api.domain.workoutplan.dto.WorkoutSheetResponseDTO;
import learning_api.workout_api.service.workoutplan.WorkoutPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Workout Plans", description = "Workout plan management and personalized sheet generation. All endpoints require JWT.")
@RestController
@RequestMapping("/workout-plans")
@SecurityRequirement(name = OpenAPpiConfig.BEARER_AUTH)
public class WorkoutPlanController {

    @Autowired
    private WorkoutPlanService service;

    @Operation(summary = "Create workout plan", description = "Creates a plan for a user. Optionally attaches exercises on creation.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Plan created",
                    content = @Content(schema = @Schema(implementation = WorkoutPlanResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "User not found or invalid data"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Not authenticated")
    })
    @PostMapping
    public ResponseEntity<WorkoutPlanResponseDTO> createPlan(@org.springframework.web.bind.annotation.RequestBody @Valid WorkoutPlanRequestDTO dto) {
        WorkoutPlanResponseDTO response = service.createPlan(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Add exercises to plan", description = "Appends exercises to an existing workout plan.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exercises added",
                    content = @Content(schema = @Schema(implementation = WorkoutPlanResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Plan not found"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Not authenticated")
    })
    @PostMapping("/{planId}/exercises")
    public ResponseEntity<WorkoutPlanResponseDTO> addExercisesToPlan(
            @Parameter(description = "Workout plan identifier", example = "1") @PathVariable Long planId,
            @RequestBody(description = "List of exercise identifiers to add", required = true,
                    content = @Content(array = @ArraySchema(schema = @Schema(type = "integer", example = "1"))))
            @org.springframework.web.bind.annotation.RequestBody List<Long> exerciseIds
    ) {
        WorkoutPlanResponseDTO response = service.addExercises(planId, exerciseIds);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update workout plan", description = "Updates title and/or exercise list. Sending exerciseIds replaces the current list.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plan updated",
                    content = @Content(schema = @Schema(implementation = WorkoutPlanResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Plan not found or invalid data"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Not authenticated")
    })
    @PutMapping("/{id}")
    public ResponseEntity<WorkoutPlanResponseDTO> update(
            @Parameter(description = "Workout plan identifier", example = "1") @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody @Valid WorkoutPlanUpdateDTO dto
    ) {
        WorkoutPlanResponseDTO updated = service.updatePlan(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "List workout plans", description = "Returns all workout plans in the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plan list",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = WorkoutPlanResponseDTO.class)))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Not authenticated")
    })
    @GetMapping
    public ResponseEntity<List<WorkoutPlanResponseDTO>> getAll() {
        List<WorkoutPlanResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get workout plan by id", description = "Returns a single workout plan with its exercises.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plan found",
                    content = @Content(schema = @Schema(implementation = WorkoutPlanResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Plan not found"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Not authenticated")
    })
    @GetMapping("/{id}")
    public ResponseEntity<WorkoutPlanResponseDTO> getById(
            @Parameter(description = "Workout plan identifier", example = "1") @PathVariable Long id
    ) {
        WorkoutPlanResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Generate workout sheet", description = "Builds a printable sheet with repetitions based on the plan owner's fitness level.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sheet generated",
                    content = @Content(schema = @Schema(implementation = WorkoutSheetResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Plan not found"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Not authenticated")
    })
    @GetMapping("/{planId}/sheet")
    public ResponseEntity<WorkoutSheetResponseDTO> getWorkoutSheet(
            @Parameter(description = "Workout plan identifier", example = "1") @PathVariable Long planId
    ) {
        WorkoutSheetResponseDTO sheet = service.generateSheet(planId);
        return ResponseEntity.ok(sheet);
    }

    @Operation(summary = "Delete workout plan", description = "Permanently removes a workout plan.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Plan deleted"),
            @ApiResponse(responseCode = "400", description = "Plan not found"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Not authenticated")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Workout plan identifier", example = "1") @PathVariable Long id
    ) {
        service.deletePlan(id);
        return ResponseEntity.noContent().build();
    }
}
