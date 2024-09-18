package com.dal.housingease.controller;

import com.dal.housingease.dto.ForgotPasswordDto;
import com.dal.housingease.dto.LoginUserDto;
import com.dal.housingease.dto.RegisterUserDto;
import com.dal.housingease.dto.ResetPasswordDto;
import com.dal.housingease.enums.RoleEnum;
import com.dal.housingease.model.Role;
import com.dal.housingease.model.User;
import com.dal.housingease.service.AuthenticationServiceImpli;
import com.dal.housingease.service.JwtServiceImpli;
import com.dal.housingease.service.UserServiceImpli;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private JwtServiceImpli jwtService;

    @Mock
    private AuthenticationServiceImpli authenticationService;

    @Mock
    private UserServiceImpli userService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void testRegisterSeeker() throws Exception {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setRole("SEEKER");

        // Create a Role and set it
        Role role = new Role();
        role.setName(RoleEnum.SEEKER);

        User user = new User();
        user.setId(1);
        user.setUserName("testUser");
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setMobileNumber("1234567890");
        user.setRole(role);  // Ensure Role is set

        when(authenticationService.signup(any(RegisterUserDto.class), eq(RoleEnum.SEEKER))).thenReturn(user);

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(registerUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userName").value("testUser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.mobileNumber").value("1234567890"))
                .andReturn();
    }
    @Test
    void testRegisterLister() throws Exception {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setRole("LISTER");

        // Create a Role and set it
        Role role = new Role();
        role.setName(RoleEnum.LISTER);

        User user = new User();
        user.setId(2);
        user.setUserName("listerUser");
        user.setEmail("lister@example.com");
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setMobileNumber("0987654321");
        user.setRole(role);  // Ensure Role is set

        when(authenticationService.signup(any(RegisterUserDto.class), eq(RoleEnum.LISTER))).thenReturn(user);

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(registerUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.userName").value("listerUser"))
                .andExpect(jsonPath("$.email").value("lister@example.com"))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.mobileNumber").value("0987654321"))
                .andReturn();
    }

    @Test
    void testAuthenticate() throws Exception {
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("test@example.com");
        loginUserDto.setPassword("password123");

        User user = new User();
        user.setId(1);
        user.setUserName("testUser");
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setMobileNumber("1234567890");

        // Mock JWT token generation
        when(jwtService.generateToken(any(User.class))).thenReturn("fake-jwt-token");
        when(authenticationService.authenticate(any(LoginUserDto.class))).thenReturn(user);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loginUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.expiresIn").exists())  // Adjust according to actual response
                .andReturn();
    }



    @Test
    void testForgotPassword() throws Exception {
        ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto();
        forgotPasswordDto.setEmail("test@example.com");

        doNothing().when(userService).requestPasswordReset(forgotPasswordDto.getEmail());

        mockMvc.perform(post("/api/auth/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(forgotPasswordDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("e-mail sent sucessfully for the user email"));
    }

    @Test
    void testResetPassword() throws Exception {
        ResetPasswordDto resetPasswordDto = new ResetPasswordDto();
        resetPasswordDto.setNewPassword("newPassword");

        // Ensure the stub uses argument matchers
        doNothing().when(userService).resetPassword(any(ResetPasswordDto.class));

        mockMvc.perform(post("/api/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(resetPasswordDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successfully."));
    }

}
