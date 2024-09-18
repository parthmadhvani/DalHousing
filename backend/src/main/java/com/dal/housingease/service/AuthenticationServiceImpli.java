package com.dal.housingease.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dal.housingease.dto.LoginUserDto;
import com.dal.housingease.dto.RegisterUserDto;
import com.dal.housingease.enums.RoleEnum;
import com.dal.housingease.exceptions.DuplicateEmailException;
import com.dal.housingease.exceptions.DuplicateMobileNumberException;
import com.dal.housingease.exceptions.DuplicateUserNameException;
import com.dal.housingease.model.Role;
import com.dal.housingease.model.User;
import com.dal.housingease.repository.RoleRepository;
import com.dal.housingease.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * Registers a new user with the specified role.
 *
 * @param input the user registration data transfer object.
 * @param roleEnum the role to be assigned to the user.
 * @return the registered user.
 * @throws DuplicateEmailException if the email already exists.
 * @throws DuplicateMobileNumberException if the mobile number already exists.
 * @throws DuplicateUserNameException if the username already exists.
 */
@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpli implements AuthenticationService
{
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpli.class);
	private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    /**
     * Registers a new user with the specified role.
     *
     * @param input the user registration data transfer object.
     * @param roleEnum the role to be assigned to the user.
     * @return the registered user.
     * @throws DuplicateEmailException if the email already exists.
     * @throws DuplicateMobileNumberException if the mobile number already exists.
     * @throws DuplicateUserNameException if the username already exists.
     */
    public User signup(RegisterUserDto input, RoleEnum roleEnum)
    {

    	// Check for duplicate email
        if (userRepository.findByEmail(input.getEmail()).isPresent())
        {
            logger.error("Email already exists: {}", input.getEmail());
            throw new DuplicateEmailException("Email already exists: " + input.getEmail());
        }

        // Check for duplicate mobile number
        if (userRepository.findByMobileNumber(input.getMobileNumber()).isPresent())
        {
            logger.error("Mobile number already exists: {}", input.getMobileNumber());
            throw new DuplicateMobileNumberException("Mobile number already exists: " + input.getMobileNumber());
        }

        // Check for duplicate username
        if (userRepository.findByUserName(input.getUserName()).isPresent())
        {
            logger.error("Username already exists: {}", input.getUserName());
            throw new DuplicateUserNameException("Username already exists: " + input.getUserName());
        }
        Optional<Role> optionalRole = roleRepository.findByName(roleEnum);
        if (optionalRole.isEmpty())
        {
        	logger.error("Role not found: {}", roleEnum);
            return null;
        }
        String rawPassword = input.getPassword();
        if (rawPassword == null)
        {
            throw new IllegalArgumentException("Password cannot be null");
        }
        var user = new User()
            .setUserName(input.getUserName())
            .setEmail(input.getEmail())
            .setPassword(passwordEncoder.encode(input.getPassword()))
            .setFirstName(input.getFirstName())
            .setLastName(input.getLastName())
            .setMobileNumber(input.getMobileNumber())
            .setRole(optionalRole.get());
        logger.info("User signed up successfully with email");
        return userRepository.save(user);
    }
    /**
     * Authenticates a user with the given login credentials.
     *
     * @param input the user login data transfer object.
     * @return the authenticated user.
     * @throws IllegalArgumentException if the user is not found or the password is invalid.
     */
    public User authenticate(LoginUserDto input)
    {
        logger.info("Attempting to authenticate user with email: {}", input.getEmail());
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Validate password with encryption)
        if (!passwordEncoder.matches(input.getPassword(), user.getPassword()))
        {
        	logger.error("Invalid password attempt for email: {}", input.getEmail());
            throw new IllegalArgumentException("Invalid password");
        }
        logger.info("User authenticated successfully with email: {}", input.getEmail());
        return user;
    }
    /**
     * Retrieves all users.
     *
     * @return a list of all users.
     */
    public List<User> allUsers()
    {
    	logger.info("Fetching all users");
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        logger.info("Found {} users", users.size());
        return users;
    }
}
