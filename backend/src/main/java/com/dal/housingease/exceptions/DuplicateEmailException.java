package com.dal.housingease.exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Custom exception to be thrown when a duplicate email is encountered during registration.
 */
public class DuplicateEmailException extends RuntimeException 
{
	private static final Logger logger = LoggerFactory.getLogger(DuplicateEmailException.class);
	/**
     * Constructs a new DuplicateEmailException with the specified detail message.
     *
     * @param message the detail message
     */
    public DuplicateEmailException(String message) 
    {
        super(message);
        logger.error("Already registered with the email", message);
    }
}

