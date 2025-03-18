package Validators;

import Domain.ExerciseType;
import dto.ExerciseTypeDTO;

import java.util.Collection;

public class ExerciseTypeValidator {

    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }

    private void validateCategory(String category) {
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
    }


    public void validateExerciseType(ExerciseTypeDTO exerciseType) {
        validateName(exerciseType.getName());
        validateCategory(exerciseType.getCategory());
        validateDescription(exerciseType.getDescription());
    }
}
