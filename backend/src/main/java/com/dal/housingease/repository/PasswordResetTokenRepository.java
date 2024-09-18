package com.dal.housingease.repository;

import com.dal.housingease.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Repository interface for accessing and managing PasswordResetToken entities in the database.
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> 
{
	/**
     * Finds a password reset token by its token string.
     *
     * @param token the token string.
     * @return the PasswordResetToken entity if found, or null if not.
     */
    PasswordResetToken findByToken(String token);
}

