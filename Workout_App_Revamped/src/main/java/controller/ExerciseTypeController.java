package controller;

import Domain.ExerciseType;
import Service.ExerciseTypeService;
import Validators.ExerciseTypeValidator;
import dto.DTOUtils;
import dto.ExerciseTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/workout_app/exercise_type")
public class ExerciseTypeController {

    @Autowired
    private ExerciseTypeService exerciseTypeService;

    // Get all exercise types
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        System.out.println("Get all exercise types ...");
        Collection<ExerciseType> exerciseTypes = exerciseTypeService.getAllExerciseTypes();

        // Map entities to response DTOs
        List<ExerciseTypeDTO> exerciseTypeDTOs = exerciseTypes.stream()
                .map(DTOUtils::makeDTO)
                .toList();

        return ResponseEntity.ok(exerciseTypeDTOs);
    }

    // Get exercise type by ID
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        System.out.println("Get exercise type by ID: " + id);
        ExerciseType exerciseType = exerciseTypeService.getExerciseTypeById(id);

        if (exerciseType != null) {
            ExerciseTypeDTO dto = DTOUtils.makeDTO(exerciseType);
            return ResponseEntity.ok(dto);
        } else {
            return new ResponseEntity<>("Exercise type not found", HttpStatus.NOT_FOUND);
        }
    }

    // Create new exercise type
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody ExerciseTypeDTO exerciseTypeDTO) {
        try {
            ExerciseTypeValidator validator = new ExerciseTypeValidator();
            validator.validateExerciseType(exerciseTypeDTO);

            System.out.println("Creating new exercise type ...");

            // Convert DTO to Entity
            ExerciseType newExerciseType = DTOUtils.fromDTO(exerciseTypeDTO);

            // Add the new exercise type to the database
            exerciseTypeService.addExerciseType(newExerciseType);

            // Return the ID of the created exercise type in the response body
            return ResponseEntity.status(HttpStatus.CREATED).body(newExerciseType.getId()); // Return the ID
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating exercise type: " + e.getMessage());
        }
    }


    // Partially update an exercise type
    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdate(@PathVariable Integer id, @RequestBody ExerciseTypeDTO exerciseTypeDTO) {
        try {
            System.out.println("Partially updating exercise type ...");

            ExerciseType existingExerciseType = exerciseTypeService.getExerciseTypeById(id);
            if (existingExerciseType == null) {
                return new ResponseEntity<>("Exercise type not found", HttpStatus.NOT_FOUND);
            }

            // Update only the fields present in the request body
            if (exerciseTypeDTO.getName() != null) {
                existingExerciseType.setName(exerciseTypeDTO.getName());
            }
            if (exerciseTypeDTO.getCategory() != null) {
                existingExerciseType.setCategory(exerciseTypeDTO.getCategory());
            }
            if (exerciseTypeDTO.getDescription() != null) {
                existingExerciseType.setDescription(exerciseTypeDTO.getDescription());
            }

            if (exerciseTypeDTO.getLink() != null){
                existingExerciseType.setLink(exerciseTypeDTO.getLink());
            }

            exerciseTypeService.updateExerciseType(id, existingExerciseType);
            return ResponseEntity.ok("Exercise type updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating exercise type: " + e.getMessage());
        }
    }

    // Delete exercise type by ID
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        System.out.println("Deleting exercise type ... " + id);
        ExerciseType exerciseType = exerciseTypeService.getExerciseTypeById(id);
        if (exerciseType == null) {
            return new ResponseEntity<>("Exercise type not found", HttpStatus.NOT_FOUND);
        }

        exerciseTypeService.deleteExerciseType(id);
        return ResponseEntity.ok("Exercise type deleted successfully");
    }
}
