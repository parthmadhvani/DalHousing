package com.dal.housingease.service;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
/**
 * Implementation of the EmailService for sending emails.
 */
@RequiredArgsConstructor
@Service
public class EmailServiceImpli implements EmailService
{
	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpli.class);
	private final JavaMailSender javaMailSender;
	/**
     * Sends a password reset email to the specified email address.
     *
     * @param email the recipient's email address.
     * @param resetToken the password reset token to be included in the email.
     */
    public void sendPasswordResetEmail(String email, String resetToken)
    {
        logger.info("Preparing to send password reset email to: {}", email);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset");
        message.setText("Click the following link to reset your password: http://172.17.0.139:3000/reset-password?token=" + resetToken);
        javaMailSender.send(message);
        logger.info("Password reset email sent successfully to: {}", email);
    }

}
