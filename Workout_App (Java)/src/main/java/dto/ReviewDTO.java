package dto;

public class ReviewDTO {
    private Integer id;
    private Float starRating;
    private String message;
    private String username;

    public Float getStarRating() {
        return starRating;
    }

    public void setStarRating(Float starRating) {
        this.starRating = starRating;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

    @Override
    public String toString() {
        return "ReviewDTO{" +
                "id=" + id +
                ", starRating=" + starRating +
                ", message='" + message + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
