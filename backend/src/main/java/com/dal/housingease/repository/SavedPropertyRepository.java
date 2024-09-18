package com.dal.housingease.repository;

import com.dal.housingease.model.Properties;
import com.dal.housingease.model.SavedProperty;
import com.dal.housingease.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing saved properties for users.
 */
@Repository
public interface SavedPropertyRepository extends JpaRepository<SavedProperty, Integer> {

    /**
     * Finds saved properties by user ID with pagination support.
     *
     * @param userId the ID of the user
     * @param pageable the pagination information
     * @return a page of saved properties for the user
     */
    Page<SavedProperty> findByUserId(Integer userId, Pageable pageable);

    /**
     * Finds a saved property by user and property.
     *
     * @param user the user who saved the property
     * @param property the property that was saved
     * @return an optional containing the saved property if found, or empty if not found
     */
    Optional<SavedProperty> findByUserAndProperty(User user, Properties property);
}
