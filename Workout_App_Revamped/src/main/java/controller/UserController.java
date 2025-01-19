package controller;

import Domain.AppUser;
import Domain.Goal;
import Domain.Review;
import Domain.Workout;
import Service.ReviewService;
import Service.UserService;
import Service.WorkoutService;
import Service.GoalService;
import Validators.UserValidator;
import dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/workout_app/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private GoalService goalService;

    @Autowired
    private ReviewService reviewService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        System.out.println("Get all appUsers ...");
        Collection<AppUser> appUsers = userService.getAllUsers();

        // Map entities to response DTOs
        List<UserResponseDTO> userDTOs = appUsers.stream()
                .map(user -> {
                    UserResponseDTO dto = new UserResponseDTO();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setEmail(user.getEmail());
                    dto.setWorkoutIds(user.getWorkouts().stream().map(Workout::getId).collect(Collectors.toList())); // Include workout IDs
                    dto.setGoalIds(user.getGoals().stream().map(Goal::getId).collect(Collectors.toList())); // Include goal IDs
                    dto.setAdmin(user.getAdmin());
                    dto.setReviewId(user.getReviewId());
                    dto.setDateOfBirth(user.getDateOfBirth());
                    dto.setSubExpirationDate(user.getSubExpirationDate());
                    return dto;
                }).toList();

        return ResponseEntity.ok(userDTOs);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        System.out.println("Get by id " + id);
        AppUser appUser = userService.getUserById(id);
        if (appUser == null) {
            return new ResponseEntity<>("AppUser not found", HttpStatus.NOT_FOUND);
        }

        UserDTO userDTO = DTOUtils.makeDTO(appUser);

        return ResponseEntity.ok(userDTO);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody UserDTO userDTO) {
        try {
            UserValidator userValidator = new UserValidator();
            userValidator.validateUser(userDTO);

            AppUser appUser = DTOUtils.fromDTO(userDTO);
            userService.addUser(appUser);

            return ResponseEntity.status(HttpStatus.CREATED).body("AppUser created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdate(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        try {
            System.out.println("Partially updating user ...");

            AppUser existingAppUser = userService.getUserById(id);
            if (existingAppUser == null) {
                return new ResponseEntity<>("AppUser not found", HttpStatus.NOT_FOUND);
            }

            // Update only the fields present in the request body
            if (userDTO.getUsername() != null) {
                existingAppUser.setUsername(userDTO.getUsername());
            }
            if (userDTO.getEmail() != null) {
                existingAppUser.setEmail(userDTO.getEmail());
            }
            if (userDTO.getPassword() != null) {
                existingAppUser.setPassword(userDTO.getPassword());
            }
            if (userDTO.getDateOfBirth() != null) {
                existingAppUser.setDateOfBirth(userDTO.getDateOfBirth());
            }
            if(userDTO.getSubExpirationDate() != null){
                existingAppUser.setSubExpirationDate(userDTO.getSubExpirationDate());
            }

            userService.updateUser(id, existingAppUser);
            return ResponseEntity.ok("AppUser updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        System.out.println("Deleting appUser ... " + id);
        AppUser appUser = userService.getUserById(id);
        if (appUser == null) {
            return new ResponseEntity<>("AppUser not found", HttpStatus.NOT_FOUND);
        }

        userService.deleteUser(appUser.getId());
        return ResponseEntity.ok("AppUser deleted successfully");
    }

    @PostMapping("/{userId}/workout/{workoutId}")
    public ResponseEntity<?> addWorkoutToUser(@PathVariable Integer userId, @PathVariable Integer workoutId) {
        AppUser appUser = userService.getUserById(userId);
        if (appUser == null) {
            return new ResponseEntity<>("AppUser not found", HttpStatus.NOT_FOUND);
        }

        Workout workout = workoutService.getWorkoutById(workoutId);
        if (workout == null) {
            return new ResponseEntity<>("Workout not found", HttpStatus.NOT_FOUND);
        }

        appUser.getWorkouts().add(workout);
        workout.getUsers().add(appUser);

        userService.updateUser(userId, appUser); // Save changes
        workoutService.updateWorkout(workoutId, workout);

        return ResponseEntity.ok("Workout added to AppUser successfully");
    }

    // Get all workouts for a user
    @GetMapping("/{userId}/workouts")
    public ResponseEntity<?> getUserWorkouts(@PathVariable Integer userId) {
        AppUser appUser = userService.getUserById(userId);
        if (appUser == null) {
            return new ResponseEntity<>("AppUser not found", HttpStatus.NOT_FOUND);
        }

        Set<WorkoutDTO> workoutDTOs = appUser.getWorkouts().stream()
                .map(DTOUtils::makeDTO)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(workoutDTOs);
    }

    // Associate a user with a goal
    @PostMapping("/{userId}/goal/{goalId}")
    public ResponseEntity<?> addGoalToUser(@PathVariable Integer userId, @PathVariable Integer goalId) {
        AppUser appUser = userService.getUserById(userId);
        if (appUser == null) {
            return new ResponseEntity<>("AppUser not found", HttpStatus.NOT_FOUND);
        }

        Goal goal = goalService.getGoalById(goalId);
        if (goal == null) {
            return new ResponseEntity<>("Goal not found", HttpStatus.NOT_FOUND);
        }

        appUser.getGoals().add(goal);
        goal.getAppUsers().add(appUser);

        userService.updateUser(userId, appUser);
        goalService.updateGoal(goalId, goal);

        return ResponseEntity.ok("Goal added to AppUser successfully");
    }

    // Get all goals for a user
    @GetMapping("/{userId}/goals")
    public ResponseEntity<?> getUserGoals(@PathVariable Integer userId) {
        AppUser appUser = userService.getUserById(userId);
        if (appUser == null) {
            return new ResponseEntity<>("AppUser not found", HttpStatus.NOT_FOUND);
        }

        Set<GoalDTO> goalDTOs = appUser.getGoals().stream()
                .map(DTOUtils::makeDTO)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(goalDTOs);
    }

    @GetMapping("/review")
    public ResponseEntity<?> getTopUserReviews() {
        // Fetch the top 6 reviews
        List<Review> topReviews = reviewService.getTopReviews(6).stream().toList();

        // Convert to response DTOs if necessary
        List<ReviewDTO> reviewDTOs = topReviews.stream()
                .map(review -> {
                    ReviewDTO dto = new ReviewDTO();
                    dto.setId(review.getId());
                    dto.setStarRating(review.getStarRating());
                    dto.setMessage(review.getMessage());
                    dto.setUsername(review.getAppUser().getUsername()); // Include username of the user who wrote the review
                    return dto;
                })
                .toList();

        // Return as a ResponseEntity
        return ResponseEntity.ok(reviewDTOs);
    }

    @PostMapping("/review/{userId}")
    public ResponseEntity<Review> submitReview(@RequestBody Review review, @PathVariable Integer userId) {
        try {
            // Retrieve the user by ID
            AppUser appUser = userService.getUserById(userId);

            if (appUser == null) {
                return ResponseEntity.status(404).body(null); // Return error if user is not found
            }

            // Set the appUser reference in the review
            review.setAppUser(appUser);

            // Save the review, which will now have the user reference
            Review createdReview = reviewService.addReview(review);

            // Set the reviewId in the AppUser after the review is created
            appUser.setReviewId(createdReview.getId());

            // Optionally, save the AppUser again to persist the reviewId
            userService.updateUser(userId, appUser);  // Ensure the user gets updated in the database

            return ResponseEntity.ok(createdReview);  // Return the created review
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);  // Return error if there's an issue
        }
    }

}
