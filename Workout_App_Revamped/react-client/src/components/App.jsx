import React, { useState, useEffect, createContext } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Navbar from './Navbar';
import Login from './Login';
import SignUp from './SignUp';
import UserManagement from './UserManagement';
import Workouts from './Workouts';
import Goals from "./Goals.jsx";
import HomePage from "./HomePage.jsx";
import ExerciseLibrary from "./ExerciseLibrary.jsx";

// Create context for authentication state
export const AuthContext = createContext();

function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(() => {
        return localStorage.getItem('userToken') === 'true'; // Check for token
    });

    const [isAdmin, setIsAdmin] = useState(() => {
        return localStorage.getItem('isAdmin') === 'true'; // Check for admin flag
    });

    const [isNavbarOpen, setIsNavbarOpen] = useState(isAuthenticated);

    useEffect(() => {
        if (isAuthenticated) {
            localStorage.setItem('userToken', 'true');
            localStorage.setItem('isAdmin', isAdmin ? 'true' : 'false');
        } else {
            localStorage.removeItem('userToken');
            localStorage.removeItem('isAdmin');
        }
    }, [isAuthenticated, isAdmin]);

    useEffect(() => {
        setIsNavbarOpen(isAuthenticated);
    }, [isAuthenticated]);

    return (
        <AuthContext.Provider value={{ isAuthenticated, setIsAuthenticated, isAdmin, setIsAdmin }}>
            <Router>
                {isNavbarOpen && <Navbar />}
                <div className="content">
                    <Routes>
                        <Route path="/login" element={<Login />} />
                        <Route path="/signup" element={<SignUp />} />
                        <Route path="/" element={<HomePage/>} />
                        {/*<Route path="/" element={isAuthenticated ? <Workouts /> : <Navigate to="/login" />} />*/}
                        <Route path="/workouts" element={isAuthenticated ? <Workouts /> : <Navigate to="/login" />} />
                        <Route path="/exercise_library" element={isAuthenticated ? <ExerciseLibrary/> : <Navigate to="/login" />}/>

                        {/* Admin-only Route */}
                        <Route
                            path="/appUser-management"
                            element={isAuthenticated && isAdmin ? <UserManagement /> : <Navigate to="/" />}
                        />

                        <Route path="/goals" element={isAuthenticated ? <Goals /> : <Navigate to="/login" />} />
                        <Route path="*" element={<Navigate to={isAuthenticated ? "/workouts" : "/login"} />} />
                    </Routes>
                </div>
            </Router>
        </AuthContext.Provider>
    );
}

export default App;

