package dto;

import Domain.Review;

import java.sql.Date;
import java.util.List;

public class UserDTO {

    private Integer id;
    private String username;
    private String password;
    private String email;
    private Date dateOfBirth;
    private List<Integer> goalIds;;  // List of Goal IDs
    private List<Integer> workoutIds;;  // List of Workout IDs
    private Boolean isAdmin;
    private Integer reviewId;
    private Date subExpirationDate;

    public UserDTO(Integer id, String username, String password, String email, Date dateOfBirth, List<Integer> goalIds, List<Integer> workoutIds, Boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.goalIds = goalIds;
        this.workoutIds = workoutIds;
        this.isAdmin = isAdmin;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<Integer> getGoalIds() {
        return goalIds;
    }

    public void setGoalIds(List<Integer> goalIds) {
        this.goalIds = goalIds;
    }

    public List<Integer> getWorkoutIds() {
        return workoutIds;
    }

    public void setWorkoutIds(List<Integer> workoutIds) {
        this.workoutIds = workoutIds;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public Date getSubExpirationDate() {
        return subExpirationDate;
    }

    public void setSubExpirationDate(Date subExpirationDate) {
        this.subExpirationDate = subExpirationDate;
    }
}
