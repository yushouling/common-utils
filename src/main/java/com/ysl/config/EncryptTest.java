package com.ysl.config;

import com.alibaba.druid.filter.config.ConfigTools;
/**
 * 加密和解密(数据库密码)
 */
public class EncryptTest {

	public static void main(String[] args) throws Exception {
		// 加密
		String encryptPwd = ConfigTools.encrypt("password123");
		System.out.println(encryptPwd);
		// 解密
		String decryptPwd = ConfigTools.decrypt(encryptPwd);
		System.out.println(decryptPwd);
	}
}
