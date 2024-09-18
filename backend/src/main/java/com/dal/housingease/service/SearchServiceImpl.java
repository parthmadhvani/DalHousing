package com.dal.housingease.service;

import com.dal.housingease.dto.PropertyDTO;
import com.dal.housingease.model.Properties;
import com.dal.housingease.model.PropertyImages;
import com.dal.housingease.repository.PropertyImagesRepository;
import com.dal.housingease.repository.SearchRepository;
import com.dal.housingease.utils.Checker;
import com.dal.housingease.utils.PropertyResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing property searches and filters.
 */
@RequiredArgsConstructor
@Service
public class SearchServiceImpl implements SearchService 
{
	private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);
    private final SearchRepository searchRepository;
    private final ModelMapper modelMapper;
    private final PropertyImagesRepository propertyImagesRepository;
    private final Checker checker;

    /**
     * Filter properties based on various criteria.
     *
     * @param city          the city of the property
     * @param property_type the type of property
     * @param min_price     the minimum price
     * @param max_price     the maximum price
     * @param bedrooms      the number of bedrooms
     * @param bathrooms     the number of bathrooms
     * @param furnishing    the furnishing status
     * @param parking       the parking availability
     * @param pageNumber    the page number for pagination
     * @param pageSize      the page size for pagination
     * @param sortBy        the field to sort by
     * @param sortDir       the direction to sort (asc or desc)
     * @return a response containing the filtered properties and pagination details
     */
    @Override
    public PropertyResponse filterProperties(String city, String property_type, Double min_price, Double max_price, Integer bedrooms, Integer bathrooms, String furnishing, String parking, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        logger.debug("Filtering properties for city '{}', property type '{}', min price '{}', max price '{}', bedrooms '{}', bathrooms '{}', furnishing '{}', parking '{}', page number {}, page size {}, sort by '{}', sort direction '{}'",
                city, property_type, min_price, max_price, bedrooms, bathrooms, furnishing, parking, pageNumber, pageSize, sortBy, sortDir);

        // Validate filter values
        checker.validateFilterValues(min_price, max_price, bedrooms, bathrooms);

        // Create pageable object for pagination and sorting
        Pageable pageable = getPageable(pageNumber, pageSize, sortBy, sortDir);

        // Fetch filtered properties from the repository
        Page<Properties> pageProperties = searchRepository.filterProperties(city, property_type, min_price, max_price, bedrooms, bathrooms, furnishing, parking, pageable);
        logger.info("Filter completed with {} properties found for city '{}', property type '{}'", pageProperties.getTotalElements(), city, property_type);

        // Create and return property response
        return createPropertyResponse(pageProperties);
    }

    /**
     * Create a Pageable object for pagination and sorting.
     *
     * @param pageNumber the page number for pagination
     * @param pageSize   the page size for pagination
     * @param sortBy     the field to sort by
     * @param sortDir    the direction to sort (asc or desc)
     * @return the pageable object
     */
    private static Pageable getPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        logger.debug("Creating pageable object with page number {}, page size {}, sort by '{}', sort direction '{}'", pageNumber, pageSize, sortBy, sortDir);
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return pageable;
    }

    /**
     * Create a PropertyResponse object from a Page of Properties.
     *
     * @param pageProperties the page of properties
     * @return the property response
     */
    private PropertyResponse createPropertyResponse(Page<Properties> pageProperties) {
    	logger.debug("Converting properties to DTOs. Total properties: {}", pageProperties.getTotalElements());

        // Convert properties to DTOs
        List<PropertyDTO> propertyDTOs = convertToDTOs(pageProperties);

        // Build and return property response
        PropertyResponse propertyResponse = PropertyResponse.builder()
                .properties(propertyDTOs)
                .currentPageNumber(pageProperties.getNumber())
                .currentPageSize(pageProperties.getSize())
                .totalProperties((int) pageProperties.getTotalElements())
                .totalPages(pageProperties.getTotalPages())
                .lastPage(pageProperties.isLast())
                .build();
        logger.debug("Property response constructed. Total properties: {}, Total pages: {}", propertyResponse.getTotalProperties(), propertyResponse.getTotalPages());
        return propertyResponse;
    }

    /**
     * Convert a Page of Properties to a list of PropertyDTOs.
     *
     * @param pageProperties the page of properties
     * @return the list of property DTOs
     */
    private List<PropertyDTO> convertToDTOs(Page<Properties> pageProperties) {
        logger.debug("Mapping properties to DTOs");
        List<Properties> properties = pageProperties.getContent();

        // Map each property to a PropertyDTO
        List<PropertyDTO> propertyDTOs = properties.stream()
                .map(property -> {
                    PropertyDTO dto = modelMapper.map(property, PropertyDTO.class);
                    dto.setImageUrls(propertyImagesRepository.findByProperty_Id(property.getId()).stream()
                            .map(PropertyImages::getImage_url).collect(Collectors.toList()));

                    logger.debug("Mapped property with ID {} to DTO", property.getId());
                    return dto;
                }).collect(Collectors.toList());

        logger.debug("Converted {} properties to DTOs", propertyDTOs.size());
        return propertyDTOs;
    }

    /**
     * Get all unique cities from the repository.
     *
     * @return a list of unique cities
     */
    @Override
    public List<String> getAllUniqueCities()
    {
    	logger.debug("Fetching all unique cities.");
        return searchRepository.findAllUniqueCities();
    }

    /**
     * Get all property types from the repository.
     *
     * @return a list of property types
     */
    @Override
    public List<String> getAllPropertyTypes()
    {
    	 logger.debug("Fetching all property types.");
        return searchRepository.findAllPropertyTypes();
    }

    /**
     * Search properties with filters.
     *
     * @param keyword     the keyword to search
     * @param city        the city of the property
     * @param property_type the type of property
     * @param min_price   the minimum price
     * @param max_price   the maximum price
     * @param bedrooms    the number of bedrooms
     * @param bathrooms   the number of bathrooms
     * @param furnishing  the furnishing status
     * @param parking     the parking availability
     * @param pageNumber  the page number for pagination
     * @param pageSize    the page size for pagination
     * @param sortBy      the field to sort by
     * @param sortDir     the direction to sort (asc or desc)
     * @return a response containing the filtered properties and pagination details
     */
    @Override
    public PropertyResponse searchPropertiesWithFilters(String keyword, String city, String property_type, Double min_price, Double max_price, Integer bedrooms,
            Integer bathrooms, String furnishing, String parking, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

    	 logger.debug("Searching properties with filters: keyword '{}', city '{}', property type '{}', min price '{}', max price '{}', bedrooms '{}', bathrooms '{}', furnishing '{}', parking '{}', page number {}, page size {}, sort by '{}', sort direction '{}'",
                 keyword, city, property_type, min_price, max_price, bedrooms, bathrooms, furnishing, parking, pageNumber, pageSize, sortBy, sortDir);

        // Validate filter values
        checker.validateFilterValues(min_price, max_price, bedrooms, bathrooms);

        // Create pageable object for pagination and sorting
        Pageable pageable = getPageable(pageNumber, pageSize, sortBy, sortDir);

        // Fetch properties based on keyword and filters
        Page<Properties> pageProperties = (keyword != null && !keyword.isEmpty()) ?
                searchRepository.searchPropertiesWithFilters(keyword, city, property_type, min_price, max_price, bedrooms, bathrooms, furnishing, parking, pageable) :
                searchRepository.filterProperties(city, property_type, min_price, max_price, bedrooms, bathrooms, furnishing, parking, pageable);

        logger.info("Search with filters completed with {} properties found.", pageProperties.getTotalElements());

        // Create and return property response
        return createPropertyResponse(pageProperties);
    }


}
