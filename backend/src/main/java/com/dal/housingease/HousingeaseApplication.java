package com.dal.housingease;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class HousingeaseApplication 
{
	private static final Logger logger = LogManager.getLogger(HousingeaseApplication.class);

	public static void main(String[] args) 
	{
		logger.info("Starting HousingeaseApplication...");
		SpringApplication.run(HousingeaseApplication.class, args);
		logger.info("HousingeaseApplication started successfully.");
	}
}