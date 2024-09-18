package com.dal.housingease.service;

import com.dal.housingease.dto.ChangePasswordDto;
import com.dal.housingease.dto.ResetPasswordDto;
import com.dal.housingease.model.PasswordResetToken;
import com.dal.housingease.model.Role;
import com.dal.housingease.model.User;
import com.dal.housingease.repository.PasswordResetTokenRepository;
import com.dal.housingease.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserServiceImpli userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        when(userRepository.findAll()).thenReturn(users);
        List<User> result = userService.allUsers();
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testRequestPasswordReset() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordResetTokenRepository.save(any(PasswordResetToken.class))).thenReturn(new PasswordResetToken());
        userService.requestPasswordReset("test@example.com");
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(passwordResetTokenRepository, times(1)).save(any(PasswordResetToken.class));
        verify(emailService, times(1)).sendPasswordResetEmail(eq("test@example.com"), anyString());
    }

    @Test
    void testRequestPasswordReset_UserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.requestPasswordReset("test@example.com");
        });
        assertEquals("User not found for email: test@example.com", exception.getMessage());
    }

    @Test
    void testResetPassword() {
        User user = new User();
        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusHours(1));
        when(passwordResetTokenRepository.findByToken("valid-token")).thenReturn(token);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        ResetPasswordDto dto = new ResetPasswordDto();
        dto.setResetToken("valid-token");
        dto.setNewPassword("newPassword");
        userService.resetPassword(dto);
        assertEquals("encodedNewPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
        verify(passwordResetTokenRepository, times(1)).delete(token);
    }

    @Test
    void testResetPassword_InvalidToken() {
        when(passwordResetTokenRepository.findByToken("invalid-token")).thenReturn(null);
        ResetPasswordDto dto = new ResetPasswordDto();
        dto.setResetToken("invalid-token");
        dto.setNewPassword("newPassword");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.resetPassword(dto);
        });
        assertEquals("Invalid reset token.", exception.getMessage());
    }

    @Test
    void testResetPassword_ExpiredToken() {
        PasswordResetToken token = new PasswordResetToken();
        token.setExpiryDate(LocalDateTime.now().minusHours(1));
        when(passwordResetTokenRepository.findByToken("expired-token")).thenReturn(token);
        ResetPasswordDto dto = new ResetPasswordDto();
        dto.setResetToken("expired-token");
        dto.setNewPassword("newPassword");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.resetPassword(dto);
        });
        assertEquals("Reset token has expired.", exception.getMessage());
    }

    @Test
    void testChangePassword() {
        User user = new User();
        user.setPassword("encodedCurrentPassword");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("currentPassword", "encodedCurrentPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setEmail("test@example.com");
        dto.setCurrentPassword("currentPassword");
        dto.setNewPassword("newPassword");
        userService.changePassword(dto);
        assertEquals("encodedNewPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testChangePassword_UserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setEmail("test@example.com");
        dto.setCurrentPassword("currentPassword");
        dto.setNewPassword("newPassword");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.changePassword(dto);
        });
        assertEquals("User not found for email: test@example.com", exception.getMessage());
    }

    @Test
    void testChangePassword_IncorrectCurrentPassword() {
        User user = new User();
        user.setPassword("encodedCurrentPassword");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("incorrectCurrentPassword", "encodedCurrentPassword")).thenReturn(false);
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setEmail("test@example.com");
        dto.setCurrentPassword("incorrectCurrentPassword");
        dto.setNewPassword("newPassword");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.changePassword(dto);
        });
        assertEquals("Incorrect current password.", exception.getMessage());
    }

    @Test
    void testFindUserIdByEmail() {
        User user = new User();
        user.setId(1);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        Integer userId = userService.findUserIdByEmail("test@example.com");
        assertEquals(1, userId);
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testFindUserIdByEmail_UserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.findUserIdByEmail("test@example.com");
        });
        assertEquals("User not found for email: test@example.com", exception.getMessage());
    }



    @Test
    void testGetRoleIdByEmail_UserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getRoleIdByEmail("test@example.com");
        });
        assertEquals("User not found for email: test@example.com", exception.getMessage());
    }
    @Test
    void testGetRoleIdByEmail() {
        // Define the email within the test method
        String testEmail = "test@example.com";

        // Arrange
        User user = new User();
        Role role = new Role();
        user.setRole(role);
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(user));

        // Act
        Role result = userService.getRoleIdByEmail(testEmail);

        // Assert
        assertEquals(role, result);
        verify(userRepository, times(1)).findByEmail(testEmail);
    }
}
