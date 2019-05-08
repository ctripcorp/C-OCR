package util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @param obj
     * @return
     * @throws Exception
     * @Title: beanToJson
     * @Description: 对象转JsonString
     */
    public static String beanToJson(Object obj) {
        if (obj == null) {
            return null;
        }
        String json = null;
        try {


            json = objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return json;
    }

    /**
     * @param json
     * @param clazz
     * @return
     * @throws Exception
     * @Title: jsonToBean
     * @Description:JsonString 转 对象
     */
    public static <T> T jsonToBean(String json, Class<T> clazz) {
        T obj = null;
        try {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            obj = objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return obj;
    }

    /**
     * @param json
     * @param clazz
     * @return
     * @throws Exception
     * @Title: jsonToBean
     * @Description:JsonString 转 对象
     */
    public static <T> List<T> jsonToList(String json, Class<T> clazz) {
        List<T> obj = null;
        try {
            obj = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return obj;
    }
}
