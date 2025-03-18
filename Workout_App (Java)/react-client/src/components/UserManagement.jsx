import React, { useState, useEffect } from "react";
import styles from "../styles/UserManagement.module.css"; // Import the CSS module

const UserManagement = () => {
    const [users, setUsers] = useState([]);
    const [newUser, setNewUser] = useState({
        id: null,
        username: "",
        email: "",
        password: "",
        dateOfBirth: "",
        goalIds: [],
        workoutIds: [],
        subExpirationDate: ""
    });
    const [editingUser, setEditingUser] = useState(null);

    // Fetch users function
    const fetchUsers = async () => {
        try {
            const response = await fetch("http://localhost:8080/workout_app/user");
            if (response.ok) {
                const data = await response.json();
                setUsers(data);
            } else {
                console.error("Failed to fetch users:", response.statusText);
            }
        } catch (error) {
            console.error("Error fetching users:", error);
        }
    };

    // Call fetchUsers once when the component is mounted
    useEffect(() => {
        fetchUsers();
    }, []);

    const handleCreateUser = async () => {
        try {
            const requestBody = {
                ...newUser,
                goalIds: newUser.goalIds.map(Number),
                workoutIds: newUser.workoutIds.map(Number),
            };
            const response = await fetch("http://localhost:8080/workout_app/user", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(requestBody),
            });
            if (response.ok) {
                fetchUsers(); // Fetch updated user list after creation
                setNewUser({
                    id: null,
                    username: "",
                    email: "",
                    password: "",
                    dateOfBirth: "",
                    goalIds: [],
                    workoutIds: [],
                    subExpirationDate: "",
                });
            } else {
                console.error("Error creating user:", response.statusText);
            }
        } catch (error) {
            console.error("Error creating user:", error);
        }
    };

    const handleUpdateUser = async () => {
        if (!editingUser) return;

        const updatedFields = {
            ...editingUser,
            goalIds: editingUser.goalIds.map(Number),
            workoutIds: editingUser.workoutIds.map(Number),
        };

        try {
            const response = await fetch(
                `http://localhost:8080/workout_app/user/${editingUser.id}`,
                {
                    method: "PATCH",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(updatedFields),
                }
            );
            if (response.ok) {
                setUsers((prevUsers) =>
                    prevUsers.map((user) =>
                        user.id === editingUser.id ? { ...user, ...updatedFields } : user
                    )
                );
                setEditingUser(null);
            } else {
                console.error("Error updating user");
            }
        } catch (error) {
            console.error("Error updating user:", error);
        }
    };

    const handleDeleteUser = async (id) => {
        try {
            const response = await fetch(
                `http://localhost:8080/workout_app/user/${id}`,
                {
                    method: "DELETE",
                }
            );
            if (response.ok) {
                setUsers((prevUsers) => prevUsers.filter((user) => user.id !== id));
            } else {
                console.error("Error deleting user");
            }
        } catch (error) {
            console.error("Error deleting user:", error);
        }
    };

    const handleNewUserChange = (e) => {
        const { name, value } = e.target;
        setNewUser((prevState) => ({
            ...prevState,
            [name]: name === "goalIds" || name === "workoutIds"
                ? value.split(",").map((v) => v.trim())
                : value,
        }));
    };

    const handleEditUserChange = (e) => {
        const { name, value } = e.target;
        setEditingUser((prevState) => ({
            ...prevState,
            [name]: name === "goalIds" || name === "workoutIds"
                ? value.split(",").map((v) => v.trim())
                : value,
        }));
    };

    return (
        <div className={styles.container}>
            {/* Left Section for Forms */}
            <div className={styles.formSection}>
                <h1 className={styles.header}>User Management</h1>

                {/* Create User Form */}
                <h2>Create New User</h2>
                <div className={styles.formGroup}>
                    <label htmlFor="username" className={styles.label}>
                        Username:
                    </label>
                    <input
                        className={`${styles.input} ${styles.username}`}
                        type="text"
                        name="username"
                        value={newUser.username}
                        onChange={handleNewUserChange}
                        placeholder="Username"
                    />
                    <label htmlFor="email" className={styles.label}>
                        Email:
                    </label>
                    <input
                        className={styles.input}
                        type="email"
                        name="email"
                        value={newUser.email}
                        onChange={handleNewUserChange}
                        placeholder="Email"
                    />
                    <label htmlFor="password" className={styles.label}>
                        Password:
                    </label>
                    <input
                        className={styles.input}
                        type="password"
                        name="password"
                        value={newUser.password}
                        onChange={handleNewUserChange}
                        placeholder="Password"
                    />
                    <label htmlFor="dateOfBirth" className={styles.label}>
                        Date Of Birth:
                    </label>
                    <input
                        className={styles.input}
                        type="date"
                        name="dateOfBirth"
                        value={newUser.dateOfBirth}
                        onChange={handleNewUserChange}
                        placeholder="Date of Birth"
                    />
                    <label htmlFor="subExpirationDate" className={styles.label}>
                        Subscription Expiration Date:
                    </label>
                    <input
                        className={styles.input}
                        type="date"
                        name="subExpirationDate"
                        value={newUser.subExpirationDate}
                        onChange={handleNewUserChange}
                        placeholder="Subscription Expiration Date"
                    />
                    <label htmlFor="goalIds" className={styles.label}>
                        Goal IDs:
                    </label>
                    <input
                        className={`${styles.input} ${styles.goalIds}`}
                        type="text"
                        name="goalIds"
                        value={newUser.goalIds.join(", ")}
                        onChange={handleNewUserChange}
                        placeholder="Goal IDs (comma-separated)"
                    />
                    <label htmlFor="workoutIds" className={styles.label}>
                        Workout IDs:
                    </label>
                    <input
                        className={`${styles.input} ${styles.workoutIds}`}
                        type="text"
                        name="workoutIds"
                        value={newUser.workoutIds.join(", ")}
                        onChange={handleNewUserChange}
                        placeholder="Workout IDs (comma-separated)"
                    />
                    <button className={styles.button} onClick={handleCreateUser}>Create User</button>
                </div>

                {/* Edit User Form */}
                {editingUser && (
                    <>
                        <h2>Edit User</h2>
                        <div className={styles.formGroup}>
                            <label htmlFor="username" className={styles.label}>
                                Username:
                            </label>
                            <input
                                className={styles.input}
                                type="text"
                                name="username"
                                value={editingUser.username}
                                onChange={handleEditUserChange}
                                placeholder="Username"
                            />
                            <label htmlFor="email" className={styles.label}>
                                Email:
                            </label>
                            <input
                                className={styles.input}
                                type="email"
                                name="email"
                                value={editingUser.email}
                                onChange={handleEditUserChange}
                                placeholder="Email"
                            />
                            <label htmlFor="password" className={styles.label}>
                                Password:
                            </label>
                            <input
                                className={styles.input}
                                type="password"
                                name="password"
                                value={editingUser.password}
                                onChange={handleEditUserChange}
                                placeholder="Password"
                            />
                            <label htmlFor="dateOfBirth" className={styles.label}>
                                Date Of Birth:
                            </label>
                            <input
                                className={styles.input}
                                type="date"
                                name="dateOfBirth"
                                value={editingUser.dateOfBirth}
                                onChange={handleEditUserChange}
                                placeholder="Date of Birth"
                            />
                            <label htmlFor="subExpirationDate" className={styles.label}>
                                Subscription Expiration Date:
                            </label>
                            <input
                                className={styles.input}
                                type="date"
                                name="subExpirationDate"
                                value={editingUser.subExpirationDate}
                                onChange={handleEditUserChange}
                                placeholder="Subscription Expiration Date"
                            />
                            <label htmlFor="goalIds" className={styles.label}>
                                Goal IDs:
                            </label>
                            <input
                                className={styles.input}
                                type="text"
                                name="goalIds"
                                value={editingUser.goalIds.join(", ")}
                                onChange={handleEditUserChange}
                                placeholder="Goal IDs (comma-separated)"
                            />
                            <label htmlFor="dateOfBirth" className={styles.label}>
                                Workout IDs:
                            </label>
                            <input
                                className={styles.input}
                                type="text"
                                name="workoutIds"
                                value={editingUser.workoutIds.join(", ")}
                                onChange={handleEditUserChange}
                                placeholder="Workout IDs (comma-separated)"
                            />
                            <button className={styles.button} onClick={handleUpdateUser}>Update User</button>
                        </div>
                    </>
                )}
            </div>

            {/* Right Section for Table */}
            <div className={styles.tableSection}>
                <h2>Users Table</h2>
                <table className={styles.table}>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>DOB</th>
                        <th>Exp. Date</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {users.length > 0 ? (
                        users.map((user) => (
                            <tr key={user.id}>
                                <td>{user.id}</td>
                                <td>{user.username}</td>
                                <td>{user.email}</td>
                                <td>{user.dateOfBirth}</td>
                                <td>{user.subExpirationDate}</td>
                                <td>
                                    <button className={styles.editButton} onClick={() => setEditingUser(user)}>Edit
                                    </button>
                                    <button className={styles.deleteButton}
                                            onClick={() => handleDeleteUser(user.id)}>Delete
                                    </button>
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="7">No users available</td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default UserManagement;
