package com.dal.housingease.exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom exception to be thrown when a specified role is not found in the system.
 */
public class RoleNotFoundException extends RuntimeException
{
	 private static final Logger logger = LoggerFactory.getLogger(RoleNotFoundException.class);

	 /**
	     * Constructs a new RoleNotFoundException with the specified detail message.
	     *
	     * @param message the detail message
	 */
    public RoleNotFoundException(String message) 
    {
        super(message);
     // Log the exception message
        logger.error("Role Not Found Exception occurred: {}", message);
    }
}
