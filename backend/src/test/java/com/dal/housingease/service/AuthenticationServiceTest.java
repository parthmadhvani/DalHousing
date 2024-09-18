package com.dal.housingease.service;

import com.dal.housingease.dto.LoginUserDto;
import com.dal.housingease.dto.RegisterUserDto;
import com.dal.housingease.enums.RoleEnum;
import com.dal.housingease.exceptions.DuplicateEmailException;
import com.dal.housingease.exceptions.DuplicateMobileNumberException;
import com.dal.housingease.exceptions.DuplicateUserNameException;
import com.dal.housingease.model.Role;
import com.dal.housingease.model.User;
import com.dal.housingease.repository.RoleRepository;
import com.dal.housingease.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest
{
	@Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthenticationServiceImpli authenticationService;

    @Test
    void testSignupLister()
    {
        RegisterUserDto registerUserDto = new RegisterUserDto()
                .setEmail("test@example.com")
                .setPassword("password")
                .setUserName("testuser")
                .setRole("SEEKER");

        Role role = new Role();
        when(roleRepository.findByName(RoleEnum.SEEKER)).thenReturn(Optional.of(role));

        String rawPassword = registerUserDto.getPassword();
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1); // Changed to Integer type
            user.setPassword(encodedPassword);
            return user;
        });

        User savedUser = authenticationService.signup(registerUserDto, RoleEnum.SEEKER);

        assertNotNull(savedUser);
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals("testuser", savedUser.getUserName());
        assertFalse(passwordEncoder.matches(rawPassword, savedUser.getPassword()));
        assertEquals(role, savedUser.getRole());

        verify(roleRepository, times(1)).findByName(RoleEnum.SEEKER);
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSignupRoleNotFound() {
        RegisterUserDto registerUserDto = new RegisterUserDto()
                .setEmail("test@example.com")
                .setPassword("password")
                .setUserName("testuser")
                .setRole("SEEKER");

        when(roleRepository.findByName(RoleEnum.SEEKER)).thenReturn(Optional.empty());

        User savedUser = authenticationService.signup(registerUserDto, RoleEnum.SEEKER);

        assertNull(savedUser);

        verify(roleRepository, times(1)).findByName(RoleEnum.SEEKER);
        verify(passwordEncoder, times(0)).encode(any(String.class));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testSignupNullPassword() {
        RegisterUserDto registerUserDto = new RegisterUserDto()
                .setEmail("test@example.com")
                .setPassword(null)
                .setUserName("testuser")
                .setRole("SEEKER");

        Role role = new Role();
        when(roleRepository.findByName(RoleEnum.SEEKER)).thenReturn(Optional.of(role));

        assertThrows(IllegalArgumentException.class, () -> {
            authenticationService.signup(registerUserDto, RoleEnum.SEEKER);
        });

        verify(roleRepository, times(1)).findByName(RoleEnum.SEEKER);
        verify(passwordEncoder, times(0)).encode(any(String.class));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testAuthenticate() {
        LoginUserDto loginUserDto = new LoginUserDto()
                .setEmail("test@example.com")
                .setPassword("password");

        User user = new User()
                .setEmail("test@example.com")
                .setPassword("encodedPassword");

        when(userRepository.findByEmail(loginUserDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginUserDto.getPassword(), user.getPassword())).thenReturn(true);

        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        assertNotNull(authenticatedUser);
        assertEquals(user, authenticatedUser);

        verify(userRepository, times(1)).findByEmail(loginUserDto.getEmail());
        verify(passwordEncoder, times(1)).matches(loginUserDto.getPassword(), user.getPassword());
    }

    @Test
    void testAuthenticateUserNotFound() {
        LoginUserDto loginUserDto = new LoginUserDto()
                .setEmail("test@example.com")
                .setPassword("password");

        when(userRepository.findByEmail(loginUserDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            authenticationService.authenticate(loginUserDto);
        });

        verify(userRepository, times(1)).findByEmail(loginUserDto.getEmail());
        verify(passwordEncoder, times(0)).matches(any(String.class), any(String.class));
    }

    @Test
    void testAuthenticateInvalidPassword() {
        LoginUserDto loginUserDto = new LoginUserDto()
                .setEmail("test@example.com")
                .setPassword("password");

        User user = new User()
                .setEmail("test@example.com")
                .setPassword("encodedPassword");

        when(userRepository.findByEmail(loginUserDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginUserDto.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            authenticationService.authenticate(loginUserDto);
        });

        verify(userRepository, times(1)).findByEmail(loginUserDto.getEmail());
        verify(passwordEncoder, times(1)).matches(loginUserDto.getPassword(), user.getPassword());
    }

    @Test
    void testAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User().setId(1).setUserName("user1")); // Changed to Integer type
        users.add(new User().setId(2).setUserName("user2")); // Changed to Integer type

        when(userRepository.findAll()).thenReturn(users);

        List<User> allUsers = authenticationService.allUsers();

        assertNotNull(allUsers);
        assertEquals(2, allUsers.size());
        assertEquals("user1", allUsers.get(0).getUserName());
        assertEquals("user2", allUsers.get(1).getUserName());

        verify(userRepository, times(1)).findAll();
    }
    @Test
    void testSignupDuplicateEmail() {
        RegisterUserDto registerUserDto = new RegisterUserDto()
                .setEmail("test@example.com")
                .setPassword("password")
                .setUserName("testuser")
                .setMobileNumber("1234567890")
                .setRole("SEEKER");

        when(userRepository.findByEmail(registerUserDto.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(DuplicateEmailException.class, () -> {
            authenticationService.signup(registerUserDto, RoleEnum.SEEKER);
        });

        verify(userRepository, times(1)).findByEmail(registerUserDto.getEmail());
        verify(userRepository, times(0)).findByMobileNumber(any(String.class));
        verify(userRepository, times(0)).findByUserName(any(String.class));
        verify(roleRepository, times(0)).findByName(RoleEnum.SEEKER);
        verify(passwordEncoder, times(0)).encode(any(String.class));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testSignupDuplicateMobileNumber() {
        RegisterUserDto registerUserDto = new RegisterUserDto()
                .setEmail("test@example.com")
                .setPassword("password")
                .setUserName("testuser")
                .setMobileNumber("1234567890")
                .setRole("SEEKER");

        when(userRepository.findByEmail(registerUserDto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByMobileNumber(registerUserDto.getMobileNumber())).thenReturn(Optional.of(new User()));

        assertThrows(DuplicateMobileNumberException.class, () -> {
            authenticationService.signup(registerUserDto, RoleEnum.SEEKER);
        });

        verify(userRepository, times(1)).findByEmail(registerUserDto.getEmail());
        verify(userRepository, times(1)).findByMobileNumber(registerUserDto.getMobileNumber());
        verify(userRepository, times(0)).findByUserName(any(String.class));
        verify(roleRepository, times(0)).findByName(RoleEnum.SEEKER);
        verify(passwordEncoder, times(0)).encode(any(String.class));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testSignupDuplicateUserName() {
        RegisterUserDto registerUserDto = new RegisterUserDto()
                .setEmail("test@example.com")
                .setPassword("password")
                .setUserName("testuser")
                .setMobileNumber("1234567890")
                .setRole("SEEKER");

        when(userRepository.findByEmail(registerUserDto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByMobileNumber(registerUserDto.getMobileNumber())).thenReturn(Optional.empty());
        when(userRepository.findByUserName(registerUserDto.getUserName())).thenReturn(Optional.of(new User()));

        assertThrows(DuplicateUserNameException.class, () -> {
            authenticationService.signup(registerUserDto, RoleEnum.SEEKER);
        });

        verify(userRepository, times(1)).findByEmail(registerUserDto.getEmail());
        verify(userRepository, times(1)).findByMobileNumber(registerUserDto.getMobileNumber());
        verify(userRepository, times(1)).findByUserName(registerUserDto.getUserName());
        verify(roleRepository, times(0)).findByName(RoleEnum.SEEKER);
        verify(passwordEncoder, times(0)).encode(any(String.class));
        verify(userRepository, times(0)).save(any(User.class));
    }
}
