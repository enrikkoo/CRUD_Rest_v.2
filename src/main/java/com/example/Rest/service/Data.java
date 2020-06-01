package com.example.Rest.service;

import com.example.Rest.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static com.example.Rest.RestApplication.LOGGER;
import static com.example.Rest.RestApplication.PROPERTIES;

public  class Data {


    public static Map<Integer, User> getUsersDatabase() {
        return USERS_DATABASE;
    }

    /**
     * Stores the user database in the program
     */
    static final Map<Integer, User> USERS_DATABASE = new HashMap<>();


    /**
     * Stores deleted user ids
     */
    public static final List<Integer> freeIds = new LinkedList<>();

    /**
     * Stores the current id
     */
    public static int current_id =initializationId(PROPERTIES);

    /**
     * ObjectMapper from Jackson
     */
    static final ObjectMapper mapper = new ObjectMapper();

    /**
     * This method writes {@link USERS_DATABASE} to {@link JSON_User}
     */
    static void writeToJSON_User(Map<Integer,User> database,String file_path){
        try {
            mapper.writeValue(new File(file_path),database);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING,"An exception occurred when updating the database");
        }
    }

    public static void initializationfreeIds(List<Integer> freeIds,String file_path){
        freeIds.clear();
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(file_path));
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
     * This method writes updated {@link current_id} and {@link freeIds} to {@link PROPERTIES}
     */
     public static void updateProperties(String file_path,Integer current_id,List<Integer> freeIds){
        JSONObject object = new JSONObject();
        object.put("currentId",current_id);
        object.put("freeIds",freeIds);
        try {
            FileWriter writer = new FileWriter(file_path);
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
    public static int chooseId(List<Integer> freeIds,String file_path,Integer current_id){
        initializationfreeIds(freeIds,file_path);
        //JSONObject object = new JSONObject();
        if (!freeIds.isEmpty()){
            int id = freeIds.remove(0);
            updateProperties(file_path,current_id,freeIds);
            return id;
        }
        ++current_id;
        updateProperties(file_path,current_id,freeIds);
        return current_id;
    }
    /**
     * This method reads id from {@link PROPERTIES} and updates {@link current_id} in the program
     * @return Current ID
     */
    public static int initializationId(String file_path) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(new File(file_path));
            current_id = jsonNode.get("currentId").asInt();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING,"An exception occurred during IDE initialization");
        }
        return current_id;
    }
    /**
     * This method reads users from {@link JSON_User} and updates {@link USERS_DATABASE} in the program
     */
    public static void setUsersDatabase(Map<Integer,User> usersDatabase,String path){
        try {
            usersDatabase = mapper.readValue(new File(path), new TypeReference<Map<Integer, User>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING,"An exception occurred while reading the database");
        }
    }
}
