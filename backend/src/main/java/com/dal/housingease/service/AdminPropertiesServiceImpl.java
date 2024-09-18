package com.dal.housingease.service;

import com.dal.housingease.enums.PropertiesEnums;
import com.dal.housingease.model.AdminProperties;
import com.dal.housingease.repository.AdminPropertiesRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
/**
 * Implementation of the AdminPropertiesService interface.
 * Provides methods for managing admin properties, including retrieving, saving,
 * deleting, and updating the status of properties.
 */
@RequiredArgsConstructor
@Service
public class AdminPropertiesServiceImpl implements AdminPropertiesService 
{
	private static final Logger logger = LoggerFactory.getLogger(AdminPropertiesServiceImpl.class);
    private final AdminPropertiesRepository adminPropertiesRepository;
    /**
     * Retrieves a list of all properties.
     *
     * @return a List of AdminProperties objects.
     */
    @Override
    public List<AdminProperties> getAllProperties()
    {
    	logger.info("Fetching all properties");
        return adminPropertiesRepository.findAll();
    }
    /**
     * Retrieves a property by its unique ID.
     *
     * @param id the ID of the property to be retrieved.
     * @return an Optional containing the AdminProperties object if found, or empty if not found.
     */
    @Override
    public Optional<AdminProperties> getPropertyById(Integer id) 
    {
    	logger.info("Fetching property with ID: {}", id);
        return adminPropertiesRepository.findById(id);
    }
    /**
     * Saves a new property or updates an existing property.
     *
     * @param adminProperties the AdminProperties object to be saved.
     * @return the saved AdminProperties object.
     */
    @Override
    public AdminProperties saveProperty(AdminProperties adminProperties) 
    {
    	logger.info("Saving property: {}", adminProperties);
        return adminPropertiesRepository.save(adminProperties);
    }
    /**
     * Deletes a property by its unique ID.
     *
     * @param id the ID of the property to be deleted.
     */
    @Override
    public void deleteProperty(Integer id)
    {
    	logger.info("Deleting property with ID: {}", id);
        adminPropertiesRepository.deleteById(id);
    }
    /**
     * Updates the status of a property identified by its unique ID.
     *
     * @param id the ID of the property to be updated.
     * @param status the new status to be set for the property.
     * @return the updated AdminProperties object.
     * @throws RuntimeException if the property is not found with the specified ID.
     */
    @Override
    public AdminProperties updatePropertyStatus(Integer id, String status)
    {
    	logger.info("Updating property status for ID: {} to {}", id, status);
        Optional<AdminProperties> propertyOptional = adminPropertiesRepository.findById(id);
        if (propertyOptional.isPresent())
        {
            AdminProperties property = propertyOptional.get();
            property.setStatus(PropertiesEnums.Status.valueOf(status));
            logger.info("Property status updated for ID: {}", id);
            return adminPropertiesRepository.save(property);
        }
        else 
        {
        	 logger.warn("Property not found with ID: {}", id);
            throw new RuntimeException("Property not found with id: " + id);
        }
    }
}
