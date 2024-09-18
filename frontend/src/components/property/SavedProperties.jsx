import React, { useEffect, useState, useCallback } from 'react';
import { getSavedProperties, removeSavedProperty } from '../../service/Api';
import PropertyCard from './PropertyCard';
import '../../style/SavedProperties.css';
import HeaderSavedProperties from '../../layout/HeaderSavedProperties';
import { useNavigate } from 'react-router-dom';
import Footer from '../../layout/Footer';

const SavedProperties = () => {
    const [savedProperties, setSavedProperties] = useState([]);
    const [pagination, setPagination] = useState({
        currentPageNumber: 0,
        currentPageSize: 6,
        totalProperties: 0,
        totalPages: 0,
    });

    const navigate = useNavigate();

    const fetchSavedProperties = useCallback(async () => {
        try {
            const userId = localStorage.getItem('userId');
            const response = await getSavedProperties(userId, pagination.currentPageNumber, pagination.currentPageSize);
            setSavedProperties(response.data.properties);
            setPagination({
                currentPageNumber: response.data.currentPageNumber,
                currentPageSize: response.data.currentPageSize,
                totalProperties: response.data.totalProperties,
                totalPages: response.data.totalPages,
            });
        } catch (error) {
            console.error('Failed to fetch saved properties', error);
        }
    }, [pagination.currentPageNumber, pagination.currentPageSize]);

    useEffect(() => {
        fetchSavedProperties();
    }, [fetchSavedProperties]);

    const handlePageChange = (newPage) => {
        setPagination({ ...pagination, currentPageNumber: newPage });
    };

    const handleRemove = async (propertyId) => {
        try {
            const userId = localStorage.getItem('userId');
            await removeSavedProperty(userId, propertyId);
            alert('Property removed successfully!');
            fetchSavedProperties();
        } catch (error) {
            console.error('Failed to remove property', error);
            alert('Failed to remove property!');
        }
    };

    const handleView = (id) => {
        navigate(`/view_property/${id}`, { state: { property_id: id } });
    };

    return (
        <div className="saved-properties">
            <HeaderSavedProperties />
            <h2>Saved Properties</h2>
            <div className="property-list">
                {savedProperties.length === 0 ? (
                    <p className="saved-prop-page">No saved properties found.</p>
                ) : (
                    savedProperties.map(property => (
                        <PropertyCard 
                            key={property.id} 
                            property={property} 
                            onView={handleView} 
                            onRemove={handleRemove}
                            isSaved={true} 
                        />
                    ))
                )}
            </div>
            <div className="pagination">
                {Array.from({ length: pagination.totalPages }, (_, index) => (
                    <button
                        key={index}
                        onClick={() => handlePageChange(index)}
                        className={index === pagination.currentPageNumber ? 'active' : ''}
                    >
                        {index + 1}
                    </button>
                ))}
            </div>
            <Footer />
        </div>
    );
};

export default SavedProperties;
