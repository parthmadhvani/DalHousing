package com.dal.housingease.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/**
 * Global exception handler for the application, handling various exceptions and providing appropriate responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	/**
     * Handles InvalidPropertyDataException and returns a bad request response.
     *
     * @param ex the InvalidPropertyDataException
     * @return a ResponseEntity with a bad request status and the exception message
     */
    @ExceptionHandler(InvalidPropertyDataException.class)
    public ResponseEntity<String> handleInvalidPropertyDataException(InvalidPropertyDataException ex) 
    {
    	logger.error("Invalid property data exception occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    /**
     * Handles generic exceptions and provides appropriate problem details based on the exception type.
     *
     * @param exception the Exception
     * @return a ProblemDetail object containing the error details
     */
	@ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception) 
	{
        ProblemDetail errorDetail = null;
        logger.error("Unhandled exception occurred: {}", exception.getMessage(), exception);
        exception.printStackTrace();
        // Handling different types of exceptions and creating appropriate ProblemDetail responses
        if (exception instanceof BadCredentialsException) 
        {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
            errorDetail.setProperty("description", "The username or password is incorrect");
            logger.warn("Bad credentials: {}", exception.getMessage());
            return errorDetail;
        }

        if (exception instanceof AccountStatusException) 
        {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            logger.warn("Account status exception: {}", exception.getMessage());
            errorDetail.setProperty("description", "The account is locked");
        }

        if (exception instanceof AccessDeniedException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            logger.warn("Access denied: {}", exception.getMessage());
            errorDetail.setProperty("description", "You are not authorized to access this resource");
        }

       if (exception instanceof SignatureException) 
       {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            logger.warn("JWT signature exception: {}", exception.getMessage());
            errorDetail.setProperty("description", "The JWT signature is invalid");
        }

        if (exception instanceof ExpiredJwtException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            logger.warn("JWT token expired: {}", exception.getMessage());
            errorDetail.setProperty("description", "The JWT token has expired");
        }

        if (errorDetail == null) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), exception.getMessage());
            logger.error("Internal server error: {}", exception.getMessage());
            errorDetail.setProperty("description", "Unknown internal server error.");
        }
        return errorDetail;
    }
}
