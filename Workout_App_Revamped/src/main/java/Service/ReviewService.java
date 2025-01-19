package Service;

import Domain.Review;
import Interfaces.IReviewRepository;
import dto.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private IReviewRepository reviewRepository;

    @Autowired
    public ReviewService(IReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // Add a review
    public Review addReview(Review review) {
        // Here you can associate the review with the authenticated user if needed
        // For now, just save the review as it is
        return reviewRepository.save(review);
    }

    public Collection<Review> getTopReviews(Integer top) {
        return reviewRepository.findAll().stream()
                .sorted((r1, r2) -> r2.getStarRating().compareTo(r1.getStarRating())) // Sort descending
                .limit(top) // Limit to the top N reviews
                .collect(Collectors.toList());
    }

}
