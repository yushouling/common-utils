package com.ysl.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取IP、语言及设置本地语言
 */
public class IpUtil {

    private static Logger logger = LoggerFactory.getLogger(IpUtil.class);
    
    /**
     * 根据请求获取用户IP
     */
    public static String getIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet != null ? inet.getHostAddress() : null;
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(',') > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(','));
            }
        }
        return ipAddress == null ? "" : ipAddress;
    }


    /**
     * 根据浏览器获取语言
     *
     * @param request
     * @return
     */
    public static String findLangByWin(HttpServletRequest request) {
        String lang = "";
        try {
            String acceptLang = request.getHeader("Accept-Language");
            String winLang = "";
            if (acceptLang != null) {
                winLang = acceptLang.split(",")[0];
            }
            if ("".equals(winLang)) {
                lang = "en";
            } else {

            }
        } catch (Exception e) {
            lang = "zh";
        }
        return lang;
    }

    /**
     * 根据IP获取语言
     *
     * @param ip IP地址
     * @return
     */
    public static String getAddrByIp(String ip) {
        String addr = null;
        try {
        	String strIp = StringUtils.substringBeforeLast(ip, ".");
        	Map<String, String> map = new HashMap<>();
        	// map = ;
        	if("203.19".equals(strIp)){
        		addr = "HK";
        	}else{
	            String[] ips = ip.split("\\.");
	            if (ips[1].length() < 2) {
	                ips[1] = "00" + ips[1];
	            }
	            if (ips[1].length() < 3) {
	                ips[1] = "0" + ips[1];
	            }
	            if (ips[2].length() < 2) {
	                ips[2] = "00" + ips[2];
	            }
	            if (ips[2].length() < 3) {
	                ips[2] = "0" + ips[2];
	            }
	            String resultIp = ips[0] + ips[1] + ips[2];
	            for (String str : map.keySet()) {
	                int lastIp = Integer.parseInt(resultIp);
	                int startIp = Integer.parseInt(StringUtils.substringBefore(str, "-"));
	                int endIp = Integer.parseInt(StringUtils.substringAfter(str, "-"));
	                if (lastIp >= startIp && lastIp < endIp) {
	                    addr = map.get(str);
	                }
	            }
        	}
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
        return addr;
    }

}
