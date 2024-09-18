package com.dal.housingease.controller;

import com.dal.housingease.dto.ForgotPasswordDto;
import com.dal.housingease.dto.LoginUserDto;
import com.dal.housingease.dto.RegisterUserDto;
import com.dal.housingease.dto.ResetPasswordDto;
import com.dal.housingease.enums.RoleEnum;
import com.dal.housingease.model.User;
import com.dal.housingease.service.AuthenticationServiceImpli;
import com.dal.housingease.service.JwtServiceImpli;
import com.dal.housingease.service.UserServiceImpli;
import com.dal.housingease.utils.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * REST controller for managing authentication-related operations.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://172.17.0.139:3000")
public class AuthenticationController
{
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final JwtServiceImpli jwtService;
    private final AuthenticationServiceImpli authenticationService;
    private final UserServiceImpli userService;
    /**
     * Constructor to inject dependencies.
     *
     * @param jwtService            Service for managing JWT tokens
     * @param authenticationService Service for managing authentication
     * @param userService           Service for managing user operations
     */
    public AuthenticationController(JwtServiceImpli jwtService, AuthenticationServiceImpli authenticationService , UserServiceImpli userService) 
    {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
		this.userService = userService;
    }
    /**
     * Registers a new user.
     *
     * @param registerUserDto Data transfer object containing user registration information
     * @return ResponseEntity containing the registered user
     */
    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@RequestBody RegisterUserDto registerUserDto)
    {
    	logger.info("Registering new user with email: {}", registerUserDto.getEmail());
    	// Register the user using the authentication service
        User registeredUser = authenticationService.signup(registerUserDto,RoleEnum.valueOf(registerUserDto.getRole()));
        logger.info("User registered successfully with ID: {}", registeredUser.getId());
        return ResponseEntity.ok(registeredUser);
    }
    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param loginUserDto Data transfer object containing login information
     * @return ResponseEntity containing the login response with JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) 
    {
    	logger.info("Authenticating user with email: {}", loginUserDto.getEmail());
    	// Authenticate the user using the authentication service
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        // Generate a JWT token for the authenticated user
        String jwtToken = jwtService.generateToken(authenticatedUser);
        // Create a login response with the generated token and expiration time
        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());
        logger.info("User authenticated successfully. JWT Token generated.");
        return ResponseEntity.ok(loginResponse);	
    }
    /**
     * Initiates the password reset process for a user.
     *
     * @param forgotPasswordDto Data transfer object containing the email for password reset
     * @return ResponseEntity containing the status of the password reset request
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) 
    {
    	logger.info("Password reset requested for email: {}", forgotPasswordDto.getEmail());
        try
        {
        	// Request password reset using the user service
            userService.requestPasswordReset(forgotPasswordDto.getEmail());
            logger.info("Password reset email sent successfully to: {}", forgotPasswordDto.getEmail());
            return ResponseEntity.ok("e-mail sent sucessfully for the user email");
        } 
        catch (IllegalArgumentException e) 
        {
        	logger.error("Error sending password reset email for email: {}", forgotPasswordDto.getEmail(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    /**
     * Resets the password for a user.
     *
     * @param resetPasswordDto Data transfer object containing the password reset information
     * @return ResponseEntity containing the status of the password reset
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) 
    {
    	logger.info("Password reset request received for user ");
        try
        {
            userService.resetPassword(resetPasswordDto);
            logger.info("Password reset successfully for user with email");
            return ResponseEntity.ok("Password reset successfully.");
        } 
        catch (IllegalArgumentException e) 
        {
        	logger.error("Error resetting password for user with email");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
