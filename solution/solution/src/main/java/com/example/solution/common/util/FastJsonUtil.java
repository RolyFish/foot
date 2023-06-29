package com.example.solution.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * fast json 工具类
 *
 * 
 * @date 2021/12/28
 */
public class FastJsonUtil {


    private FastJsonUtil() {
    }

    /**
     * 功能描述：把JSON数据转换成指定的java对象
     *
     * @param jsonData JSON数据
     * @param clazz    指定的java对象
     * @return 指定的java对象
     */
    public static <T> T jsonToBean(String jsonData, Class<T> clazz) {
        return JSON.parseObject(jsonData, clazz);
    }

    /**
     * 功能描述：把java对象转换成JSON数据
     *
     * @param object java对象
     * @return JSON数据
     */
    public static String beanToJson(Object object) {
        return JSON.toJSONString(object);
    }

    /**
     * 功能描述：把java对象转换成JSON数据
     *
     * @param object   java对象
     * @param features 转换特征列表
     * @return JSON数据
     */
    public static String beanToJson(Object object, SerializerFeature... features) {
        return JSON.toJSONString(object, features);
    }

    /**
     * 功能描述：把JSON数据转换成指定的java对象列表
     *
     * @param jsonData JSON数据
     * @param clazz    指定的java对象
     * @return List<T>
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> clazz) {
        return JSON.parseArray(jsonData, clazz);
    }

    /**
     * 功能描述：把JSON数据转换成较为复杂的List<Map<String, Object>>
     *
     * @param jsonData JSON数据
     * @return List<Map < String, Object>>
     */
    public static List<Map<String, Object>> jsonToListMap(String jsonData) {
        return JSON.parseObject(jsonData, new TypeReference<>() {
            @Override
            public Type getType() {
                return Map.class;
            }
        });
    }

    /**
     * List<T> 转 json 保存到数据库
     */
    public static <T> String listToJson(List<T> ts) {
        return JSON.toJSONString(ts);
    }

    /**
     * 两个类之间值的转换
     * 从object》》tClass
     *
     * @param object 有数据的目标类
     * @param tClass 转换成的类
     * @param <T>
     * @return 返回tClass类
     */
    public static <T> T getObjectToClass(Object object, Class<T> tClass) {
        String json = beanToJson(object);
        return jsonToBean(json, tClass);
    }

    /**
     * json 转 List<T>
     */
    public static <T> List<T> jsonToClassList(String jsonString, Class<T> clazz) {
        return JSONArray.parseArray(jsonString, clazz);
    }

}
