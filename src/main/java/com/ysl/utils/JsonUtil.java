package com.ysl.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

/**
 * JSON转换工具类
 */
public final class JsonUtil {

    /**
     * 将对象转换成json
     * 
     * @param obj
     * @return
     */
    public static String toJson(final Object obj) {
        return gson.toJson(obj);
    }

    /**
     * 将对象转换成json(并自定义日期格式)
     * 
     * @param obj
     * @param dateformat
     * @return
     */
    public static String toJson(final Object obj, final String dateformat) {
        if (obj == null || StringUtils.isBlank(dateformat)) {
            return toJson(obj);
        }
        Gson builder = new GsonBuilder().registerTypeHierarchyAdapter(Date.class, new JsonSerializer<Date>() {
            @Override
            public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(DateUtil.format(src, dateformat));
            }
        }).setDateFormat(dateformat).serializeNulls().create();
        return builder.toJson(obj);
    }

    /**
     * 将对象中被@Expose注解的属性转换成json
     * 
     * @param obj
     * @return
     */
    public static String toJsonWithExpose(final Object obj) {
        return gsonWithExpose.toJson(obj);
    }

    /**
     * 将对象中被@Expose注解的属性转换成json(并自定义日期格式)
     * 
     * @param obj
     * @param dateformat
     * @return
     */
    public static String toJsonWithExpose(final Object obj, final String dateformat) {
        if (obj == null || StringUtils.isBlank(dateformat)) {
            return toJsonWithExpose(obj);
        }
        Gson builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeHierarchyAdapter(Date.class, new JsonSerializer<Date>() {
            @Override
            public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(DateUtil.format(src, dateformat));
            }
        }).setDateFormat(dateformat).serializeNulls().create();
        return builder.toJson(obj);
    }

    /**
     * 不序列化对象的空值
     * 
     * @param obj
     * @return
     */
    public static String toJsonDefault(final Object obj) {
        return gsonDefault.toJson(obj);
    }

    /**
     * 将对象转换成json，忽略属性名含有str的属性
     * 
     * @param obj
     * @param str
     * @return
     */
    public static String toJsonSkipFieldByStr(final Object obj, final String str) {
        if (obj == null) {
            return toJson(obj);
        }
        Gson builder = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes field) {
                return field.getName().indexOf(str) > -1;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).serializeNulls().create();
        return builder.toJson(obj);
    }

    /**
     * 将对象转换成json，只保留fields属性
     * 
     * @param obj
     * @param fields
     * @return
     */
    public static String toJsonKeepFields(final Object obj, final String... fields) {
        if (obj == null) {
            return toJson(obj);
        }
        Gson builder = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes field) {
                for (String fieldName : fields) {
                    if (fieldName.equals(field.getName())) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).serializeNulls().create();
        return builder.toJson(obj);
    }

    /**
     * 将对象转换成json(日期转换成LONG)
     * 
     * @param obj
     * @return
     */
    public static String toJsonDateFormatLong(final Object obj) {
        if (obj == null) {
            return toJson(obj);
        }

        Gson builder = new GsonBuilder().registerTypeHierarchyAdapter(Date.class, new JsonSerializer<Date>() {
            @Override
            public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {

                return new JsonPrimitive(src.getTime());
            }
        }).setDateFormat(DateFormat.LONG).serializeNulls().create();
        return builder.toJson(obj);
    }

    /**
     * 将json转换成任意对象
     * 
     * @param json
     * @return
     */
    public static <T> T jsonToObject(final String json) {
        Type type = new TypeToken<T>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    /**
     * 将json转换成bean对象
     * 
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T jsonToBean(final String json, final Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    /**
     * 将json转换成map对象
     * 
     * @param json
     * @return
     */
    public static Map<Object, Object> jsonToMap(final String json) {
        Map<Object, Object> map = null;
        Type type = new TypeToken<Map<Object, Object>>() {
        }.getType();
        map = gson.fromJson(json, type);
        if (map == null) {
            map = new HashMap<Object, Object>();
        }
        return map;
    }

    /**
     * 将json格式转换成map对象
     * 
     * @param json
     * @return
     */
    public static <T> Map<String, T> jsonToMap(String json, Type type) {
        Map<String, T> objMap = null;
        if (gson != null) {
            objMap = gson.fromJson(json, type);
        }
        if (objMap == null) {
            objMap = new HashMap<String, T>();
        }
        return objMap;
    }
    
    
    public static Map<?, ?> jsonToMapByType(String json, Type type) {
        Map<?, ?> objMap = null;
        if (gson != null) {
            objMap = gson.fromJson(json, type);
        }
        if (objMap == null) {
            objMap = new HashMap<>();
        }
        return objMap;
    }
    
    
    
    /**
     * 将json转换成List对象
     * 
     * @param json
     * @return
     */
    public static <T> List<T> jsonToList(final String json) {
        List<T> list = null;
        Type type = new TypeToken<List<T>>() {
        }.getType();
        list = gson.fromJson(json, type);
        if (list == null) {
            list = new ArrayList<T>();
        }
        return list;
    }

    /**
     * 将json格式转换成List对象
     * 
     * @param json
     * @param type
     *            如： Type type = new TypeToken<List<?>>() {}.getType();
     * @return
     */
    public static <T> List<T> jsonToList(String json, Type type) {
        List<T> list = null;
        if (gson != null) {
            list = gson.fromJson(json, type);
        }
        return list;
    }

    /**
     * 将对象转换成json(日期转换成LONG)
     * 
     * @param obj
     * @return
     */
    public static <T> T jsonToBeanDateFromLong(final String json, final Class<T> clazz) {
        if (json == null) {
            return null;
        }

        Gson builder = new GsonBuilder().registerTypeHierarchyAdapter(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(element.getAsJsonPrimitive().getAsLong());
            }
        }).setDateFormat(DateFormat.LONG).serializeNulls().create();
        return builder.fromJson(json, clazz);
    }
    
    /**
     * 在json字符串中，根据key值找到value
     * 
     * @param json
     * @param key
     * @return
     */
    public static Object getValue(final String json, final String key) {
        return jsonToMap(json).get(key);
    }
    
    public static <T> List<T> jsonToList(final List<String> jsons , final Class<T> clazz){
        List<T> list = new ArrayList<T>();
        for(String json : jsons){
            if(StringUtils.isNotEmpty(json)){
                list.add(jsonToBean(json,clazz));
            }
        }
        return list;     
    }
    
    public static final String EMPTY_MAP = "{}";

    private static Gson gson;
    private static Gson gsonDefault;
    private static Gson gsonWithExpose;
    static {
        gson = new GsonBuilder().serializeNulls().create();
        gsonDefault = new Gson();
        // 没有@Expose注解的属性将不会被序列化
        gsonWithExpose = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
    }

    private JsonUtil() {
    }
}