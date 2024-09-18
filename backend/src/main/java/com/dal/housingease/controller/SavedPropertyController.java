package com.dal.housingease.controller;

import com.dal.housingease.config.AppConstants;
import com.dal.housingease.exceptions.UnauthorizedActionException;
import com.dal.housingease.service.SavedPropertyService;
import com.dal.housingease.utils.PropertyResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling saved property actions.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/properties")
public class SavedPropertyController 
{
	private static final Logger logger = LoggerFactory.getLogger(SavedPropertyController.class);
    private final SavedPropertyService savedPropertyService;

    /**
     * Saves a property for a user.
     *
     * @param userId the ID of the user
     * @param propertyId the ID of the property to be saved
     * @return a response entity indicating the result of the save operation
     */
    @PostMapping("/{userId}/save-property/{propertyId}")
    public ResponseEntity<String> savePropertyForSeeker(
            @PathVariable("userId") Integer userId,
            @PathVariable("propertyId") Integer propertyId) 
    {
    	logger.info("Received request to save property with ID: {} for user ID: {}", propertyId, userId);
        try 
        {
            // Attempt to save the property for the user
            savedPropertyService.savePropertyForSeeker(userId, propertyId);
            logger.info("Property with ID: {} saved successfully for user ID: {}", propertyId, userId);
            return ResponseEntity.ok("Property saved successfully");
        }
        catch (UnauthorizedActionException ex) 
        {
            logger.error("Unauthorized action while saving property with ID: {} for user ID: {}", propertyId, userId, ex);
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Retrieves all saved properties for a user with pagination.
     *
     * @param userId the ID of the user whose saved properties are to be retrieved
     * @param pageNumber the page number for pagination
     * @param pageSize the page size for pagination
     * @return a response entity containing the list of saved properties
     */
    @GetMapping("/{userId}/saved-properties")
    public ResponseEntity<PropertyResponse> getSavedPropertiesForUser(
            @PathVariable Integer userId,
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize)
    {
    	logger.info("Received request to get saved properties for user ID: {} with pageNumber: {} and pageSize: {}", userId, pageNumber, pageSize);

        // Retrieve saved properties for the user
        PropertyResponse savedProperties = savedPropertyService.getSavedPropertiesForUser(userId, pageNumber, pageSize);
        logger.info("Retrieved {} saved properties for user ID: {}", savedProperties.getProperties().size(), userId);
        return ResponseEntity.ok(savedProperties);
    }

    /**
     * Deletes a saved property for a user.
     *
     * @param userId the ID of the user whose saved property is to be deleted
     * @param propertyId the ID of the property to be deleted
     * @return a response entity indicating success or failure
     */
    @DeleteMapping("/{userId}/saved-properties/{propertyId}")
    public ResponseEntity<String> deleteSavedProperty(
            @PathVariable Integer userId,
            @PathVariable Integer propertyId) 
    {
    	logger.info("Received request to delete saved property with ID: {} for user ID: {}", propertyId, userId);
        try 
        {
            // Attempt to delete the saved property
            boolean deleted = savedPropertyService.deleteSavedProperty(userId, propertyId);
            logger.info("Delete operation result for property ID: {}: {}", propertyId);
            return ResponseEntity.ok(deleted ? "Saved property deleted successfully !!!" : "Property not deleted");
        } 
        catch (IllegalArgumentException e) 
        {
        	logger.error("Error deleting saved property with ID: {} for user ID: {}", propertyId, userId, e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}




