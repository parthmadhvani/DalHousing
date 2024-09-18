package com.dal.housingease.controller;

import com.dal.housingease.dto.ChangePasswordDto;
import com.dal.housingease.model.Role;
import com.dal.housingease.service.UserServiceImpli;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
/**
 * REST controller for managing user-related operations.
 */
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://172.17.0.139:3000")
@RestController
public class UserController 
{
	private final UserServiceImpli userService;
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	/**
     * Constructor to inject dependencies.
     *
     * @param userService Service for managing user operations
     */
    public UserController(UserServiceImpli userService)
    {
        this.userService = userService;
    }
    /**
     * Changes the password for a user.
     *
     * @param changePasswordDto Data transfer object containing the email and new password
     * @return ResponseEntity containing a message about the password change status
     */
    @PostMapping("/change-password")
    public ResponseEntity<HashMap> changePassword(@RequestBody ChangePasswordDto changePasswordDto) 
    {
    	HashMap<String, String> responseMap = new HashMap();
    	logger.info("Received request to change password for user with email: {}", changePasswordDto.getEmail());
        try
        {
            userService.changePassword(changePasswordDto);
            responseMap.put("message", "Password reset successfull");
            logger.info("Password successfully changed for user with email: {}", changePasswordDto.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(responseMap);
        } 
        catch (IllegalArgumentException e) 
        {
            logger.error("Error changing password for user with email: {}", changePasswordDto.getEmail(), e);
        	responseMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
        }
    }
    /**
     * Retrieves the user ID by email.
     *
     * @param email Email of the user
     * @return ResponseEntity containing the user ID
     */
    @GetMapping("/user-id")
    public ResponseEntity<Integer> getUserIdByEmail(@RequestParam String email) 
    {
    	logger.info("Received request to get user ID by email: {}", email);
    	// Find the user ID by email using the user service
        Integer userId = userService.findUserIdByEmail(email);
        logger.info("User ID retrieved for email {}: {}", email, userId);
        return ResponseEntity.ok(userId);
    }
    /**
     * Retrieves the role by email.
     *
     * @param email Email of the user
     * @return Role of the user
     */
    @GetMapping("/role-id")
    public Role getRoleIdByEmail(@RequestParam String email)
    {
    	logger.info("Received request to get role ID by email: {}", email);
    	// Get the role by email using the user service
        return userService.getRoleIdByEmail(email);
    }
}
