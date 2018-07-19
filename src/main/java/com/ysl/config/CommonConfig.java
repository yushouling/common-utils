package com.ysl.config;

import java.util.Properties;
import org.springframework.stereotype.Component;
import com.ysl.utils.FileLoaderUtil;

/**
 * 公共属性配置
 */
@Component
public class CommonConfig {
    private static Properties properties;

    static {
        // 初始化系统配置信息
        init();
    }

    private static void init() {
        properties = FileLoaderUtil.loadProByClassPath("common.properties");
    }

    public static String getValue(String name) {
    	if(name != null) {
    		return properties.getProperty(name);
    	} else {
    		return null;
    	}
    }

}
