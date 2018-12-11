package com.ysl.utils;

import java.io.StringWriter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * XML格式数据转换工具类
 */
public class XstreamUtil {
    private static XStream xstream;

    static {
        xstream = new XStream(new DomDriver("UTF-8"));
    }

    /**
     * 以压缩的方式输出XML
     * @param obj
     * @return
     */
    public static String toCompressXml(Object obj) {
        // 识别obj类中的注解
        xstream.processAnnotations(obj.getClass());
        StringWriter sw = new StringWriter();
        xstream.marshal(obj, new CompactWriter(sw));
        return sw.toString();
    }

    /**
     * 以格式化的方式输出XML
     * @param obj
     * @return
     */
    public static String toXml(Object obj) {
        // 识别obj类中的注解
        xstream.processAnnotations(obj.getClass());
        return xstream.toXML(obj);
    }

    /**
     * 转换成JavaBean
     * @param xmlStr
     * @param cls
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T toBean(String xmlStr, Class<T> cls) {
        // 识别cls类中的注解
        xstream.processAnnotations(cls);
        T t = ((T) xstream.fromXML(xmlStr));
        return t;
    }
}