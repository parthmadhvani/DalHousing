package com.dal.housingease.utils;

import com.dal.housingease.exceptions.InvalidPropertyDataException;
import com.dal.housingease.model.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class Checker
{
	private static final Logger logger = LoggerFactory.getLogger(Checker.class);
    public boolean checkNegative(Integer number)
    {
    	 logger.debug("Checking if number is negative: {}", number);
        return number < 0;
    }

    public boolean checkZero(Integer number)
    {
    	logger.debug("Checking if number is zero: {}", number);
        return number == 0;
    }

    public boolean checkEmpty(String string)
    {
    	logger.debug("Checking if string is empty: '{}'", string);
        return string == null || string.equals("");
    }

    public boolean checkNegativeZero(Integer number)
    {
    	logger.debug("Checking if number is negative or zero: {}", number);
        return checkNegative(number) || checkZero(number);
    }

    public boolean checkProperties(Properties properties)
    {

        if (checkEmpty(properties.getProperty_type()))
        {
        	logger.error("Invalid property type");
            throw new InvalidPropertyDataException("invalid property type");
        }
        if (checkEmpty(properties.getEmail()))
        {
        	logger.error("Invalid email address");
            throw new InvalidPropertyDataException("invalid email address");
        }
        if (checkEmpty(properties.getStreet_address())) 
        {
        	logger.error("Invalid street address");
            throw new InvalidPropertyDataException("Invalid street address");
        }
        if (checkEmpty(properties.getMobile()))
        {
        	logger.error("Invalid mobile number");
            throw new InvalidPropertyDataException("Invalid mobile number");
        }
        if (checkEmpty(properties.getProvince())) 
        {
        	logger.error("Invalid province");
            throw new InvalidPropertyDataException("Invalid province");
        }
        if (checkEmpty(properties.getCity())) 
        {
        	logger.error("Invalid city");
            throw new InvalidPropertyDataException("Invalid city");
        }
        if (checkEmpty(properties.getPostal_code()))
        {
        	logger.error("Invalid postal code");
            throw new InvalidPropertyDataException("Invalid postal code");
        }
        if (checkEmpty(properties.getUnit_number()))
        {
        	logger.error("Invalid unit number");
            throw new InvalidPropertyDataException("Invalid unit number");
        }
        if (properties.getMonthly_rent()==null)
        {
        	logger.error("Invalid rent");
            throw new InvalidPropertyDataException("Invalid rent");
        }
        if (checkNegativeZero(properties.getMonthly_rent().intValue())) 
        {
        	logger.error("Invalid rent");
            throw new InvalidPropertyDataException("Invalid rent");
        }
        if (properties.getSecurity_deposite()==null)
        {
        	 logger.error("Invalid security deposit");
            throw new InvalidPropertyDataException("Invalid security deposit");
        }
        if (checkNegativeZero(properties.getSecurity_deposite().intValue())) 
        {
        	 logger.error("Invalid security deposit");
            throw new InvalidPropertyDataException("Invalid security deposit");
        }
        if (properties.getAvailability() == null) 
        {
        	logger.error("Invalid availability date");
            throw new InvalidPropertyDataException("Invalid availability date");
        }
        if (checkEmpty(properties.getLatitude())) 
        {
        	logger.error("Please choose the address from the autofill");
            throw new InvalidPropertyDataException("Please choose the address from the autofill");
        }
        if (checkEmpty(properties.getLongitude()))
        {
        	logger.error("Please choose the address from the autofill");
            throw new InvalidPropertyDataException("Please choose the address from the autofill");
        }
        if (checkEmpty(properties.getProperty_heading()))
        {
        	 logger.error("Invalid heading");
            throw new InvalidPropertyDataException("Invalid heading");
        }
        if (checkEmpty(properties.getFull_description())) 
        {
        	logger.error("Invalid description");
            throw new InvalidPropertyDataException("Invalid description");
        }
        if (properties.getBedrooms()==null)
        {
        	 logger.error("Invalid number for bedrooms");
            throw new InvalidPropertyDataException("Invalid number for bedrooms");
        }
        if (checkNegativeZero(properties.getBedrooms())) 
        {
        	 logger.error("Invalid number for bedrooms");
            throw new InvalidPropertyDataException("Invalid number for bedrooms");
        }
        if (properties.getBathrooms()==null)
        {
        	 logger.error("Invalid number for bathrooms");
            throw new InvalidPropertyDataException("Invalid number for bathrooms");
        }
        if (checkNegativeZero(properties.getBathrooms())) 
        {
        	 logger.error("Invalid number for bathrooms");
            throw new InvalidPropertyDataException("Invalid number for bathrooms");
        }
        if (checkEmpty(properties.getParking())) 
        {
        	 logger.error("Invalid value for parking");
            throw new InvalidPropertyDataException("Invalid value for parking");
        }
        if (checkEmpty(properties.getFurnishing())) 
        {
        	logger.error("Invalid value for furnishing");
            throw new InvalidPropertyDataException("Invalid value for furnishing");
        }
        if (properties.getMobile().length()!=10)
        {
        	 logger.error("Mobile number is not valid");
            throw new InvalidPropertyDataException("Mobile number is not valid");
        }
        logger.info("Property data is valid");
        return true;
    }

    public boolean checkImages(List<MultipartFile> files) {
        // Check if the list is null or has fewer than 4 images
        if (files == null || files.size() < 4)
        {
        	 logger.error("Minimum 4 images must be uploaded.");
            throw new InvalidPropertyDataException("Minimum 4 images must be uploaded.");
        }

        if (files.size()>10)
        {
        	 logger.error("Maximum 10 images can be uploaded.");
            throw new InvalidPropertyDataException("Maximum 10 images can be uploaded.");
        }

        // Check the size of each file
        for (MultipartFile file : files) {
            long fileSizeInBytes = file.getSize();
            logger.debug("Checking image file size: {} bytes", fileSizeInBytes);
            System.out.println("Size:"+fileSizeInBytes);
            // Example: Check if file size exceeds 5 MB (5 * 1024 * 1024 bytes)
            if (fileSizeInBytes > 2 * 1024 * 1024)
            {
            	logger.error("Each image must be less than 2 MB. Size: {} bytes", fileSizeInBytes);
                throw new InvalidPropertyDataException("Each image must be less than 2 MB.");
            }
        }
        logger.info("Image files are valid");
        return true;
    }

    public void validateFilterValues(Double minPrice, Double maxPrice, Integer bedrooms, Integer bathrooms) {
        if (minPrice != null && minPrice <= 0)
        {
        	logger.error("Invalid minimum price as it cannot be negative or zero. Value: {}", minPrice);
            throw new InvalidPropertyDataException("Invalid minimum price as it cannot be negative or zero.");
        }
        if (maxPrice != null && maxPrice <= 0)
        {
        	logger.error("Invalid maximum price as it cannot be negative or zero. Value: {}", maxPrice);
            throw new InvalidPropertyDataException("Invalid minimum price as it cannot be negative or zero.");
        }
        if (bedrooms != null && bedrooms <= 0) 
        {
        	logger.error("Invalid number of bedrooms as it cannot be negative or zero. Value: {}", bedrooms);
            throw new InvalidPropertyDataException("Invalid number of bedrooms as it cannot be negative or zero.");
        }
        if (bathrooms != null && bathrooms <= 0) 
        {
        	 logger.error("Invalid number of bathrooms as it cannot be negative or zero. Value: {}", bathrooms);
            throw new InvalidPropertyDataException("Invalid number of bathrooms as it cannot be negative or zero.");
        }
        logger.info("Filter values are valid");
    }
}
