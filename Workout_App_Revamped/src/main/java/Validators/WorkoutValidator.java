package Validators;

import dto.WorkoutDTO;

import java.sql.Date;
import java.time.LocalDate;

public class WorkoutValidator {

    private void validateNotes(String notes) {
        if (notes == null) {
            throw new IllegalArgumentException("Notes cannot be null");
        }
    }

    private void validateDate(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        // Get the current date without the time component
        LocalDate today = LocalDate.now();

        // Convert the input `Date` to LocalDate for comparison
        LocalDate inputDate = date.toLocalDate();

        if (!inputDate.equals(today)) {
            throw new IllegalArgumentException("Date should be today");
        }
    }

    private void validateCompletionPercentage(Float completionPercentage) {
        if (completionPercentage < 0 || completionPercentage > 100) {
            throw new IllegalArgumentException("Completion percentage should be between 0 and 100");
        }
    }

    public void validateWorkout(WorkoutDTO workout) {
        validateNotes(workout.getNotes());
        validateCompletionPercentage(workout.getCompletionPercentage());
        validateDate(workout.getDate());
    }
}
