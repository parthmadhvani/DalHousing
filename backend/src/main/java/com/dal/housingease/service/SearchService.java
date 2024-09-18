package com.dal.housingease.service;

import com.dal.housingease.utils.PropertyResponse;

import java.util.List;

/**
 * Service interface for searching and filtering properties.
 */
public interface SearchService {

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
    PropertyResponse filterProperties(String city, String property_type, Double min_price, Double max_price, Integer bedrooms,
                                      Integer bathrooms, String furnishing, String parking,
                                      Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

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
    PropertyResponse searchPropertiesWithFilters(String keyword, String city, String property_type, Double min_price, Double max_price, Integer bedrooms,
                                                 Integer bathrooms, String furnishing, String parking,
                                                 Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    /**
     * Get all unique cities from the repository.
     *
     * @return a list of unique cities
     */
    List<String> getAllUniqueCities();

    /**
     * Get all property types from the repository.
     *
     * @return a list of property types
     */
    List<String> getAllPropertyTypes();
}
