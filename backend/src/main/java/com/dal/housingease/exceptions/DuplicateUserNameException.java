package com.dal.housingease.exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom exception to be thrown when a username already exists in the system.
 */
public class DuplicateUserNameException extends RuntimeException 
{
	private static final Logger logger = LoggerFactory.getLogger(DuplicateUserNameException.class);

    /**
     * Constructs a new DuplicateUserNameException with the specified detail message.
     *
     * @param message the detail message
     */
    public DuplicateUserNameException(String message) 
    {
        super(message);
        // Log the exception message
        logger.error("Duplicate UserName Exception occurred: {}", message);
    }
}
