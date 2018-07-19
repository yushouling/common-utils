package com.ysl.utils;

import java.util.ArrayList;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HttpUtil {
    private static final Logger LOG = LoggerFactory.getLogger(HttpUtil.class.getName());

    private HttpUtil() {
    }

    public static final String httpClientPost(String url) {
        String result = "";
        HttpClient client = HttpClients.createDefault();
        HttpGet getMethod = new HttpGet(url);
        try {
            HttpResponse response = client.execute(getMethod);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                long len = entity.getContentLength();
                if (len != -1 && len < 2048) {
                    result= EntityUtils.toString(entity);
                    EntityUtils.consume(entity);  
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            getMethod.releaseConnection();
        }
        return result;
    }

    public static final String httpClientPost(String url, ArrayList<NameValuePair> list) {
        String result = "";
        HttpClient client = HttpClients.createDefault();
        HttpPost postMethod = new HttpPost(url);
        postMethod.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, Consts.UTF_8);  
            postMethod.setEntity(entity);
            HttpResponse response = client.execute(postMethod);  
            int statusCode = response.getStatusLine().getStatusCode();  
            if (statusCode == 200) {  
                HttpEntity respEntity = response.getEntity();  
                if (respEntity != null) {  
                    result = EntityUtils.toString(respEntity, "utf-8");  
                }  
                EntityUtils.consume(respEntity);  
                return result;  
            }else{
                LOG.error("请求失败 "+url);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            postMethod.releaseConnection();
        }
        return result;
    }


}
