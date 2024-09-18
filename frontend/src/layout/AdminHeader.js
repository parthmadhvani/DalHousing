import React from 'react';
import { useNavigate, Link } from 'react-router-dom';
import './Header.css';
import logo from '../components/Designer (22).png'; // Import the logo image

const AdminHeader = () => {
    const navigate = useNavigate();

    const navigateToLogout = () => {
        navigate('/logout');
    };

    const navigateToChangePassword = () => {
        navigate('/change-password');
    };

    return (
        <header className="d-flex align-items-center bg-light py-2 px-4">
            <Link to="/" className="d-flex align-items-center">
                <img src={logo} alt="Logo" style={{ height: '50px', marginRight: '15px' }} /> {/* Adjust logo height and margin */}
                <h1 className="mb-0" style={{ fontFamily: "'Pacifico', cursive", fontWeight: "bold", fontSize: '24px' }}><a href="/">DalHousingEase</a></h1>
            </Link>
            <nav className="ms-auto d-flex align-items-center">
                <button className="btn btn-primary me-2" onClick={navigateToLogout}>Logout</button>
                <button className="btn btn-secondary" onClick={navigateToChangePassword}>Change Password</button>
            </nav>
        </header>
    );
};

export default AdminHeader;
