package com.dal.housingease.service;

import com.dal.housingease.dto.PropertiesDTO;
import com.dal.housingease.dto.PropertiesTableDTO;
import com.dal.housingease.dto.PropertyImagesDTO;
import com.dal.housingease.enums.PropertiesEnums;
import com.dal.housingease.exceptions.InvalidPropertyDataException;
import com.dal.housingease.model.Properties;
import com.dal.housingease.model.PropertyImages;
import com.dal.housingease.repository.PropertiesRepository;
import com.dal.housingease.utils.Checker;
import com.dal.housingease.utils.DTOConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PropertyServiceImpliTest {

    @InjectMocks
    private PropertyServiceImpli propertyService;

    @Mock
    private PropertiesRepository propertiesRepository;
    @Mock
    ImageUploadServiceImpli imageUploadService;
    @Mock
    private Checker checker;

    @Mock
    private DTOConverter propertyDTOConverter;

    private Properties property;
    private PropertiesDTO propertiesDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        property = new Properties();
        property.setId(1);
        Date currentDate = new Date();
        property.setAvailability(currentDate);
        property.setLatitude("1.0");
        property.setLongitude("1.0");
        property.setOwnerId(1);
        property.setProperty_type("Type");
        property.setMonthly_rent(1000.0);
        property.setSecurity_deposite(500.0);
        property.setStreet_address("Street");
        property.setCity("City");
        property.setFull_description("Description");
        property.setPostal_code("12345");
        property.setProperty_heading("Heading");
        property.setProvince("Province");
        property.setStatus(PropertiesEnums.Status.Pending);
        property.setUnit_number("101");
        property.setBathrooms(1);
        property.setBedrooms(1);
        property.setImages(new ArrayList<>());

        PropertyImages image = new PropertyImages();
        image.setImage_url("image_url");
        List<PropertyImages> images = new ArrayList<>();
        images.add(image);
        property.setImages(images);

        propertiesDTO = new PropertiesDTO();
        propertiesDTO.setProperty_type("Type");
        propertiesDTO.setStreet_address("Street");
        propertiesDTO.setImages(new ArrayList<>());
        PropertyImagesDTO propertyImagesDTO = new PropertyImagesDTO();
        propertiesDTO.getImages().add(propertyImagesDTO);
        propertiesDTO.getImages().get(0).setImage_url("image_url");
    }

    @Test
    public void testSaveProperty_Success() {
        Properties properties = new Properties();
        List<MultipartFile> files = List.of(mock(MultipartFile.class)); // Mock a list of MultipartFile

        Properties savedProperty = new Properties();
        savedProperty.setId(1);

        when(propertiesRepository.save(properties)).thenReturn(savedProperty);
        when(imageUploadService.handleMultipartFile(files, 1)).thenReturn("Success");

        String result = propertyService.saveProperty(properties, files);

        verify(checker).checkProperties(properties);
        verify(checker).checkImages(files);
        verify(propertiesRepository).save(properties);
        verify(imageUploadService).handleMultipartFile(files, 1);

        assertEquals("Success", result);
    }

    @Test
    public void testSaveProperty_InvalidProperty() {
        Properties properties = new Properties();
        List<MultipartFile> files = List.of(mock(MultipartFile.class));

        // Mocking the check to fail
        doThrow(new InvalidPropertyDataException("Invalid properties data")).when(checker).checkProperties(properties);

        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            propertyService.saveProperty(properties, files);
        });

        assertEquals("Invalid properties data", exception.getMessage());
        verify(propertiesRepository, never()).save(any());
        verify(imageUploadService, never()).handleMultipartFile(any(), anyInt());
    }

    @Test
    public void testSaveProperty_InvalidImages() {
        Properties properties = new Properties();
        List<MultipartFile> files = List.of(mock(MultipartFile.class));

        // Mocking properties check to pass
        when(checker.checkProperties(properties)).thenReturn(true);
        // Mocking images check to fail
        doThrow(new InvalidPropertyDataException("Invalid image data")).when(checker).checkImages(files);

        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            propertyService.saveProperty(properties, files);
        });

        assertEquals("Invalid image data", exception.getMessage());
        verify(propertiesRepository, never()).save(any());
        verify(imageUploadService, never()).handleMultipartFile(any(), anyInt());
    }

    @Test
    public void testFindById_ValidId() {
        when(checker.checkNegativeZero(1)).thenReturn(false);
        when(propertiesRepository.findById(1)).thenReturn(Optional.of(property));

        Properties result = propertyService.findById(1);

        assertNotNull(result);
        assertEquals(property, result);
        verify(propertiesRepository, times(1)).findById(1);
    }

    @Test
    public void testSaveProperty_SavedPropertyIsNull() {
        Properties properties = new Properties();
        List<MultipartFile> files = List.of(mock(MultipartFile.class));

        // Mocking properties check to pass
        when(checker.checkProperties(properties)).thenReturn(true);
        // Mocking images check to pass
        when(checker.checkImages(files)).thenReturn(true);
        // Mocking save method to return null
        when(propertiesRepository.save(properties)).thenReturn(null);

        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            propertyService.saveProperty(properties, files);
        });

        assertEquals("Could not add property.", exception.getMessage());
        verify(imageUploadService, never()).handleMultipartFile(any(), anyInt());
    }

    @Test
    public void testFindById_InvalidId() {
        when(checker.checkNegativeZero(1)).thenReturn(true);

        Properties result = propertyService.findById(1);

        assertNull(result);
        verify(propertiesRepository, never()).findById(1);
    }

    @Test
    public void testFindAll() {
        List<Properties> propertiesList = new ArrayList<>();
        propertiesList.add(property);
        when(propertiesRepository.findAll()).thenReturn(propertiesList);

        List<Properties> result = propertyService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(propertiesRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllByOwnerId_ValidId() {
        when(checker.checkNegativeZero(1)).thenReturn(false);
        List<Properties> propertiesList = new ArrayList<>();
        propertiesList.add(property);
        when(propertiesRepository.findAllByOwnerId(1)).thenReturn(propertiesList);
        when(propertyDTOConverter.convertToPropertiesTableDTO(any(Properties.class))).thenReturn(new PropertiesTableDTO());

        List<PropertiesTableDTO> result = propertyService.findAllByOwnerId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(propertiesRepository, times(1)).findAllByOwnerId(1);
    }

    @Test
    public void testFindAllByOwnerId_InvalidId() {
        when(checker.checkNegativeZero(1)).thenReturn(true);

        List<PropertiesTableDTO> result = propertyService.findAllByOwnerId(1);

        assertNull(result);
        verify(propertiesRepository, never()).findAllByOwnerId(1);
    }

    @Test
    public void testUpdateProperty_ValidProperty() {
        when(checker.checkNegativeZero(1)).thenReturn(false);
        when(checker.checkProperties(property)).thenReturn(true);
        when(propertiesRepository.updateProperty(
                eq(property.getAvailability()),
                eq(property.getLatitude()),
                eq(property.getLongitude()),
                eq(property.getProperty_type()),
                eq(property.getMonthly_rent()),
                eq(property.getSecurity_deposite()),
                eq(property.getStreet_address()),
                eq(property.getCity()),
                eq(property.getFull_description()),
                eq(property.getPostal_code()),
                eq(property.getProperty_heading()),
                eq(property.getProvince()),
                eq(String.valueOf(property.getStatus())),
                eq(property.getUnit_number()),
                eq(1),
                eq(property.getMobile()),
                eq(property.getEmail()),
                eq(property.getBathrooms()),
                eq(property.getBedrooms()),
                eq(property.getParking()),
                eq(property.getFurnishing())
        )).thenReturn(1);

        String result = propertyService.updateProperty(1, property);

        assertEquals(result,"Property updated successfully.");
    }

    @Test
    public void testFindByPropertyId_InvalidId() {
        when(checker.checkNegativeZero(1)).thenReturn(true);

        PropertiesDTO result = propertyService.findByPropertyId(1);

        assertNull(result);
        verify(propertiesRepository, never()).findById(1);
    }

    @Test
    public void testFindByPropertyId_ValidId() {
        when(checker.checkNegativeZero(1)).thenReturn(false);
        when(propertiesRepository.findById(1)).thenReturn(Optional.of(property));
        when(propertyDTOConverter.convertToPropertiesDTO(any(Properties.class))).thenReturn(propertiesDTO);

        PropertiesDTO result = propertyService.findByPropertyId(1);

        assertNotNull(result);
        assertEquals(property.getProperty_type(), result.getProperty_type());
        assertEquals(property.getStreet_address(), result.getStreet_address());

        // Handle the case where images might be empty
        if (!property.getImages().isEmpty()) {
            assertEquals(property.getImages().get(0).getImage_url(), result.getImages().get(0).getImage_url());
        } else {
            assertTrue(result.getImages().isEmpty());
        }

        verify(propertiesRepository, times(1)).findById(1);
    }

    @Test
    public void testDeletePropertyById_ValidId() {
        when(checker.checkNegativeZero(1)).thenReturn(false);

        propertyService.deletePropertyById(1);

        verify(propertiesRepository, times(1)).deleteById(1);
    }
}
