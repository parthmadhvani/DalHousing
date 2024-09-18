package com.dal.housingease.exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Custom exception to be thrown when an unauthorized action is attempted.
 */
public class UnauthorizedActionException extends RuntimeException 
{
    private static final Logger logger = LoggerFactory.getLogger(UnauthorizedActionException.class);
    /**
     * Constructs a new UnauthorizedActionException with the specified detail message.
     *
     * @param message the detail message
     */
    public UnauthorizedActionException(String message) 
    {
        super(message);
        logger.error("UnauthorizedActionException occurred: {}", message);
    }
}
