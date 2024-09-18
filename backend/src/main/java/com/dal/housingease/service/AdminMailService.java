package com.dal.housingease.service;
/**
 * Service interface for sending administrative emails related to property verification.
 */

public interface AdminMailService
{
	 /**
     * Sends a verification email for the specified property ID.
     *
     * @param propertyId the ID of the property for which the verification email is to be sent.
     */
    void sendVerificationEmail(Integer propertyId);
}
