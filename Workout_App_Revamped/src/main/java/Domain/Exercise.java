package Domain;

import jakarta.persistence.*;

import java.util.Objects;

@jakarta.persistence.Entity
@Table(name = "Exercise")
public class Exercise extends Domain.Entity<Integer> {

    private Integer exerciseType_id;
    private Integer sets;
    private Integer reps;
    private Float weight;
    private Boolean isCompleted;

    public Exercise(Integer exerciseType_id, Integer sets, Integer reps, Float weight, Boolean isCompleted) {
        this.exerciseType_id = exerciseType_id;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.isCompleted = isCompleted;
    }

    public Exercise() {
    }

    public Integer getExerciseType_id() {
        return exerciseType_id;
    }

    public void setExerciseType_id(Integer exerciseType_id) {
        this.exerciseType_id = exerciseType_id;
    }

    public Integer getSets() {
        return sets;
    }

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return Objects.equals(exerciseType_id, exercise.exerciseType_id) && Objects.equals(sets, exercise.sets) && Objects.equals(reps, exercise.reps) && Objects.equals(weight, exercise.weight) && Objects.equals(isCompleted, exercise.isCompleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exerciseType_id, sets, reps, weight, isCompleted);
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", exerciseType_id=" + exerciseType_id +
                ", sets=" + sets +
                ", reps=" + reps +
                ", weight=" + weight +
                ", isCompleted=" + isCompleted +
                '}';
    }

}
