package com.ysl.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JSON转换工具类
 */
public final class GsonUtil {

    private static Gson gson = null;
    private static Gson gson2 = null;
    private static Gson gsonDefault;

    /**
     * 不序列化对象的空值
     *
     * @param obj
     * @return
     */
    public static String toJsonDefault(final Object obj) {
        return gsonDefault.toJson(obj);
    }

    static {
        if (gson == null) {
            gson = new GsonBuilder().serializeNulls().create();
        }
        if (gson2 == null) {
            // 没有@Expose注解的属性将不会被序列化
            gson2 = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
        }
        gsonDefault = new Gson();
    }

    private GsonUtil() {
    }


    /**
     * 判断字符串是否包含中文
     *
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();

    }

    /**
     * 将对象转换成json字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return "{}";
        }
        return gson.toJson(obj);
    }

    /**
     * 将对象中被@Expose注解的属性转换成json字符串
     *
     * @param obj
     * @return
     */
    public static String toJsonWithExpose(Object obj) {
        if (obj == null) {
            return "{}";
        }
        return gson2.toJson(obj);
    }

    /**
     * 在json字符串中，根据key值找到value
     *
     * @param json
     * @param key
     * @return
     */
    public static Object getValue(String json, String key) {
        Object rulsObj = null;
        Map<?, ?> rulsMap = jsonToMap(json);
        if (rulsMap != null && rulsMap.size() > 0) {
            rulsObj = rulsMap.get(key);
        }
        return rulsObj;
    }

    /**
     * 将json格式转换成map对象
     *
     * @param json
     * @return
     */
    public static Map<String, Object> jsonToMap(String json) {
        Map<String, Object> objMap = null;
        if (gson != null) {
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();
            objMap = gson.fromJson(json, type);
        }
        if (objMap == null) {
            objMap = new HashMap<String, Object>();
        }
        return objMap;
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

    /**
     * 将json格式转换成map对象
     *
     * @param json
     * @return
     */
    public static <T> Map<Integer, T> jsonToMapForKeyInt(String json, Type type) {
        Map<Integer, T> objMap = null;
        if (gson != null) {
            objMap = gson.fromJson(json, type);
        }
        if (objMap == null) {
            objMap = new HashMap<Integer, T>();
        }
        return objMap;
    }

    /**
     * 将json格式转换成linkMap对象
     *
     * @param json
     * @return
     */
    public static Map<String, Object> jsonToLinkMap(String json) {
        LinkedHashMap<String, Object> objMap = null;
        if (gson != null) {
            Type type = new TypeToken<LinkedHashMap<String, Object>>() {
            }.getType();
            objMap = gson.fromJson(json, type);
        }
        if (objMap == null) {
            objMap = new LinkedHashMap<String, Object>();
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
     * 将json转换成bean对象
     *
     * @param <T>
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T jsonToBean(String json, Class<T> clazz) {
        T obj = null;
        if (gson != null) {
            obj = gson.fromJson(json, clazz);
        }
        return obj;
    }


    public static <T> T jsonToBeanDateForLong(String json, Class<T> clazz) {
        T obj = null;

        Gson builder = new GsonBuilder().registerTypeHierarchyAdapter(Date.class, new JsonDeserializer<Object>() {

            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
            }

        }).setDateFormat(DateFormat.LONG).serializeNulls().create();

        obj = builder.fromJson(json, clazz);
        return obj;
    }


    public static <T> List<T> getListFromJSON(String json, Class<T[]> type) {
        T[] list = gson.fromJson(json, type);
        return Arrays.asList(list);
    }

    /**
     * 将json格式转换成List对象
     *
     * @param json
     * @param type 如： Type type = new TypeToken<List<?>>() {}.getType();
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
     * 将对象转换成json格式(并自定义日期格式)
     *
     * @param obj
     * @param dateformat
     * @return
     */
    public static String toJson(Object obj, final String dateformat) {
        if (obj == null || StringUtils.isBlank(dateformat)) {
            return toJson(obj);
        }
        Gson builder = new GsonBuilder().registerTypeHierarchyAdapter(Date.class, new JsonSerializer<Date>() {
            @Override
            public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
                DateTimeFormatter DF = DateTimeFormat.forPattern(dateformat);
                return new JsonPrimitive(new DateTime(src.getTime()).toString(DF));
            }
        }).setDateFormat(dateformat).serializeNulls().create();
        return builder.toJson(obj);
    }

    /**
     * 将对象转换成json格式，忽略属性名含有下划线的属性
     *
     * @param obj
     * @return
     */
    public static String toJsonSkipFieldContains_(Object obj) {
        if (obj == null) {
            return toJson(null);
        }
        Gson builder = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes field) {
                return field.getName().indexOf('_') > -1;
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
            return toJson(null);
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
        }).create();
        return builder.toJson(obj);
    }

}
