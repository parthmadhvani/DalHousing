package com.dal.housingease.dto;
/**
 * Data Transfer Object for resetting a user's password.
 */
public class ResetPasswordDto 
{
	private String resetToken;
    private String newPassword;
	public String getResetToken() 
	{
		return resetToken;
	}
	public void setResetToken(String resetToken) 
	{
		this.resetToken = resetToken;
	}
	public String getNewPassword() 
	{
		return newPassword;
	}
	public void setNewPassword(String newPassword) 
	{
		this.newPassword = newPassword;
	}
}
