package com.dal.housingease.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Custom exception to be thrown when a user is not found in the system.
 */
public class UserNotFoundException extends RuntimeException
{
	private static final Logger logger = LoggerFactory.getLogger(UserNotFoundException.class);
	/**
     * Constructs a new UserNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public UserNotFoundException(String message) 
    {
        super(message);
        logger.error("User Not Found Exception occurred: {}", message);
    }
}
