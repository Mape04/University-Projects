import React, { useState, useEffect } from "react";
import styles from "../styles/ExerciseLibrary.module.css";

const ExerciseLibrary = () => {
    const [exercises, setExercises] = useState([]); // All exercises
    const [filteredExercises, setFilteredExercises] = useState([]); // Filtered exercises
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [searchQuery, setSearchQuery] = useState(""); // Search query state
    const [selectedCategory, setSelectedCategory] = useState(""); // Selected category state

    // Helper function to convert YouTube links to embed format
    const convertToEmbedURL = (link) => {
        if (!link) return null;
        const url = new URL(link);
        if (url.hostname === "www.youtube.com" && url.searchParams.has("v")) {
            return `https://www.youtube.com/embed/${url.searchParams.get("v")}`;
        }
        if (url.hostname === "youtu.be") {
            return `https://www.youtube.com/embed/${url.pathname.slice(1)}`;
        }
        return null;
    };

    // Fetch exercises from the API
    const handleGetAllExercises = async () => {
        try {
            const response = await fetch("http://localhost:8080/workout_app/exercise_type");
            if (!response.ok) {
                throw new Error("Failed to fetch exercise types");
            }
            const data = await response.json();
            setExercises(data);
            setFilteredExercises(data); // Initialize filtered exercises
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    // Filter exercises based on search query and selected category
    const filterExercises = () => {
        let filtered = exercises;

        // Filter by search query
        if (searchQuery.trim() !== "") {
            filtered = filtered.filter((exercise) =>
                exercise.name.toLowerCase().includes(searchQuery.toLowerCase())
            );
        }

        // Filter by selected category
        if (selectedCategory) {
            filtered = filtered.filter((exercise) => exercise.category === selectedCategory);
        }

        setFilteredExercises(filtered);
    };

    // Update filtered exercises when search query or selected category changes
    useEffect(() => {
        filterExercises();
    }, [searchQuery, selectedCategory]);

    useEffect(() => {
        handleGetAllExercises();
    }, []);

    if (loading) {
        return <div className={styles.loading}>Loading...</div>;
    }

    if (error) {
        return <div className={styles.error}>Error: {error}</div>;
    }

    // Extract unique categories for the dropdown
    const categories = [...new Set(exercises.map((exercise) => exercise.category))];

    return (
        <div className={styles.exerciseLibrary}>
            <h1 className={styles.title}>Exercise Library</h1>

            {/* Search Bar */}
            <input
                type="text"
                placeholder="Search exercises..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className={styles.searchBar}
            />

            {/* Dropdown Filter */}
            Filter:
            <select
                value={selectedCategory}
                onChange={(e) => setSelectedCategory(e.target.value)}
                className={styles.dropdown}
            >
                <option value="">All Categories</option>
                {categories.map((category) => (
                    <option key={category} value={category}>
                        {category}
                    </option>
                ))}
            </select>

            {/* Exercises */}
            <div className={styles.exercisesContainer}>
                {filteredExercises.map((exercise) => {
                    const embedURL = convertToEmbedURL(exercise.link);
                    return (
                        <div key={exercise.id} className={styles.exerciseCard}>
                            <h2 className={styles.exerciseName}>{exercise.name}</h2>
                            <p className={styles.exerciseCategory}>
                                <strong>Category:</strong> {exercise.category}
                            </p>
                            <p className={styles.exerciseDescription}>
                                {exercise.description}
                            </p>
                            {embedURL && (
                                <div className={styles.videoContainer}>
                                    <iframe
                                        src={embedURL}
                                        title={exercise.name}
                                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                                        allowFullScreen
                                    ></iframe>
                                </div>
                            )}
                        </div>
                    );
                })}
            </div>
        </div>
    );
};

export default ExerciseLibrary;
