package com.dal.housingease.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidPropertyDataException extends RuntimeException 
{
	 private static final Logger logger = LoggerFactory.getLogger(InvalidPropertyDataException.class);
    public InvalidPropertyDataException(String message) 
    {
        super(message);
        logger.error("InvalidPropertyDataException occurred: {}", message);
    }
}
