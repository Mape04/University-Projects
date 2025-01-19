import React, {useEffect, useState} from "react";
import styles from "../styles/HomePage.module.css";
import {Link} from "react-router-dom";

const HomePage = () => {
    const [reviews, setReviews] = useState([]); // State to store reviews
    const [loading, setLoading] = useState(true); // State for loading status
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [message, setMessage] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsLoading(true);
        setErrorMessage('');
        setSuccessMessage('');

        if (!name || !email || !message) {
            setErrorMessage('All fields are required.');
            setIsLoading(false);
            return;
        }

        try {
            // Send message data to backend
            const response = await fetch('http://localhost:8080/workout_app/contact', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ name, email, message }),
            });

            const data = await response.text(); // Getting raw text response

            if (response.ok) {
                setSuccessMessage(data || 'Message sent successfully!');
                setName('');
                setEmail('');
                setMessage('');
            } else {
                setErrorMessage(data || 'Message failed to send. Please try again.');
            }
        } catch (error) {
            setErrorMessage('An error occurred. Please try again later.');
        } finally {
            setIsLoading(false);
        }
    };

    // Fetch reviews from backend
    useEffect(() => {
        const fetchReviews = async () => {
            try {
                const response = await fetch("http://localhost:8080/workout_app/user/review", {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                    },
                }); // Fetch from the endpoint
                if (!response.ok) {
                    throw new Error("Failed to fetch reviews");
                }
                const data = await response.json();
                setReviews(data); // Update state with reviews
            } catch (error) {
                console.error("Error fetching reviews:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchReviews();
    }, []);


    const renderStars = (rating) => {
        const fullStars = Math.floor(rating); // Full stars
        const emptyStars = 5 - fullStars; // Empty stars

        // Create full stars with unique keys
        const fullStarElements = Array.from({ length: fullStars }, (_, index) => (
            <span key={`full-${index}`} className={styles.star}>★</span>
        ));

        // Create empty stars with unique keys
        const emptyStarElements = Array.from({ length: emptyStars }, (_, index) => (
            <span key={`empty-${index}`} className={styles.emptyStar}>★</span>
        ));

        return [...fullStarElements, ...emptyStarElements];
    };

    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();

            document.querySelector(this.getAttribute('href')).scrollIntoView({
                behavior: 'smooth'
            });
        });
    });

    return (
        <div className={styles.homePage}>
            <nav className={styles.navbar}>
                <div className={styles.navbarContainer}>
                    {/* Brand Name */}
                    <div className={styles.logo}>
                        <a href="/">Equinox</a>
                    </div>

                    {/* Navigation Links */}
                    <div className={styles.navLinks}>
                        <a href="#features" className={styles.navLink}>
                            Features
                        </a>
                        <a href="#about" className={styles.navLink}>
                            About
                        </a>
                        <a href="#contact" className={styles.navLink}>
                            Contact
                        </a>
                    </div>

                    {/* Buttons */}
                    <div className={styles.authButtons}>
                        <Link to="/login">
                            <button className={styles.loginButton}>Login</button>
                        </Link>
                        <Link to={"/signup"}>
                            <button className={styles.signupButton}>Sign Up</button>
                        </Link>
                    </div>
                </div>
            </nav>

            {/* Hero Section */}
            <section className={styles.hero}>
                <div className={styles.heroOverlay}></div>
                <div className={styles.heroContent}>
                    <h1>Unleash Your Full Potential</h1>
                    <p>
                        Simplify your fitness journey with easy-to-use tools for tracking, goal-setting, and progress
                        monitoring.
                    </p>
                    <Link to="/signup">
                    <button className={styles.ctaButton}>Start Your Journey</button>
                    </Link>
                </div>
            </section>

            {/* Features Section */}
            <section id="features" className={styles.features}>
                <h2>Why Choose Us?</h2>
                <div className={styles.featureGrid}>
                    {[
                        {
                            imgSrc: "track-icon.svg",
                            altText: "Track Workouts Icon",
                            title: "Track Your Workouts",
                            description: "Record your exercises, sets, and reps seamlessly and never lose track of your progress."
                        },
                        {
                            imgSrc: "goals-icon.svg",
                            altText: "Set Goals Icon",
                            title: "Set Goals",
                            description: "Personalize your goals and milestones to match your fitness journey."
                        },
                        {
                            imgSrc: "analyze-icon.svg",
                            altText: "Analyze Progress Icon",
                            title: "Analyze Your Progress",
                            description: "Visualize your growth and stay motivated with detailed insights and charts."
                        },
                        {
                            imgSrc: "library.svg",
                            altText: "Exercise Library Icon",
                            title: "Ever-expanding exercise library",
                            description: "So you never have to worry about not having access to machines if you don't know how to use them"
                        }
                    ].map((feature, index) => (
                        <div key={index} className={styles.featureCard}>
                            <img src={feature.imgSrc} alt={feature.altText} className={styles.featureIcon}/>
                            <h3>{feature.title}</h3>
                            <p>{feature.description}</p>
                        </div>
                    ))}
                </div>
            </section>


            {/* Top Reviews Section */}
            <section id="about" className={styles.reviewsSection}>
                <h2 className={styles.reviewsTitle}>User Reviews</h2>
                {loading ? (
                    <p className={styles.loadingText}>Loading reviews...</p>
                ) : (
                    <div className={styles.reviewsGrid}>
                        {reviews.map((review) => (
                            <div key={review.id} className={styles.reviewCard}>
                                <div className={styles.reviewHeader}>
                                    <h3 className={styles.reviewUsername}>{review.username}</h3>
                                    <div className={styles.starRating}>
                                        {renderStars(review.starRating)}
                                    </div>
                                </div>
                                <p className={styles.reviewMessage}>"{review.message}"</p>
                            </div>
                        ))}
                    </div>
                )}
            </section>


            <section id="contact" className={styles.contactSection}>
                <h2>Contact Us</h2>
                <p>If you have any questions or need support, feel free to reach out to us!</p>

                <form onSubmit={handleSubmit} className={styles.contactForm}>
                    <div className={styles.inputGroup}>
                        <label htmlFor="name">Your Name:</label>
                        <input
                            type="text"
                            id="name"
                            placeholder="Enter your name"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
                        />
                    </div>
                    <div className={styles.inputGroup}>
                        <label htmlFor="email">Your Email:</label>
                        <input
                            type="email"
                            id="email"
                            placeholder="Enter your email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>
                    <div className={styles.inputGroup}>
                        <label htmlFor="message">Your Message:</label>
                        <textarea
                            id="message"
                            placeholder="Enter your message"
                            value={message}
                            onChange={(e) => setMessage(e.target.value)}
                            required
                        />
                    </div>

                    {errorMessage && <p className={styles.error}>{errorMessage}</p>}
                    {successMessage && <p className={styles.success}>{successMessage}</p>}

                    <button type="submit" className={styles.submitBtn} disabled={isLoading}>
                        {isLoading ? 'Sending...' : 'Send Message'}
                    </button>
                </form>
            </section>
        </div>
    );
};

export default HomePage;