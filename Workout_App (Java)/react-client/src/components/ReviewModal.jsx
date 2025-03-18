import React, { useState } from "react";
import styles from "../styles/ReviewModal.module.css"; // Import your modal CSS styles

const ReviewModal = ({ closeModal }) => {
    const [review, setReview] = useState({
        starRating: '',
        message: '',
    });

    const userId = localStorage.getItem("userId");

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setReview((prevReview) => ({
            ...prevReview,
            [name]: value,
        }));
    };

    const handleSubmitReview = async () => {
        try {
            const response = await fetch(`http://localhost:8080/workout_app/user/review/${userId}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    starRating: review.starRating,
                    message: review.message,
                }),
            });

            if (response.ok) {
                alert("Review submitted successfully");
                closeModal(); // Close the modal after submission
            } else {
                alert("Failed to submit review");
            }
        } catch (error) {
            console.error("Error submitting review:", error);
        }
    };

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modal}>
                <h2>Leave a Review</h2>
                <label>Rating:</label>
                <input
                    type="number"
                    name="starRating"
                    value={review.starRating}
                    onChange={handleInputChange}
                    min="1"
                    max="5"
                    placeholder="Rating (1-5)"
                />
                <label>Message:</label>
                <textarea
                    name="message"
                    value={review.message}
                    onChange={handleInputChange}
                    placeholder="Your message"
                />
                <div className={styles.modalButtons}>
                    <button onClick={handleSubmitReview}>Submit</button>
                    <button onClick={closeModal}>Cancel</button>
                </div>
            </div>
        </div>
    );
};

export default ReviewModal;
