package com.dal.housingease.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyNotFoundException extends RuntimeException 
{
	private static final Logger logger = LoggerFactory.getLogger(PropertyNotFoundException.class);
    public PropertyNotFoundException(String message) 
    {
        super(message);
        logger.error("PropertyNotFoundException occurred: {}", message);
    }
}
