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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import static com.example.Rest.RestApplication.LOGGER;

public  class Data {

    /**
     * Stores the user database in the program
     */
    static Map<Integer, User> USERS_DATABASE = new HashMap<>();

    /**
     * Stores the current id
     */
    public static AtomicInteger currentID = new AtomicInteger();

    /**
     * Stores deleted user ids
     */
    public static List<AtomicInteger> freeIds = new LinkedList<>();

    /**
     * ObjectMapper from Jackson
     */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * This method writes  USERS_DATABASE to JSON_USER
     * @param database - stores user`s data
     * @param file_path - path to the file where the data is taken from
     */
    static void writeToJSON_User(Map<Integer,User> database,String file_path){
        try {
            mapper.writeValue(new File(file_path),database);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING,"An exception occurred when updating the database");
        }
    }

    /**
     * This method reads id`s from PROPERTIES and updates freeIds in the program
     * @param freeIds - deleted Ids
     * @param file_path - path to the file where the data is taken from
     */
    public static void initializationfreeIds(List<AtomicInteger> freeIds,String file_path){
        freeIds.clear();
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(file_path));
            JSONArray idsArray = (JSONArray) jsonObject.get("freeIds");
            for (Object x : idsArray){
                freeIds.add(new AtomicInteger((int)(long)(Long)x));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING,"An exception occurred when initializing free IDS");
        }
    }

    /**
     * This method writes updated current_id and freeIds to PROPERTIES
     * @param file_path - path to the file with properties
     * @param currentID - current id in queue
     * @param freeIds - deleted Ids
     */
      static void updateProperties(String file_path,AtomicInteger currentID,List<AtomicInteger> freeIds){
        JSONObject object = new JSONObject();
        object.put("currentId",currentID.get());
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
     * If there are no deleted Ids in freeIds,the next ID in the queue will be selected
     * Otherwise, the first ID will be selected from freeIds
     * @param freeIds - deleted Ids
     * @param file_path - path to the file with properties
     * @param currentID - current id in queue
     * @return ID
     */
    public static AtomicInteger chooseId(List<AtomicInteger> freeIds,String file_path,AtomicInteger currentID){
        initializationfreeIds(freeIds,file_path);
        if (!freeIds.isEmpty()){
            AtomicInteger current_id2 = freeIds.remove(0);
            updateProperties(file_path,currentID,freeIds);
            return current_id2;
        }
        currentID.incrementAndGet();
        updateProperties(file_path,currentID,freeIds);
        return currentID;
    }

    /**
     * This method reads id from PROPERTIES and updates currentID in the program
     * @param file_path - path to the file with ID
     */
    public static void initializationId(String file_path) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(new File(file_path));
            currentID.set(jsonNode.get("currentId").asInt());
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING,"An exception occurred during IDE initialization");
        }
    }

    /**
     * This method reads users from JSON_User and updates USERS_DATABASE in the program
     * @param file_path - path to the file with user`s data
     */
    public static void setUsersDatabase(String file_path){
        try {
             USERS_DATABASE = mapper.readValue(new File(file_path), new TypeReference<Map<Integer, User>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING,"An exception occurred while reading the database");
        }
    }
}
