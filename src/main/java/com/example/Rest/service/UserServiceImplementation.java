package com.example.Rest.service;

import com.example.Rest.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

import static com.example.Rest.RestApplication.LOGGER;


@Service
public class UserServiceImplementation implements UserService {

    /**
     * Database from JSON file
     */
    private static final String JSON_User = "src/main/resources/templates/JSON_User";

    /**
     * Properties for app
     * Default values for properties at first launch must be {"currentId": 0,"freeIds":[]}
     */
    private static final String PROPERTIES = "src/main/resources/templates/Properties";

    /**
     * Stores the user database in the program
     */
    private static Map<Integer, User> USERS_DATABASE = new HashMap<>();

    /**
     * Stores deleted user ids
     */
    private static final List<Integer> freeIds = new LinkedList<>();

    /**
     * Stores the current id
     */
    public static int current_id = initializationId();

    /**
     * ObjectMapper from Jackson
     */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * This method reads users from {@link JSON_User} and updates {@link USERS_DATABASE} in the program
     */
    public static void setUsersDatabase(){
        try {
            USERS_DATABASE = mapper.readValue(new File(JSON_User), new TypeReference<Map<Integer, User>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING,"An exception occurred while reading the database");
        }
    }

    /**
     * This method reads id from {@link PROPERTIES} and updates {@link current_id} in the program
     * @return Current ID
     */
    public static int initializationId() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(new File(PROPERTIES));
            current_id = jsonNode.get("currentId").asInt();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING,"An exception occurred during IDE initialization");
        }
        return current_id;
    }

    /**
     * This method reads id`s from {@link PROPERTIES} and updates {@link freeIds} in the program
     */
    public static void initializationfreeIds(){
        freeIds.clear();
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(PROPERTIES));
            JSONArray idsArray = (JSONArray) jsonObject.get("freeIds");
            for (Object x : idsArray){
                Integer in = (int)(long)(Long) x;
                freeIds.add(in);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING,"An exception occurred when initializing free IDS");
        }
    }

    /**
     * This method writes {@link USERS_DATABASE} to {@link JSON_User}
     */
    private void writeToJSON_User(){
        try {
            mapper.writeValue(new File(JSON_User),USERS_DATABASE);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING,"An exception occurred when updating the database");
        }
    }

    /**
     * This method writes updated {@link current_id} and {@link freeIds} to {@link PROPERTIES}
     */
    private void updateProperties(){
        JSONObject object = new JSONObject();
        object.put("currentId",current_id);
        object.put("freeIds",freeIds);
        try {
            FileWriter writer = new FileWriter(PROPERTIES);
            writer.write(object.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING,"An exception occurred when updating properties");
        }
    }

    /**
     * This method selects the most appropriate ID for the situation
     * If there are no deleted Ids in {@link freeIds},the next ID in the queue will be selected
     * Otherwise, the first ID will be selected from {@link freeIds}
     * @return ID
     */
    private int chooseId(){
        initializationfreeIds();
        JSONObject object = new JSONObject();
        if (!freeIds.isEmpty()){
            int id = freeIds.remove(0);
            updateProperties();
            return id;
        }
        ++current_id;
        updateProperties();
        return current_id;
    }

    /**
     * Here the classic CRUD methods are overridden
     */
    @Override
    public void create(User user){
        LOGGER.log(Level.INFO,"Create user");
        USERS_DATABASE.put(chooseId(),user);
        writeToJSON_User();
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
            writeToJSON_User();
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
            updateProperties();
            writeToJSON_User();
            return true;
        }
        LOGGER.log(Level.WARNING,"Couldn't delete the user,check id");
        return false;
    }
}
