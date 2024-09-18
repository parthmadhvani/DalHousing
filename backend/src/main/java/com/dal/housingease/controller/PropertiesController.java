package com.dal.housingease.controller;

import com.dal.housingease.dto.PropertiesDTO;
import com.dal.housingease.dto.PropertiesTableDTO;
import com.dal.housingease.model.Properties;
import com.dal.housingease.service.ImageUploadService;
import com.dal.housingease.service.PropertyService;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST controller for managing properties, including CRUD operations and file uploads.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/properties")
@CrossOrigin
public class PropertiesController 
{
	private static final Logger logger = LoggerFactory.getLogger(PropertiesController.class);
    private final PropertyService propertyService;
    private final ImageUploadService imageUploadService;

    /**
     * Adds a new property with optional image files.
     *
     * @param properties the property details to be added
     * @param files optional image files associated with the property
     * @return a response entity indicating success or failure
     */
    @PostMapping("/add")
    public ResponseEntity<String> addProperty(
            @RequestPart("property") Properties properties,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) 
    {
    	logger.info("Received request to add property with details: {}", properties);
    	/*
        if (files != null && !files.isEmpty()) 
        {
            logger.info("Received {} files for upload", files.size());
        }*/
        try 
        {
            // Save property details and handle image file uploads
            String result = propertyService.saveProperty(properties, files);
            logger.info("Property added successfully with result: {}", result);
            return ResponseEntity.ok(result);
        } 
        catch (Exception e)
        {
        	logger.error("Error adding property with details: {}", properties, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Retrieves properties associated with a specific owner.
     *
     * @param id the ID of the owner
     * @return a response entity containing the list of properties or no content if none found
     */
    @GetMapping("/get_properties/{id}")
    public ResponseEntity<List<PropertiesTableDTO>> findAllByOwnerId(@PathVariable int id) 
    {
    	logger.info("Received request to find properties by owner ID: {}", id);
        List<PropertiesTableDTO> properties = propertyService.findAllByOwnerId(id);
        if (properties != null && !properties.isEmpty())
        {
        	 logger.info("Found {} properties for owner ID: {}", properties.size(), id);
            return ResponseEntity.ok(properties);
        } 
        else
        {
            logger.warn("No properties found for owner ID: {}", id);
            return ResponseEntity.noContent().build();
        }
    }


    /**
     * Updates an existing property with new details and optional image files.
     *
     * @param id the ID of the property to be updated
     * @param properties the new property details
     * @param files optional image files associated with the property
     * @return a response entity indicating success or failure
     * @throws Exception if an error occurs during the update
     */
    @PostMapping("/edit_property/{id}")
    public ResponseEntity<String> editProperty(@PathVariable int id ,
                            @RequestPart("property") Properties properties,
                            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws Exception
    {
    	 logger.info("Received request to edit property with ID: {}", id);
        try
        {
            // Handle image file uploads if provided
            if (files!=null)
            {
            	logger.info("Received {} files for upload", files.size());
                imageUploadService.handleMultipartFile(files, id);
            }
            // Update property details
            String response = propertyService.updateProperty(id , properties);
            logger.info("Property with ID: {} updated successfully", id);
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
        	logger.error("Error updating property with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    /**
     * Retrieves property details by its ID.
     *
     * @param id the ID of the property
     * @return a response entity containing the property details or not found if the property does not exist
     */
    @GetMapping("/edit_property/{id}")
    public ResponseEntity<PropertiesDTO> getPropertyById(@PathVariable int id) 
    {
    	logger.info("Received request to get property by ID: {}", id);
        PropertiesDTO property = propertyService.findByPropertyId(id);
        if (property != null) 
        {
        	logger.info("Property details retrieved for ID: {}", id);
            return ResponseEntity.ok(property);
        } 
        else
        {
        	logger.warn("Property not found for ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves detailed property information by its ID.
     *
     * @param id the ID of the property
     * @return a response entity containing the property details or not found if the property does not exist
     */
    @GetMapping("/get_property_details/{id}")
    public ResponseEntity<PropertiesDTO> getPropertyDetailsById(@PathVariable int id) 
    {
    	logger.info("Received request to get property details by ID: {}", id);
        PropertiesDTO property = propertyService.findByPropertyId(id);
        if (property != null)
        {
        	logger.info("Property details retrieved for ID: {}", id);
            return ResponseEntity.ok(property);
        }
        else 
        {
        	logger.warn("Property details not found for ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a property by its ID.
     *
     * @param id the ID of the property to be deleted
     * @return a response entity indicating the result of the delete operation
     */
    @DeleteMapping("/delete_property/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable int id) 
    {
    	logger.info("Received request to delete property with ID: {}", id);
        propertyService.deletePropertyById(id);
        logger.info("Property with ID: {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}
