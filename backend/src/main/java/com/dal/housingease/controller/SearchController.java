package com.dal.housingease.controller;

import com.dal.housingease.config.AppConstants;
import com.dal.housingease.service.SearchService;
import com.dal.housingease.utils.PropertyResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for handling property search and filter requests.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/properties")
public class SearchController 
{
	private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    private final SearchService searchService;

    /**
     * Filters properties based on various criteria.
     *
     * @param city the city of the property
     * @param property_type the type of the property
     * @param min_price the minimum price of the property
     * @param max_price the maximum price of the property
     * @param bedrooms the number of bedrooms
     * @param bathrooms the number of bathrooms
     * @param furnishing the furnishing status
     * @param parking the parking availability
     * @param pageNumber the page number for pagination
     * @param pageSize the page size for pagination
     * @param sortBy the field to sort by
     * @param sortDir the direction of sorting (asc/desc)
     * @return a response entity containing the filtered properties
     */
    @GetMapping("/filter")
    public ResponseEntity<PropertyResponse> filterProperties(
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "property_type", required = false) String property_type,
            @RequestParam(value = "min_price", required = false) Double min_price,
            @RequestParam(value = "max_price", required = false) Double max_price,
            @RequestParam(value = "bedrooms", required = false) Integer bedrooms,
            @RequestParam(value = "bathrooms", required = false) Integer bathrooms,
            @RequestParam(value = "furnishing", required = false) String furnishing,
            @RequestParam(value = "parking", required = false) String parking,
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) 
    {
    	logger.info("Received request to filter properties with city: '{}', property_type: '{}', min_price: {}, max_price: {}, bedrooms: {}, bathrooms: {}, furnishing: '{}', parking: '{}', pageNumber: {}, pageSize: {}, sortBy: {}, sortDir: {}", 
                city, property_type, min_price, max_price, bedrooms, bathrooms, furnishing, parking, pageNumber, pageSize, sortBy, sortDir);
        PropertyResponse propertyResponse = searchService.filterProperties(city, property_type, min_price, max_price, bedrooms, bathrooms, furnishing, parking, pageNumber, pageSize, sortBy, sortDir);
        logger.info("Filtered results returned with {} properties", propertyResponse.getProperties().size());
        return new ResponseEntity<>(propertyResponse, HttpStatus.OK);
    }

    /**
     * Retrieves all unique cities with approved properties.
     *
     * @return a response entity containing the list of unique cities
     */
    @GetMapping("/cities")
    public ResponseEntity<List<String>> getAllUniqueCities() 
    {
    	logger.info("Received request to get all unique cities");
        List<String> cities = searchService.getAllUniqueCities();
        logger.info("Retrieved {} unique cities", cities.size());
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

    /**
     * Retrieves all unique property types with approved properties.
     *
     * @return a response entity containing the list of unique property types
     */
    @GetMapping("/propertyTypes")
    public ResponseEntity<List<String>> getAllPropertyTypes() 
    {
    	logger.info("Received request to get all property types");
        List<String> propertyTypes = searchService.getAllPropertyTypes();
        logger.info("Retrieved {} property types", propertyTypes.size());
        return new ResponseEntity<>(propertyTypes, HttpStatus.OK);
    }

    /**
     * Searches and filters properties based on various criteria.
     *
     * @param keyword the keyword for search
     * @param city the city of the property
     * @param property_type the type of the property
     * @param min_price the minimum price of the property
     * @param max_price the maximum price of the property
     * @param bedrooms the number of bedrooms
     * @param bathrooms the number of bathrooms
     * @param furnishing the furnishing status
     * @param parking the parking availability
     * @param pageNumber the page number for pagination
     * @param pageSize the page size for pagination
     * @param sortBy the field to sort by
     * @param sortDir the direction of sorting (asc/desc)
     * @return a response entity containing the searched and filtered properties
     */
    @GetMapping("/search-filter")
    public ResponseEntity<PropertyResponse> searchAndFilterProperties(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "property_type", required = false) String property_type,
            @RequestParam(value = "min_price", required = false) Double min_price,
            @RequestParam(value = "max_price", required = false) Double max_price,
            @RequestParam(value = "bedrooms", required = false) Integer bedrooms,
            @RequestParam(value = "bathrooms", required = false) Integer bathrooms,
            @RequestParam(value = "furnishing", required = false) String furnishing,
            @RequestParam(value = "parking", required = false) String parking,
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir)
    {
    	logger.info("Received request to search and filter properties with keyword: '{}', city: '{}', property_type: '{}', min_price: {}, max_price: {}, bedrooms: {}, bathrooms: {}, furnishing: '{}', parking: '{}', pageNumber: {}, pageSize: {}, sortBy: {}, sortDir: {}", 
                keyword, city, property_type, min_price, max_price, bedrooms, bathrooms, furnishing, parking, pageNumber, pageSize, sortBy, sortDir);
        PropertyResponse propertyResponse = searchService.searchPropertiesWithFilters(
                keyword, city, property_type, min_price, max_price, bedrooms, bathrooms, furnishing, parking, pageNumber, pageSize, sortBy, sortDir);
        logger.info("Search and filter results returned with {} properties", propertyResponse.getProperties().size());
        return new ResponseEntity<>(propertyResponse, HttpStatus.OK);
    }

}