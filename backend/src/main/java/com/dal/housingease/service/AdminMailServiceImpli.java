package com.dal.housingease.service;

import com.dal.housingease.model.AdminProperties;
import com.dal.housingease.repository.AdminPropertiesRepository;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;
/**
 * Implementation of the AdminMailService for sending verification emails related to admin properties.
 */

@Service
@RequiredArgsConstructor
public class AdminMailServiceImpli implements AdminMailService
{
	private static final Logger logger = LoggerFactory.getLogger(AdminPropertiesServiceImpl.class);
    private final JavaMailSender javaMailSender;
    private final AdminPropertiesRepository adminPropertiesRepository;
    /**
     * Sends a verification email for a specified property ID.
     *
     * @param propertyId the ID of the property for which the verification email is to be sent.
     */
    public void sendVerificationEmail(Integer propertyId)
    {
    	logger.info("Attempting to send verification email for property ID: {}", propertyId);
    	// Fetch the property from the repository
        Optional<AdminProperties> property = adminPropertiesRepository.findById(propertyId);
        // Check if the property exists
        if (property.isPresent())
        {
        	// Get the email address associated with the property
            String email = property.get().getEmail();
            logger.info("Property found. Sending verification email to: {}", email);
            // Create a simple email message
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Listing Verification");
            message.setText("We have received your listing. Please fill out the following form to complete the verification process: https://forms.gle/P7onAahiz9a2i3gF7");
            logger.info("Verification email sent successfully to: {}", email);
            // Send the email
            javaMailSender.send(message);
        }
        else
        {
        	logger.error("Property not found with ID: {}", propertyId);
            throw new RuntimeException("Property not found with id: " + propertyId);
        }
    }
}
