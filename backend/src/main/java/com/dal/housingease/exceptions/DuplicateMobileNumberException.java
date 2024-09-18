package com.dal.housingease.exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom exception to be thrown when a mobile number already exists in the system.
 */
public class DuplicateMobileNumberException extends RuntimeException 
{
	private static final Logger logger = LoggerFactory.getLogger(DuplicateMobileNumberException.class);
    /**
     * Constructs a new DuplicateMobileNumberException with the specified detail message.
     *
     * @param message the detail message
     */
    public DuplicateMobileNumberException(String message) 
    {
        super(message);
         // Log the exception message
        logger.error("DuplicateMobileNumberException occurred: {}", message);
    }
}
