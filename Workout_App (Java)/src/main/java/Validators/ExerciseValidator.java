package Validators;

import dto.ExerciseDTO;

public class ExerciseValidator {

    private void validateSets(Integer sets){
        if (sets < 1){
            throw new IllegalArgumentException("Sets cannot be less than 1");
        }
    }

    private void validateReps(Integer reps){
        if (reps < 1){
            throw new IllegalArgumentException("Reps cannot be less than 1");
        }
    }

    private void validateWeight(Float weight){
        if (weight <= 0){
            throw new IllegalArgumentException("Weight cannot be less than or equal to 0");
        }
    }

    private void validateExerciseTypeId(Integer exerciseTypeId){
        if (exerciseTypeId == null){
            throw new IllegalArgumentException("ExerciseTypeId cannot be null");
        }
    }

    public void validateExercise(ExerciseDTO exercise){
        validateExerciseTypeId(exercise.getExerciseType_id());
        validateSets(exercise.getSets());
        validateReps(exercise.getReps());
        validateWeight(exercise.getWeight());
    }
}
