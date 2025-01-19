package dto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class WorkoutDTO {

    private Integer id;
    private Date date;
    private String notes;
    private Float completionPercentage;
    private List<Integer> exerciseIds = new ArrayList<>();  // List of Exercise IDs
    private List<Integer> userIds = new ArrayList<>();  // List of AppUser IDs

    public WorkoutDTO() {}

    public WorkoutDTO(Integer id, Date date, String notes, Float completionPercentage, List<Integer> exerciseIds, List<Integer> userIds) {
        this.id = id;
        this.date = date;
        this.notes = notes;
        this.completionPercentage = completionPercentage;
        this.exerciseIds = exerciseIds;
        this.userIds = userIds;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Float getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(Float completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public List<Integer> getExerciseIds() {
        return exerciseIds;
    }

    public void setExerciseIds(List<Integer> exerciseIds) {
        this.exerciseIds = exerciseIds != null ? exerciseIds : new ArrayList<>(); // Safeguard against null
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }
}
