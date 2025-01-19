package Domain;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Objects;

@jakarta.persistence.Entity
@Table(name = "Review")
public class Review extends Entity<Integer>{
    private Float starRating;
    private String message;

    @ManyToOne // Many reviews can belong to one user
    @JoinColumn(name = "user_id") // This will create a foreign key to the AppUser entity
    private AppUser appUser; // Reference to the AppUser who owns the review

    public Review() {
    }

    public Review(Float starRating, String message) {
        this.starRating = starRating;
        this.message = message;
    }

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

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(starRating, review.starRating) && Objects.equals(message, review.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(starRating, message);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id + '\'' +
                ", starRating=" + starRating + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
