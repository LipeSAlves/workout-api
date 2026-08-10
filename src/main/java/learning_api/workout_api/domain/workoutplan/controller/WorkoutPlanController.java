package learning_api.workout_api.domain.workoutplan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

@Tag(name = "Workout Plans", description = "Manage http requests that call for creating, updating and deleting workout plans, as well as operations meant to disclose workout plan information.")
@RestController
@RequestMapping("/workout-plans")
public class WorkoutPlanController {

    @Autowired
    private WorkoutPlanService service;


    @Operation(summary = "Create a workout plan", description = "Creates a workout plan tailor-made for a user. The request data may optionally include a list of exercises to populate the workout plan on creation")
    @PostMapping
    public ResponseEntity<WorkoutPlanResponseDTO> createPlan(@RequestBody @Valid WorkoutPlanRequestDTO dto) {
      WorkoutPlanResponseDTO response = service.createPlan(dto);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Add exercises to workout plan", description = "Uses the provided request data to populate an existing workout plan with exercises")
    @PostMapping("/{planId}/exercises")
    public ResponseEntity<WorkoutPlanResponseDTO> addExercisesToPlan(
            @PathVariable Long planId,
            @RequestBody List<Long> exerciseIds) {

        WorkoutPlanResponseDTO response = service.addExercises(planId, exerciseIds);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Update workout plan", description = "Uses provided request data to update an existing workout plan with new title and new exercise. Note that adding exercises to the request data causes previous exercises added to the workout to be cleared.")
    @PutMapping("/{id}")
    public ResponseEntity<WorkoutPlanResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid WorkoutPlanUpdateDTO dto
    ) {
        WorkoutPlanResponseDTO updated = service.updatePlan(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "List all workout plans", description = "Lists all workout plans currently saved in the database")
    @GetMapping
    public ResponseEntity<List<WorkoutPlanResponseDTO>> getAll() {
        List<WorkoutPlanResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "List a workout plan", description = "Returns one specific workout plan identified using provided request data")
    @GetMapping("/{id}")
    public ResponseEntity<WorkoutPlanResponseDTO> getById(@PathVariable Long id) {
        WorkoutPlanResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Print workout sheet", description = "Returns a workout sheet meant to be printed out for the user. Dynamically calculates the amount of repetitions for each of the workout plan's exercises based on the user's fitness level.")
    @GetMapping("/{planId}/sheet")
    public ResponseEntity<WorkoutSheetResponseDTO> getWorkoutSheet(@PathVariable Long planId) {
        WorkoutSheetResponseDTO sheet = service.generateSheet(planId);
        return ResponseEntity.ok(sheet);
    }

    @Operation(summary = "Delete workout plan", description = "Deletes a workout plan from the database using provided request data")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deletePlan(id);
        return ResponseEntity.noContent().build();
    }
}
