package dto;

import Domain.*;
import Service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DTOUtils {
    private final ExerciseService exerciseService;

    @Autowired
    public DTOUtils(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    // **AppUser DTO Methods**
    public static UserDTO makeDTO(AppUser appUser) {
        UserDTO userDTO = new UserDTO(
                appUser.getId(),
                appUser.getUsername(),
                appUser.getPassword(),
                appUser.getEmail(),
                appUser.getDateOfBirth(),
                appUser.getGoals().stream().map(Goal::getId).collect(Collectors.toList()),
                appUser.getWorkouts().stream().map(Workout::getId).collect(Collectors.toList()),
                appUser.getAdmin()
        );
        userDTO.setReviewId(appUser.getReviewId());
        userDTO.setSubExpirationDate(appUser.getSubExpirationDate());
        return userDTO;
    }

    public static AppUser fromDTO(UserDTO userDTO) {
        AppUser appUser = new AppUser(
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getEmail(),
                userDTO.getDateOfBirth(),
                userDTO.getWorkoutIds().stream().map(workoutId -> new Workout(workoutId)).collect(Collectors.toSet()), // Placeholder: Replace with real lookup
                userDTO.getGoalIds().stream().map(goalId -> new Goal(goalId)).collect(Collectors.toSet()), // Placeholder: Replace with real lookup
                userDTO.getAdmin()
        );
        appUser.setId(userDTO.getId());
        appUser.setReviewId(userDTO.getReviewId());
        appUser.setSubExpirationDate(userDTO.getSubExpirationDate());
        return appUser;
    }

    // **Goal DTO Methods**
    public static GoalDTO makeDTO(Goal goal) {
        return new GoalDTO(
                goal.getId(),
                goal.getDescription(),
                goal.getDeadline(),
                goal.getIsAchieved(),
                goal.getCreationDate(),
                goal.getAppUsers().stream().map(AppUser::getId).collect(Collectors.toList())
        );
    }

    public static Goal fromDTO(GoalDTO goalDTO) {
        Goal goal = new Goal(goalDTO.getDeadline(), goalDTO.getIsAchieved(), goalDTO.getCreationDate(), goalDTO.getDescription());
        goal.setId(goalDTO.getId());
        return goal;
    }

    // **ExerciseType DTO Methods**
    public static ExerciseTypeDTO makeDTO(ExerciseType exerciseType) {
        return new ExerciseTypeDTO(
                exerciseType.getId(),
                exerciseType.getName(),
                exerciseType.getCategory(),
                exerciseType.getDescription(),
                exerciseType.getLink()
        );
    }

    public static ExerciseType fromDTO(ExerciseTypeDTO exerciseTypeDTO) {
        ExerciseType exerciseType = new ExerciseType(
                exerciseTypeDTO.getName(),
                exerciseTypeDTO.getCategory(),
                exerciseTypeDTO.getDescription(),
                exerciseTypeDTO.getLink()
        );
        exerciseType.setId(exerciseTypeDTO.getId());
        return exerciseType;
    }

    // **Exercise DTO Methods**
    public static ExerciseDTO makeDTO(Exercise exercise) {
        return new ExerciseDTO(
                exercise.getId(),
                exercise.getExerciseType_id(),
                exercise.getSets(),
                exercise.getReps(),
                exercise.getWeight(),
                exercise.getCompleted()
        );
    }

    public static Exercise fromDTO(ExerciseDTO exerciseDTO) {
        Exercise exercise = new Exercise(
                exerciseDTO.getExerciseType_id(),
                exerciseDTO.getSets(),
                exerciseDTO.getReps(),
                exerciseDTO.getWeight(),
                exerciseDTO.getCompleted()
        );
        exercise.setId(exerciseDTO.getId());
        return exercise;
    }

    // **Workout DTO Methods**
    public static WorkoutDTO makeDTO(Workout workout) {
        return new WorkoutDTO(
                workout.getId(),
                workout.getDate(),
                workout.getNotes(),
                workout.getCompletionPercentage(),
                workout.getExercises().stream().map(Exercise::getId).collect(Collectors.toList()),
                workout.getUsers().stream().map(AppUser::getId).collect(Collectors.toList())
        );
    }


    public static Workout fromDTO(WorkoutDTO workoutDTO, ExerciseService exerciseService) {
        Workout workout = new Workout();

        workout.setId(workoutDTO.getId());
        workout.setDate(workoutDTO.getDate());
        workout.setNotes(workoutDTO.getNotes());
        workout.setCompletionPercentage(workoutDTO.getCompletionPercentage());

        // Map exercise IDs to Exercise entities, but only if exerciseIds is not empty
        if (workoutDTO.getExerciseIds() != null && !workoutDTO.getExerciseIds().isEmpty()) {
            Set<Exercise> exercises = workoutDTO.getExerciseIds().stream()
                    .map(exerciseService::getExerciseById)
                    .collect(Collectors.toSet());
            workout.setExercises(exercises);
        }

        return workout;
    }

}
