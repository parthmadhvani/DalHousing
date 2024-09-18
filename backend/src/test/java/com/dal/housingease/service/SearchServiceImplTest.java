package com.dal.housingease.service;

import com.dal.housingease.dto.PropertyDTO;
import com.dal.housingease.model.Properties;
import com.dal.housingease.model.PropertyImages;
import com.dal.housingease.repository.PropertyImagesRepository;
import com.dal.housingease.repository.SavedPropertyRepository;
import com.dal.housingease.repository.SearchRepository;
import com.dal.housingease.repository.UserRepository;
import com.dal.housingease.utils.Checker;
import com.dal.housingease.utils.PropertyResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Integer.valueOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchServiceImplTest {

    @Mock
    private SearchRepository searchRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PropertyImagesRepository propertyImagesRepository;
    @Mock
    private Checker checker;
    @InjectMocks
    private SearchServiceImpl propertyService;

    @Test
    void testFilterPropertiesAscending() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Properties property1 = new Properties();
        property1.setId(1);
        Properties property2 = new Properties();
        property2.setId(2);
        List<Properties> propertyList = new ArrayList<>();
        propertyList.add(property1);
        propertyList.add(property2);
        Page<Properties> properties = new PageImpl<>(propertyList, pageable, 2);

        PropertyImages propertyImage = new PropertyImages();
        propertyImage.setImage_url("http://image.com/image.jpg");

        doNothing().when(checker).validateFilterValues(anyDouble(), anyDouble(), anyInt(), anyInt());

        when(searchRepository.filterProperties(anyString(), anyString(), anyDouble(), anyDouble(), anyInt(), anyInt(), anyString(), anyString(), eq(pageable)))
                .thenReturn(properties);
        when(propertyImagesRepository.findByProperty_Id(anyInt())).thenReturn(Collections.singletonList(propertyImage));
        when(modelMapper.map(any(Properties.class), eq(PropertyDTO.class))).thenAnswer(invocation -> {
            Properties source = invocation.getArgument(0);
            PropertyDTO dto = new PropertyDTO();
            dto.setId(valueOf(source.getId()));
            return dto;
        });

        PropertyResponse response = propertyService.filterProperties("city", "type", 100.0, 200.0, 2, 1, "furnished", "parking", 0, 10, "id", "asc");

        assertNotNull(response);
        assertEquals(2, response.getTotalProperties());
        assertEquals(2, response.getProperties().size());
        assertEquals("http://image.com/image.jpg", response.getProperties().get(0).getImageUrls().get(0));
        assertEquals(valueOf(1), response.getProperties().get(0).getId());
        assertEquals(valueOf(2), response.getProperties().get(1).getId());
    }

    @Test
    void testFilterPropertiesDescending() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Properties property1 = new Properties();
        property1.setId(2);
        Properties property2 = new Properties();
        property2.setId(1);
        List<Properties> propertyList = new ArrayList<>();
        propertyList.add(property1);
        propertyList.add(property2);
        Page<Properties> properties = new PageImpl<>(propertyList, pageable, 2);

        PropertyImages propertyImage = new PropertyImages();
        propertyImage.setImage_url("http://image.com/image.jpg");

        when(searchRepository.filterProperties(anyString(), anyString(), anyDouble(), anyDouble(), anyInt(), anyInt(), anyString(), anyString(), eq(pageable)))
                .thenReturn(properties);
        when(propertyImagesRepository.findByProperty_Id(anyInt())).thenReturn(Collections.singletonList(propertyImage));
        when(modelMapper.map(any(Properties.class), eq(PropertyDTO.class))).thenAnswer(invocation -> {
            Properties source = invocation.getArgument(0);
            PropertyDTO dto = new PropertyDTO();
            dto.setId(valueOf(source.getId()));
            return dto;
        });

        PropertyResponse response = propertyService.filterProperties("city", "type", 100.0, 200.0, 2, 1, "furnished", "parking", 0, 10, "id", "desc");

        assertNotNull(response);
        assertEquals(2, response.getTotalProperties());
        assertEquals(2, response.getProperties().size());
        assertEquals("http://image.com/image.jpg", response.getProperties().get(0).getImageUrls().get(0));
        assertEquals(valueOf(2), response.getProperties().get(0).getId());
        assertEquals(valueOf(1), response.getProperties().get(1).getId());
    }

    @Test
    void testFilterProperties() {
        Pageable pageable = PageRequest.of(0, 10);
        Properties property = new Properties();
        property.setId(1);
        Page<Properties> properties = new PageImpl<>(Collections.singletonList(property), pageable, 1);

        PropertyImages propertyImage = new PropertyImages();
        propertyImage.setImage_url("http://image.com/image.jpg");

        when(searchRepository.filterProperties(anyString(), anyString(), anyDouble(), anyDouble(), anyInt(), anyInt(), anyString(), anyString(), any(Pageable.class)))
                .thenReturn(properties);
        when(propertyImagesRepository.findByProperty_Id(anyInt())).thenReturn(Collections.singletonList(propertyImage));
        when(modelMapper.map(any(Properties.class), eq(PropertyDTO.class))).thenAnswer(invocation -> {
            Properties source = invocation.getArgument(0);
            PropertyDTO dto = new PropertyDTO();
            dto.setId(valueOf(source.getId()));
            return dto;
        });

        PropertyResponse response = propertyService.filterProperties("city", "type", 100.0, 200.0, 2, 1, "furnished", "parking", 0, 10, "id", "asc");

        assertNotNull(response);
        assertEquals(1, response.getTotalProperties());
        assertFalse(response.getProperties().isEmpty());
        assertEquals("http://image.com/image.jpg", response.getProperties().get(0).getImageUrls().get(0));
    }

    @Test
    void testGetAllUniqueCities() {
        List<String> expectedCities = Collections.singletonList("Halifax");

        when(searchRepository.findAllUniqueCities()).thenReturn(expectedCities);

        List<String> actualCities = propertyService.getAllUniqueCities();

        assertEquals(expectedCities, actualCities);
    }

    @Test
    void testGetAllPropertyTypes() {
        List<String> expectedTypes = Collections.singletonList("Apartment");

        when(searchRepository.findAllPropertyTypes()).thenReturn(expectedTypes);

        List<String> actualTypes = propertyService.getAllPropertyTypes();

        assertEquals(expectedTypes, actualTypes);
    }

    @Test
    void testSearchPropertiesWithFilters_WithKeyword() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Properties property = new Properties();
        property.setId(1);
        List<Properties> propertyList = new ArrayList<>();
        propertyList.add(property);
        Page<Properties> properties = new PageImpl<>(propertyList, pageable, 1);

        PropertyImages propertyImage = new PropertyImages();
        propertyImage.setImage_url("http://image.com/image.jpg");

        when(searchRepository.searchPropertiesWithFilters(anyString(), anyString(), anyString(), anyDouble(), anyDouble(), anyInt(), anyInt(), anyString(), anyString(), eq(pageable)))
                .thenReturn(properties);
        when(propertyImagesRepository.findByProperty_Id(anyInt())).thenReturn(Collections.singletonList(propertyImage));
        when(modelMapper.map(any(Properties.class), eq(PropertyDTO.class))).thenAnswer(invocation -> {
            Properties source = invocation.getArgument(0);
            PropertyDTO dto = new PropertyDTO();
            dto.setId(valueOf(source.getId()));
            return dto;
        });


        PropertyResponse response = propertyService.searchPropertiesWithFilters("keyword", "city", "type", 100.0, 200.0, 2, 1, "furnished", "parking", 0, 10, "id", "asc");

        assertNotNull(response);
        assertEquals(1, response.getTotalProperties());
        assertEquals(1, response.getProperties().size());
        assertEquals("http://image.com/image.jpg", response.getProperties().get(0).getImageUrls().get(0));
        assertEquals(valueOf(1), response.getProperties().get(0).getId());
    }

    @Test
    void testSearchPropertiesWithFilters_WithoutKeyword() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Properties property = new Properties();
        property.setId(1);
        List<Properties> propertyList = new ArrayList<>();
        propertyList.add(property);
        Page<Properties> properties = new PageImpl<>(propertyList, pageable, 1);

        PropertyImages propertyImage = new PropertyImages();
        propertyImage.setImage_url("http://image.com/image.jpg");

        when(searchRepository.filterProperties(anyString(), anyString(), anyDouble(), anyDouble(), anyInt(), anyInt(), anyString(), anyString(), eq(pageable)))
                .thenReturn(properties);
        when(propertyImagesRepository.findByProperty_Id(anyInt())).thenReturn(Collections.singletonList(propertyImage));
        when(modelMapper.map(any(Properties.class), eq(PropertyDTO.class))).thenAnswer(invocation -> {
            Properties source = invocation.getArgument(0);
            PropertyDTO dto = new PropertyDTO();
            dto.setId(valueOf(source.getId()));
            return dto;
        });

        PropertyResponse response = propertyService.searchPropertiesWithFilters("", "city", "type", 100.0, 200.0, 2, 1, "furnished", "parking", 0, 10, "id", "asc");

        assertNotNull(response);
        assertEquals(1, response.getTotalProperties());
        assertEquals(1, response.getProperties().size());
        assertEquals("http://image.com/image.jpg", response.getProperties().get(0).getImageUrls().get(0));
        assertEquals(valueOf(1), response.getProperties().get(0).getId());
    }


    @Test
    void testSearchPropertiesWithFilters_KeywordProvided() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Properties property = new Properties();
        property.setId(1);
        Page<Properties> properties = new PageImpl<>(Collections.singletonList(property), pageable, 1);

        PropertyImages propertyImage = new PropertyImages();
        propertyImage.setImage_url("http://image.com/image.jpg");

        when(searchRepository.searchPropertiesWithFilters(
                anyString(), anyString(), anyString(), anyDouble(), anyDouble(), anyInt(), anyInt(), anyString(), anyString(), any(Pageable.class)))
                .thenReturn(properties);
        when(propertyImagesRepository.findByProperty_Id(anyInt())).thenReturn(Collections.singletonList(propertyImage));
        when(modelMapper.map(any(Properties.class), eq(PropertyDTO.class))).thenAnswer(invocation -> {
            Properties source = invocation.getArgument(0);
            PropertyDTO dto = new PropertyDTO();
            dto.setId(valueOf(source.getId()));
            return dto;
        });

        PropertyResponse response = propertyService.searchPropertiesWithFilters(
                "keyword", "city", "type", 100.0, 200.0, 2, 1, "furnished", "parking", 0, 10, "id", "asc");

        assertNotNull(response);
        assertEquals(1, response.getTotalProperties());
        assertEquals(1, response.getProperties().size());
        assertEquals("http://image.com/image.jpg", response.getProperties().get(0).getImageUrls().get(0));
    }

    @Test
    void testSearchPropertiesWithFilters_NoKeyword() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Properties property = new Properties();
        property.setId(1);
        Page<Properties> properties = new PageImpl<>(Collections.singletonList(property), pageable, 1);

        PropertyImages propertyImage = new PropertyImages();
        propertyImage.setImage_url("http://image.com/image.jpg");

        when(searchRepository.filterProperties(
                anyString(), anyString(), anyDouble(), anyDouble(), anyInt(), anyInt(), anyString(), anyString(), any(Pageable.class)))
                .thenReturn(properties);
        when(propertyImagesRepository.findByProperty_Id(anyInt())).thenReturn(Collections.singletonList(propertyImage));
        when(modelMapper.map(any(Properties.class), eq(PropertyDTO.class))).thenAnswer(invocation -> {
            Properties source = invocation.getArgument(0);
            PropertyDTO dto = new PropertyDTO();
            dto.setId(valueOf(source.getId()));
            return dto;
        });

        PropertyResponse response = propertyService.searchPropertiesWithFilters(
                null, "city", "type", 100.0, 200.0, 2, 1, "furnished", "parking", 0, 10, "id", "asc");

        assertNotNull(response);
        assertEquals(1, response.getTotalProperties());
        assertEquals(1, response.getProperties().size());
        assertEquals("http://image.com/image.jpg", response.getProperties().get(0).getImageUrls().get(0));
    }

    @Test
    void testSearchPropertiesWithFiltersAscending() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Properties property = new Properties();
        property.setId(1);
        Page<Properties> properties = new PageImpl<>(Collections.singletonList(property), pageable, 1);

        PropertyImages propertyImage = new PropertyImages();
        propertyImage.setImage_url("http://image.com/image.jpg");

        when(searchRepository.searchPropertiesWithFilters(
                anyString(), anyString(), anyString(), anyDouble(), anyDouble(), anyInt(), anyInt(), anyString(), anyString(), any(Pageable.class)))
                .thenReturn(properties);
        when(propertyImagesRepository.findByProperty_Id(anyInt())).thenReturn(Collections.singletonList(propertyImage));
        when(modelMapper.map(any(Properties.class), eq(PropertyDTO.class))).thenAnswer(invocation -> {
            Properties source = invocation.getArgument(0);
            PropertyDTO dto = new PropertyDTO();
            dto.setId(valueOf(source.getId()));
            return dto;
        });

        PropertyResponse response = propertyService.searchPropertiesWithFilters(
                "keyword", "city", "type", 100.0, 200.0, 2, 1, "furnished", "parking", 0, 10, "id", "asc");

        assertNotNull(response);
        assertEquals(1, response.getTotalProperties());
        assertEquals(1, response.getProperties().size());
        assertEquals("http://image.com/image.jpg", response.getProperties().get(0).getImageUrls().get(0));
    }

    @Test
    void testSearchPropertiesWithFiltersDescending() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Properties property = new Properties();
        property.setId(1);
        Page<Properties> properties = new PageImpl<>(Collections.singletonList(property), pageable, 1);

        PropertyImages propertyImage = new PropertyImages();
        propertyImage.setImage_url("http://image.com/image.jpg");

        when(searchRepository.searchPropertiesWithFilters(
                anyString(), anyString(), anyString(), anyDouble(), anyDouble(), anyInt(), anyInt(), anyString(), anyString(), any(Pageable.class)))
                .thenReturn(properties);
        when(propertyImagesRepository.findByProperty_Id(anyInt())).thenReturn(Collections.singletonList(propertyImage));
        when(modelMapper.map(any(Properties.class), eq(PropertyDTO.class))).thenAnswer(invocation -> {
            Properties source = invocation.getArgument(0);
            PropertyDTO dto = new PropertyDTO();
            dto.setId(valueOf(source.getId()));
            return dto;
        });

        PropertyResponse response = propertyService.searchPropertiesWithFilters(
                "keyword", "city", "type", 100.0, 200.0, 2, 1, "furnished", "parking", 0, 10, "id", "desc");

        assertNotNull(response);
        assertEquals(1, response.getTotalProperties());
        assertEquals(1, response.getProperties().size());
        assertEquals("http://image.com/image.jpg", response.getProperties().get(0).getImageUrls().get(0));
    }
}
