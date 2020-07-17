package com.example.Rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


/**
 * This is classic Spring Boot Restful CRUD application
 *
 * @version 1.1
 * @autor Erik Osipov
 */

@SpringBootApplication
public class RestApplication {

    public static Logger LOGGER;

    /**
     * This block initializes the logger for logging
     */
    static {
        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream("src/main/resources/log.properties"));
            LOGGER = Logger.getLogger(RestApplication.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING, "IOException");
        }
    }

    /**
     * Main method run SpringApplication
     */
    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
        LOGGER.log(Level.INFO, "App started");
    }
}