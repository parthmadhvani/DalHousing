package com.dal.housingease.config;

import com.dal.housingease.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Configuration class for setting up security-related beans and services.
 * This includes user authentication, password encoding, and user details service.
 */
@Configuration
public class ApplicationConfiguration 
{
	private final UserRepository userRepository;
	private static final Logger logger = LoggerFactory.getLogger(ApplicationConfiguration.class);

    /**
     * Constructor for initializing the configuration class with the UserRepository.
     *
     * @param userRepository the {@link UserRepository} to be used for user data access
     */
    public ApplicationConfiguration(UserRepository userRepository)
    {
        this.userRepository = userRepository;
        logger.info("ApplicationConfiguration initialized with UserRepository");
    }

    /**
     * Bean for providing user details service used for authentication.
     * This service loads user details by email from the user repository.
     *
     * @return a {@link UserDetailsService} instance
     */
    @Bean
    UserDetailsService userDetailsService() 
    {
    	logger.info("Attempting to load user by email: {}");
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }

    /**
     * Bean for encoding passwords using BCrypt.
     * This provides an instance of {@link BCryptPasswordEncoder}.
     *
     * @return a {@link BCryptPasswordEncoder} instance
     */
    @Bean
    BCryptPasswordEncoder passwordEncoder() 
    {
    	logger.info("Creating BCryptPasswordEncoder bean");
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean for providing authentication manager.
     * This is used for managing authentication processes.
     *
     * @param config the {@link AuthenticationConfiguration} used to configure the authentication manager
     * @return an {@link AuthenticationManager} instance
     * @throws Exception if there is an error creating the authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception 
    {
        return config.getAuthenticationManager();
    }

    /**
     * Bean for providing authentication provider.
     * This uses {@link DaoAuthenticationProvider} configured with user details service and password encoder.
     *
     * @return an {@link AuthenticationProvider} instance
     */
    @Bean
    AuthenticationProvider authenticationProvider() 
    {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}
