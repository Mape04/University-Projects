package Domain;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@jakarta.persistence.Entity
@Table(name = "Goal")
public class Goal extends Domain.Entity<Integer> {

    private String description;
    private java.sql.Date deadline;
    private Boolean isAchieved;
    private java.sql.Date creationDate;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "User_Goal",  // Join table name
            joinColumns = @JoinColumn(name = "goal_id"),  // Foreign key for Goal
            inverseJoinColumns = @JoinColumn(name = "user_id")  // Foreign key for AppUser
    )
    private Set<AppUser> appUsers = new HashSet<>();

    public Goal(Date deadline, Boolean isAchieved, Date creationDate, String description) {
        this.description = description;
        this.deadline = deadline;
        this.isAchieved = isAchieved;
        this.creationDate = creationDate;
    }

    public Goal() {
    }

    public Goal(Integer goalId) {
        this.id = goalId;
    }

    // Getters and setters
    public Set<AppUser> getAppUsers() {
        return appUsers;
    }

    public void setAppUsers(Set<AppUser> appUsers) {
        this.appUsers = appUsers;
    }

    public void addAppUser(AppUser appUser) {
        appUsers.add(appUser);
    }

    public void removeAppUser(AppUser appUser) {
        appUsers.remove(appUser);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Boolean getIsAchieved() {
        return isAchieved;
    }

    public void setIsAchieved(Boolean isAchieved) {
        this.isAchieved = isAchieved;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "description=" + description +
                ", deadline=" + deadline +
                ", isAchieved=" + isAchieved +
                ", creationDate=" + creationDate +
                '}';
    }
}
