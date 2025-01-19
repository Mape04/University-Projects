package dto;

import java.sql.Date;
import java.util.List;

public class UserResponseDTO {
    private Integer id;
    private String username;
    private String email;
    private List<Integer> workoutIds; // List of workout IDs
    private List<Integer> goalIds;    // List of goal IDs
    private Boolean isAdmin;
    private Integer reviewId;
    private Date subExpirationDate;
    private Date dateOfBirth;

    // Getters and Setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Integer> getWorkoutIds() {
        return workoutIds;
    }

    public void setWorkoutIds(List<Integer> workoutIds) {
        this.workoutIds = workoutIds;
    }

    public List<Integer> getGoalIds() {
        return goalIds;
    }

    public void setGoalIds(List<Integer> goalIds) {
        this.goalIds = goalIds;
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "UserResponseDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", workoutIds=" + workoutIds + '\'' +
                ", goalIds=" + goalIds + '\'' +
                ", isAdmin=" + isAdmin + '\'' +
                ", reviewId=" + reviewId + '\'' +
                ", subExpirationDate=" + subExpirationDate + '\'' +
                ", dateOfBirth=" + dateOfBirth + '\'' +
                '}';
    }
}
