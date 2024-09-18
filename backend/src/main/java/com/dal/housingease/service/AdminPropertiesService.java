package com.dal.housingease.service;

import com.dal.housingease.model.AdminProperties;

import java.util.List;
import java.util.Optional;
/**
 * AdminPropertiesService interface provides methods for managing admin properties.
 * This includes retrieving all properties, getting a property by ID, saving a new property,
 * deleting a property, and updating the status of a property.
 */
public interface AdminPropertiesService
{
	/**
     * Retrieves a list of all properties.
     *
     * @return a List of AdminProperties objects.
     */
    List<AdminProperties> getAllProperties();
    /**
     * Retrieves a property by its unique ID.
     *
     * @param id the ID of the property to be retrieved.
     * @return an Optional containing the AdminProperties object if found, or empty if not found.
     */
    Optional<AdminProperties> getPropertyById(Integer id);
    /**
     * Saves a new property or updates an existing property.
     *
     * @param adminProperties the AdminProperties object to be saved.
     * @return the saved AdminProperties object.
     */
    AdminProperties saveProperty(AdminProperties adminProperties);
    /**
     * Deletes a property by its unique ID.
     *
     * @param id the ID of the property to be deleted.
     */
    void deleteProperty(Integer id);
    /**
     * Updates the status of a property identified by its unique ID.
     *
     * @param id the ID of the property to be updated.
     * @param status the new status to be set for the property.
     * @return the updated AdminProperties object.
     */
    AdminProperties updatePropertyStatus(Integer id, String status);
    //void sendVerificationEmail(Integer propertyId);
}
