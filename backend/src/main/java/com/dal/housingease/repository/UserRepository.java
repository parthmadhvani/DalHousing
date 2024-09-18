package com.dal.housingease.repository;

import com.dal.housingease.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Repository interface for accessing and managing User entities in the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> 
{
	/**
     * Finds a user by their email.
     *
     * @param email the email of the user.
     * @return an Optional containing the user if found, or an empty Optional if not.
     */
    Optional<User> findByEmail(String email);
    /**
     * Finds a user by their mobile number.
     *
     * @param mobileNumber the mobile number of the user.
     * @return an Optional containing the user if found, or an empty Optional if not.
     */
    Optional<User> findByMobileNumber(String mobileNumber);
    /**
     * Finds a user by their username.
     *
     * @param userName the username of the user.
     * @return an Optional containing the user if found, or an empty Optional if not.
     */
	Optional<User> findByUserName(String userName);  
}