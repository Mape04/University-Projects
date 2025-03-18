package controller;

import Domain.Goal;
import Domain.AppUser;
import Service.GoalService;
import Service.UserService;
import Validators.GoalValidator;
import dto.DTOUtils;
import dto.GoalDTO;
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
@RequestMapping("/workout_app/goal")
public class GoalController {

    @Autowired
    private GoalService goalService;

    @Autowired
    private UserService appUserService; // Service to fetch users by their IDs

    // Get all goals
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        System.out.println("Get all goals ...");
        Collection<Goal> goals = goalService.getAllGoals();

        // Use DTOUtils to map entities to DTOs
        List<GoalDTO> goalDTOs = goals.stream()
                .map(DTOUtils::makeDTO)  // Correct usage of the injected non-static method
                .collect(Collectors.toList());

        return ResponseEntity.ok(goalDTOs);
    }

    // Get goal by ID
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        System.out.println("Get by id " + id);
        Goal goal = goalService.getGoalById(id);
        if (goal == null) {
            return new ResponseEntity<>("Goal not found", HttpStatus.NOT_FOUND);
        }

        // Convert Goal entity to GoalDTO using DTOUtils
        GoalDTO goalDTO = DTOUtils.makeDTO(goal);

        return ResponseEntity.ok(goalDTO);
    }

    // Create new goal
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody GoalDTO goalDTO) {
        try {
            GoalValidator validator = new GoalValidator();
            validator.validateGoal(goalDTO);

            // Convert GoalDTO to Goal entity using DTOUtils
            Goal goal = DTOUtils.fromDTO(goalDTO);

            // Optional: If you need to associate AppUsers to the goal, handle it here:
            if (goalDTO.getAppUserIds() != null) {
                Set<AppUser> users = goalDTO.getAppUserIds().stream()
                        .map(appUserService::getUserById) // Use a service to fetch users by ID
                        .collect(Collectors.toSet());
                goal.setAppUsers(users);
            }

            goalService.addGoal(goal);

            return ResponseEntity.status(HttpStatus.CREATED).body(goal.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating goal: " + e.getMessage());
        }
    }


    // Delete goal by ID
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        System.out.println("Deleting goal ... " + id);
        Goal goal = goalService.getGoalById(id);
        if (goal == null) {
            return new ResponseEntity<>("Goal not found", HttpStatus.NOT_FOUND);
        }

        goalService.deleteGoal(goal.getId());
        return ResponseEntity.ok("Goal deleted successfully");
    }

    @RequestMapping(value = "/{goalId}/user", method = RequestMethod.POST)
    public ResponseEntity<?> addUserToGoal(@PathVariable Integer goalId, @RequestBody Integer userId) {
        System.out.println("Linking user ID: " + userId + " to goal ID: " + goalId);

        // Fetch the goal by ID
        Goal goal = goalService.getGoalById(goalId);
        if (goal == null) {
            return new ResponseEntity<>("Goal not found", HttpStatus.NOT_FOUND);
        }

        // Fetch the user by ID
        AppUser user = appUserService.getUserById(userId);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        try {
            // Associate the user with the goal
            goal.getAppUsers().add(user);
            goalService.updateGoal(goal.getId(), goal);

            return ResponseEntity.status(HttpStatus.CREATED).body("User linked to goal successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error linking user to goal: " + e.getMessage());
        }
    }

    @PatchMapping("/complete/{goalId}")
    public ResponseEntity<?> markGoalAsComplete(@PathVariable Integer goalId) {
        Goal goal = goalService.getGoalById(goalId);
        if (goal == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Goal not found");
        }
        goal.setIsAchieved(true);
        return ResponseEntity.ok("Goal marked as complete");
    }


    @RequestMapping(value = "/{goalId}/user/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeUserFromGoal(@PathVariable Integer goalId, @PathVariable Integer userId) {
        System.out.println("Removing user ID: " + userId + " from goal ID: " + goalId);

        // Fetch the goal by ID
        Goal goal = goalService.getGoalById(goalId);
        if (goal == null) {
            return new ResponseEntity<>("Goal not found", HttpStatus.NOT_FOUND);
        }

        // Fetch the user by ID
        AppUser user = appUserService.getUserById(userId);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        try {
            // Remove the user from the goal
            if (goal.getAppUsers().remove(user)) {
                goalService.updateGoal(goal.getId(), goal);
                return ResponseEntity.status(HttpStatus.OK).body("User removed from goal successfully");
            } else {
                return new ResponseEntity<>("User not associated with this goal", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing user from goal: " + e.getMessage());
        }
    }


}
