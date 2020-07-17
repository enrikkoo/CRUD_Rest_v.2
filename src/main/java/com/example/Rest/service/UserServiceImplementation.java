package com.example.Rest.service;

import com.example.Rest.configuration.DatabaseConfig;
import com.example.Rest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.logging.Level;

import static com.example.Rest.RestApplication.LOGGER;
import static com.example.Rest.service.Data.*;


@Service
public class UserServiceImplementation implements UserService {

    private final Data data;
    private final DatabaseConfig databaseConfig;

    @Autowired
    public UserServiceImplementation(Data data, DatabaseConfig databaseConfig) {
        this.data = data;
        this.databaseConfig = databaseConfig;

    }

    /**
     * Here the classic CRUD methods are overridden
     */
    @Override
    public void create(User user) {
        LOGGER.log(Level.INFO, "Creating a user");
        USERS_DATABASE.put(data.chooseId(freeIds, databaseConfig.PROPERTIES, currentID), user);
        data.writeToJSON_User(USERS_DATABASE, databaseConfig.JSON_USER);
        LOGGER.log(Level.INFO, "User created");
    }

    @Override
    public Map<Integer, User> read() {
        LOGGER.log(Level.INFO, "Getting all users");
        return USERS_DATABASE;
    }

    @Override
    public User readUserById(int id) {
        LOGGER.log(Level.INFO, "Getting a user by ID");
        if (!USERS_DATABASE.containsKey(id)) {
            LOGGER.log(Level.WARNING, "Couldn't read the user,check id");
        }
        return USERS_DATABASE.get(id);
    }

    @Override
    public boolean update(User user, int id) {
        LOGGER.log(Level.INFO, "Updating the user");
        if (USERS_DATABASE.containsKey(id)) {
            USERS_DATABASE.put(id, user);
            data.writeToJSON_User(USERS_DATABASE, databaseConfig.JSON_USER);
            LOGGER.log(Level.INFO, "User updated");
            return true;
        }
        LOGGER.log(Level.WARNING, "Couldn't update the user,check id");
        return false;
    }

    @Override
    public boolean delete(int id) {
        LOGGER.log(Level.INFO, "Deleting a user");
        if (USERS_DATABASE.containsKey(id)) {
            USERS_DATABASE.remove(id);
            freeIds.add(id);
            data.updateProperties(databaseConfig.PROPERTIES, currentID, freeIds);
            data.writeToJSON_User(USERS_DATABASE, databaseConfig.JSON_USER);
            LOGGER.log(Level.INFO, "User deleted");
            return true;
        }
        LOGGER.log(Level.WARNING, "Couldn't delete the user,check id");
        return false;
    }
}
