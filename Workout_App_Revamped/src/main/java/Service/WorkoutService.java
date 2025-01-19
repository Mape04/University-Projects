package Service;

import Domain.Exercise;
import Domain.Workout;
import Interfaces.IExerciseRepository;
import Interfaces.IWorkoutRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WorkoutService {

    private static final Logger logger = LogManager.getLogger(WorkoutService.class);

    private final IWorkoutRepository workoutRepository;
    private final IExerciseRepository exerciseRepository;

    // Constructor injection of WorkoutRepository
    @Autowired
    public WorkoutService(IWorkoutRepository workoutRepository, IExerciseRepository exerciseRepository) {
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
    }

    // Add a new workout
    public void addWorkout(Workout workout) {
        logger.info("Adding Workout: {}", workout);
        workoutRepository.save(workout); // JPARepository save method (used for insert and update)
        logger.info("Workout added successfully");
    }

    // Delete a workout by ID
    public void deleteWorkout(Integer id) {
        logger.info("Deleting Workout with id: {}", id);
        // JPARepository deleteById method
        workoutRepository.deleteById(id);
        logger.info("Deleted Workout with id: {}", id);
    }

    @Transactional
    public Workout updateWorkout(Integer id, Workout updatedWorkout) {
        Workout existingWorkout = workoutRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Workout with id " + id + " not found"));

        // Update fields
        if (updatedWorkout.getDate() != null) {
            existingWorkout.setDate(updatedWorkout.getDate());
        }
        if (updatedWorkout.getNotes() != null) {
            existingWorkout.setNotes(updatedWorkout.getNotes());
        }
        if (updatedWorkout.getCompletionPercentage() != null) {
            existingWorkout.setCompletionPercentage(updatedWorkout.getCompletionPercentage());
        }

        // Exercises should only be updated explicitly (not cleared if not provided)
        if (updatedWorkout.getExercises() != null && !updatedWorkout.getExercises().isEmpty()) {
            existingWorkout.setExercises(updatedWorkout.getExercises());
        }

        return workoutRepository.save(existingWorkout); // Save the updated workout
    }


    // Retrieve workout by ID
    public Workout getWorkoutById(Integer id) {
        logger.info("Retrieving Workout with id: {}", id);
        // JPARepository findById method, returns an Optional
        Optional<Workout> workout = workoutRepository.findById(id);
        return workout.orElse(null); // Return null if workout is not found
    }

    // Retrieve all workouts
    public Collection<Workout> getAllWorkouts() {
        logger.info("Retrieving all Workouts");
        // JPARepository findAll method
        return workoutRepository.findAll();
    }

    public void setExerciseIds(Workout workout, List<Integer> exerciseIds) {
        if (exerciseIds != null) {
            // Fetch exercises by their IDs using exerciseRepository
            Set<Exercise> exercises = exerciseIds.stream().map(id -> exerciseRepository.findById(id).orElse(null)) // Use exerciseRepository to get Exercise by ID
                    .filter(exercise -> exercise != null)  // Filter out any null values in case of invalid IDs
                    .collect(Collectors.toSet());  // Collect into a Set

            // Set the exercises to the workout
            workout.setExercises(exercises);  // Assuming Workout has a Set<Exercise> exercises field
        } else {
            workout.setExercises(null);  // If the exerciseIds list is null, set the exercises to null
        }
    }

    public Workout getWorkoutByExercise(Exercise exercise) {
        return workoutRepository.findByExercisesContaining(exercise.getId()).orElseThrow(() -> new ResourceNotFoundException("Workout not found for Exercise ID: " + exercise.getId()));
    }

    public String analyzeWorkoutFocus(Workout workout) {
        if (workout == null || workout.getExercises() == null || workout.getExercises().isEmpty()) {
            return "No Data"; // Return "No Data" if no exercises are available
        }

        long hypertrophyCount = workout.getExercises().stream()
                .filter(ex -> ex.getReps() != null && ex.getReps() >= 6 && ex.getReps() <= 12) // Reps in hypertrophy range
                .count();

        long strengthCount = workout.getExercises().stream()
                .filter(ex -> ex.getReps() != null && ex.getReps() >= 1 && ex.getReps() <= 5) // Reps in strength range
                .count();

        if (hypertrophyCount > strengthCount) {
            return "Hypertrophy";
        } else if (strengthCount > hypertrophyCount) {
            return "Strength";
        } else {
            return "General"; // Equal focus or no clear majority
        }
    }

    public String getUserWorkoutPerformance(Integer userId) {
        List<Workout> userWorkouts = workoutRepository.findByUsers_Id(userId); // Custom query for user's workouts

        if (userWorkouts.isEmpty()) {
            return "No workouts found for the user.";
        }

        long completedWorkouts = userWorkouts.stream()
                .filter(workout -> workout.getCompletionPercentage() != null && workout.getCompletionPercentage() == 100)
                .count();

        long hypertrophyWorkouts = userWorkouts.stream()
                .filter(workout -> "Hypertrophy".equals(analyzeWorkoutFocus(workout)))
                .count();

        long strengthWorkouts = userWorkouts.stream()
                .filter(workout -> "Strength".equals(analyzeWorkoutFocus(workout)))
                .count();

        long generalWorkouts = userWorkouts.stream()
                .filter(workout -> "General".equals(analyzeWorkoutFocus(workout)))
                .count();

        return String.format(
                "User Workout Performance (User ID: %d):\n" +
                        "- Total Workouts: %d\n" +
                        "- Completed Workouts: %d\n" +
                        "- Hypertrophy-Focused Workouts: %d\n" +
                        "- Strength-Focused Workouts: %d\n" +
                        "- General Workouts: %d",
                userId, userWorkouts.size(), completedWorkouts, hypertrophyWorkouts, strengthWorkouts, generalWorkouts
        );
    }

}
