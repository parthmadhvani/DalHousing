package com.dal.housingease.controller;

import com.dal.housingease.enums.PropertiesEnums.Status;
import com.dal.housingease.model.AdminProperties;
import com.dal.housingease.service.AdminMailServiceImpli;
import com.dal.housingease.service.AdminPropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
/**
 * REST controller for managing admin properties.
 */
@RestController
@RequestMapping("/admin/properties")
public class AdminPropertiesController 
{
	private static final Logger logger = LoggerFactory.getLogger(AdminPropertiesController.class);
    private final AdminPropertiesService adminPropertiesService;
    private final AdminMailServiceImpli adminMailService;
    /**
     * Constructor to inject dependencies.
     *
     * @param adminPropertiesService Service for managing properties
     * @param adminMailService       Service for sending verification emails
     */
    @Autowired
    public AdminPropertiesController(AdminPropertiesService adminPropertiesService, AdminMailServiceImpli adminMailService) {
        this.adminPropertiesService = adminPropertiesService;
        this.adminMailService = adminMailService;
    }
    /**
     * Retrieves all properties.
     *
     * @return List of all properties
     */
    @GetMapping
    public List<AdminProperties> getAllProperties() 
    {
    	logger.info("Fetching all properties");
        return adminPropertiesService.getAllProperties();
    }
    /**
     * Retrieves a property by its ID.
     *
     * @param id ID of the property
     * @return ResponseEntity containing the property or 404 status if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdminProperties> getPropertyById(@PathVariable Integer id)
    {
    	logger.info("Fetching property with ID: {}", id);
        return adminPropertiesService.getPropertyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    /**
     * Creates a new property.
     *
     * @param adminProperties Property to be created
     * @return The created property
     */
    @PostMapping
    public AdminProperties createProperty(@RequestBody AdminProperties adminProperties) 
    {
    	 logger.info("Creating new property: {}", adminProperties);
        return adminPropertiesService.saveProperty(adminProperties);
    }
    /**
     * Updates the status of a property.
     *
     * @param id          ID of the property
     * @param statusUpdate Map containing the new status
     * @return ResponseEntity containing the updated property or error message
     */
    @PostMapping("/{id}")
    public ResponseEntity<?> updatePropertyStatus(@PathVariable Integer id, @RequestBody Map<String, String> statusUpdate) {
    	logger.info("Updating status of property with ID: {} to {}", id, statusUpdate.get("status"));
    	try 
        {
            AdminProperties updatedProperty = adminPropertiesService.updatePropertyStatus(id, String.valueOf(Status.valueOf(statusUpdate.get("status"))));
            logger.info("Updated property status successfully for ID: {}", id);
            return ResponseEntity.ok(updatedProperty);
        }
    	catch (Exception e) 
    	{
    		 logger.error("Error updating property status for ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating property status.");
        }
    }
    /**
     * Deletes a property by its ID.
     *
     * @param id ID of the property to be deleted
     * @return ResponseEntity with no content status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Integer id)
    {
    	logger.info("Deleting property with ID: {}", id);
        adminPropertiesService.deleteProperty(id);
        logger.info("Property with ID: {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
    /**
     * Sends a verification email for a property.
     *
     * @param id ID of the property to verify
     * @return ResponseEntity containing success message or error message
     */
    @PostMapping("/{id}/verify")
    public ResponseEntity<String> sendVerificationEmail(@PathVariable Integer id)
    {
    	logger.info("Sending verification email for property with ID: {}", id);
        try 
        {
            adminMailService.sendVerificationEmail(id);
            logger.info("Verification email sent successfully for property ID: {}", id);
            return ResponseEntity.ok("Verification email sent successfully.");
        }
        catch (RuntimeException e) 
        {
        	logger.error("Error sending verification email for property ID: {}", id, e);
            return ResponseEntity.status(500).body("Error sending verification email: " + e.getMessage());
        }
    }
}