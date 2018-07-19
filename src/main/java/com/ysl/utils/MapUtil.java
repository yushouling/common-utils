package com.ysl.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MapUtil {

    public static Map<String, Object> bean2Map (Object object) throws Exception{
        Map<String, Object> result = new HashMap<String, Object>();
        if (object == null) {
            return result;
        }
        Class<? extends Object> cls = object.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equalsIgnoreCase("serialVersionUID")) {
                continue;
            }
            result.put(field.getName(), field.get(object));
        }
        return result;
    }

    public static Object map2Bean (Map<String, Object> map, Class<?> cls) throws Exception{
        Object object = cls.newInstance();
        for (String key : map.keySet()){
            Field temFiels = cls.getDeclaredField(key);
            temFiels.setAccessible(true);
            temFiels.set(object, map.get(key));
        }
        return object;
    }
}
