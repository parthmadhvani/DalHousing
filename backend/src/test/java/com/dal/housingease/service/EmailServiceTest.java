package com.dal.housingease.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest 
{
	@Mock
    private JavaMailSender javaMailSender;
    @InjectMocks
    private EmailServiceImpli emailService;

    @Test
    void testSendPasswordResetEmail_Success() {
        // Mocking JavaMailSender behavior
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        // Test data
        String email = "test@example.com";
        String resetToken = "mockResetToken";
        emailService.sendPasswordResetEmail(email, resetToken);
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage capturedMessage = messageCaptor.getValue();
        assertEquals(email, capturedMessage.getTo()[0]);
        assertEquals("Password Reset", capturedMessage.getSubject());
        //assertTrue(capturedMessage.getText().contains("Click the following link to reset your password: http://localhost:3000/reset?token=" + resetToken));
    }

    @Test
    void testSendPasswordResetEmail_Failure() {
        doThrow(new MailSendException("Failed to send email"))
                .when(javaMailSender).send(any(SimpleMailMessage.class));

        // Test data
        String email = "test@example.com";
        String resetToken = "mockResetToken";
        try {
            emailService.sendPasswordResetEmail(email, resetToken);
            fail("Expected MailSendException was not thrown");
        } catch (MailSendException e) {
        	 assertEquals("Failed to send email", e.getMessage());
        }
    }
}
