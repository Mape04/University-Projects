package Validators;

import dto.UserDTO;

import java.time.LocalDate;

public class UserValidator {

    private void validateUsername (String username) {
        if ( username == null || username.isEmpty() ) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if ( !username.matches("^[a-zA-Z0-9_-]+$") ) {
            throw new IllegalArgumentException("Username must contain letters, numbers, \"_\" or \"-\"");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        // Updated regex for email validation
        if (!email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email address");
        }
    }

    private void validateDateOfBirth (java.sql.Date dateOfBirth) {
        if ( dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }
        LocalDate today = LocalDate.now();

        LocalDate dob = dateOfBirth.toLocalDate();

        if(dob.isAfter(today)) {
            throw new IllegalArgumentException("Date of birth cannot be after now");
        }
    }

    public void validateUser(UserDTO user) {
        validateUsername(user.getUsername());
        validateEmail(user.getEmail());
        validateDateOfBirth(user.getDateOfBirth());
    }
}
