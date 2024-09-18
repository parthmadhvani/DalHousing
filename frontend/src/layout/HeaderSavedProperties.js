import React from 'react';
import { useNavigate } from 'react-router-dom';

const HeaderSavedProperties = () => {

    const navigate = useNavigate();

    const navigateToLogout = () => {
        navigate('/logout');
    };

    return (
        <header>
            <h1><a href="/">DalHousingEase</a></h1>
            <nav className="header">
                <button onClick={navigateToLogout}>Log Out</button>
            </nav>
        </header>
    );
};


export default HeaderSavedProperties;
