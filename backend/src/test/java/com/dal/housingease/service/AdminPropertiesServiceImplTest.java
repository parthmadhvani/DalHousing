package com.dal.housingease.service;
import com.dal.housingease.model.AdminProperties;
import com.dal.housingease.repository.AdminPropertiesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminPropertiesServiceImplTest {

    @InjectMocks
    private AdminPropertiesServiceImpl adminPropertiesService;

    @Mock
    private AdminPropertiesRepository adminPropertiesRepository;

    @Mock
    private AdminProperties adminProperties;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProperties() {
        // Arrange
        AdminProperties property1 = new AdminProperties();
        AdminProperties property2 = new AdminProperties();
        when(adminPropertiesRepository.findAll()).thenReturn(Arrays.asList(property1, property2));

        // Act
        List<AdminProperties> properties = adminPropertiesService.getAllProperties();

        // Assert
        assertEquals(2, properties.size());
        verify(adminPropertiesRepository).findAll();
    }

    @Test
    void testGetPropertyByIdFound() {
        // Arrange
        Integer id = 1;
        when(adminPropertiesRepository.findById(id)).thenReturn(Optional.of(adminProperties));

        // Act
        Optional<AdminProperties> property = adminPropertiesService.getPropertyById(id);

        // Assert
        assertTrue(property.isPresent());
        assertEquals(adminProperties, property.get());
        verify(adminPropertiesRepository).findById(id);
    }

    @Test
    void testGetPropertyByIdNotFound() {
        // Arrange
        Integer id = 2;
        when(adminPropertiesRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<AdminProperties> property = adminPropertiesService.getPropertyById(id);

        // Assert
        assertFalse(property.isPresent());
        verify(adminPropertiesRepository).findById(id);
    }

    @Test
    void testSaveProperty() {
        // Arrange
        when(adminPropertiesRepository.save(adminProperties)).thenReturn(adminProperties);

        // Act
        AdminProperties savedProperty = adminPropertiesService.saveProperty(adminProperties);

        // Assert
        assertEquals(adminProperties, savedProperty);
        verify(adminPropertiesRepository).save(adminProperties);
    }

    @Test
    void testDeleteProperty() {
        // Arrange
        Integer id = 3;

        // Act
        adminPropertiesService.deleteProperty(id);

        // Assert
        verify(adminPropertiesRepository).deleteById(id);
    }



    @Test
    void testUpdatePropertyStatusNotFound() {
        // Arrange
        Integer id = 5;
        String status = "Rejected";
        when(adminPropertiesRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                        adminPropertiesService.updatePropertyStatus(id, status),
                "Expected updatePropertyStatus() to throw, but it didn't"
        );
        assertEquals("Property not found with id: " + id, thrown.getMessage());
        verify(adminPropertiesRepository).findById(id);
        verify(adminPropertiesRepository, never()).save(any(AdminProperties.class));
    }

    @Test
    void testUpdatePropertyStatusInvalidStatus() {
        // Arrange
        Integer id = 6;
        String status = "InvalidStatus";
        when(adminPropertiesRepository.findById(id)).thenReturn(Optional.of(adminProperties));

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                        adminPropertiesService.updatePropertyStatus(id, status),
                "Expected updatePropertyStatus() to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("No enum constant"));
        verify(adminPropertiesRepository).findById(id);
        verify(adminPropertiesRepository, never()).save(any(AdminProperties.class));
    }
}
