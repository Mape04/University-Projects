package Service;

import Domain.Goal;
import Interfaces.IGoalRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class GoalService {

    private static final Logger logger = LogManager.getLogger(GoalService.class);

    private final IGoalRepository goalRepository;

    // Constructor injection of GoalRepository
    @Autowired
    public GoalService(IGoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    // Add a new goal
    public void addGoal(Goal goal) {
        logger.info("Adding Goal: {}", goal);
        goalRepository.save(goal); // JPARepository save method (used for insert and update)
    }

    // Delete a goal by ID
    public void deleteGoal(Integer id) {
        logger.info("Deleting Goal with id: {}", id);
        // JPARepository deleteById method
        goalRepository.deleteById(id);
        logger.info("Deleted Goal with id: {}", id);
    }

    // Update an existing goal
    public void updateGoal(Integer id, Goal updatedGoal) {
        logger.info("Updating Goal with id: {}", id);
        // If the goal exists, update it
        if (goalRepository.existsById(id)) {
            updatedGoal.setId(id);
            goalRepository.save(updatedGoal); // JPARepository save method (used for update)
            logger.info("Updated Goal with id: {}", id);
        } else {
            logger.warn("Goal with id {} not found for update", id);
        }
    }

    // Retrieve goal by ID
    public Goal getGoalById(Integer id) {
        logger.info("Retrieving Goal with id: {}", id);
        // JPARepository findById method, returns an Optional
        Optional<Goal> goal = goalRepository.findById(id);
        return goal.orElse(null); // Return null if goal is not found
    }

    // Retrieve all goals
    public Collection<Goal> getAllGoals() {
        logger.info("Retrieving all Goals");
        // JPARepository findAll method
        return goalRepository.findAll();
    }
}
