import React from 'react';
import { useNavigate } from 'react-router-dom';

import './Header.css';

const ListerHeader = () => {

    const navigate = useNavigate();
    const navigateToLogout = () => {
        navigate('/logout');
    };

    const navigateToChangePassword = () => {
        navigate('/change-password');
    };


    return (
        <header>
            <h1><a href="/">DalHousingEase</a></h1>
            <nav className="filter">
                <button onClick={navigateToLogout}>Logout</button>
                <button onClick={navigateToChangePassword}>Change Password</button>
            </nav>
        </header>
    );
};


export default ListerHeader;