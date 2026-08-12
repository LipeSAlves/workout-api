package learning_api.workout_api.domain.exercise.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import learning_api.workout_api.domain.exercise.dto.ExerciseRequestDTO;
import learning_api.workout_api.domain.exercise.dto.ExerciseResponseDTO;
import learning_api.workout_api.domain.exercise.dto.ExerciseUpdateDTO;
import learning_api.workout_api.service.exercise.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Exercises", description = "Manage http requests that call for creating, updating and deleting exercises, as well as operations meant to disclose exercise information.")
@RestController
@RequestMapping("/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @Operation(summary = "list all exercises", description = "returns a list comprised of all exercises currently saved in the database")
    @GetMapping
    public ResponseEntity<List<ExerciseResponseDTO>> getAllExercises() {
        return ResponseEntity.ok(exerciseService.findAll());
    }

    @Operation(summary = "list a specific exercise", description = "returns one exercise saved in the database, identified using the provided request data")
    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(exerciseService.findById(id));
    }

    @Operation(summary = "create a new exercise", description = "saves a new exercise to the database")
    @PostMapping
    public ResponseEntity<ExerciseResponseDTO> createNewExercise(@RequestBody @Valid ExerciseRequestDTO dto){
        ExerciseResponseDTO saved = exerciseService.saveExercise(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "update an exercise", description = "updates one exercise saved to the database with new information included in the request data, overwriting previous entries")
    @PutMapping("/{id}")
    public ResponseEntity<ExerciseResponseDTO> update(@PathVariable Long id, @RequestBody @Valid ExerciseUpdateDTO dto) {
        return ResponseEntity.ok(exerciseService.updateExercise(id, dto));
    }

    @Operation(summary = "delete one exercise", description = "deletes one exercise entry from the database")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }
}
