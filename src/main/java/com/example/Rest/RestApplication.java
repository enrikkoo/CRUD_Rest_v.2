package com.example.Rest;

import com.example.Rest.service.UserServiceImplementation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * This is classic Spring Boot Restful CRUD application
 * @autor Erik Osipov
 * @version 1.0
 */

@SpringBootApplication
public class RestApplication {

	public static Logger LOGGER;

	/**
	 * This block initializes the logger for logging
	 */
	static{
		try {
			LogManager.getLogManager().readConfiguration(new FileInputStream("src/main/resources/log.properties"));
			LOGGER = Logger.getLogger(RestApplication.class.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The Main class calls methods for initializing the user ID,database and run SpringApplication
	 */
	public static void main(String[] args) {
		UserServiceImplementation.initializationfreeIds();
		UserServiceImplementation.setUsersDatabase();
		SpringApplication.run(RestApplication.class, args);
	}
}