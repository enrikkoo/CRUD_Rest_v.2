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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static com.example.Rest.RestApplication.LOGGER;

public class Data {

    /**
     * ObjectMapper from Jackson
     */
    private static final ObjectMapper mapper = new ObjectMapper();
    /**
     * Stores the current id
     */
    public static Integer currentID;

    /**
     * Stores deleted user ids
     */
    public static List<Integer> freeIds = new LinkedList<>();
    /**
     * Stores the user database in the program
     */
    static Map<Integer, User> USERS_DATABASE;

    /**
     * This method writes  USERS_DATABASE to JSON_USER
     *
     * @param database  - stores user`s data
     * @param file_path - path to the file where the data is taken from
     */
    void writeToJSON_User(Map<Integer, User> database, String file_path) {
        try {
            mapper.writeValue(new File(file_path), database);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING, "An exception occurred when updating the database");
        }
    }

    /**
     * This method reads id`s from PROPERTIES and updates freeIds in the program
     *
     * @param freeIds   - deleted Ids
     * @param file_path - path to the file where the data is taken from
     */
    public void initializationfreeIds(List<Integer> freeIds, String file_path) {
        freeIds.clear();
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(file_path));
            JSONArray idsArray = (JSONArray) jsonObject.get("freeIds");
            for (Object x : idsArray) {
                freeIds.add((int) (long) (Long) x);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING, "An exception occurred when initializing free IDS");
        }
    }

    /**
     * This method writes updated current_id and freeIds to PROPERTIES
     *
     * @param file_path - path to the file with properties
     * @param currentID - current id in queue
     * @param freeIds   - deleted Ids
     */
    void updateProperties(String file_path, Integer currentID, List<Integer> freeIds) {
        JSONObject object = new JSONObject();
        object.put("currentId", currentID);
        object.put("freeIds", freeIds);
        try {
            FileWriter writer = new FileWriter(file_path);
            writer.write(object.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING, "An exception occurred when updating properties");
        }
    }

    /**
     * This method selects the most appropriate ID for the situation
     * If there are no deleted Ids in freeIds,the next ID in the queue will be selected
     * Otherwise, the first ID will be selected from freeIds
     *
     * @param freeIds       - deleted Ids
     * @param filePath      - path to the file with properties
     * @param currentIdCopy - current id in queue
     * @return ID
     */
    public Integer chooseId(List<Integer> freeIds, String filePath, Integer currentIdCopy) {
        initializationfreeIds(freeIds, filePath);
        if (!freeIds.isEmpty()) {
            Integer current_id2 = freeIds.remove(0);
            updateProperties(filePath, currentID, freeIds);
            return current_id2;
        }
        currentID = currentIdCopy + 1;
        updateProperties(filePath, currentID, freeIds);
        return currentID;
    }

    /**
     * This method reads id from PROPERTIES and updates currentID in the program
     *
     * @param file_path - path to the file with ID
     */

    public void initializationId(String file_path) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(new File(file_path));
            currentID = (jsonNode.get("currentId").asInt());
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING, "An exception occurred during IDE initialization");
        }
    }

    /**
     * This method reads users from JSON_User and updates USERS_DATABASE in the program
     *
     * @param file_path - path to the file with user`s data
     */
    public void setUsersDatabase(String file_path) {
        try {
            USERS_DATABASE = mapper.readValue(new File(file_path), new TypeReference<Map<Integer, User>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING, "An exception occurred while reading the database");
        }
    }
}
