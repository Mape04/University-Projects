package Validators;

import Domain.Goal;
import dto.GoalDTO;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;

public class GoalValidator {
    private void validateIsAchieved(Boolean isAchieved) {
        if (isAchieved == null) {
            throw new IllegalArgumentException("isAchieved is null");
        }
    }

    private void validateCreationDate(Date creationDate) {
        if (creationDate == null) {
            throw new IllegalArgumentException("creationDate is null");
        }
        // Get the current date without the time component
        LocalDate today = LocalDate.now();

        // Convert the input `Date` to LocalDate for comparison
        LocalDate inputDate = creationDate.toLocalDate();

        if (!inputDate.equals(today)) {
            throw new IllegalArgumentException("Date should be today");
        }
    }

    private void validateDeadline(Date deadline) {
        if (deadline == null) {
            throw new IllegalArgumentException("deadline is null");
        }
        // Get the current date without the time component
        LocalDate today = LocalDate.now();

        // Convert the input `Date` to LocalDate for comparison
        LocalDate inputDate = deadline.toLocalDate();

        if (inputDate.isBefore(today)){
            throw new IllegalArgumentException("deadline is incorrect");
        }
    }

    private void validateDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("description is null");
        }
    }
    public void validateGoal(GoalDTO goal) {
        validateIsAchieved(goal.getIsAchieved());
        validateCreationDate(goal.getCreationDate());
        validateDeadline(goal.getDeadline());
        validateDescription(goal.getDescription());
    }

}
