import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useLocation, useNavigate } from 'react-router-dom';
import Modal from 'react-modal';
import './PropertyDetails.css';
import FullScreenImage from './FullScreenImage';
import { saveProperty } from '../../service/Api';
import Footer from '../../layout/Footer';
import Header from '../../layout/Header';
import authService from '../../services/auth.service';
import HeaderWithoutLogin from '../../layout/HeaderWithoutLogin';


Modal.setAppElement('#root');

export default function PropertyDetails() {
  const [data, setData] = useState({
    street_address: "",
    unit_number: "",
    city: "",
    province: "",
    email: "",
    mobile: "",
    monthly_rent: "",
    security_deposite: "",
    availability: "",
    property_heading: "",
    full_description: "",
    latitude: "",
    longitude: "",
    bedrooms: "",
    bathrooms: "",
    furnishing: "",
    parking: "",
    youtube: "",
    imagesList: {}
  });


  const location = useLocation();
  const { property_id } = location.state || {};
  const [imagesList, setimagesList] = useState([]);

  const [isModalOpen, setIsModalOpen] = useState(false);
  
  const [fullscreenImageIndex, setFullscreenImageIndex] = useState(null);

  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    const currentUser = authService.getCurrentUser();
    setIsLoggedIn(!!currentUser);
  }, []);

  const navigate = useNavigate();

  const getURL = `http://172.17.0.139:8080/properties/get_property_details/${property_id}`;

  const token = localStorage.getItem('token')
  useEffect(() => {
    axios.get(getURL)
      .then(response => {
        console.log(response.data);
        setData(response.data);
        const links = response.data.images.map(image => image.image_url);
        setimagesList(links);
      })
      .catch(error => console.log(error));
  }, [getURL]);

  const showMap = () => {
    navigate(`/show_map/${property_id}`, { state: { latitude: data.latitude, longitude: data.longitude, address: data.street_address } });
  };

  const openModal = (index) => {
    if (index >= 3) { // Check if the clicked index is 3 or more (indicating the "+4", "+5" thumbnail)
      
      setIsModalOpen(true);
    }
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setFullscreenImageIndex(null); // Reset fullscreen image index on modal close
  };

  const [showComponentB, setShowComponentB] = useState({
    flag: false,
    imageUrl: ""
  });

  const handleClick = (imageUrl) => {
    console.log("fjhdfjdjf");
    setShowComponentB({
      flag: true,
      imageUrl: imageUrl
    });
  };

  const onClose = () => {
    setShowComponentB({
      flag: false,
      imageUrl: ""
    }
    );
  }


  const handleSave = async (id) => {

    const currentUser = authService.getCurrentUser();
        if (!currentUser) {
            alert('Please log in to save the property.');
            navigate('/login');
            return;
        }
        
        const storedRoleId = localStorage.getItem('roleId');
        if (storedRoleId != 2) {
            alert('Property lister cannot save property')
        } else {
            try {
                const userId = localStorage.getItem('userId');
                const token = localStorage.getItem('token'); 
                await saveProperty(userId, id, token);
                alert('Property saved successfully!');
            } catch (error) {
                if (error.message === 'Property already saved') {
                    alert('Property already saved.');
                } else {
                    console.error('Failed to save property', error);
                    alert('Failed to save property');
                }
            }
        }
  };

  const formatPhoneNumber = (phoneNumber) => {
    const cleaned = ('' + phoneNumber).replace(/\D/g, '');
    const match = cleaned.match(/^(\d{3})(\d{3})(\d{4})$/);
    if (match) {
      return `(${match[1]}) ${match[2]}-${match[3]}`;
    }
    return phoneNumber;
  };

  const getWhatsAppLink = (mobile, message, countryCode = '1') => {
    const formattedMobile = `${countryCode}${mobile}`;
    const encodedMessage = encodeURIComponent(message);
    return `https://wa.me/${formattedMobile}?text=${encodedMessage}`;
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    date.setMinutes(date.getMinutes() + date.getTimezoneOffset());
    const options = { day: 'numeric', month: 'long', year: 'numeric' };
    return date.toLocaleDateString('en-US', options);
  }

  const getYouTubeVideoId = (url) => {
    const regex = /(?:https?:\/\/)?(?:www\.)?youtube\.com\/(?:[^\/\n\s]+\/\S+\/|(?:v|e(?:mbed)?)\/|.*[?&]v=|.*[?&]vi=|.*[?&]v%3D)([a-zA-Z0-9_-]{11})/;
    const match = url.match(regex);
    return match ? match[1] : null;
  };

  return (
    <>
      {isLoggedIn ? <Header /> : <HeaderWithoutLogin />}
      <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-12 sm:py-8 lg:py-8">
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 lg:gap-12">
          <div className="grid grid-cols-1 gap-4">
            <div className="relative" style={{ marginBottom: "-20px" }}>
              <img
                src={imagesList[0]}
                alt="Main"
                className="w-full h-[400px] object-cover rounded-lg cursor-pointer"
                onClick={() => handleClick(imagesList[0])}
              />
            </div>
            <div className="grid grid-cols-3 gap-2 w-full h-[100px] object-cover rounded cursor-pointer">
              {imagesList.slice(1, 4).map((image, index) => (
                <div key={index} className="relative">
                  <img
                    src={image}
                    alt={`Thumbnail ${index + 1}`}
                    className="w-full h-[100px] object-cover rounded cursor-pointer"
                    onClick={() => handleClick(imagesList[index + 1])}
                  />
                  {index === 2 && imagesList.length > 3 && (
                    <div onClick={() => openModal(3)} className="hover:cursor-pointer absolute inset-0 flex items-center justify-center bg-black bg-opacity-50 text-white text-xl font-bold">
                      +{imagesList.length - 3}
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>
          <div className="grid grid-cols-1 gap-6">
            <div className="flex flex-col gap-2">
              <h1 className="text-3xl font-bold">{data.street_address}, {data.city}, {data.province}</h1>
              <div className="text-2xl font-bold">${data.monthly_rent}/month</div>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4" style={{ marginTop: "10px", marginBottom: "10px" }}>
                <div className="flex items-center gap-2">
                  <i className="fas fa-map-marker-alt text-black"></i>
                  <span onClick={showMap} className="text-primary hover:underline cursor-pointer">
                    View on Map
                  </span>
                </div>
                <div className="flex items-center gap-2">
                  <i className="fas fa-phone-alt text-black"></i>
                  <span>{formatPhoneNumber(data.mobile)}</span>
                </div>
              </div>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div className="flex items-center gap-2">
                  <i className="fas fa-parking text-black"></i>
                  <strong>Parking: </strong>
                  <span>{data.parking}</span>
                </div>
                <div className="flex items-center gap-2">
                  <i className="fas fa-couch text-black"></i>
                  <strong>Furnishing: </strong>
                  <span>{data.furnishing}</span>
                </div>
                <div className="flex items-center gap-2">
                  <i className="fas fa-bed text-black"></i>
                  <strong>Bedrooms: </strong>
                  <span>{data.bedrooms}</span>
                </div>
                <div className="flex items-center gap-2">
                  <i className="fas fa-bath text-black"></i>
                  <strong>Bathrooms: </strong>
                  <span>{data.bathrooms}</span>
                </div>
                <div className="flex items-center gap-2">
                  <i className="fas fa-calendar-alt text-black"></i>
                  <strong>Availability: </strong>
                  <span>{formatDate(data.availability)}</span>
                </div>
                <div className="flex items-center gap-2">
                  <i className="fas fa-shield-alt text-black"></i>
                  <strong>Security deposit: </strong>
                  <span>${data.security_deposite}</span>
                </div>
              </div>
              <div className="flex justify-end gap-4" style={{ margin: "15px" }}>
                <button className="bg-black text-white font-semibold py-2 px-4 border rounded" onClick={() => handleSave(property_id)}>
                  Save
                </button>
                <a
                  href={getWhatsAppLink(data.mobile, `Hello, I am interested in your property at ${data.street_address}. Could you please give me more details about your property?`)}
                  className="bg-green-500 hover:bg-green-600 text-white font-semibold py-2 px-4 rounded"
                  target="_blank"
                  rel="noopener noreferrer"
                  style={{ textDecoration: "none" }}
                >
                  Chat on WhatsApp
                </a>
              </div>
              <div className="grid grid-cols-1 gap-4">
                <p className="text-muted-foreground">
                  <strong>Info: </strong>
                  <br />
                  <span>{data.full_description}</span>
                </p>
                {data.youtube && (
                  <div>
                    <h2 className="text-2xl font-bold mb-2">YouTube Video</h2>
                    <div className="aspect-w-16 aspect-h-9">
                      <iframe
                        src={`https://www.youtube.com/embed/${getYouTubeVideoId(data.youtube)}`}
                        title="YouTube video"
                        className="w-full h-full rounded-lg"
                        allowFullScreen
                      ></iframe>
                    </div>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>

        {showComponentB.flag && <FullScreenImage imageUrl={showComponentB.imageUrl} onClose={onClose} />}

        <Modal
          isOpen={isModalOpen}
          onRequestClose={closeModal}
          contentLabel="Image Gallery"
          className="modal-gallery"
          overlayClassName="overlay"
        >
          <div className="modal-content">
            <button onClick={closeModal} className="close-button">Close</button>
            <div className="image-gallery">
              {imagesList.map((image, index) => (
                <img
                  key={index}
                  src={image}
                  alt={`${index + 1}`}
                  className={`modal-image ${fullscreenImageIndex === index ? 'fullscreen' : ''}`}
                  onClick={() => handleClick(imagesList[index])}
                />
              ))}
            </div>
          </div>
        </Modal>
      </div>
      <Footer />
    </>

  );
}
