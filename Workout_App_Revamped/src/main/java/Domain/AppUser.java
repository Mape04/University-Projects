package Domain;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@jakarta.persistence.Entity
@Table(name = "AppUser")
public class AppUser extends Entity<Integer> {

    private String username;
    private String password;
    private String email;
    private java.sql.Date dateOfBirth;
    private Boolean isAdmin = false;
    private Integer reviewId;
    private java.sql.Date subExpirationDate;

    @ManyToMany(mappedBy = "appUsers")  // Bidirectional many-to-many relationship
    private Set<Goal> goals = new HashSet<>();

    @ManyToMany(mappedBy = "users") // For Workout relationship
    private Set<Workout> workouts = new HashSet<>();


    public AppUser(String username, String password, String email, Date dateOfBirth, Set<Workout> workouts, Set<Goal> goals, Boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.workouts = workouts;
        this.goals = goals;
        this.isAdmin = isAdmin;
    }

    public AppUser() {
    }

    public AppUser(Integer userId) {
        this.id = userId;
    }

    // Getters and setters
    public Set<Goal> getGoals() {
        return goals;
    }

    public void setGoals(Set<Goal> goals) {
        this.goals = goals;
    }

    public void addGoal(Goal goal) {
        goals.add(goal);
    }

    public void removeGoal(Goal goal) {
        goals.remove(goal);
    }

    public Set<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(Set<Workout> workouts) {
        this.workouts = workouts;
    }

    public void addWorkout(Workout workout) {
        workouts.add(workout);
    }

    public void removeWorkout(Workout workout) {
        workouts.remove(workout);
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

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", workouts='" + workouts + '\'' +
                ", goals='" + goals + '\'' +
                ", isAdmin='" + isAdmin + '\'' +
                ", reviewId='" + reviewId + '\'' +
                ", subExpirationDate='" + subExpirationDate + '\'' +
                '}';
    }
}
