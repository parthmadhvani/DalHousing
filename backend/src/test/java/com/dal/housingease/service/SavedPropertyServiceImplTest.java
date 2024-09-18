package com.dal.housingease.service;

import com.dal.housingease.dto.PropertyDTO;
import com.dal.housingease.exceptions.UnauthorizedActionException;
import com.dal.housingease.exceptions.UserNotFoundException;
import com.dal.housingease.model.Properties;
import com.dal.housingease.model.PropertyImages;
import com.dal.housingease.model.SavedProperty;
import com.dal.housingease.model.User;
import com.dal.housingease.repository.PropertyImagesRepository;
import com.dal.housingease.repository.SavedPropertyRepository;
import com.dal.housingease.repository.SearchRepository;
import com.dal.housingease.repository.UserRepository;
import com.dal.housingease.utils.PropertyResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SavedPropertyServiceImplTest {

    @Mock
    private SearchRepository searchRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SavedPropertyRepository savedPropertyRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PropertyImagesRepository propertyImagesRepository;

    @InjectMocks
    private SavedPropertyServiceImpl savedPropertyService;


    @Test
    void testSavePropertyForSeeker() {
        User user = new User();
        Properties property = new Properties();
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(searchRepository.findById(anyInt())).thenReturn(Optional.of(property));

        boolean result = savedPropertyService.savePropertyForSeeker(1, 1);

        assertTrue(result);
        verify(savedPropertyRepository, times(1)).save(any(SavedProperty.class));
    }

    @Test
    void testSavePropertyForSeekerUserNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            savedPropertyService.savePropertyForSeeker(1, 1);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testGetSavedPropertiesForUser() {
        Pageable pageable = PageRequest.of(0, 10);
        User user = new User();
        user.setId(1);
        Properties property = new Properties();
        property.setId(1);
        SavedProperty savedProperty = new SavedProperty();
        savedProperty.setUser(user);
        savedProperty.setProperty(property);
        Page<SavedProperty> savedPropertiesPage = new PageImpl<>(Collections.singletonList(savedProperty), pageable, 1);

        PropertyImages propertyImage = new PropertyImages();
        propertyImage.setImage_url("http://image.com/image.jpg");

        when(savedPropertyRepository.findByUserId(anyInt(), any(Pageable.class))).thenReturn(savedPropertiesPage);
        when(propertyImagesRepository.findByProperty_Id(anyInt())).thenReturn(Collections.singletonList(propertyImage));
        when(modelMapper.map(any(Properties.class), eq(PropertyDTO.class))).thenAnswer(invocation -> {
            Properties source = invocation.getArgument(0);
            PropertyDTO dto = new PropertyDTO();
            dto.setId(source.getId());
            return dto;
        });

        PropertyResponse response = savedPropertyService.getSavedPropertiesForUser(1, 0, 10);

        assertNotNull(response);
        assertEquals(1, response.getTotalProperties());
        assertFalse(response.getProperties().isEmpty());
        assertEquals("http://image.com/image.jpg", response.getProperties().get(0).getImageUrls().get(0));
    }

    @Test
    void testSavePropertyForSeekerAlreadySaved() {
        User user = new User();
        user.setId(1);
        Properties property = new Properties();
        property.setId(1);
        SavedProperty savedProperty = new SavedProperty();
        savedProperty.setUser(user);
        savedProperty.setProperty(property);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(searchRepository.findById(anyInt())).thenReturn(Optional.of(property));
        when(savedPropertyRepository.findByUserAndProperty(any(User.class), any(Properties.class))).thenReturn(Optional.of(savedProperty));

        UnauthorizedActionException exception = assertThrows(UnauthorizedActionException.class, () -> {
            savedPropertyService.savePropertyForSeeker(1, 1);
        });

        assertEquals("Property already saved", exception.getMessage());
    }

    @Test
    void testDeleteSavedProperty() {
        User user = new User();
        Properties property = new Properties();
        SavedProperty savedProperty = new SavedProperty();
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(searchRepository.findById(anyInt())).thenReturn(Optional.of(property));
        when(savedPropertyRepository.findByUserAndProperty(any(User.class), any(Properties.class))).thenReturn(Optional.of(savedProperty));
        boolean result = savedPropertyService.deleteSavedProperty(1, 1);
        assertTrue(result);
        verify(savedPropertyRepository, times(1)).delete(savedProperty);
    }
}