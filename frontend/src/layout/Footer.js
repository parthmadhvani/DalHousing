import React from 'react';
import '../style/Footer.css';

const Footer = () => {
    return (
        <footer className="footer">
            <div className="footer-content">
                <p>&copy; {new Date().getFullYear()} DalHousingEase.</p>
            </div>
        </footer>
    );
};

export default Footer;
