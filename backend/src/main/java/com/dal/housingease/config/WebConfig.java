package com.dal.housingease.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * Configuration class to set up CORS (Cross-Origin Resource Sharing) settings for the application.
 */
@Configuration
public class WebConfig 
{
	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
	/**
     * Configures CORS settings for the application.
     *
     * @return a {@link WebMvcConfigurer} with the configured CORS settings
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() 
    {
    	logger.info("Configuring CORS settings");
        return new WebMvcConfigurer() 
        {
            @Override
            public void addCorsMappings(CorsRegistry registry)
            {
            	logger.info("Adding CORS mappings");
                registry.addMapping("/**")  // will apply to all endpoints in the application
                        .allowedOrigins("http://172.17.0.139:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // OPTIONS -> browsers often send an OPTIONS request to determine if the actual request is safe to send
                        .allowedHeaders("*")
                        .allowCredentials(true); // allows cookies and HTTP authentication
                logger.info("CORS mappings configured: allowedOrigins=http://172.17.0.139:3000, allowedMethods=GET, POST, PUT, DELETE, OPTIONS, allowedHeaders=*, allowCredentials=true");
            }
        };
    }
}
