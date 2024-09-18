package com.dal.housingease.utils;

import com.dal.housingease.dto.PropertyDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyResponse 
{
    private List<PropertyDTO> properties;
    private Integer currentPageNumber;
    private Integer currentPageSize;
    private Integer totalProperties;
    private Integer totalPages;
    private Boolean lastPage;
}
