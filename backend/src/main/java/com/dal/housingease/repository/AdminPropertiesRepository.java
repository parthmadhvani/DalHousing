package com.dal.housingease.repository;

import com.dal.housingease.model.AdminProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Repository interface for accessing and managing AdminProperties entities in the database.
 */
@Repository
public interface AdminPropertiesRepository extends JpaRepository<AdminProperties, Integer> 
{
    
}
