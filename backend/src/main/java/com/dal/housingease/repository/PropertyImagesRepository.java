package com.dal.housingease.repository;

import com.dal.housingease.model.PropertyImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link PropertyImages} entities.
 */
@Repository
public interface PropertyImagesRepository extends JpaRepository<PropertyImages, Integer> {

    /**
     * Finds all images associated with a specific property.
     *
     * @param propertyId the ID of the property for which images are to be retrieved
     * @return a list of {@link PropertyImages} associated with the specified property
     */
    List<PropertyImages> findByProperty_Id(Integer propertyId);
}
