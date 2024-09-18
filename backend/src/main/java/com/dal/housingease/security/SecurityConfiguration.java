package com.dal.housingease.security;

import com.dal.housingease.config.JwtAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
/**
 * Security configuration class for setting up HTTP security and CORS settings.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration 
{
	private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);
	private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    /**
     * Constructs the SecurityConfiguration with the provided JwtAuthenticationFilter and AuthenticationProvider.
     *
     * @param jwtAuthenticationFilter the JWT authentication filter.
     * @param authenticationProvider the authentication provider.
     */
    public SecurityConfiguration(
        JwtAuthenticationFilter jwtAuthenticationFilter,
        AuthenticationProvider authenticationProvider
    ) 
    {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        logger.info("SecurityConfiguration initialized with JwtAuthenticationFilter and AuthenticationProvider");
    }
    /**
     * Configures the security filter chain.
     *
     * @param http the HttpSecurity to configure.
     * @return the configured SecurityFilterChain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception 
    {
    	logger.info("Configuring security filter chain");
    	http.cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/**")
                .permitAll()
                .requestMatchers("/properties/search-filter")
                .permitAll()
                .requestMatchers("/properties/propertyTypes")
                .permitAll()
                .requestMatchers("/properties/filter")
                .permitAll()
                .requestMatchers("/properties/cities")
                .permitAll()
                .requestMatchers("/properties/get_property_details/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    	logger.info("Security filter chain configured successfully");
        return http.build();
    }
    /**
     * Configures the CORS settings.
     *
     * @return the configured CorsConfigurationSource.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() 
    {
    	logger.info("Configuring CORS settings");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://172.17.0.139:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Origin", "Accept"));
        configuration.setAllowCredentials(true); // If you want to allow credentials
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        logger.info("CORS settings configured successfully");
        return source;
    }

}
