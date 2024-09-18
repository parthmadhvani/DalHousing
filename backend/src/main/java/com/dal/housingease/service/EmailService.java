package com.dal.housingease.service;
/**
 * Service interface for sending emails during password reset token.
 */
public interface EmailService 
{
    void sendPasswordResetEmail(String email, String resetToken);
}


