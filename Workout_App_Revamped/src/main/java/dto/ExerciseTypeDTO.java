package dto;

import java.io.Serializable;

public class ExerciseTypeDTO implements Serializable {
    private Integer id;
    private String name;
    private String category;
    private String description;
    private String link;

    public ExerciseTypeDTO(Integer id,String name, String category, String description, String link) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.link = link;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "ExerciseTypeDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
