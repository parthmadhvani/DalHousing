import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Header.css';
import AuthService from '../services/auth.service';

const Header = () => {
    const navigate = useNavigate();

    const navigateToLogout = () => {
        navigate('/logout');
    };

    const navigateToChangePassword = () => {
        navigate('/change-password');
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
            alert('Please log in to see saved properties.');
            navigate('/login'); // Redirect to login if user is not logged in
        }
    };

    return (
        <header className="d-flex align-items-center bg-light py-2 px-4">
            <h1 className="mb-0"><a href="/">DalHousingEase</a></h1>
            <nav className="ms-auto d-flex align-items-center">
                <button onClick={navigateToLogout}>Logout</button>
                <button onClick={navigateToChangePassword}>Change Password</button>
                <button onClick={handleSavedClick}>Saved</button>
            </nav>
        </header>
    );
};

export default Header;
