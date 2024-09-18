package com.dal.housingease.service;

import com.dal.housingease.dto.ChangePasswordDto;
import com.dal.housingease.dto.ResetPasswordDto;
import com.dal.housingease.model.Role;
import com.dal.housingease.model.User;

import java.util.List;
/**
 * UserService interface provides methods for user management.
 * This includes retrieving all users, handling password reset requests,
 * changing passwords, and retrieving user and role information by email.
 */
public interface UserService 
{
    List<User> allUsers();
    void requestPasswordReset(String email);
    void resetPassword(ResetPasswordDto resetPasswordDto);
    void changePassword(ChangePasswordDto changePasswordDto);
    Integer findUserIdByEmail(String email);
    Role getRoleIdByEmail(String email);
}


