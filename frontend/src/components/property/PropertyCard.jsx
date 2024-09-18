import React from 'react';
import '../../style/PropertyCard.css';
import '../../index.css';


const PropertyCard = ({ property, onSave, onView, onRemove, isSaved }) => {

    const handleImageError = (e) => {
        e.target.src = '/default/image.jpg';
    };

    return (
        <div className="space-y-6 w-1/4 mt-6">
            <div className="w-full max-w-md rounded-lg overflow-hidden bg-white shadow-md">
                <img
                    src={property.imageUrls[0] || '/placeholder.svg'}
                    alt={`Property in ${property.city}`}
                    width={400}
                    height={200}
                    className="w-full object-cover"
                    onError={handleImageError}
                />
                <div className="p-3 space-y-4">
                    <div className="flex items-center justify-between">
                        <div className="text-2xl font-bold">${property.monthly_rent}/mo</div>
                        {property.status === 'Approved' ? (
                            <span className="inline-flex items-center bg-green-100 text-green-800 text-xs font-medium px-2.5 py-0.5 rounded-full !text-green-800">
                                <span className="w-2 h-2 me-1 bg-green-500 rounded-full"></span>
                                Verified by Us
                            </span>
                        ) : (
                            <span className="inline-flex items-center bg-yellow-100 text-yellow-800 text-xs font-medium px-2.5 py-0.5 rounded-full !text-yellow-800">
                                <span className="w-2 h-2 me-1 bg-yellow-500 rounded-full"></span>
                                Not verified
                            </span>
                        )}
                    </div>
                    <div className="space-y-1">
                        <div className="text-lg font-medium">{property.street_address}, {property.city}</div>
                        <div className="text-muted-foreground text-sm">
                            <span>{property.property_type}</span> &middot; <span>{property.bedrooms} Beds</span> &middot; <span>{property.bathrooms} Baths</span>
                        </div>
                    </div>
                    <div className="flex items-center justify-between">
                        <button
                            onClick={() => onView(property.id)}
                            className="px-4 py-2 border border-muted rounded-md hover:bg-muted/50 transition-colors"
                        >
                            View Details
                        </button>
                        {isSaved ? (
                            <button
                                onClick={() => onRemove(property.id)}
                                className="px-4 py-2 bg-black text-white rounded-md hover:bg-primary/90 transition-colors"
                            >
                                Remove
                            </button>
                        ) : (
                            <button
                                onClick={() => onSave(property.id)}
                                className="px-4 py-2 bg-black text-white rounded-md hover:bg-navy/90 transition-colors"
                            >
                                Save
                            </button>
                        )}
                    </div>
                </div>
            </div>
        </div>

    );
};

export default PropertyCard;
