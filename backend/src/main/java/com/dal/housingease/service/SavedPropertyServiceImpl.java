package com.dal.housingease.service;

import com.dal.housingease.dto.PropertyDTO;
import com.dal.housingease.exceptions.PropertyNotFoundException;
import com.dal.housingease.exceptions.UnauthorizedActionException;
import com.dal.housingease.exceptions.UserNotFoundException;
import com.dal.housingease.model.Properties;
import com.dal.housingease.model.PropertyImages;
import com.dal.housingease.model.SavedProperty;
import com.dal.housingease.model.User;
import com.dal.housingease.repository.PropertyImagesRepository;
import com.dal.housingease.repository.SavedPropertyRepository;
import com.dal.housingease.repository.SearchRepository;
import com.dal.housingease.repository.UserRepository;
import com.dal.housingease.utils.PropertyResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for managing saved properties.
 */
@RequiredArgsConstructor
@Service
public class SavedPropertyServiceImpl implements SavedPropertyService {
    private static final Logger logger = LoggerFactory.getLogger(SavedPropertyServiceImpl.class);

    private final SearchRepository searchRepository;
    private final UserRepository userRepository;
    private final SavedPropertyRepository savedPropertyRepository;
    private final ModelMapper modelMapper;
    private final PropertyImagesRepository propertyImagesRepository;

    /**
     * Save a property for a seeker.
     *
     * @param userId     the ID of the user
     * @param propertyId the ID of the property
     * @return true if the property was saved successfully, false otherwise
     * @throws UserNotFoundException       if the user is not found
     * @throws PropertyNotFoundException   if the property is not found
     * @throws UnauthorizedActionException if the property is already saved
     */
    @Override
    public boolean savePropertyForSeeker(Integer userId, Integer propertyId) {
        logger.debug("Attempting to save property {} for user {}", propertyId, userId);

        // Fetch the user and property from their respective repositories
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Properties property = searchRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Property not found"));

        return saveNewPropertyForSeeker(userId, propertyId, user, property);
    }

    /**
     * Helper method to save a new property for a seeker.
     *
     * @param userId     the ID of the user
     * @param propertyId the ID of the property
     * @param user       the user entity
     * @param property   the property entity
     * @return true if the property was saved successfully, false otherwise
     * @throws UnauthorizedActionException if the property is already saved
     */
    private boolean saveNewPropertyForSeeker(Integer userId, Integer propertyId, User user, Properties property) {
        // Check if the property is already saved by the user
        Optional<SavedProperty> existingSavedProperty = savedPropertyRepository.findByUserAndProperty(user, property);
        if (existingSavedProperty.isPresent()) {
            logger.warn("Property with ID {} is already saved by user {}", propertyId, userId);
            throw new UnauthorizedActionException("Property already saved");
        }

        // Save the new property for the user
        SavedProperty savedProperty = SavedProperty.builder()
                .user(user)
                .property(property)
                .build();
        savedPropertyRepository.save(savedProperty);

        logger.info("Property with ID {} saved successfully for user {}", propertyId, userId);
        return true;
    }

    /**
     * Get the saved properties for a user with pagination.
     *
     * @param userId     the ID of the user
     * @param pageNumber the page number
     * @param pageSize   the page size
     * @return a response containing the saved properties and pagination details
     */
    @Override
    public PropertyResponse getSavedPropertiesForUser(Integer userId, Integer pageNumber, Integer pageSize) {
        logger.debug("Fetching saved properties for user {} with page number {} and page size {}", userId, pageNumber, pageSize);

        // Fetch the saved properties for the user with pagination
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<SavedProperty> savedPropertiesPage = savedPropertyRepository.findByUserId(userId, pageable);

        // Convert the saved properties to DTOs
        List<PropertyDTO> properties = convertToDTOs(savedPropertiesPage);

        // Create and return the response
        return createPropertyResponse(userId, properties, savedPropertiesPage);
    }

    /**
     * Convert the saved properties to DTOs.
     *
     * @param savedPropertiesPage the page of saved properties
     * @return a list of property DTOs
     */
    private List<PropertyDTO> convertToDTOs(Page<SavedProperty> savedPropertiesPage) {
        logger.debug("Converting properties to DTOs. Total properties: {}", savedPropertiesPage.getTotalElements());

        // Map each saved property to a PropertyDTO
        List<PropertyDTO> properties = savedPropertiesPage.stream()
                .map(savedProperty -> {
                    PropertyDTO dto = modelMapper.map(savedProperty.getProperty(), PropertyDTO.class);
                    dto.setImageUrls(propertyImagesRepository.findByProperty_Id(savedProperty.getProperty().getId())
                            .stream().map(PropertyImages::getImage_url).collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());

        return properties;
    }

    /**
     * Create a response containing the saved properties and pagination details.
     *
     * @param userId             the ID of the user
     * @param properties         the list of property DTOs
     * @param savedPropertiesPage the page of saved properties
     * @return a response containing the saved properties and pagination details
     */
    private static PropertyResponse createPropertyResponse(Integer userId, List<PropertyDTO> properties, Page<SavedProperty> savedPropertiesPage) {
        logger.debug("Creating property response for user {}.", userId);

        // Build and return the property response
        PropertyResponse propertyResponse = PropertyResponse.builder()
                .properties(properties)
                .currentPageNumber(savedPropertiesPage.getNumber())
                .currentPageSize(savedPropertiesPage.getSize())
                .totalProperties((int) savedPropertiesPage.getTotalElements())
                .totalPages(savedPropertiesPage.getTotalPages())
                .lastPage(savedPropertiesPage.isLast())
                .build();
        logger.info("Fetched {} properties for user {}. Page {} of {}.", properties.size(), userId, savedPropertiesPage.getNumber(), savedPropertiesPage.getTotalPages());
        return propertyResponse;
    }

    /**
     * Delete a saved property for a user.
     *
     * @param userId     the ID of the user
     * @param propertyId the ID of the property
     * @return true if the property was deleted successfully, false otherwise
     * @throws UserNotFoundException     if the user is not found
     * @throws PropertyNotFoundException if the property is not found or not saved
     */
    @Override
    public boolean deleteSavedProperty(Integer userId, Integer propertyId) {
        logger.debug("Attempting to delete saved property {} for user {}", propertyId, userId);

        // Fetch the user and property from their respective repositories
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Properties property = searchRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Property not found"));

        // Check if the saved property exists for the user
        SavedProperty savedProperty = savedPropertyRepository.findByUserAndProperty(user, property)
                .orElseThrow(() -> new PropertyNotFoundException("Saved property not found"));

        // Delete the saved property
        savedPropertyRepository.delete(savedProperty);

        logger.info("Deleted saved property with ID {} for user {}", propertyId, userId);
        return true;
    }
}
