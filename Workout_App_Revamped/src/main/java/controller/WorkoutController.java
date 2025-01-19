package controller;

import Domain.AppUser;
import Domain.Exercise;
import Domain.Workout;
import Service.ExerciseService;
import Service.UserService;
import Service.WorkoutService;
import Validators.ExerciseValidator;
import Validators.WorkoutValidator;
import dto.DTOUtils;
import dto.ExerciseDTO;
import dto.WorkoutDTO;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/workout_app/workout")
public class WorkoutController {

    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private UserService appUserService;

    // Get workouts of the logged-in user
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getWorkoutsForUser(@PathVariable Integer userId) {
        System.out.println("Fetching workouts for user with ID: " + userId);

        // Fetch the user by ID
        AppUser user = appUserService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        // Fetch workouts for the user
        Set<Workout> workouts = user.getWorkouts();

        // Convert to DTOs
        List<WorkoutDTO> workoutDTOs = workouts.stream().map(DTOUtils::makeDTO).collect(Collectors.toList());

        return ResponseEntity.ok(workoutDTOs);
    }

    // Get all workouts
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        System.out.println("Get all workouts ...");
        Collection<Workout> workouts = workoutService.getAllWorkouts();

        // Map entities to response DTOs
        List<WorkoutDTO> workoutDTOs = workouts.stream().map(DTOUtils::makeDTO).toList();

        return ResponseEntity.ok(workoutDTOs);
    }

    // Get workout by ID
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        System.out.println("Get workout by ID: " + id);
        Workout workout = workoutService.getWorkoutById(id);
        if (workout == null) {
            return new ResponseEntity<>("Workout not found", HttpStatus.NOT_FOUND);
        }

        WorkoutDTO dto = DTOUtils.makeDTO(workout);
        return ResponseEntity.ok(dto);
    }

    // Create new workout
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody WorkoutDTO workoutDTO) {
        try {
            WorkoutValidator validator = new WorkoutValidator();
            validator.validateWorkout(workoutDTO);

            System.out.println("Creating new workout ...");
            Workout newWorkout = DTOUtils.fromDTO(workoutDTO, exerciseService);
            workoutService.addWorkout(newWorkout);

            // Return the ID of the newly created workout
            return ResponseEntity.status(HttpStatus.CREATED).body(newWorkout.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating workout: " + e.getMessage());
        }
    }


    // Partially update workout by ID
    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdate(@PathVariable Integer id, @RequestBody WorkoutDTO workoutDTO) {
        try {
            Workout existingWorkout = workoutService.getWorkoutById(id);
            if (existingWorkout == null) {
                return new ResponseEntity<>("Workout not found", HttpStatus.NOT_FOUND);
            }

            // Update only the fields present in the request body
            if (workoutDTO.getDate() != null) {
                existingWorkout.setDate(workoutDTO.getDate());
            }
            if (workoutDTO.getNotes() != null) {
                existingWorkout.setNotes(workoutDTO.getNotes());
            }
            if (workoutDTO.getCompletionPercentage() != null) {
                existingWorkout.setCompletionPercentage(workoutDTO.getCompletionPercentage());
            }

            // Only update exercises if exerciseIds is provided and non-empty
            if (workoutDTO.getExerciseIds() != null && !workoutDTO.getExerciseIds().isEmpty()) {
                Set<Exercise> updatedExercises = workoutDTO.getExerciseIds().stream().map(exerciseService::getExerciseById) // Map IDs to Exercise entities
                        .collect(Collectors.toSet());
                existingWorkout.setExercises(updatedExercises);
            }

            workoutService.updateWorkout(id, existingWorkout);
            return ResponseEntity.ok("Workout updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating workout: " + e.getMessage());
        }
    }


    // Delete workout by ID
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        System.out.println("Deleting workout ... " + id);
        Workout workout = workoutService.getWorkoutById(id);
        if (workout == null) {
            return new ResponseEntity<>("Workout not found", HttpStatus.NOT_FOUND);
        }

        workoutService.deleteWorkout(id);
        return ResponseEntity.ok("Workout deleted successfully");
    }

    // Add exercise to a workout
    @RequestMapping(value = "/{workoutId}/exercise", method = RequestMethod.POST)
    public ResponseEntity<?> addExerciseToWorkout(@PathVariable Integer workoutId, @RequestBody ExerciseDTO exerciseDTO) {
        ExerciseValidator validator = new ExerciseValidator();
        validator.validateExercise(exerciseDTO);

        System.out.println("Adding exercise to workout ID: " + workoutId);

        Workout workout = workoutService.getWorkoutById(workoutId);
        if (workout == null) {
            return new ResponseEntity<>("Workout not found", HttpStatus.NOT_FOUND);
        }

        try {
            // Convert DTO to Exercise entity
            Exercise newExercise = DTOUtils.fromDTO(exerciseDTO);
            exerciseService.addExercise(newExercise);

            // Associate the exercise with the workout
            workout.getExercises().add(newExercise);
            workoutService.updateWorkout(workoutId, workout);


//            return ResponseEntity.status(HttpStatus.CREATED).body("Exercise added to 'workout' successfully");
            return ResponseEntity.ok(newExercise.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding exercise: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/{workoutId}/exercise/{exerciseId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeExerciseFromWorkout(@PathVariable Integer workoutId, @PathVariable Integer exerciseId) {
        System.out.println("Removing exercise with ID: " + exerciseId + " from workout ID: " + workoutId);

        // Fetch the workout by ID
        Workout workout = workoutService.getWorkoutById(workoutId);
        if (workout == null) {
            return new ResponseEntity<>("Workout not found", HttpStatus.NOT_FOUND);
        }

        // Fetch the exercise by ID
        Exercise exercise = exerciseService.getExerciseById(exerciseId);
        if (exercise == null) {
            return new ResponseEntity<>("Exercise not found", HttpStatus.NOT_FOUND);
        }

        // Remove the exercise from the workout's exercises set
        workout.getExercises().remove(exercise);


        // Update the workout in the database
        workoutService.updateWorkout(workoutId, workout);


        return ResponseEntity.ok("Exercise removed from workout successfully");
    }

    @RequestMapping(value = "/statistics/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getWorkoutStatisticsForUser(@PathVariable Integer userId) {
        String statistics = workoutService.getUserWorkoutPerformance(userId);
        if (statistics == null) {
            return new ResponseEntity<>("Statistics not found", HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity<>(statistics, HttpStatus.OK);
    }
}
