package com.example.Rest;

import com.example.Rest.service.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DataTest {

    public static Data data = new Data();

    static List<Integer> freeIdsTest = new LinkedList<>();

    private static final String PROPERTIES_TEST = "src/test/resources/PropertiesTest";
    private static final String JSON_USER_TEST = "src/test/resources/JSON_USER_TEST";


    @Before
    public void setUp() throws IOException {
        List<Integer> freeIds = Arrays.asList(10, 15);
        HashMap<String, Object> map = new HashMap<>();
        map.put("currentId", 19);
        map.put("freeIds", freeIds);
        new ObjectMapper().writeValue(new File(PROPERTIES_TEST), map);
    }

    public boolean JSONValid() throws IOException {
        new ObjectMapper().readTree(new File(JSON_USER_TEST));
        return true;
    }

    @Test
    public void isJSONValid() throws IOException {
        Assert.assertTrue(JSONValid());
    }


    @Test
    public void initializationfreeIdsTest() {
        List<Integer> exp = Arrays.asList(10, 15);
        data.initializationfreeIds(freeIdsTest, PROPERTIES_TEST);
        boolean result = true;
        if (exp.size() == freeIdsTest.size()) {
            for (int i = 0; i < exp.size(); i++) {
                if (!exp.get(i).equals(freeIdsTest.get(i))) {
                    result = false;
                    break;
                }
            }
        } else result = false;
        Assert.assertTrue(result);
    }


    @Test
    public void choiceTest() throws IOException {

        Integer currentIdTest = 19;

        Assert.assertEquals(Integer.valueOf(10), data.chooseId(freeIdsTest, PROPERTIES_TEST, currentIdTest));
        Assert.assertEquals(Integer.valueOf(15), data.chooseId(freeIdsTest, PROPERTIES_TEST, currentIdTest));
        Assert.assertEquals(Integer.valueOf(20), data.chooseId(freeIdsTest, PROPERTIES_TEST, currentIdTest));
    }

}

