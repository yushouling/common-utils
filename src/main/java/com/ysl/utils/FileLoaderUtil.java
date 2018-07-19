package com.ysl.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class FileLoaderUtil {
    public static int TYPE_CLASSPATH = 1;

    public static Properties loadProByClassPath(String filePath) {
        Properties pro = null;
        URL url = FileLoaderUtil.class.getClassLoader().getResource(filePath);
        if (url != null) {
            InputStream in = null;
            try {
                in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
                pro = new Properties();
                pro.load(in);
            } catch (Exception e) {
                System.out.println("load file fail : " + filePath);
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("properties file in class path not found: " + filePath);
        }
        return pro;
    }

    public static Properties loadProByFilePath(String filePath) {
        Properties pro = null;
        File file = new File(filePath);
        if (file.exists()) {
            InputStream in = null;
            try {
                in = new FileInputStream(file);
                pro = new Properties();
                pro.load(in);
            } catch (Exception e) {
                System.out.println("load file fail : " + filePath);
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("properties file in file path not found: " + filePath);
        }
        return pro;
    }

    public static InputStream getInputStream(String filePath, int pathType) {
        InputStream in = null;
        if (TYPE_CLASSPATH == pathType) {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        } else {
            try {
                in = new FileInputStream(filePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return in;
    }

    public static void main(String[] args) {
        Properties pro = loadProByClassPath("advertisement.properties");
        System.out.println(pro.get("WEEK_MATCH_PAGE"));
    }

    public static Properties loadPro(String filePath, int pathType) {
        if (pathType == TYPE_CLASSPATH) {
            return loadProByClassPath(filePath);
        }
        return loadProByFilePath(filePath);
    }
}
