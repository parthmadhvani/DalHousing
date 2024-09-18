import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Header.css';
import AuthService from '../services/auth.service';

const LandingPageHeaderLoggedIn = () => {

    const navigate = useNavigate();

    const navigateToLogout = () => {
        navigate('/logout');
    };


    const handleSavedClick = (e) => {
        e.preventDefault();
        const currentUser = AuthService.getCurrentUser();
        const storedRoleId = localStorage.getItem('roleId');
        if (currentUser && storedRoleId === '2') {
            navigate('/saved-properties'); // Redirect to saved properties page
        } else if (currentUser && storedRoleId !== '2') {
            alert('You can\'t access saved properties as you are not a property seeker.');
        } else {
            alert('Please log in to see save properties.');
            navigate('/login'); // Redirect to login if user is not logged in
        }
    };

    const handlePostRental = () => {
        const currentUser = AuthService.getCurrentUser();
        const storedRoleId = localStorage.getItem('roleId');
        if (currentUser && storedRoleId === '3') {
            navigate('/add_properties'); // Redirect to property rental form
        } else if (currentUser && storedRoleId !== '3') {
            alert('You do not have permission to post rental properties.');
        } else {
            navigate('/login'); // Redirect to login if user is not logged in
        }
    };

    return (
        <header>
            <h1><a href="/">DalHousingEase</a></h1>
            <nav className="header">
                <button onClick={handleSavedClick}>Saved</button>
                <button onClick={navigateToLogout}>Logout</button>
                <button onClick={handlePostRental}>Post Rental</button>
            </nav>
        </header>
    );
};


export default LandingPageHeaderLoggedIn;
