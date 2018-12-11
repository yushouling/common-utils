package com.ysl.utils;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.*;

/**
 * 一些通用的工具方法
 *
 * @author luoyong-014
 * @date 2018/7/31 15:29
 */
public class XmlUtil {

    protected static final Logger LOG = LoggerFactory.getLogger(XmlUtil.class.getName());

    public static String toXml(SortedMap<String, String> map) {
        StringBuilder bud = new StringBuilder();
        bud.append("<xml>");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            bud.append("<").append(entry.getKey()).append(">");
            bud.append("<![CDATA[").append(entry.getValue()).append("]]>");
            bud.append("</").append(entry.getKey()).append(">\n");
        }
        bud.append("</xml>");
        return bud.toString();
    }

    public static String toXml(Map<String, String> map) {
        StringBuilder bud = new StringBuilder();
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);
        bud.append("<xml>");
        for (String key : keys) {
            String val = map.get(key);
            if (val == null) {
                continue;
            }
            bud.append("<").append(key).append(">");
            bud.append("<![CDATA[").append(map.get(key)).append("]]>");
            bud.append("</").append(key).append(">\n");
        }
        bud.append("</xml>");
        return bud.toString();
    }

    /**
     * xml 转map
     *
     * @param xmlBytes
     * @return
     */
    public static Map<String, String> toMap(byte[] xmlBytes) {
        InputSource source = new InputSource(new ByteArrayInputStream(xmlBytes));
        source.setEncoding("UTF-8");
        return toMap(source);
    }

    /**
     * xml 转map
     *
     * @param xml
     * @return
     */
    public static Map<String, String> toMap(String xml) {
        InputSource source = new InputSource(new StringReader(xml));
        source.setEncoding("UTF-8");
        return toMap(source);
    }

    /**
     * 转MAP
     *
     * @param source
     * @return
     */
    private static Map<String, String> toMap(InputSource source) {
        SAXReader reader = new SAXReader(false);
        Map<String, String> map = new HashMap<String, String>();
        try {
            // 防御XXE攻击
            reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
            reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            Document doc = reader.read(source);
            List<Element> els = doc.getRootElement().elements();
            for (Element el : els) {
                map.put(el.getName().toLowerCase(), el.getTextTrim());
            }
        } catch (Exception e) {
            LOG.error("CommUtil toMap error ", e);
        }
        return map;
    }
}

