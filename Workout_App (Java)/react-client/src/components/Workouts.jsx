import React, {useState, useEffect} from "react";
import styles from '../styles/Workouts.module.css';

const Workouts = () => {
    const [workouts, setWorkouts] = useState([]);
    const [expandedWorkout, setExpandedWorkout] = useState(null);
    const [exerciseTypes, setExerciseTypes] = useState([]); // State for available exercise types
    const [exerciseDetails, setExerciseDetails] = useState({});
    const [username, setUsername] = useState('');
    const [newExercise, setNewExercise] = useState({
        id: 0,
        sets: '',
        reps: '',
        weight: '',
        completed: false,
        exerciseType: {name: "", category: "", description: ""},
    });
    const [notesDrafts, setNotesDrafts] = useState({}); // Local drafts for notes

    const getDayName = (dateString) => {
        const daysOfWeek = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
        const date = new Date(dateString); // Parse the date string
        return daysOfWeek[date.getDay()]; // Get the day name
    };

    useEffect(() => {
        const userId = localStorage.getItem("userId");
        if (userId) {
            handleGetUsername(userId);
        }
        const fetchExerciseTypes = async () => {
            try {
                const response = await fetch("http://localhost:8080/workout_app/exercise_type", {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    credentials: "include", // Include credentials for authentication
                });
                if (response.ok) {
                    const data = await response.json();
                    setExerciseTypes(data); // Set the exercise types
                } else {
                    console.error("Failed to fetch exercise types:", response.statusText);
                }
            } catch (error) {
                console.error("Error fetching exercise types:", error);
            }
        };

        fetchExerciseTypes();
    }, []);

    // Fetch workouts for the logged-in user
    useEffect(() => {
        const fetchWorkouts = async () => {
            const userId = localStorage.getItem('userId'); // Get the userId from localStorage (or use AuthContext)
            if (!userId) {
                console.error("User not logged in");
                return;
            }

            try {
                const response = await fetch(`http://localhost:8080/workout_app/user/${userId}/workouts`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    credentials: "include", // Include credentials for authentication (if needed)
                });
                if (response.ok) {
                    const data = await response.json();
                    setWorkouts(data);
                } else {
                    console.error("Failed to fetch workouts:", response.statusText);
                }
            } catch (error) {
                console.error("Error fetching workouts:", error);
            }
        };

        fetchWorkouts();
    }, []);

    const fetchExerciseDetails = async (exerciseIds) => {
        if (!exerciseIds || exerciseIds.length === 0) return [];

        try {
            const responses = await Promise.all(
                exerciseIds.map((id) =>
                    fetch(`http://localhost:8080/workout_app/exercise/${id}`, {
                        method: "GET",
                        headers: {
                            "Content-Type": "application/json",
                        },
                        credentials: "include",
                    }).then((res) => (res.ok ? res.json() : null))
                )
            );
            const validResponses = responses.filter((exercise) => exercise !== null);

            // Fetch exercise type details for each exercise
            const detailedExercises = await Promise.all(
                validResponses.map(async (exercise) => {
                    if (exercise.exerciseType_id) {
                        const exerciseTypeResponse = await fetch(
                            `http://localhost:8080/workout_app/exercise_type/${exercise.exerciseType_id}`,
                            {
                                method: "GET",
                                headers: {
                                    "Content-Type": "application/json",
                                },
                                credentials: "include",
                            }
                        );
                        const exerciseType = exerciseTypeResponse.ok ? await exerciseTypeResponse.json() : null;
                        return {
                            ...exercise,
                            exerciseType: exerciseType || {name: "Unknown"}, // Include type details
                        };
                    }
                    return {...exercise, exerciseType: {name: "Unknown"}};
                })
            );

            return detailedExercises;
        } catch (error) {
            console.error("Error fetching exercise details:", error);
            return [];
        }
    };


    // Toggle workout expansion and fetch exercise details if needed
    const toggleWorkoutExpansion = async (workoutId, exerciseIds) => {
        if (expandedWorkout === workoutId) {
            setExpandedWorkout(null);
        } else {
            if (!exerciseDetails[workoutId]) {
                const exercises = await fetchExerciseDetails(exerciseIds);
                setExerciseDetails((prev) => ({...prev, [workoutId]: exercises}));
            }
            setExpandedWorkout(workoutId);
        }
    };

    // Handle notes changes with debounce
    const handleNotesChange = (workoutId, value) => {
        setNotesDrafts((prev) => ({...prev, [workoutId]: value}));

        // Debounce save
        if (handleNotesChange.timer) {
            clearTimeout(handleNotesChange.timer);
        }

        handleNotesChange.timer = setTimeout(() => {
            saveNotes(workoutId, value);
        }, 1000); // Save after 1 second of inactivity
    };

    const saveNotes = async (workoutId, notes) => {
        try {
            const response = await fetch(`http://localhost:8080/workout_app/workout/${workoutId}`, {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include", // Include cookies for authentication
                body: JSON.stringify({
                    notes // Only send the notes field
                }),
            });

            if (!response.ok) {
                console.error("Failed to save notes:", response.statusText);
            } else {
                setWorkouts((prevWorkouts) =>
                    prevWorkouts.map((workout) =>
                        workout.id === workoutId ? {...workout, notes} : workout
                    )
                );
            }
        } catch (error) {
            console.error("Error saving notes:", error);
        }
    };

    const handleAddExercise = async (workoutId) => {
        try {
            const exerciseTypeResponse = await fetch('http://localhost:8080/workout_app/exercise_type', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
                body: JSON.stringify(newExercise.exerciseType),
            });

            if (!exerciseTypeResponse.ok) {
                throw new Error('Failed to create ExerciseType');
            }

            const exerciseTypeId = await exerciseTypeResponse.json();

            const exerciseData = {
                exerciseType_id: exerciseTypeId,
                sets: newExercise.sets,
                reps: newExercise.reps,
                weight: newExercise.weight,
                isCompleted: newExercise.completed
            };

            // Step 1: Add the new exercise to the backend
            const addExerciseResponse = await fetch(`http://localhost:8080/workout_app/workout/${workoutId}/exercise`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
                body: JSON.stringify(exerciseData),
            });

            const exerciseId = await addExerciseResponse.text();  // Get the exercise ID as text
            const newExerciseId = parseInt(exerciseId, 10);     // Convert the ID to an integer


            if (addExerciseResponse.ok) {
                // Step 2: Update the exercise details in the local state

                handlePercentageUpdate(workoutId);

                const newExerciseData = {
                    id: exerciseId, // Use the exercise ID returned from the response if available
                    ...exerciseData, // Include other exercise details
                    exerciseType: newExercise.exerciseType,
                };

                // Add the new exercise to the current workout's exercise list
                setExerciseDetails((prev) => {
                    const updatedExercises = prev[workoutId] ? [...prev[workoutId], newExerciseData] : [newExerciseData];
                    return {...prev, [workoutId]: updatedExercises};
                });


                // Step 3: Reset the new exercise form
                setNewExercise({
                    sets: '',
                    reps: '',
                    weight: '',
                    completed: false,
                    exerciseType: {name: '', category: '', description: ''},
                });

            } else {
                console.error('Failed to add exercise:', addExerciseResponse.statusText);
            }
        } catch (error) {
            console.error('Error creating or adding exercise:', error);
        }
    };

    const handleAddWorkout = async () => {
        const currentDate = new Date().toISOString().split("T")[0]; // Get today's date in YYYY-MM-DD format
        const userId = localStorage.getItem('userId'); // Fetch the logged-in user's ID

        if (!userId) {
            console.error("User not logged in");
            return;
        }

        try {
            // Step 1: Create a new workout
            const workoutResponse = await fetch("http://localhost:8080/workout_app/workout", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include",
                body: JSON.stringify({date: currentDate, notes: "", completionPercentage: 0}), // Empty workout details
            });

            if (!workoutResponse.ok) {
                console.error("Failed to create workout:", workoutResponse.statusText);
                return;
            }

            // Step 2: Get the workout ID (which is just a plain integer)
            const workoutId = await workoutResponse.text();  // Get the workout ID as text
            const newWorkoutId = parseInt(workoutId, 10);     // Convert the ID to an integer

            if (!newWorkoutId) {
                console.error("Invalid workout ID returned:", workoutId);
                return;
            }

            // Step 3: Associate the workout with the user
            const associateResponse = await fetch(
                `http://localhost:8080/workout_app/user/${userId}/workout/${newWorkoutId}`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    credentials: "include",
                }
            );

            if (!associateResponse.ok) {
                console.error("Failed to associate workout with user:", associateResponse.statusText);
                return;
            }

            // Step 4: Update the frontend state
            setWorkouts((prev) => [
                ...prev,
                {id: newWorkoutId, date: currentDate, notes: ""},
            ]);
        } catch (error) {
            console.error("Error creanting and associating workout:", error);
        }
    };


    const handleExerciseUpdate = async (workoutId, exerciseId, field, value) => {
        const updatedExercises = exerciseDetails[workoutId].map((exercise) =>
            exercise.id === exerciseId ? {...exercise, [field]: value} : exercise
        );

        setExerciseDetails((prev) => ({
            ...prev,
            [workoutId]: updatedExercises
        }));

        // Optionally, you can send the updated exercise to the backend
        try {
            const response = await fetch(`http://localhost:8080/workout_app/exercise/${exerciseId}`, {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include",
                body: JSON.stringify({[field]: value})
            });

            if (!response.ok) {
                console.error("Failed to update exercise:", response.statusText);
            }

            handlePercentageUpdate(workoutId);

        } catch (error) {
            console.error("Error updating exercise:", error);
        }
    };

    const handleDeleteExercise = (workoutId, exerciseId) => {
        // Send the DELETE request to the backend
        fetch(`http://localhost:8080/workout_app/workout/${workoutId}/exercise/${exerciseId}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (response.ok) {
                    handlePercentageUpdate(workoutId);
                    // Update local state to remove the exercise
                    setExerciseDetails((prevDetails) => ({
                        ...prevDetails,
                        [workoutId]: prevDetails[workoutId].filter(exercise => exercise.id !== exerciseId)
                    }));
                    console.log('Exercise removed successfully');
                } else {
                    console.error('Failed to remove exercise');
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    };

    const handlePercentageUpdate = (workoutId) => {
        fetch(`http://localhost:8080/workout_app/exercise/updatePercentage/${workoutId}`, {
            method: 'PATCH',
        })
            .then(response => {
                if (response.ok) {
                    // Fetch the updated workout data to get the new percentage
                    return fetch(`http://localhost:8080/workout_app/workout/${workoutId}`, {
                        method: 'GET',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                    });
                } else {
                    throw new Error("Percentage NOT updated");
                }
            })
            .then(response => response.json())
            .then(updatedWorkout => {
                // Update the workout state locally with the new percentage
                setWorkouts((prevWorkouts) =>
                    prevWorkouts.map(workout =>
                        workout.id === updatedWorkout.id
                            ? {...workout, completionPercentage: updatedWorkout.completionPercentage}
                            : workout
                    )
                );
                console.log("Percentage updated and frontend state refreshed");
            })
            .catch(error => console.error("Error updating percentage:", error));
    };

    // Function to handle deleting a workout
    const handleDeleteWorkout = (workoutId) => {
        // Send the DELETE request to the backend
        fetch(`http://localhost:8080/workout_app/workout/${workoutId}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (response.ok) {
                    // Update the local state by removing the workout
                    setWorkouts((prevWorkouts) => prevWorkouts.filter(workout => workout.id !== workoutId));
                    console.log('Workout removed successfully');
                } else {
                    console.error('Failed to remove workout');
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    };

    const handleGetUsername = (userId) => {
        fetch(`http://localhost:8080/workout_app/user/${userId}`, {
            method: 'GET',
        }).then(response => {
            if (response.ok) {
                return response.json(); // Parsing the response as JSON
            } else {
                throw new Error("Failed to fetch username");
            }
        }).then(data => {
            setUsername(data.username); // Assuming 'username' is a field in UserDTO
        }).catch(error => {
            console.error("Error fetching username:", error);
        });
    };

    return (
        <div className={styles['workouts']}>
            <div className={styles["workouts-container"]}>
                <h1>Hello, {username} ! Here are your Workouts:</h1>
                <button onClick={handleAddWorkout} className={styles['add-workout-button']}>
                    Add New Workout
                </button>

                <div className={styles['workoutsBg']}>
                    {workouts.length > 0 ? (
                        workouts.map((workout) => (
                            <div key={workout.id} className={styles['workout-container']}>
                                <div
                                    className={styles['workout-header']}
                                    onClick={() =>
                                        toggleWorkoutExpansion(workout.id, workout.exerciseIds || [])
                                    }
                                >
                                    <h2>
                                        Workout on {workout.date} - {getDayName(workout.date)}{" "}
                                        ({workout.completionPercentage}% completed)
                                        <button
                                            onClick={() => handleDeleteWorkout(workout.id)}
                                            className={styles['delete-workout-button']}
                                        >
                                            Delete Workout
                                        </button>
                                    </h2>
                                    {/* Delete Workout Button */}

                                </div>

                                {expandedWorkout === workout.id && (
                                    <div className={styles['workout-details']}>
                                        <h3>Notes</h3>
                                        <textarea
                                            className={styles['notes-textarea']}
                                            value={notesDrafts[workout.id] || workout.notes || ""}
                                            onChange={(e) =>
                                                handleNotesChange(workout.id, e.target.value)
                                            }
                                            placeholder="Write your workout notes here..."
                                        />

                                        <h3>Exercises</h3>
                                        {exerciseDetails[workout.id] && exerciseDetails[workout.id].length > 0 ? (
                                            <ul className={styles['exercise-list']}>
                                                {exerciseDetails[workout.id].map((exercise, index) => (
                                                    <li
                                                        key={exercise.id ? `exercise-${exercise.id}` : `workout-${workout.id}-exercise-${index}`}
                                                        className={styles['exercise-item']}
                                                    >
                                                        <strong>Type:</strong> {exercise.exerciseType ? exercise.exerciseType.name : "Unknown"},
                                                        <strong>Sets:</strong> {exercise.sets},
                                                        <strong>Reps:</strong> {exercise.reps},
                                                        <strong>Weight:</strong> {exercise.weight}kg,
                                                        <strong>Completed:</strong>
                                                        <input
                                                            type="checkbox"
                                                            checked={exercise.completed || false}
                                                            onChange={(e) => handleExerciseUpdate(workout.id, exercise.id, "completed", e.target.checked)}
                                                        />

                                                        {/* Delete Button for Exercise */}
                                                        <button
                                                            onClick={() => handleDeleteExercise(workout.id, exercise.id)}
                                                            className={styles['delete-exercise-button']}
                                                        >
                                                            Delete
                                                        </button>
                                                    </li>
                                                ))}
                                            </ul>
                                        ) : (
                                            <p className={styles['no-exercises-message']}>No exercises available for
                                                this workout.</p>
                                        )}

                                        <h4>Add Exercise</h4>

                                        {/* Dropdown to choose exercise type */}
                                        <select
                                            className={styles['exercise-input']}
                                            value={newExercise.exerciseType.id || ""}
                                            onChange={(e) => {
                                                const selectedType = exerciseTypes.find(
                                                    (type) => type.id === parseInt(e.target.value)
                                                );
                                                setNewExercise((prev) => ({
                                                    ...prev,
                                                    exerciseType: selectedType || {},
                                                }));
                                            }}
                                        >
                                            <option value="">Select Exercise Type</option>
                                            {exerciseTypes.map((type) => (
                                                <option key={type.id} value={type.id}>
                                                    {type.name}
                                                </option>
                                            ))}
                                        </select>

                                        {/* Option to add custom exercise type */}
                                        <button
                                            onClick={() => setNewExercise((prev) => ({
                                                ...prev,
                                                isCustom: true,
                                            }))}
                                            className={styles['add-custom-exercise-button']}
                                        >
                                            Add Custom Exercise Type
                                        </button>

                                        {newExercise.isCustom && (
                                            <div>
                                                <input
                                                    type="text"
                                                    placeholder="Custom Exercise Name"
                                                    className={styles['exercise-input']}
                                                    value={newExercise.exerciseType.name}
                                                    onChange={(e) =>
                                                        setNewExercise((prev) => ({
                                                            ...prev,
                                                            exerciseType: {...prev.exerciseType, name: e.target.value},
                                                        }))
                                                    }
                                                />
                                                <input
                                                    type="text"
                                                    placeholder="Category"
                                                    className={styles['exercise-input']}
                                                    value={newExercise.exerciseType.category}
                                                    onChange={(e) =>
                                                        setNewExercise((prev) => ({
                                                            ...prev,
                                                            exerciseType: {
                                                                ...prev.exerciseType,
                                                                category: e.target.value
                                                            },
                                                        }))
                                                    }
                                                />
                                                <input
                                                    type="text"
                                                    placeholder="Description"
                                                    className={styles['exercise-input']}
                                                    value={newExercise.exerciseType.description}
                                                    onChange={(e) =>
                                                        setNewExercise((prev) => ({
                                                            ...prev,
                                                            exerciseType: {
                                                                ...prev.exerciseType,
                                                                description: e.target.value
                                                            },
                                                        }))
                                                    }
                                                />
                                            </div>
                                        )}

                                        {/* Other exercise details */}
                                        <input
                                            type="number"
                                            placeholder="Sets"
                                            className={styles['exercise-input']}
                                            value={newExercise.sets}
                                            onChange={(e) =>
                                                setNewExercise((prev) => ({
                                                    ...prev,
                                                    sets: e.target.value,
                                                }))
                                            }
                                        />
                                        <input
                                            type="number"
                                            placeholder="Reps"
                                            className={styles['exercise-input']}
                                            value={newExercise.reps}
                                            onChange={(e) =>
                                                setNewExercise((prev) => ({
                                                    ...prev,
                                                    reps: e.target.value,
                                                }))
                                            }
                                        />
                                        <input
                                            type="number"
                                            placeholder="Weight"
                                            className={styles['exercise-input']}
                                            value={newExercise.weight}
                                            onChange={(e) =>
                                                setNewExercise((prev) => ({
                                                    ...prev,
                                                    weight: e.target.value,
                                                }))
                                            }
                                        />
                                        <button
                                            onClick={() => handleAddExercise(workout.id)}
                                            className={styles['add-exercise-button']}
                                        >
                                            Add Exercise
                                        </button>
                                    </div>
                                )}
                            </div>
                        ))
                    ) : (
                        <p className={styles['no-workouts-message']}>No workouts available.</p>
                    )}

                </div>
            </div>
        </div>
    );

}
export default Workouts;
