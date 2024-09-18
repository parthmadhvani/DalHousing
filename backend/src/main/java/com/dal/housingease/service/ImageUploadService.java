package com.dal.housingease.service;

import com.dal.housingease.model.PropertyImages;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Service interface for handling image uploads related to properties.
 * Provides methods to upload images, save image records, and manage image data.
 */
@Service
public interface ImageUploadService {

    /**
     * Uploads a single file and returns a map containing details about the uploaded file.
     *
     * @param file the {@link MultipartFile} to be uploaded
     * @return a {@link Map} containing details about the uploaded file
     * @throws Exception if an error occurs during the upload process
     */
    Map upload(MultipartFile file) throws Exception;

    /**
     * Saves a {@link PropertyImages} object to the database.
     *
     * @param propertyImages the {@link PropertyImages} object to be saved
     * @return the saved {@link PropertyImages} object
     */
    PropertyImages saveImage(PropertyImages propertyImages);

    /**
     * Retrieves a {@link PropertyImages} object by its ID.
     *
     * @param id the ID of the {@link PropertyImages} object to be retrieved
     * @return the {@link PropertyImages} object with the specified ID, or null if not found
     */
    PropertyImages findById(int id);

    /**
     * Retrieves all {@link PropertyImages} objects.
     *
     * @return a list of all {@link PropertyImages} objects
     */
    List<PropertyImages> findAll();

    /**
     * Handles the upload of multiple files and associates them with a specific property.
     *
     * @param files a list of {@link MultipartFile} objects representing the files to be uploaded
     * @param savedPropertyId the ID of the property to which the images are associated
     * @return a confirmation message indicating the result of the file handling operation
     */
    String handleMultipartFile(List<MultipartFile> files, int savedPropertyId);
}
