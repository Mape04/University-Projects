import React, { useState } from 'react';
import styles from '../styles/Auth.module.css';
import {useNavigate} from "react-router-dom";  // Import the CSS module

const SignUp = () => {
    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrorMessage('');
        setSuccessMessage('');
        setIsLoading(true);

        if (password !== confirmPassword) {
            setErrorMessage('Passwords do not match.');
            setIsLoading(false);
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/workout_app/auth/signup', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, email, password, isAdmin: false }),
            });

            const textData = await response.text();  // Get the raw text of the response
            console.log('Raw Response:', textData);

            // Now, try parsing the JSON only if the response is valid JSON
            let data = {};
            try {
                data = JSON.parse(textData);
                console.log('Parsed Data:', data);
            } catch (jsonError) {
                console.error('Error parsing JSON:', jsonError);
                setErrorMessage('Unexpected response format.');
                return;
            }

            if (response.ok) {
                setSuccessMessage(data.message || 'Signup successful! Please log in.');
                setEmail('');
                setPassword('');
                setConfirmPassword('');
                navigate("/login");
            } else {
                setErrorMessage(data.message || 'Signup failed. Please try again.');
            }
        } catch (error) {
            console.error('Request failed:', error);
            setErrorMessage('An error occurred. Please try again later.');
        } finally {
            setIsLoading(false);
        }
    };


    return (
        <div className={styles['auth-container']}>
            <div className={styles['auth-box']}>
                <h2>Sign Up</h2>
                <form onSubmit={handleSubmit}>
                    <div className={styles['auth-inputs']}>
                        <input
                            type="text"
                            placeholder="Enter your username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>
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
                    <div className={styles['auth-inputs']}>
                        <input
                            type="password"
                            placeholder="Confirm your password"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                        />
                    </div>
                    {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
                    {successMessage && <p style={{ color: 'green' }}>{successMessage}</p>}
                    <button type="submit" className={styles['auth-btn']} disabled={isLoading}>
                        {isLoading ? 'Signing up...' : 'Sign Up'}
                    </button>
                </form>
                <div className={styles['auth-switch']}>
                    <p>Already have an account? <a href="/login">Login</a></p>
                </div>
            </div>
        </div>
    );
};

export default SignUp;
