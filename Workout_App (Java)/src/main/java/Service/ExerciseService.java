package Service;

import Domain.Exercise;
import Interfaces.IExerciseRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ExerciseService {

    private static final Logger logger = LogManager.getLogger(ExerciseService.class);

    private final IExerciseRepository exerciseRepository;

    // Constructor injection of ExerciseRepository
    @Autowired
    public ExerciseService(IExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    // Add a new exercise
    public void addExercise(Exercise exercise) {
        logger.info("Adding Exercise: {}", exercise);
        exerciseRepository.save(exercise); // JPARepository save method (used for insert and update)
    }

    // Delete an exercise by ID
    public void deleteExercise(Integer id) {
        logger.info("Deleting Exercise with id: {}", id);
        // JPARepository deleteById method
        exerciseRepository.deleteById(id);
        logger.info("Deleted Exercise with id: {}", id);
    }

    // Update an existing exercise
    public void updateExercise(Integer id, Exercise updatedExercise) {
        logger.info("Updating Exercise with id: {}", id);
        // If the exercise exists, update it
        if (exerciseRepository.existsById(id)) {
            updatedExercise.setId(id);
            exerciseRepository.save(updatedExercise); // JPARepository save method (used for update)
            logger.info("Updated Exercise with id: {}", id);
        } else {
            logger.warn("Exercise with id {} not found for update", id);
        }
    }

    // Retrieve exercise by ID
    public Exercise getExerciseById(Integer id) {
        logger.info("Retrieving Exercise with id: {}", id);
        // JPARepository findById method, returns an Optional
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        return exercise.orElse(null); // Return null if exercise is not found
    }

    // Retrieve all exercises
    public Collection<Exercise> getAllExercises() {
        logger.info("Retrieving all Exercises");
        // JPARepository findAll method
        return exerciseRepository.findAll();
    }

}
