package com.dal.housingease.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.dal.housingease.dto.ChangePasswordDto;
import com.dal.housingease.dto.ResetPasswordDto;
import com.dal.housingease.model.PasswordResetToken;
import com.dal.housingease.model.Role;
import com.dal.housingease.model.User;
import com.dal.housingease.repository.PasswordResetTokenRepository;
import com.dal.housingease.repository.RoleRepository;
import com.dal.housingease.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
/**
 * Implementation of the UserService interface.
 * Provides methods for user management, including retrieving all users,
 * handling password reset requests, changing passwords, and retrieving user and role information by email.
 */
@RequiredArgsConstructor
@Service
public class UserServiceImpli implements UserService
{
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpli.class);
	private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    /**
     * Retrieves a list of all registered users.
     *
     * @return a List of User objects.
     */
    public List<User> allUsers()
    {
        logger.debug("Fetching all users.");
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        logger.info("Fetched {} users.", users.size());
        return users;
    }
    /**
     * Requests a password reset for the user with the specified email.
     * Typically involves sending a password reset email.
     *
     * @param email the email of the user requesting a password reset.
     */
    public void requestPasswordReset(String email)
    {
    	logger.debug("Requesting password reset for email '{}'.", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));

        // Generate reset token
        String resetToken = UUID.randomUUID().toString();
        logger.debug("Generated password reset token '{}'.", resetToken);
        // Create a password reset token and associate it with the user
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(resetToken);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // Example: token expires in 24 hours
        passwordResetToken.setUser(user);

        // Save the password reset token
        passwordResetTokenRepository.save(passwordResetToken);
        logger.info("Password reset token saved for email '{}'.", email);
        // Send the password reset email with the reset token
        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
        logger.info("Password reset email sent to '{}'.", email);
    }
    /**
     * Resets the user's password based on the provided reset password details.
     *
     * @param resetPasswordDto the details required to reset the password.
     */
    public void resetPassword(ResetPasswordDto resetPasswordDto)
    {
    	logger.debug("Resetting the password with token '{}'.", resetPasswordDto.getResetToken());
        // Find the password reset token by token string
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(resetPasswordDto.getResetToken());
        if (passwordResetToken == null)
        {
            logger.error("Invalid reset token: '{}'.", resetPasswordDto.getResetToken());
            throw new IllegalArgumentException("Invalid reset token.");
        }

        // Check if the token is expired
        if (passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now()))
        {
            logger.error("Reset token '{}' has expired.", resetPasswordDto.getResetToken());
            throw new IllegalArgumentException("Reset token has expired.");
        }

        // Set the new password for the user
        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
        userRepository.save(user);
        logger.info("Password successfully reset for user with email '{}'.", user.getEmail());
        // Delete the password reset token
        passwordResetTokenRepository.delete(passwordResetToken);
        logger.info("Password reset token '{}' deleted.", resetPasswordDto.getResetToken());
    }
    /**
     * Changes the user's password based on the provided change password details.
     *
     * @param changePasswordDto the details required to change the password.
     */
    public void changePassword(ChangePasswordDto changePasswordDto)
    {
    	logger.debug("Changing password for email '{}'.", changePasswordDto.getEmail());
        Optional<User> optionalUser = userRepository.findByEmail(changePasswordDto.getEmail());
        if (optionalUser.isEmpty())
        {
        	logger.error("User not found for email: '{}'.", changePasswordDto.getEmail());
            throw new IllegalArgumentException("User not found for email: " +changePasswordDto.getEmail());
        }

        User user = optionalUser.get();

        // Check if the current password matches
        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword()))
        {
        	logger.error("Incorrect current password for email: '{}'.", changePasswordDto.getEmail());
            throw new IllegalArgumentException("Incorrect current password.");
        }

        // Update the password with the new one
        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
        logger.info("Password successfully changed for email '{}'.", changePasswordDto.getEmail());
    }
    /**
     * Finds the user ID associated with the specified email.
     *
     * @param email the email of the user.
     * @return the user ID.
     */
    public Integer findUserIdByEmail(String email)
    {
    	logger.debug("Finding user ID for email '{}'.", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));
        logger.info("Found user ID for email '{}'.", email);
        return user.getId();
    }
    /**
     * Retrieves the role associated with the user identified by the specified email.
     *
     * @param email the email of the user.
     * @return the Role object associated with the user.
     */
    public Role getRoleIdByEmail(String email)
    {
        logger.debug("Getting role for email '{}'.", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));
    	logger.info("Found role for email '{}'.", email);
        return user.getRole();

    }
}
