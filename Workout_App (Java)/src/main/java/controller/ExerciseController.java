package controller;

import Domain.Exercise;
import Domain.Workout;
import Service.ExerciseService;
import Service.WorkoutService;
import Validators.ExerciseValidator;
import dto.DTOUtils;
import dto.ExerciseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/workout_app/exercise")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private WorkoutService workoutService;

    // Get all exercises
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        System.out.println("Get all exercises ...");
        Collection<Exercise> exercises = exerciseService.getAllExercises();

        // Map entities to response DTOs
        List<ExerciseDTO> exerciseDTOs = exercises.stream()
                .map(DTOUtils::makeDTO)
                .toList();

        return ResponseEntity.ok(exerciseDTOs);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        System.out.println("Get exercise by ID: " + id);
        Exercise exercise = exerciseService.getExerciseById(id);

        if (exercise != null) {
            ExerciseDTO dto = DTOUtils.makeDTO(exercise);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Exercise not found");
        }
    }

    // Create new exercise
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody ExerciseDTO exerciseDTO) {
        try {
            ExerciseValidator validator = new ExerciseValidator();
            validator.validateExercise(exerciseDTO);

            System.out.println("Creating new exercise ...");
            Exercise newExercise = DTOUtils.fromDTO(exerciseDTO);
            exerciseService.addExercise(newExercise);

            return ResponseEntity.status(HttpStatus.CREATED).body("Exercise created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating exercise: " + e.getMessage());
        }
    }

    // Partially update an exercise
    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdate(@PathVariable Integer id, @RequestBody ExerciseDTO exerciseDTO) {
        try {
            System.out.println("Partially updating exercise ...");

            Exercise existingExercise = exerciseService.getExerciseById(id);
            if (existingExercise == null) {
                return new ResponseEntity<>("Exercise not found", HttpStatus.NOT_FOUND);
            }

            // Update only the fields present in the request body
            if (exerciseDTO.getExerciseType_id() != null) {
                existingExercise.setExerciseType_id(exerciseDTO.getExerciseType_id());
            }
            if (exerciseDTO.getSets() != null) {
                existingExercise.setSets(exerciseDTO.getSets());
            }
            if (exerciseDTO.getReps() != null) {
                existingExercise.setReps(exerciseDTO.getReps());
            }
            if (exerciseDTO.getWeight() != null) {
                existingExercise.setWeight(exerciseDTO.getWeight());
            }
            if (exerciseDTO.getCompleted() != null){
                existingExercise.setCompleted(exerciseDTO.getCompleted());
            }

            exerciseService.updateExercise(id, existingExercise); // This handles both creation and update

            return ResponseEntity.ok("Exercise updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating exercise: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/updatePercentage/{workoutId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updatePercentage(@PathVariable Integer workoutId) {
        // Fetch the associated workout
        Workout workout = workoutService.getWorkoutById(workoutId);
        if (workout != null) {
            workout.updateCompletionPercentage();
            workoutService.updateWorkout(workout.getId(), workout);
        }
        return ResponseEntity.ok("Percentage updated successfully");
    }

    // Delete exercise by ID
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        System.out.println("Deleting exercise ... " + id);
        Exercise exercise = exerciseService.getExerciseById(id);
        if (exercise == null) {
            return new ResponseEntity<>("Exercise not found", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok("Exercise deleted successfully");
    }
}
