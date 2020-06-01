package com.example.Rest.service;

import com.example.Rest.model.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.logging.Level;

import static com.example.Rest.RestApplication.*;
import static com.example.Rest.service.Data.*;


@Service
public class UserServiceImplementation implements UserService {


    /**
     * Here the classic CRUD methods are overridden
     */
    @Override
    public void create(User user){
        LOGGER.log(Level.INFO,"Create user");
        USERS_DATABASE.put(Data.chooseId(freeIds,PROPERTIES,current_id),user);
        writeToJSON_User(USERS_DATABASE,JSON_User);
    }

    @Override
    public Map<Integer, User> read(){
        LOGGER.log(Level.INFO,"Getting all users");
        return USERS_DATABASE;
    }

    @Override
    public User readUserById(int id){
        LOGGER.log(Level.INFO,"Getting a user by ID");
        return USERS_DATABASE.get(id);
    }

    @Override
    public boolean update(User user, int id){
        LOGGER.log(Level.INFO,"Updating the user");
        if (USERS_DATABASE.containsKey(id)){
            USERS_DATABASE.put(id,user);
            writeToJSON_User(USERS_DATABASE,JSON_User);
            return true;
            }
        LOGGER.log(Level.WARNING,"Couldn't update the user,check id");
        return false;
    }

    @Override
    public boolean delete(int id) {
        if(USERS_DATABASE.containsKey(id)){
            LOGGER.log(Level.INFO,"Deleting a user");
            USERS_DATABASE.remove(id);
            freeIds.add(id);
            updateProperties(PROPERTIES,current_id,freeIds);
            writeToJSON_User(USERS_DATABASE,JSON_User);
            return true;
        }
        LOGGER.log(Level.WARNING,"Couldn't delete the user,check id");
        return false;
    }
}
