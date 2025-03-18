import React, { useState, useEffect } from 'react';
import { Pie } from 'react-chartjs-2';
import {
    Chart as ChartJS,
    ArcElement,
    Tooltip,
    Legend,
} from 'chart.js';

ChartJS.register(ArcElement, Tooltip, Legend);
import styles from '../styles/Goals.module.css';

const Goals = () => {
    const [goals, setGoals] = useState([]);
    const [statistics, setStatistics] = useState(null); // State for user statistics
    const [newGoal, setNewGoal] = useState({
        description: '',
        deadline: '',
        isAchieved: false,
        creationDate: '',
        appUserIds: [],
    });
    const [isEditing, setIsEditing] = useState(false);
    const [editingGoal, setEditingGoal] = useState(null);

    // Fetch goals for the logged-in user
    useEffect(() => {
        const userId = localStorage.getItem("userId"); // Replace with AuthContext if available
        if (!userId) {
            console.error("User not logged in");
            return;
        }

        const fetchGoals = async () => {
            const userId = localStorage.getItem("userId"); // Replace with AuthContext if available
            if (!userId) {
                console.error("User not logged in");
                return;
            }

            try {
                const response = await fetch(`http://localhost:8080/workout_app/user/${userId}/goals`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    credentials: "include",
                });

                if (response.ok) {
                    const data = await response.json();
                    setGoals(data);
                } else {
                    console.error("Failed to fetch goals:", response.statusText);
                }
            } catch (error) {
                console.error("Error fetching goals:", error);
            }
        };

        const fetchStatistics = async () => {
            try {
                const response = await fetch(`http://localhost:8080/workout_app/workout/statistics/${userId}`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                    },
                });

                if (response.ok) {
                    const textData = await response.text(); // Fetch plain text response

                    // Parse the plain text into a structured object
                    const parsedStatistics = parseStatistics(textData);

                    setStatistics(parsedStatistics);
                } else {
                    console.error("Failed to fetch statistics:", response.statusText);
                }
            } catch (error) {
                console.error("Error fetching statistics:", error);
            }
        };

        fetchGoals();
        fetchStatistics();
    }, []);

    const parseStatistics = (textData) => {
        const lines = textData.split("\n").map((line) => line.trim());

        const statistics = {
            userId: lines[0].match(/\d+/)[0], // Extract User ID from the first line
            totalWorkouts: parseInt(lines[1].match(/\d+/)[0], 10),
            completedWorkouts: parseInt(lines[2].match(/\d+/)[0], 10),
            hypertrophyWorkouts: parseInt(lines[3].match(/\d+/)[0], 10),
            strengthWorkouts: parseInt(lines[4].match(/\d+/)[0], 10),
            generalWorkouts: parseInt(lines[5].match(/\d+/)[0], 10),
        };

        return statistics;
    };


    // Handle input changes for creating or editing goals
    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setNewGoal((prevGoal) => ({
            ...prevGoal,
            [name]: value,
        }));
    };

    // Handle creating a new goal
    const handleCreateGoal = async () => {
        const currentDate = new Date().toISOString().split("T")[0]; // Get today's date in YYYY-MM-DD format
        const userId = localStorage.getItem('userId'); // Fetch the logged-in user's ID

        if (!userId) {
            console.error("User not logged in");
            return;
        }

        try {
            // Step 1: Create a new goal
            const goalResponse = await fetch("http://localhost:8080/workout_app/goal", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    description: newGoal.description,
                    deadline: newGoal.deadline,
                    isAchieved: newGoal.isAchieved,
                    creationDate: currentDate,
                    appUserIds: [], // Empty initially, will add user later
                }),
            });

            if (!goalResponse.ok) {
                console.error("Failed to create goal:", goalResponse.statusText);
                return;
            }

            // Step 2: Get the goal ID (which is just a plain integer)
            const goalId = await goalResponse.text();  // Get the goal ID as text
            const newGoalId = parseInt(goalId, 10);    // Convert the ID to an integer

            if (!newGoalId) {
                console.error("Invalid goal ID returned:", goalId);
                return;
            }

            // Step 3: Associate the goal with the user
            const associateResponse = await fetch(
                `http://localhost:8080/workout_app/user/${userId}/goal/${newGoalId}`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    credentials: "include",
                }
            );

            if (!associateResponse.ok) {
                console.error("Failed to associate goal with user:", associateResponse.statusText);
                return;
            }

            // Step 4: Update the frontend state
            setGoals((prevGoals) => [
                ...prevGoals,
                {
                    id: newGoalId,
                    description: newGoal.description,
                    deadline: newGoal.deadline,
                    isAchieved: newGoal.isAchieved,
                    creationDate: currentDate
                },
            ]);
        } catch (error) {
            console.error("Error creating and associating goal:", error);
        }
    };


    // Handle deleting a goal
    const handleDeleteGoal = (goalId) => {
        // Send the DELETE request to the backend
        fetch(`http://localhost:8080/workout_app/goal/${goalId}`, {
            method: 'DELETE',
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then(response => {
                if (response.ok) {
                    // Update the local state by removing the goal
                    setGoals((prevGoals) => prevGoals.filter(goal => goal.id !== goalId));
                    console.log('Goal removed successfully');
                } else {
                    console.error('Failed to remove goal');
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    };

    // Handle marking a goal as complete
    const handleCompleteGoal = async (goalId) => {
        try {
            const response = await fetch(`http://localhost:8080/workout_app/goal/complete/${goalId}`, {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json",
                },
            });

            if (response.ok) {
                // Update the goal's status locally
                setGoals((prevGoals) =>
                    prevGoals.map((goal) =>
                        goal.id === goalId ? { ...goal, isAchieved: true } : goal
                    )
                );
                console.log("Goal marked as complete");
            } else {
                console.error("Failed to mark goal as complete:", response.statusText);
            }
        } catch (error) {
            console.error("Error completing goal:", error);
        }
    };

    const chartData = statistics
        ? {
            labels: ["Hypertrophy", "Strength", "General"],
            datasets: [
                {
                    label: "Workout Distribution",
                    data: [
                        statistics.hypertrophyWorkouts,
                        statistics.strengthWorkouts,
                        statistics.generalWorkouts,
                    ],
                    backgroundColor: ["#FF6384", "#36A2EB", "#FFCE56"],
                    hoverBackgroundColor: ["#FF6384AA", "#36A2EBAA", "#FFCE56AA"],
                },
            ],
        }
        : null;

    return (
        <div className={styles.container}>
            <div className={styles.goalsContainer}>
                <h1>Your Goals</h1>

                {/* Goal Creation Form */}
                <div className={styles.goalForm}>
                    <h3>Create New Goal</h3>
                    <input
                        type="text"
                        name="description"
                        value={newGoal.description}
                        onChange={handleInputChange}
                        placeholder="Description"
                    />
                    Deadline:
                    <input
                        type="date"
                        name="deadline"
                        value={newGoal.deadline}
                        onChange={handleInputChange}
                        placeholder="Deadline"
                    />
                    <label>
                        Achieved:
                        <input
                            type="checkbox"
                            name="isAchieved"
                            checked={newGoal.isAchieved}
                            onChange={(e) => setNewGoal((prevGoal) => ({...prevGoal, isAchieved: e.target.checked}))}
                        />
                    </label>
                    <button onClick={handleCreateGoal}>Create Goal</button>
                </div>

                {/* Display Goals */}
                <div>
                    <h3>All Goals</h3>
                    {goals.length > 0 ? (
                        <div className={styles.goalCardsContainer}>
                            {goals.map((goal) => (
                                <div key={goal.id} className={styles.goalCard}>
                                    <h4>{goal.description}</h4>
                                    <p><strong>Deadline:</strong> {goal.deadline}</p>
                                    <p><strong>Achieved:</strong> {goal.isAchieved ? "Yes" : "No"}</p>
                                    <p><strong>Creation Date:</strong> {goal.creationDate}</p>

                                    {/* Buttons */}
                                    <div className={styles.goalButtons}>
                                        {!goal.isAchieved && (
                                            <button onClick={() => handleCompleteGoal(goal.id)}>Complete Goal</button>
                                        )}
                                        <button onClick={() => handleDeleteGoal(goal.id)}>Delete</button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <p>No goals available.</p>
                    )}
                </div>
            </div>

            {/* Display Statistics */}
            <div className={styles.statisticsContainer}>
                <h1>Workout Statistics</h1>

                {statistics ? (
                    <>
                        <p><strong>Total Workouts:</strong> {statistics.totalWorkouts}</p>
                        <p><strong>Completed Workouts:</strong> {statistics.completedWorkouts}</p>

                        <Pie data={chartData} width={10} height={10}/>
                    </>
                ) : (
                    <p>Loading statistics...</p>
                )}
            </div>
        </div>



    );
};
export default Goals;