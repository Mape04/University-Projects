package dto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class GoalDTO {

    private Integer id;
    private String description;
    private Date deadline;
    private Boolean isAchieved;
    private Date creationDate;
    private List<Integer> appUserIds = new ArrayList<>();  // List of AppUser IDs

    public GoalDTO(Integer id, String description, Date deadline, Boolean isAchieved, Date creationDate, List<Integer> appUserIds) {
        this.id = id;
        this.description = description;
        this.deadline = deadline;
        this.isAchieved = isAchieved;
        this.creationDate = creationDate;
        this.appUserIds = appUserIds;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<Integer> getAppUserIds() {
        return appUserIds;
    }

    public void setAppUserIds(List<Integer> appUserIds) {
        this.appUserIds = appUserIds;
    }
}
