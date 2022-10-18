package com.roily.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;

public class JsonUtil {

    public static String getJson(Object object){
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonString = "";
//        try {
//            objectMapper.writeValueAsString(object);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return jsonString;
        return getJson(object,"yyyy-mm-dd HH:mm:ss");
    }

    public static String getJson(Object object, String sdf){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS,false);
        objectMapper.setDateFormat(new SimpleDateFormat(sdf));
        String jsonString = "";
        try {
              jsonString = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

}
