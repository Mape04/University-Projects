package dto;

import java.io.Serializable;

public class ExerciseDTO implements Serializable {
    private Integer id;
    private Integer exerciseType_id;
    private Integer sets;
    private Integer reps;
    private Float weight;
    private Boolean isCompleted;

    public ExerciseDTO(Integer id, Integer exerciseType_id, Integer sets, Integer reps, Float weight, Boolean isCompleted) {
        this.id = id;
        this.exerciseType_id = exerciseType_id;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.isCompleted = isCompleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
    public String toString() {
        return "ExerciseDTO{" +
                "id=" + id +
                ", exerciseType_id=" + exerciseType_id +
                ", sets=" + sets +
                ", reps=" + reps +
                ", weight=" + weight +
                ", isCompleted=" + isCompleted +
                '}';
    }
}
