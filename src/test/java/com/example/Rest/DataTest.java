package com.example.Rest;

import com.example.Rest.model.User;
import com.example.Rest.service.Data;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.Rest.service.Data.initializationfreeIds;

public class DataTest {

    static final List<AtomicInteger> freeIdsTest = new LinkedList<>();
    private static final String PROPERTIES_TEST = "src/test/resources/PropertiesTest";
    private static final String JSON_USERTesting = "src/main/resources/templates/JSON_USER";
    private static final AtomicInteger currentIdTest = new AtomicInteger(19);

    public boolean JSONValid(){
        final ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readTree(new File(JSON_USERTesting ));
        } catch (JsonProcessingException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
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
        List<AtomicInteger> exp = new LinkedList<>();
        exp.add(new AtomicInteger(10));
        exp.add(new AtomicInteger(15));

        Assert.assertEquals(exp,freeIdsTest);
    }

    @Test
    public void choiceTest(){
        initializationfreeIds(freeIdsTest,PROPERTIES_TEST);

        Assert.assertEquals(new AtomicInteger(10),Data.chooseId(freeIdsTest,PROPERTIES_TEST,currentIdTest));
    }
}