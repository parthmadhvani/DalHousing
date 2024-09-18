package com.dal.housingease.repository;

import com.dal.housingease.model.Properties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing property search and filter operations.
 */
@Repository
public interface SearchRepository extends JpaRepository<Properties, Integer> {

    /**
     * Filters properties based on various criteria.
     *
     * @param city the city of the property
     * @param property_type the type of property
     * @param min_price the minimum price
     * @param max_price the maximum price
     * @param bedrooms the number of bedrooms
     * @param bathrooms the number of bathrooms
     * @param furnishing the furnishing status
     * @param parking the parking availability
     * @param pageable the pagination information
     * @return a page of properties matching the criteria
     */
    @Query("SELECT p FROM Properties p WHERE (p.status = 'Approved' OR p.status = 'Pending') AND " +
            "(:city IS NULL OR :city = '' OR LOWER(p.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
            "(:property_type IS NULL OR :property_type = '' OR LOWER(p.property_type) LIKE LOWER(CONCAT('%', :property_type, '%'))) AND " +
            "(:min_price IS NULL OR p.monthly_rent >= :min_price) AND " +
            "(:max_price IS NULL OR p.monthly_rent <= :max_price) AND " +
            "(:bedrooms IS NULL OR p.bedrooms = :bedrooms) AND " +
            "(:bathrooms IS NULL OR p.bathrooms = :bathrooms) AND " +
            "(:furnishing IS NULL OR :furnishing = '' OR p.furnishing = :furnishing) AND " +
            "(:parking IS NULL OR :parking = '' OR p.parking = :parking)")
    Page<Properties> filterProperties(
            @Param("city") String city,
            @Param("property_type") String property_type,
            @Param("min_price") Double min_price,
            @Param("max_price") Double max_price,
            @Param("bedrooms") Integer bedrooms,
            @Param("bathrooms") Integer bathrooms,
            @Param("furnishing") String furnishing,
            @Param("parking") String parking,
            Pageable pageable);


    /**
     * Searches properties based on a keyword and other filters.
     *
     * @param keyword the keyword to search for
     * @param city the city of the property
     * @param property_type the type of property
     * @param min_price the minimum price
     * @param max_price the maximum price
     * @param bedrooms the number of bedrooms
     * @param bathrooms the number of bathrooms
     * @param furnishing the furnishing status
     * @param parking the parking availability
     * @param pageable the pagination information
     * @return a page of properties matching the keyword and filters
     */
    @Query("SELECT p FROM Properties p WHERE (p.status = 'Approved' OR p.status = 'Pending') AND " +
            "(:keyword IS NULL OR :keyword = '' OR (LOWER(p.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.property_type) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.postal_code) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.street_address) LIKE LOWER(CONCAT('%', :keyword, '%')))) AND " +
            "(:city IS NULL OR :city = '' OR LOWER(p.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
            "(:property_type IS NULL OR :property_type = '' OR LOWER(p.property_type) LIKE LOWER(CONCAT('%', :property_type, '%'))) AND " +
            "(:min_price IS NULL OR p.monthly_rent >= :min_price) AND " +
            "(:max_price IS NULL OR p.monthly_rent <= :max_price) AND " +
            "(:bedrooms IS NULL OR p.bedrooms = :bedrooms) AND " +
            "(:bathrooms IS NULL OR p.bathrooms = :bathrooms) AND " +
            "(:furnishing IS NULL OR :furnishing = '' OR p.furnishing = :furnishing) AND " +
            "(:parking IS NULL OR :parking = '' OR p.parking = :parking)")
    Page<Properties> searchPropertiesWithFilters(@Param("keyword") String keyword,
                                                 @Param("city") String city,
                                                 @Param("property_type") String property_type,
                                                 @Param("min_price") Double min_price,
                                                 @Param("max_price") Double max_price,
                                                 @Param("bedrooms") Integer bedrooms,
                                                 @Param("bathrooms") Integer bathrooms,
                                                 @Param("furnishing") String furnishing,
                                                 @Param("parking") String parking,
                                                 Pageable pageable);


    /**
     * Retrieves all unique cities from the properties.
     *
     * @return a list of unique cities
     */
    @Query("SELECT DISTINCT p.city FROM Properties p WHERE p.status = 'Approved'")
    List<String> findAllUniqueCities();

    /**
     * Retrieves all property types from the properties.
     *
     * @return a list of property types
     */
    @Query("SELECT DISTINCT p.property_type FROM Properties p WHERE p.status = 'Approved'")
    List<String> findAllPropertyTypes();

}

