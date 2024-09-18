package com.dal.housingease.dto;

import com.dal.housingease.enums.PropertiesEnums;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertiesTableDTO {
    int propertyId;
    String property_heading;
    String street_address;
    PropertiesEnums.Status status;
}
