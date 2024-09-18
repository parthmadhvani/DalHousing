package com.dal.housingease.service;

import com.dal.housingease.model.AdminProperties;
import com.dal.housingease.repository.AdminPropertiesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AdminMailServiceTest {

    @InjectMocks
    private AdminMailServiceImpli adminMailService;
    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private AdminPropertiesRepository adminPropertiesRepository;
    @Mock
    private AdminProperties adminProperties;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendVerificationEmailSuccess() {
        // Arrange
        Integer propertyId = 1;
        String email = "test@example.com";
        when(adminPropertiesRepository.findById(propertyId)).thenReturn(Optional.of(adminProperties));
        when(adminProperties.getEmail()).thenReturn(email);

        // Act
        adminMailService.sendVerificationEmail(propertyId);

        // Assert
        verify(adminPropertiesRepository).findById(propertyId);
        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendVerificationEmailPropertyNotFound() {
        // Arrange
        Integer propertyId = 2;
        when(adminPropertiesRepository.findById(propertyId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                        adminMailService.sendVerificationEmail(propertyId),
                "Expected sendVerificationEmail() to throw, but it didn't"
        );
        assertEquals("Property not found with id: " + propertyId, thrown.getMessage());
        verify(adminPropertiesRepository).findById(propertyId);
        verify(javaMailSender, never()).send(any(SimpleMailMessage.class));
    }


    @Test
    void testSendVerificationEmailWithEmptyEmail() {
        // Arrange
        Integer propertyId = 4;
        when(adminPropertiesRepository.findById(propertyId)).thenReturn(Optional.of(adminProperties));
        when(adminProperties.getEmail()).thenReturn("");

        // Act
        adminMailService.sendVerificationEmail(propertyId);

        // Assert
        verify(adminPropertiesRepository).findById(propertyId);
        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }
}

