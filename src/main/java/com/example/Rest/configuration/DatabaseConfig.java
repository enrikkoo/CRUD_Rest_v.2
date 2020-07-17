package com.example.Rest.configuration;


import com.example.Rest.service.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.Rest.service.Data.freeIds;

@Configuration
public class DatabaseConfig {

    /**
     * Properties for app
     * Default values for properties at first launch must be {"currentId": 0,"freeIds":[]}
     */
    @Value("${databaseConfig.properties}")
    public String PROPERTIES;

    /**
     * Database from JSON file
     */
    @Value("${databaseConfig.JSON_USER}")
    public String JSON_USER;

    @Bean
    public Data data() {
        Data data = new Data();
        data.initializationId(PROPERTIES);
        data.initializationfreeIds(freeIds, PROPERTIES);
        data.setUsersDatabase(JSON_USER);
        return data;
    }
}
