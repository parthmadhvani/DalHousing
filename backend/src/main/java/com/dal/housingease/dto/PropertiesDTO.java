package com.dal.housingease.dto;

import com.dal.housingease.enums.PropertiesEnums;
import lombok.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class PropertiesDTO {
    String property_type;
    String street_address;
    String city;
    String province;
    String postal_code;
    String unit_number;
    double monthly_rent;
    double security_deposite;
    Date availability;
    String property_heading;
    String full_description;
    int bathrooms;
    int bedrooms;
    String furnishing;
    String parking;
    String email;
    String mobile;
    String latitude;
    String longitude;
    PropertiesEnums.Status status;
    String youtube;
    List<PropertyImagesDTO> images ;
}