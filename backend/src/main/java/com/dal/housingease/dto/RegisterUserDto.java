package com.dal.housingease.dto;
/**
 * Data Transfer Object for registering a new user.
 */
public class RegisterUserDto 
{
	private String email;
    private String password;
    private String userName;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String role;
    
    public String getEmail() 
    {
        return email;
    }

    public RegisterUserDto setEmail(String email) 
    {
        this.email = email;
        return this;
    }

    public String getPassword() 
    {
        return password;
    }

    public RegisterUserDto setPassword(String password) 
    {
        this.password = password;
        return this;
    }

    public String getUserName() 
    {
        return userName;
    }

    public RegisterUserDto setUserName(String userName) 
    {
        this.userName = userName;
        return this;
    }

    public String getFirstName() 
    {
        return firstName;
    }

    public RegisterUserDto setFirstName(String firstName) 
    {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() 
    {
        return lastName;
    }

    public RegisterUserDto setLastName(String lastName) 
    {
        this.lastName = lastName;
        return this;
    }

    public String getMobileNumber() 
    {
        return mobileNumber;
    }

    public RegisterUserDto setMobileNumber(String mobileNumber) 
    {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public String getRole() 
    {
        return role;
    }

    public RegisterUserDto setRole(String role) 
    {
        this.role = role;
        return this;
    }

    @Override
    public String toString() 
    {
        return "RegisterUserDto{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
