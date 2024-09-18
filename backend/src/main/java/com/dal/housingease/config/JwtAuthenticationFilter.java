package com.dal.housingease.config;

import com.dal.housingease.service.JwtServiceImpli;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
/**
 * A filter that validates JWT tokens for authentication. It extends OncePerRequestFilter to ensure
 * it is executed once per request.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter 
{
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtServiceImpli jwtService;
    private final UserDetailsService userDetailsService;
    /**
     * Constructor for JwtAuthenticationFilter.
     *
     * @param jwtService the JWT service for handling token operations
     * @param userDetailsService the service for loading user-specific data
     * @param handlerExceptionResolver the resolver for handling exceptions
     */
    public JwtAuthenticationFilter(
            JwtServiceImpli jwtService,
            UserDetailsService userDetailsService,
            HandlerExceptionResolver handlerExceptionResolver)
    {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }
    /**
     * Filters incoming requests and validates JWT tokens.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if an error occurs during filtering
     * @throws IOException if an I/O error occurs during filtering
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException 
    {
        final String authHeader = request.getHeader("Authorization");
        final String requestURI = request.getRequestURI();
        logger.debug("Processing request for URI: {}", requestURI);
         // Skip JWT validation for authentication endpoints
        if (requestURI.startsWith("/api/auth")) 
        {
            logger.debug("Skipping JWT authentication for public endpoint: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        // Check if the Authorization header is present and starts with 'Bearer '
        if (authHeader == null || !authHeader.startsWith("Bearer ")) 
        {
        	logger.debug("No JWT token found in request header or header does not start with 'Bearer '");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(jwt);
            logger.debug("Extracted user email from JWT: {}", userEmail);
            if (userEmail == null) 
            {
                logger.warn("JWT does not contain a username");
                filterChain.doFilter(request, response);
                return;
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) 
            {
            	// Load user details from the database
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (userDetails == null) 
                {
                    logger.warn("User not found for email: {}", userEmail);
                    filterChain.doFilter(request, response);
                    return;
                }
                // Validate the JWT token
                if (jwtService.isTokenValid(jwt, userDetails)) 
                {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.info("User authenticated successfully: {}", userEmail);
                }
                else 
                {
                    logger.warn("JWT token is not valid for user: {}", userEmail);
                }
            }
            filterChain.doFilter(request, response);
        }
        catch (Exception exception)
        {
            logger.error("An error occurred while processing the JWT token", exception);
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}
