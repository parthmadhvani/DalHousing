import React, { useEffect, useRef, useState } from 'react';
// import 'leaflet/dist/leaflet.css';
import { useLocation } from 'react-router-dom';


const MapComponent = () => {
  // const MapComponent = ({ latitude, longitude, address }) => {
  const mapRef = useRef(null);
  const [isScriptLoaded, setIsScriptLoaded] = useState(false);
  const location = useLocation();

  const { latitude, longitude, address } = location.state || {};

  // const latitude = 21.228125;
  // const longitude = 72.833771;
  // const address = "Katargam, Surat, Gujarat, India";

  // console.log(latitude, longitude, address);

  useEffect(() => {
    const loadScript = (url, callback) => {
      const script = document.createElement('script');
      script.type = 'text/javascript';
      script.src = url;
      script.onload = callback;
      document.head.appendChild(script);
    };

    if (!window.google) {
      loadScript(`https://maps.googleapis.com/maps/api/js?key=AIzaSyB6BYEa3LI5oNmBSsrQByfRgTD5OitQzgM&libraries=places`, () => {
        setIsScriptLoaded(true);
      });
    } else {
      setIsScriptLoaded(true);
    }
  }, []);

  useEffect(() => {
    if (isScriptLoaded && window.google) {
      const map = new window.google.maps.Map(mapRef.current, {
        center: { lat: parseFloat(latitude), lng: parseFloat(longitude) },
        zoom: 14,
      });

      const marker = new window.google.maps.Marker({
        position: { lat: parseFloat(latitude), lng: parseFloat(longitude) },
        map,
        title: address,
      });

      const infoWindow = new window.google.maps.InfoWindow({
        content: address,
      });

      marker.addListener('mouseover', () => {
        infoWindow.open(map, marker);
      });

      marker.addListener('mouseout', () => {
        infoWindow.close();
      });
    }
  }, [isScriptLoaded, latitude, longitude, address]);

  const openGoogleMapsDirections = () => {
    const directionsUrl = `https://www.google.com/maps/dir/?api=1&destination=${latitude},${longitude}&travelmode=driving`;
    window.open(directionsUrl, '_blank');
  };

  return (
    <div>
      <div style={{ height: '550px', width: '100%' }} ref={mapRef}>
        Loading map...
      </div>
      <div style={{ display: 'flex', justifyContent: 'center', marginTop: '10px' }}>
        <button
          onClick={openGoogleMapsDirections}
          style={{ padding: '10px', backgroundColor: '#4285F4', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}
        >
          Get Directions
        </button>
      </div>
    </div>


  );
};

export default MapComponent;