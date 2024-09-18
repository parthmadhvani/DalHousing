package com.dal.housingease.utils;

import com.dal.housingease.dto.PropertiesDTO;
import com.dal.housingease.dto.PropertiesTableDTO;
import com.dal.housingease.dto.PropertyImagesDTO;
import com.dal.housingease.model.Properties;
import com.dal.housingease.model.PropertyImages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DTOConverter 
{
	private static final Logger logger = LoggerFactory.getLogger(DTOConverter.class);
    public PropertiesTableDTO convertToPropertiesTableDTO(Properties property) 
    {
        logger.debug("Converting Property to PropertiesTableDTO: {}", property);
        return PropertiesTableDTO.builder()
                .propertyId(property.getId())
                .property_heading(property.getProperty_heading())
                .street_address(property.getStreet_address())
                .status(property.getStatus())
                .build();
    }

    public PropertiesDTO convertToPropertiesDTO(Properties property)
    {
    	logger.debug("Converting Property to PropertiesDTO: {}", property);
        List<PropertyImagesDTO> propertyImagesDTOs = convertToPropertyImagesDTOList(property.getImages());

        return PropertiesDTO.builder()
                .property_type(property.getProperty_type())
                .street_address(property.getStreet_address())
                .city(property.getCity())
                .province(property.getProvince())
                .postal_code(property.getPostal_code())
                .unit_number(property.getUnit_number())
                .monthly_rent(property.getMonthly_rent())
                .security_deposite(property.getSecurity_deposite())
                .availability(property.getAvailability())
                .property_heading(property.getProperty_heading())
                .full_description(property.getFull_description())
                .bathrooms(property.getBathrooms())
                .bedrooms(property.getBedrooms())
                .furnishing(property.getFurnishing())
                .email(property.getEmail())
                .mobile(property.getMobile())
                .status(property.getStatus())
                .latitude(property.getLatitude())
                .longitude(property.getLongitude())
                .parking(property.getParking())
                .youtube(property.getYoutube())
                .images(propertyImagesDTOs)
                .build();
    }

    public List<PropertyImagesDTO> convertToPropertyImagesDTOList(List<PropertyImages> propertyImagesList)
    {
    	logger.debug("Converting list of PropertyImages to list of PropertyImagesDTO: {}", propertyImagesList);
        List<PropertyImagesDTO> listToReturn = new ArrayList<>();
        for (PropertyImages propertyImages : propertyImagesList) {
            listToReturn.add(convertToPropertyImagesDTO(propertyImages));
        }
        logger.debug("Converted list of PropertyImagesDTO: {}", listToReturn);
        return listToReturn;
    }

    public PropertyImagesDTO convertToPropertyImagesDTO(PropertyImages propertyImages) 
    {
    	logger.debug("Converting PropertyImages to PropertyImagesDTO: {}", propertyImages);
        PropertyImagesDTO propertyImagesDTO = new PropertyImagesDTO();
        propertyImagesDTO.setImage_url(propertyImages.getImage_url());
        logger.debug("Converted PropertyImagesDTO: {}", propertyImagesDTO);
        return propertyImagesDTO;    }
}
