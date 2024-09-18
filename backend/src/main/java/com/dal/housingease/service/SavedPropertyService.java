package com.dal.housingease.service;

import com.dal.housingease.utils.PropertyResponse;

/**
 * Service interface for managing saved properties.
 */
public interface SavedPropertyService {

    /**
     * Save a property for a seeker.
     *
     * @param userId     the ID of the user
     * @param propertyId the ID of the property
     * @return true if the property was saved successfully, false otherwise
     */
    boolean savePropertyForSeeker(Integer userId, Integer propertyId);

    /**
     * Get the saved properties for a user with pagination.
     *
     * @param userId     the ID of the user
     * @param pageNumber the page number
     * @param pageSize   the page size
     * @return a response containing the saved properties and pagination details
     */
    PropertyResponse getSavedPropertiesForUser(Integer userId, Integer pageNumber, Integer pageSize);

    /**
     * Delete a saved property for a user.
     *
     * @param userId     the ID of the user
     * @param propertyId the ID of the property
     * @return true if the property was deleted successfully, false otherwise
     */
    boolean deleteSavedProperty(Integer userId, Integer propertyId);

}