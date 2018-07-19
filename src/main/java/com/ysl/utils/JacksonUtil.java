package com.ysl.utils;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Jackson 处理json
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class JacksonUtil {
    private static final Logger LOG = LoggerFactory.getLogger(JacksonUtil.class.getName());

    private static ObjectMapper mapper;
    static {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 将对象转换成json
     * 
     * @param obj
     * @return
     */
    public static String toJson(final Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            LOG.error("", e);
        }
        return null;
    }

    /**
     * 将json转换成bean对象
     * 
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T jsonToBean(final String json, final Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            LOG.error("", e);
        }
        return null;
    }

    /**
     * 构造Collection类型.
     */
    public static JavaType constructCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass) {
        return mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }

    /**
     * 将json转换成Collection<Bean>对象
     * 
     * @param json
     * @param javaType
     * @return
     */
    public static <T> T jsonToList(final String json, JavaType javaType) {
        try {
            if (json != null && !"".equals(json)) {
                return (T) mapper.readValue(json, javaType);
            }
        } catch (Exception e) {
            LOG.error("", e);
        }
        return null;
    }

    /**
     * 将json转换成Collection<Bean>对象
     * 
     * @param json
     * @param collectionClass
     * @param elementClass
     * @return
     */
    public static <T> T jsonToList(final String json, Class<? extends Collection> collectionClass, Class<?> elementClass) {
        try {
            if (json != null && !"".equals(json)) {
                return (T) mapper.readValue(json, constructCollectionType(collectionClass, elementClass));
            }
        } catch (Exception e) {
            LOG.error("", e);
        }
        return null;
    }

    /**
     * 构造Map类型.
     */
    public static JavaType constructMapType(Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass) {
        return mapper.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
    }

    /**
     * 将json转换成Map<Bean, Bean>对象
     * 
     * @param json
     * @param javaType
     * @return
     */
    public static <T> T jsonToMap(final String json, JavaType javaType) {
        try {
            if (json != null && !"".equals(json)) {
                return (T) mapper.readValue(json, javaType);
            }
        } catch (Exception e) {
            LOG.error("", e);
        }
        return null;
    }

    /**
     * 将json转换成Map<Bean, Bean>对象
     * 
     * @param json
     * @param mapClass
     * @param keyClass
     * @param valueClass
     * @return
     */
    public static <T> T jsonToMap(final String json, Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass) {
        try {
            if (json != null && !"".equals(json)) {
                return (T) mapper.readValue(json, constructMapType(mapClass, keyClass, valueClass));
            }
        } catch (Exception e) {
            LOG.error("", e);
        }
        return null;
    }

    private JacksonUtil() {
    }
}
