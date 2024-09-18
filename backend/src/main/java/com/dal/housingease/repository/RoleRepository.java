package com.dal.housingease.repository;

import com.dal.housingease.enums.RoleEnum;
import com.dal.housingease.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Repository interface for accessing and managing Role entities in the database.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> 
{
	/**
     * Finds a role by its name.
     *
     * @param name the name of the role.
     * @return an Optional containing the role if found, or an empty Optional if not.
     */
	Optional<Role> findByName(RoleEnum name);

}
