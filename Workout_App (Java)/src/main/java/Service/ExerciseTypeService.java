package Service;

import Domain.ExerciseType;
import Interfaces.IExerciseTypeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ExerciseTypeService {

    private static final Logger logger = LogManager.getLogger(ExerciseTypeService.class);

    private final IExerciseTypeRepository exerciseTypeRepository;

    // Constructor injection of ExerciseTypeRepository
    @Autowired
    public ExerciseTypeService(IExerciseTypeRepository exerciseTypeRepository) {
        this.exerciseTypeRepository = exerciseTypeRepository;
    }

    // Add a new exercise type
    public void addExerciseType(ExerciseType exerciseType) {
        logger.info("Adding ExerciseType: {}", exerciseType);
        exerciseTypeRepository.save(exerciseType); // JPARepository save method (used for insert and update)
        logger.info("ExerciseType added successfully");
    }

    // Delete an exercise type by ID
    public void deleteExerciseType(Integer id) {
        logger.info("Deleting ExerciseType with id: {}", id);
        // JPARepository deleteById method
        exerciseTypeRepository.deleteById(id);
        logger.info("Deleted ExerciseType with id: {}", id);
    }

    // Update an existing exercise type
    public void updateExerciseType(Integer id, ExerciseType updatedExerciseType) {
        logger.info("Updating ExerciseType with id: {}", id);
        // If the exercise type exists, update it
        if (exerciseTypeRepository.existsById(id)) {
            updatedExerciseType.setId(id);
            exerciseTypeRepository.save(updatedExerciseType); // JPARepository save method (used for update)
            logger.info("Updated ExerciseType with id: {}", id);
        } else {
            logger.warn("ExerciseType with id {} not found for update", id);
        }
    }

    // Retrieve exercise type by ID
    public ExerciseType getExerciseTypeById(Integer id) {
        logger.info("Retrieving ExerciseType with id: {}", id);
        // JPARepository findById method, returns an Optional
        Optional<ExerciseType> exerciseType = exerciseTypeRepository.findById(id);
        return exerciseType.orElse(null); // Return null if exercise type is not found
    }

    // Retrieve all exercise types
    public Collection<ExerciseType> getAllExerciseTypes() {
        logger.info("Retrieving all ExerciseTypes");
        // JPARepository findAll method
        return exerciseTypeRepository.findAll();
    }
}
