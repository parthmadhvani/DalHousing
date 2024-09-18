package com.dal.housingease.utils;
/**
 * Represents a response containing the JWT token and its expiration time 
 * upon successful login.
 */
public class LoginResponse 
{
	private String token;
    private long expiresIn;
    public String getToken()
    {
        return token;
    }
    public LoginResponse setToken(String token) 
    {
        this.token = token;
        return this;
    }
    public long getExpiresIn() 
    {
        return expiresIn;
    }
    public LoginResponse setExpiresIn(long expiresIn) 
    {
        this.expiresIn = expiresIn;
        return this;
    }
    @Override
    public String toString() 
    {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                ", expiresIn=" + expiresIn +
                '}';
    }
}
