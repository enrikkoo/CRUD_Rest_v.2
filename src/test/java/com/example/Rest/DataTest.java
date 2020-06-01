package com.example.Rest;

import com.example.Rest.model.User;
import com.example.Rest.service.Data;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.example.Rest.service.Data.current_id;
import static com.example.Rest.service.Data.initializationfreeIds;

public class DataTest {


    Map<Integer, User> USERS_DATABASE = new HashMap<>();
    static final List<Integer> freeIdsTest = new LinkedList<>();
    private static final String PROPERTIES_TEST = "src/test/resources/PropertiesTest";
    private static final String jsonInString = "";
    private static  int currentIdTest = 19;

    @BeforeClass
    public static void prepare() {

    }
    public boolean JSONValid(){
        final ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readTree(jsonInString);
        } catch (JsonProcessingException e) {
            return false;
        }
        return true;

    }

    @Test
    public void isJSONValid(){
        Assert.assertTrue(JSONValid());
    }


    @Test
    public void initializationfreeIdsTest(){
        initializationfreeIds(freeIdsTest,PROPERTIES_TEST);
        List<Integer> exp = new LinkedList<>();
        exp.add(10);
        exp.add(15);
        Assert.assertEquals(exp,freeIdsTest);
    }

    @Test
    public void choiceTest(){
        initializationfreeIds(freeIdsTest,PROPERTIES_TEST);

        Assert.assertEquals(10,Data.chooseId(freeIdsTest,PROPERTIES_TEST,currentIdTest));
        currentIdTest++;
    }




}
