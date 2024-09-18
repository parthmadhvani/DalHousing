/*
package com.dal.housingease.dto;

import com.dal.housingease.enums.PropertiesEnums;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PropertyDTO {
    private Integer id;
//    private String imageUrl;
    private Double price;
    private String address;
    private String city;
    private String propertyType;
    private PropertiesEnums.Status status;
    private Integer bedrooms;
    private Integer bathrooms;
    private String furnishing;
    private String parking;
    private List<String> imageUrls;
}
*/


package com.dal.housingease.dto;

import com.dal.housingease.enums.PropertiesEnums;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PropertyDTO {
    private Integer id;
    private Double monthly_rent;
    private String street_address;
    private String city;
    private String property_type;
    private String postal_code;
    private PropertiesEnums.Status status;
    private Integer bedrooms;
    private Integer bathrooms;
    private String furnishing;
    private String parking;
    private List<String> imageUrls;
}
