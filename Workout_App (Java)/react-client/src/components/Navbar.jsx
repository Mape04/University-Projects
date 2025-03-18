import React, {useContext, useEffect, useState} from 'react';
import {NavLink, useNavigate} from "react-router-dom";
import NavbarCSS from "../styles/Navbar.module.css"; // Import the CSS module
import {AuthContext} from './App';
import ReviewModal from './ReviewModal'; // Import ReviewModal component

const Navbar = () => {
    const {setIsAuthenticated} = useContext(AuthContext); // Get context to update auth state
    const navigate = useNavigate();
    const [isAdmin, setIsAdmin] = useState(false); // State to track if the user is an admin
    const [isAuthenticated, setIsAuthenticatedState] = useState(false); // Track if the user is authenticated
    const [isReviewModalOpen, setIsReviewModalOpen] = useState(false); // Manage modal state
    const [username, setUsername] = useState('');

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

    // Fetch `isAdmin` and authentication state from localStorage or backend
    useEffect(() => {
        const userId = localStorage.getItem("userId");
        if (userId) {
            handleGetUsername(userId);
        }

        const adminStatus = localStorage.getItem('isAdmin'); // Get the value from localStorage
        setIsAdmin(adminStatus === 'true'); // Update the state based on the value

        const userToken = localStorage.getItem('userToken');
        setIsAuthenticatedState(!!userToken); // Check if userToken exists, which means the user is logged in
    }, []);

    const handleLogout = (e) => {
        e.preventDefault();
        // Remove the token and admin status from localStorage
        localStorage.removeItem('userToken');
        localStorage.removeItem('isAdmin');
        // Set authentication state to false
        setIsAuthenticated(false); // Close the navbar and update the auth context
        // Redirect to login page
        navigate('/');
    };

    // Open and close the Review Modal
    const openReviewModal = (e) => {
        e.preventDefault(); // Prevent default link behavior
        setIsReviewModalOpen(true);
    };

    const closeReviewModal = () => {
        setIsReviewModalOpen(false);
    };

    return (
        <nav className={NavbarCSS["navbar"]}>
            <div className={NavbarCSS["navbar-container"]}>
                <a href="#home" className={NavbarCSS["navbar-logo"]}>
                    EQUINOX
                </a>
                <ul className={NavbarCSS["navbar-links"]}>
                    {/* Existing nav links */}
                    {isAdmin && isAuthenticated && (
                        <li>
                            <NavLink
                                to="/appUser-management"
                                className={({isActive}) =>
                                    isActive ? `${NavbarCSS["navbar-link"]} ${NavbarCSS["active"]}` : NavbarCSS["navbar-link"]
                                }
                            >
                                AppUser-Management
                            </NavLink>
                        </li>
                    )}
                    {isAuthenticated && (
                        <>
                            <li>
                                <NavLink
                                    to="/workouts"
                                    className={({isActive}) =>
                                        isActive ? `${NavbarCSS["navbar-link"]} ${NavbarCSS["active"]}` : NavbarCSS["navbar-link"]
                                    }
                                >
                                    Workouts
                                </NavLink>
                            </li>
                            <li>
                                <NavLink
                                    to="/exercise_library"
                                    className={({isActive}) =>
                                        isActive ? `${NavbarCSS["navbar-link"]} ${NavbarCSS["active"]}` : NavbarCSS["navbar-link"]
                                    }
                                >
                                    Exercise Library
                                </NavLink>
                            </li>
                            <li>
                                <NavLink
                                    to="/goals"
                                    className={({isActive}) =>
                                        isActive ? `${NavbarCSS["navbar-link"]} ${NavbarCSS["active"]}` : NavbarCSS["navbar-link"]
                                    }
                                >
                                    MyGoals
                                </NavLink>
                            </li>
                            <li>
                                <NavLink
                                    to="#review"
                                    onClick={openReviewModal}
                                    className={({isActive}) =>
                                        isActive
                                            ? `${NavbarCSS["navbar-link"]} ${NavbarCSS["review-link"]}`
                                            : `${NavbarCSS["navbar-link"]} ${NavbarCSS["review-link"]}`
                                    }
                                >
                                    Leave a Review
                                </NavLink>
                            </li>
                            <li>
                                <NavLink
                                    to="#user"
                                    className={({isActive}) =>
                                        isActive
                                            ? `${NavbarCSS["navbar-link"]} ${NavbarCSS["user-link"]}`
                                            : `${NavbarCSS["navbar-link"]} ${NavbarCSS["user-link"]}`
                                    }
                                >
                                    User: {username}
                                </NavLink>
                            </li>

                            <li>
                                <NavLink
                                    to="/"
                                    onClick={handleLogout}
                                    className={`${NavbarCSS["navbar-link"]} ${NavbarCSS["logout-btn"]}`}
                                >
                                    Logout
                                </NavLink>
                            </li>
                        </>
                    )}
                    {!isAuthenticated && (
                        <>
                            <li>
                                <NavLink
                                    to="/login"
                                    className={({isActive}) =>
                                        isActive ? `${NavbarCSS["navbar-link"]} ${NavbarCSS["active"]}` : NavbarCSS["navbar-link"]
                                    }
                                >
                                    Login
                                </NavLink>
                            </li>
                            <li>
                                <NavLink
                                    to="/signup"
                                    className={({isActive}) =>
                                        isActive ? `${NavbarCSS["navbar-link"]} ${NavbarCSS["active"]}` : NavbarCSS["navbar-link"]
                                    }
                                >
                                    Signup
                                </NavLink>
                            </li>
                        </>
                    )}
                </ul>
            </div>

            {/* Review Modal */}
            {isReviewModalOpen && <ReviewModal closeModal={closeReviewModal}/>}
        </nav>
    );
};

export default Navbar;
