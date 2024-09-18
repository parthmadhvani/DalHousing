
import React, { useState } from 'react';
import './FullScreenImage.css'; // Ensure to create and import your CSS file

export default function FullScreenImage({ imageUrl, onClose }) {
  const [isFullScreen, setIsFullScreen] = useState(true);

  console.log("url"+imageUrl);

  const handleClose = () => {
    setIsFullScreen(false);
    onClose();
  };

  return (
    <div className={`fullscreen-container ${isFullScreen ? 'fullscreen' : ''}`}>
      <div className="image-wrapper">
        <img src={imageUrl} alt="Full Screen" className="fullscreen-image" onClick={handleClose} />
        <button className="close-button" onClick={handleClose}>Close</button>
      </div>
    </div>
  );
}
