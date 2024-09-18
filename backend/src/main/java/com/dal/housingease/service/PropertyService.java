package com.dal.housingease.service;

import com.dal.housingease.dto.PropertiesDTO;
import com.dal.housingease.dto.PropertiesTableDTO;
import com.dal.housingease.model.Properties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service interface for managing {@link Properties}.
 */
@Service
public interface PropertyService {

    /**
     * Saves a new property and optionally uploads associated images.
     *
     * @param properties the {@link Properties} object containing the details of the property to be saved
     * @param files a list of {@link MultipartFile} objects representing images to be associated with the property
     * @return a confirmation message indicating the result of the save operation
     */
    String saveProperty(Properties properties, List<MultipartFile> files);

    /**
     * Retrieves a property by its ID.
     *
     * @param id the ID of the property to be retrieved
     * @return the {@link Properties} object with the specified ID
     */
    Properties findById(int id);

    /**
     * Retrieves all properties.
     *
     * @return a list of all {@link Properties} objects
     */
    List<Properties> findAll();

    /**
     * Retrieves all properties owned by a specific owner.
     *
     * @param id the ID of the owner
     * @return a list of {@link PropertiesTableDTO} objects representing properties owned by the specified owner
     */
    List<PropertiesTableDTO> findAllByOwnerId(int id);

    /**
     * Updates an existing property.
     *
     * @param id the ID of the property to be updated
     * @param property the {@link Properties} object containing updated details of the property
     * @return a confirmation message indicating the result of the update operation
     */
    String updateProperty(int id, Properties property);

    /**
     * Retrieves property details by its ID.
     *
     * @param id the ID of the property to be retrieved
     * @return a {@link PropertiesDTO} object containing detailed information about the property
     */
    PropertiesDTO findByPropertyId(int id);

    /**
     * Deletes a property by its ID.
     *
     * @param id the ID of the property to be deleted
     */
    void deletePropertyById(int id);
}
