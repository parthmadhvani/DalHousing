import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import backgroundImage from './landingpageimg.jpeg';
import Footer from '../layout/Footer';
import LandingPageHeader from '../layout/LandingPageHeader';
import LandingPageHeaderLoggedIn from '../layout/LandingPageHeaderLoggedIn';
import authService from '../services/auth.service';

const LandingPage = () => {
  const navigate = useNavigate();

  const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        const currentUser = authService.getCurrentUser();
        setIsLoggedIn(!!currentUser);
    }, []);

  const handleExploreProperties = () => {
    navigate('/search-filter');
  };

  return (
    <>
      <div className="d-flex flex-column min-vh-100">
        {isLoggedIn ? <LandingPageHeaderLoggedIn /> : <LandingPageHeader />}
        <main className="flex-grow-1 d-flex align-items-start" style={{ backgroundImage: `url(${backgroundImage})`, backgroundSize: 'cover', backgroundPosition: 'center' }}>
          <div className="container-fluid p-5" style={{ maxWidth: '554px', backgroundColor: 'rgba(0, 0, 0, 0.8)', color: 'white', borderRadius: '10px', marginLeft: '30px', marginTop: '110px' }}>
            <div className="text-cent">
              <h2 style={{ fontFamily: "'Pacifico', cursive", fontWeight: "bold" }}>Welcome to Dalhousing Ease  Your Trusted Housing Partner!</h2>
              <p>At Dalhousing Ease, we prioritize your safety and convenience. Our platform connects students with verified housing providers, ensuring a secure and hassle-free rental experience. Find your perfect home with confidence and ease. Welcome to a community where your comfort and security come first!</p>
              <div className="d-flex justify-content-left">
                <button
                  className="btn btn-primary btn-lg rounded"
                  onClick={handleExploreProperties}
                >
                  Explore Properties
                </button>
              </div>
            </div>
          </div>
        </main>

        {/* <footer className="bg-dark text-white bg-opacity-75 py-3 fixed-bottom w-100">
                <div>&copy; 2024 DalHousingEase.</div>
            </footer> */}
        <Footer />
      </div>
    </>

  );
};

export default LandingPage;
