package Service;

import Domain.AppUser;
import Interfaces.IUserRepository;
import dto.LoginRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);

    private final IUserRepository userRepository;

    // Constructor injection of UserRepository
    @Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String signup(AppUser newUser) {
        // Check if username or email already exists
        Optional<AppUser> existingUserByUsername = userRepository.findByUsername(newUser.getUsername());
        Optional<AppUser> existingUserByEmail = userRepository.findByEmail(newUser.getEmail());

        if (existingUserByUsername.isPresent()) {
            return "Username already exists.";
        }
        if (existingUserByEmail.isPresent()) {
            return "Email already exists.";
        }

        userRepository.save(newUser);

        return "User registered successfully.";
    }


    public String login(LoginRequest loginRequest) {
        // Validate email
        if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty()) {
            return "Email must not be null or empty.";
        }

        // Query user
        Optional<AppUser> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if (userOptional.isEmpty()) {
            return "User not found.";
        }

        AppUser user = userOptional.get();

        // Validate password
        if (loginRequest.getPassword() == null || !loginRequest.getPassword().equals(user.getPassword())) {
            return "Invalid credentials.";
        }

        return "Login successful. Welcome, " + user.getUsername() + "!";
    }



    // Log out (simple example)
    public String logout() {
        // This is typically handled via session management or JWT invalidation
        return "Logout successful.";
    }


    // Add a new user (insert or update)
    public void addUser(AppUser appUser) {
        logger.info("Adding AppUser: {}", appUser);
        // Using save() which works for both insert and update operations
        userRepository.save(appUser);
        logger.info("AppUser added/updated successfully.");
    }

    // Delete a user by ID
    public void deleteUser(Integer id) {
        logger.info("Deleting AppUser with id: {}", id);
        // Using deleteById() from JpaRepository
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            logger.info("Deleted AppUser with id: {}", id);
        } else {
            logger.warn("AppUser with id {} not found for deletion", id);
        }
    }

    // Update an existing user
    public void updateUser(Integer id, AppUser updatedAppUser) {
        logger.info("Updating AppUser with id: {}", id);

        // Retrieve the user first to ensure it exists
        Optional<AppUser> existingAppUserOpt = userRepository.findById(id);
        if (existingAppUserOpt.isEmpty()) {
            logger.warn("AppUser with id {} not found for update", id);
            throw new NoSuchElementException("AppUser not found for update");
        }

        // Check for email and username uniqueness
        Optional<AppUser> userByEmail = userRepository.findByEmail(updatedAppUser.getEmail());
        if (userByEmail.isPresent() && !userByEmail.get().getId().equals(id)) {
            logger.warn("Email {} is already in use by another appUser", updatedAppUser.getEmail());
            throw new IllegalArgumentException("Email is already in use");
        }

        Optional<AppUser> userByUsername = userRepository.findByUsername(updatedAppUser.getUsername());
        if (userByUsername.isPresent() && !userByUsername.get().getId().equals(id)) {
            logger.warn("Username {} is already in use by another appUser", updatedAppUser.getUsername());
            throw new IllegalArgumentException("Username is already in use");
        }

        // Set the ID before saving
        updatedAppUser.setId(id);
        userRepository.save(updatedAppUser);  // Use save to update the entity
        logger.info("AppUser updated successfully: {}", updatedAppUser);
    }

    // Retrieve a user by ID
    public AppUser getUserById(Integer id) {
        logger.info("Retrieving AppUser with id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("AppUser with id '" + id + "' not found"));
    }

    // Retrieve all users
    public Collection<AppUser> getAllUsers() {
        logger.info("Retrieving all Users");
        return userRepository.findAll();
    }

    // Retrieve a user by username
    public AppUser getUserByUsername(String username) {
        logger.info("Retrieving AppUser with username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("AppUser with username '" + username + "' not found"));
    }

    public String findByEmail(String email) {
        Optional<AppUser> appUser = userRepository.findByEmail(email);

        if (appUser.isPresent()) {
            Integer userId = appUser.get().getId();
            return userId.toString();
        } else {
            // Handle the case where the user is not found, e.g., return a default value or error message
            return "User not found";
        }
    }
}
