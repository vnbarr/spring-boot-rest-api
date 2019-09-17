package com.rest.api.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtilsTest {

    public static String fromJsonToObject(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        //Object to JSON in String
        return mapper.writeValueAsString(object);
    }

}
