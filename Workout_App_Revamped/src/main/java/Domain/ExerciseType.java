package Domain;

import jakarta.persistence.Table;

import java.util.Objects;

@jakarta.persistence.Entity
@Table(name = "ExerciseType")
public class ExerciseType extends Domain.Entity<Integer> {

    private String name;
    private String category;
    private String description;
    private String link;


    public ExerciseType(String name, String category, String description, String link) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.link = link;
    }

    public ExerciseType() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{name, category, description});
    }


    @Override
    public String toString() {
        return "ExerciseType{" +
                "id=" + id + '\'' +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
