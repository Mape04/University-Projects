import React, { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from './App';
import styles from "../styles/Auth.module.css"; // Import the CSS module

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const { setIsAuthenticated , setIsAdmin} = useContext(AuthContext);
    const navigate = useNavigate();

    const handleLogout = (e) => {
        e.preventDefault();
        // Remove token from localStorage
        localStorage.removeItem("userToken");

        // Set authentication to false
        setIsAuthenticated(false); // Close the navbar by setting isAuthenticated to false

        // Navigate to login page
        navigate("/login");
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('http://localhost:8080/workout_app/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password }),
                credentials: 'include',
            });

            if (response.ok) {
                const data = await response.json();  // Parse the response JSON
                const { token, userId, isAdmin} = data;  // Destructure the token and userId from the response

                setIsAuthenticated(true);
                setIsAdmin(isAdmin);

                localStorage.setItem('userToken', token);  // Save the token
                localStorage.setItem('userId', userId);    // Save the userId
                localStorage.setItem('isAdmin', isAdmin);

                navigate('/workouts');  // Redirect to workouts page
            } else {
                alert('Invalid login credentials.');
            }
        } catch (error) {
            console.error('Login error:', error);
            alert('An error occurred. Please try again.');
        }
    };

    return (
        <div className={styles['auth-container']}>
            <div className={styles['auth-box']}>
                <h2>Login</h2>
                <form onSubmit={handleSubmit}>
                    <div className={styles['auth-inputs']}>
                        <input
                            type="email"
                            placeholder="Enter your email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>
                    <div className={styles['auth-inputs']}>
                        <input
                            type="password"
                            placeholder="Enter your password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit" className={styles['auth-btn']}>
                        Login
                    </button>
                </form>
                <div className={styles['auth-switch']}>
                    <p>
                        Don't have an account? <a href="/signup">Sign up</a>
                    </p>
                </div>
            </div>
        </div>
    );
};

export default Login;
