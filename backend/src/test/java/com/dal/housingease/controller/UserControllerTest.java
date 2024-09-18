package com.dal.housingease.controller;

import com.dal.housingease.dto.ChangePasswordDto;
import com.dal.housingease.enums.RoleEnum;
import com.dal.housingease.model.Role;
import com.dal.housingease.service.UserServiceImpli;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserServiceImpli userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testChangePassword() throws Exception {
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();
        changePasswordDto.setNewPassword("newPassword");
        changePasswordDto.setCurrentPassword("oldPassword");

        doNothing().when(userService).changePassword(changePasswordDto);

        mockMvc.perform(post("/api/auth/change-password")
                .contentType("application/json")
                .content("{\"oldPassword\":\"oldPassword\",\"newPassword\":\"newPassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password reset successfull"));
    }
    @Test
    void testChangePassword_Success() throws Exception {
        // Given: Valid ChangePasswordDto data
        ChangePasswordDto validDto = new ChangePasswordDto();
        validDto.setCurrentPassword("oldPassword123");
        validDto.setNewPassword("newPassword123");

        // Mocking the service method to complete successfully
        doNothing().when(userService).changePassword(validDto);

        // Perform the POST request with valid data
        mockMvc.perform(post("/api/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"oldPassword\": \"oldPassword123\", \"newPassword\": \"newPassword123\"}"))
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.message").value("Password reset successfull"));
    }

    @Test
    void testChangePassword_WithInvalidData() throws Exception {
        // Given: Invalid ChangePasswordDto data
        ChangePasswordDto invalidDto = new ChangePasswordDto();
        invalidDto.setCurrentPassword("");
        invalidDto.setNewPassword("");

        // Mock the service method to throw IllegalArgumentException
        doThrow(new IllegalArgumentException("Invalid data")).when(userService).changePassword(any(ChangePasswordDto.class));

        // Perform the POST request with invalid data
        mockMvc.perform(post("/api/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"oldPassword\": \"\", \"newPassword\": \"\"}"))
                .andExpect(status().isBadRequest()) // Expect 400 Bad Request
                .andExpect(jsonPath("$.message").value("Invalid data"));
    }



    @Test
    void testGetUserIdByEmail() throws Exception {
        String email = "test@example.com";
        Integer userId = 1;

        when(userService.findUserIdByEmail(email)).thenReturn(userId);

        mockMvc.perform(get("/api/auth/user-id")
                .param("email", email))
                .andExpect(status().isOk())
                .andExpect(content().string(userId.toString()));
    }
    @Test
    void testGetRoleIdByEmail() throws Exception {
        // Create a Role object with default values
        Role role = new Role();
        role.setName(RoleEnum.ADMIN);
        role.setDescription("Admin role");
        role.setCreatedAt(new Date());
        role.setUpdatedAt(new Date());

        // Mock the UserService method
        Mockito.when(userService.getRoleIdByEmail("test@example.com")).thenReturn(role);

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/role-id")
                .param("email", "test@example.com")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("ADMIN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Admin role"));
    }

}
