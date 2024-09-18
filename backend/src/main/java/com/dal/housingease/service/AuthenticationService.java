package com.dal.housingease.service;

import com.dal.housingease.dto.LoginUserDto;
import com.dal.housingease.dto.RegisterUserDto;
import com.dal.housingease.enums.RoleEnum;
import com.dal.housingease.model.User;

import java.util.List;
/**

AuthenticationService interface provides methods for user authentication and registration.
This includes user signup, authentication, and retrieval of all users.
*/
public interface AuthenticationService 
{
    User signup(RegisterUserDto input, RoleEnum roleEnum);
    User authenticate(LoginUserDto input);
    List<User> allUsers();
}
