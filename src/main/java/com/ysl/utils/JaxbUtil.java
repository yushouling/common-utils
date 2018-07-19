package com.ysl.utils;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * 解析XML工具类
 */
public final class JaxbUtil {
    private static ConcurrentMap<Class<?>, JAXBContext> jaxbContexts = new ConcurrentHashMap<Class<?>, JAXBContext>();

    /**
     * 把xml转换成对应的bean
     * 
     * @param <T>
     * @param clazz
     * @param xml
     * @return
     */
    public static <T> T xml2Bean(Class<T> clazz, String xml) {
        if (StringUtils.isBlank(xml)) {
            return null;
        }
        try {
            StringReader reader = new StringReader(xml);
            return clazz.cast(createUnmarshaller(clazz).unmarshal(reader));
        } catch (JAXBException e) {
            throw new RuntimeException("Could not parse xml to class [" + clazz + "]: " + e.getMessage(), e);
        }
    }

    /**
     * 把bean转换成对应的xml
     * 
     * @param obj
     * @return
     */
    public static String bean2Xml(Object obj) {
        return bean2Xml(obj, null, false);
    }

    /**
     * 是否省略xml头信息
     * 
     * @param obj
     * @param encoding
     *            编码格式
     * @param bool
     *            是否省略xml头信息
     * @return
     */
    public static String bean2Xml(Object obj, String encoding, boolean bool) {
        if (obj == null) {
            return null;
        }
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            createMarshaller(obj.getClass(), encoding, bool).marshal(obj, os);
            return os.toString("UTF-8");
        } catch (JAXBException e) {
            throw new RuntimeException("Could not parse [" + obj + "] to xml " + e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 创建Marshaller并设定encoding(可为null). <br/>
     * 线程不安全，需要每次创建或pooling。
     */
    private static Marshaller createMarshaller(Class<?> clazz, String encoding, boolean bool) {
        try {
            JAXBContext jaxbContext = getJaxbContext(clazz);
            Marshaller marshaller = jaxbContext.createMarshaller();
            // 是否格式化生成的xml
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            // 是否省略xml头信息
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, bool);
            if (StringUtils.isNotBlank(encoding)) {
                marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            }
            return marshaller;
        } catch (JAXBException e) {
            throw new RuntimeException("Could not createMarshaller for class [" + clazz + "]: " + e.getMessage(), e);
        }
    }

    /**
     * 创建UnMarshaller. <br/>
     * 线程不安全，需要每次创建或pooling。
     */
    private static Unmarshaller createUnmarshaller(Class<?> clazz) {
        try {
            JAXBContext jaxbContext = getJaxbContext(clazz);
            return jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException("Could not createUnmarshaller for class [" + clazz + "]: " + e.getMessage(), e);
        }
    }

    /**
     * 维护jaxbContexts
     */
    private static JAXBContext getJaxbContext(Class<?> clazz) {
        Validate.notNull(clazz, "'clazz' must not be null");
        JAXBContext jaxbContext = jaxbContexts.get(clazz);
        try {
            if (jaxbContext == null) {
                jaxbContext = JAXBContext.newInstance(clazz);
                JAXBContext j = jaxbContexts.putIfAbsent(clazz, jaxbContext);
                if (j != null) {
                    jaxbContext = j;
                }
            }
        } catch (JAXBException e) {
            throw new RuntimeException("Could not instantiate JAXBContext for class [" + clazz + "]: " + e.getMessage(), e);
        }
        return jaxbContext;
    }

    /**
     * 字符转换，如：P. Macierzy&amp;#324;ski -> P. Macierzyński
     * 
     * @param input
     * @return
     */
    public static String unescapeXml(String input) {
        if (input == null) {
            return null;
        }
        String res = StringEscapeUtils.unescapeXml(input);
        if (res.indexOf("&#") > -1) {
            res = StringEscapeUtils.unescapeXml(res);
        }
        return res;
    }

    private JaxbUtil() {
    }
}
