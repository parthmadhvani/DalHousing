import React, { useEffect, useState, useCallback } from 'react';
import { getProperties, searchProperties, saveProperty, getCities, getPropertyTypes,  } from '../../service/Api';
import Filter from '../../layout/Filter';
import Header from '../../layout/Header';
import { useNavigate } from 'react-router-dom';
import '../../style/PropertyList.css'
import PropertyCard from './PropertyCard';
import Footer from '../../layout/Footer';
import authService from '../../services/auth.service';
import HeaderWithoutLogin from '../../layout/HeaderWithoutLogin';

const PropertyList = () => {
    const [properties, setProperties] = useState([]);
    const [cities, setCities] = useState([]);
    const [pagination, setPagination] = useState({
        currentPageNumber: 0,
        currentPageSize: 6,
        totalProperties: 0,
        totalPages: 0,
    });
    const [filters, setFilters] = useState({
        city: '',
        property_type: '',
        min_price: '',
        max_price: '',
        bedrooms: '',
        bathrooms: '',
        furnishing: '',
        parking: '',
        pageNumber: 0,
        pageSize: 6,
        sortBy: 'id',
        sortDir: '',
        keyword: ''
    });
    const navigate = useNavigate();
    const [propertyTypes, setPropertyTypes] = useState([]);
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        const currentUser = authService.getCurrentUser();
        setIsLoggedIn(!!currentUser);
    }, []);

    const fetchProperties = useCallback(async () => {
        try {
            const updatedFilters = { ...filters };
            const response = await (filters.keyword ? searchProperties(filters.keyword, updatedFilters) : getProperties(updatedFilters));
            setProperties(response.data.properties);
            setPagination({
                currentPageNumber: response.data.currentPageNumber,
                currentPageSize: response.data.currentPageSize,
                totalProperties: response.data.totalProperties,
                totalPages: response.data.totalPages,
            });
        } catch (error) {
            console.error('Failed to fetch properties', error);
        }
    }, [filters]);

    useEffect(() => {
        fetchProperties();
    }, [fetchProperties]);


    useEffect(() => {
        const fetchCities = async () => {
            try {
                const response = await getCities();
                setCities(response.data);
            } catch (error) {
                console.error('Failed to fetch cities', error);
            }
        };
        fetchCities();
    }, []);

    useEffect(() => {
        const fetchPropertyTypes = async () => {
            try {
                const response = await getPropertyTypes();
                setPropertyTypes(response.data);
            } catch (error) {
                console.error('Failed to fetch property types', error);
            }
        };
        fetchPropertyTypes();
    }, []);

    const handleFilterChange = (e) => {
        setFilters({ ...filters, [e.target.name]: e.target.value, pageNumber: 0 });
    };


    const handleSearch = useCallback((keyword) => {
        setFilters({ ...filters, keyword, pageNumber: 0 });
    }, [filters]);


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
    

    const handleView = (id) => {
        navigate(`/view_property/${id}`, { state: { property_id: id } });
    };

    const handlePageChange = (newPage) => {
        setFilters({ ...filters, pageNumber: newPage });
    };

    const handleSortChange = (e) => {
        const value = e.target.value;
        setFilters({
            ...filters,
            sortBy: 'monthly_rent',
            sortDir: value,
        });
    };



    return (
        <div>
            {isLoggedIn ? <Header /> : <HeaderWithoutLogin />}
            <Filter
                onFilterChange={handleFilterChange}
                onSearch={handleSearch}
                cities={cities}
                propertyTypes={propertyTypes} />
            <div className="sort-controls">
                <select className="sort-dir" onChange={handleSortChange} value={filters.sortDir}>
                    <option value="">Sort by</option>
                    <option value="asc">Price: Low to High</option>
                    <option value="desc">Price: High to Low</option>
                </select>
            </div>
            <div className="property-list">

                {properties.length === 0 ? (
                    <p className="property">No properties found.</p>
                ) : (
                    properties.map(property => (
                        <PropertyCard key={property.id} property={property} onSave={handleSave} onView={handleView} />
                    ))
                )}
            </div>
            <div className="pagination">
                <button
                    onClick={() => handlePageChange(pagination.currentPageNumber - 1)}
                    disabled={pagination.currentPageNumber === 0}
                >
                    &lt;
                </button>
                {Array.from({ length: pagination.totalPages }, (_, index) => (
                    <button
                        key={index}
                        onClick={() => handlePageChange(index)}
                        className={index === pagination.currentPageNumber ? 'active' : ''}
                    >
                        {index + 1}
                    </button>
                ))}
                <button
                    onClick={() => handlePageChange(pagination.currentPageNumber + 1)}
                    disabled={pagination.currentPageNumber === pagination.totalPages - 1}
                >
                    &gt;
                </button>
            </div>
            <Footer />
        </div>
    );
};

export default PropertyList;