package com.dal.housingease.service;

import com.dal.housingease.dto.PropertiesDTO;
import com.dal.housingease.dto.PropertiesTableDTO;
import com.dal.housingease.exceptions.InvalidPropertyDataException;
import com.dal.housingease.model.Properties;
import com.dal.housingease.repository.PropertiesRepository;
import com.dal.housingease.utils.Checker;
import com.dal.housingease.utils.DTOConverter;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PropertyServiceImpli implements PropertyService

{

    private static final Logger logger = LoggerFactory.getLogger(PropertyServiceImpli.class);
    private final PropertiesRepository propertiesRepository;

    private final DTOConverter propertyDTOConverter;

    private final ImageUploadService imageUploadService;

    private final Checker checker;

    /**
     * Saves a new property and handles associated images.
     *
     * @param properties the {@link Properties} object containing property details
     * @param files a list of {@link MultipartFile} objects representing images to be associated with the property
     * @return a confirmation message indicating the result of the save operation
     */
    @Override
    public String saveProperty(Properties properties  , List<MultipartFile> files)

    {

        logger.info("Saving property with details: {}", properties);

        // Validate the properties and images
        checker.checkProperties(properties);

        checker.checkImages(files);

        // Save the property
        Properties savedProperty = propertiesRepository.save(properties);

        if (savedProperty == null)

        {

            logger.error("Failed to save property: {}", properties);
            throw new InvalidPropertyDataException("Could not add property.");

        }
        int savedPropertyId = savedProperty.getId();

        logger.info("Property saved with ID: {}. Proceeding to upload images.", savedPropertyId);
        return imageUploadService.handleMultipartFile(files, savedPropertyId);

    }

    /**
     * Retrieves a property by its ID.
     *
     * @param id the ID of the property to be retrieved
     * @return the {@link Properties} object with the specified ID, or null if not found
     */
    @Override
    public Properties findById(int id)

    {

        logger.info("Finding property by ID: {}", id);

        if (checker.checkNegativeZero(id))

        {

            logger.warn("Invalid ID provided: {}", id);
            return null;

        }

        Optional<Properties> optional = propertiesRepository.findById(id);

        return optional.orElse(null);

    }

    /**
     * Retrieves all properties.
     *
     * @return a list of all {@link Properties} objects
     */
    @Override
    public List<Properties> findAll()

    {

        logger.info("Finding all properties");
        return propertiesRepository.findAll();

    }

    /**
     * Retrieves all properties owned by a specific owner.
     *
     * @param id the ID of the owner
     * @return a list of {@link PropertiesTableDTO} objects representing properties owned by the specified owner
     */
    @Override
    public List<PropertiesTableDTO> findAllByOwnerId(int id)

    {

        logger.info("Finding all properties by owner ID: {}", id);
        if (checker.checkNegativeZero(id)) {

            logger.warn("Invalid owner ID provided: {}", id);
            return null;

        }

        List<Properties> propertiesList = propertiesRepository.findAllByOwnerId(id);
        logger.info("Properties found: {}", propertiesList);

        return propertiesList.stream()

                .map(propertyDTOConverter::convertToPropertiesTableDTO)

                .toList();

    }

    /**
     * Updates an existing property.
     *
     * @param id the ID of the property to be updated
     * @param property the {@link Properties} object containing updated details of the property
     * @return a confirmation message indicating the result of the update operation
     */
    @Override
    public String updateProperty(int id, Properties property)

    {

        logger.info("Updating property with ID: {}", id);
        int result;

        // Validate the property details
        checker.checkProperties(property);

        // Update the property
        result = propertiesRepository.updateProperty(

                property.getAvailability(),

                property.getLatitude(),

                property.getLongitude(),

                property.getProperty_type(),

                property.getMonthly_rent(),

                property.getSecurity_deposite(),

                property.getStreet_address(),

                property.getCity(),

                property.getFull_description(),

                property.getPostal_code(),

                property.getProperty_heading(),

                property.getProvince(),

                String.valueOf(property.getStatus()),

                property.getUnit_number(),

                id,

                property.getMobile(),

                property.getEmail(),

                property.getBathrooms(),

                property.getBedrooms(),

                property.getParking(),

                property.getFurnishing()

        );

        return result == 0 ? "Could not update the property." : "Property updated successfully.";

    }

    /**
     * Retrieves property details by its ID and converts it to a DTO.
     *
     * @param id the ID of the property to be retrieved
     * @return a {@link PropertiesDTO} object containing detailed information about the property
     */
    @Override
    public PropertiesDTO findByPropertyId(int id)

    {

        logger.info("Finding property DTO by property ID: {}", id);
        if (checker.checkNegativeZero(id)) {

            logger.warn("Invalid property ID provided: {}", id);
            return null;

        }

        Properties properties = findById(id);
        logger.info("Property DTO found");
        return propertyDTOConverter.convertToPropertiesDTO(properties);

    }

    /**
     * Deletes a property by its ID.
     *
     * @param id the ID of the property to be deleted
     */
    @Override
    public void deletePropertyById(int id)

    {

        logger.info("Deleting property by ID: {}", id);
        propertiesRepository.deleteById(id);
        logger.info("Property deleted successfully by ID: {}", id);
    }

}