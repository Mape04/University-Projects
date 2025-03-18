package Domain;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@jakarta.persistence.Entity
@Table(name = "Workout")
public class Workout extends Domain.Entity<Integer> {

    private Date date;
    private String notes;
    private Float completionPercentage;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "AppUser_Workout",  // Join table to represent the many-to-many relationship
            joinColumns = @JoinColumn(name = "workout_id"),
            inverseJoinColumns = @JoinColumn(name = "appuser_id")
    )
    private Set<AppUser> users = new HashSet<>();  // Set of AppUsers associated with the workout

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "Workout_Exercise",
            joinColumns = @JoinColumn(name = "workout_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id")
    )
    private Set<Exercise> exercises = new HashSet<>();

    // Constructors, getters, setters, and other methods

    public Workout(Date date, String notes, Float completionPercentage) {
        this.date = date;
        this.notes = notes;
        this.completionPercentage = completionPercentage;
    }

    public Workout() {
    }

    public Workout(Integer workoutId) {
        this.id = workoutId;
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

    public Set<AppUser> getUsers() {
        return users;
    }

    public void setUsers(Set<AppUser> users) {
        this.users = users;
    }

    public void addUser(AppUser user) {
        users.add(user);
    }

    public void removeUser(AppUser user) {
        users.remove(user);
    }

    public Set<Exercise> getExercises() {
        return exercises;
    }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    public void removeExercise(Exercise exercise) {
        exercises.remove(exercise);
    }

    public void setExercises(Set<Exercise> exercises) {
        if (exercises != null) {
            this.exercises = exercises;
        }
    }

    public void updateCompletionPercentage() {
        if (exercises == null || exercises.isEmpty()) {
            this.completionPercentage = 0f;
            return;
        }

        long completedCount = exercises.stream().filter(Exercise::getCompleted).count();
        this.completionPercentage = (completedCount / (float) exercises.size()) * 100;
    }


    @Override
    public String toString() {
        return "Workout{" +
                "id=" + id +
                ", date=" + date +
                ", notes='" + notes + '\'' +
                ", exercises=" + exercises.stream().map(Exercise::getId).toList() +
                '}';
    }

}
