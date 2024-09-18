import React, { useState } from 'react';
import './Filter.css';

const Filter = ({ onFilterChange, onSearch, cities, propertyTypes }) => {
    const [searchKeyword, setSearchKeyword] = useState('');

    const handleSearchChange = (e) => {
        setSearchKeyword(e.target.value);
    };

    const validateAndChange = (e) => {
        const { name, value } = e.target;
        if (name === 'min_price' || name === 'max_price' || name === 'bedrooms' || name === 'bathrooms') {
            if (value < 1) {
                alert(`${name.replace(/([A-Z])/g, ' $1')} cannot be less than 1`);
                return;
            }
        }
        onFilterChange(e);
    };

    const handleSearchClick = () => {
        onSearch(searchKeyword);
    };

    const handleKeyDown = (e) => {
        if (e.key === 'Enter') {
            handleSearchClick();
        }
    };

    return (
        <div className="filter">
            <form className="search-form" onSubmit={(e) => e.preventDefault()}>
                <input
                    type="text"
                    name="city"
                    placeholder="Search by address..."
                    value={searchKeyword}
                    onChange={handleSearchChange}
                    onKeyDown={handleKeyDown}
                    className="search-input"
                />
                <button type="button" onClick={handleSearchClick} className="search-button">Search</button>
            </form>
            <div className="filters">
                <select name="property_type" onChange={validateAndChange} className="filter-input">
                    <option value="">Property Type</option>
                    {propertyTypes.map(propertyType => (
                        <option key={propertyType} value={propertyType}>{propertyType}</option>
                    ))}
                </select>
                <select name="furnishing" onChange={validateAndChange} className="filter-input">
                    <option value="">Furnishing</option>
                    <option value="Furnished">Furnished</option>
                    <option value="Semi-Furnished">Semi-Furnished</option>
                    <option value="Unfurnished">Unfurnished</option>
                </select>
                <select name="parking" onChange={validateAndChange} className="filter-input">
                    <option value="">Parking</option>
                    <option value="Available">Parking Available</option>
                    <option value="Unavailable">No Parking</option>
                </select>
                <select name="city" onChange={validateAndChange} className="filter-input">
                    <option value="">Select City</option>
                    {cities.map(city => (
                        <option key={city} value={city}>{city}</option>
                    ))}
                </select>
                <input
                    type="number"
                    name="min_price"
                    placeholder="Min Price"
                    min="1"
                    onChange={validateAndChange}
                    className="filter-input"
                />
                <input
                    type="number"
                    name="max_price"
                    placeholder="Max Price"
                    min="1"
                    onChange={validateAndChange}
                    className="filter-input"
                />
                <input
                    type="number"
                    name="bedrooms"
                    placeholder="Bedrooms"
                    min="1"
                    onChange={validateAndChange}
                    className="filter-input"
                />
                <input
                    type="number"
                    name="bathrooms"
                    placeholder="Bathrooms"
                    min="1"
                    onChange={validateAndChange}
                    className="filter-input"
                />
            </div>
        </div>
    );
};

export default Filter;