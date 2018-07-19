package com.ysl.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public class StringUtil {

	/***
	 * 压缩GZip
	 * 
	 * @return
	 */
	public static String gZip(String input) {
		byte[] bytes = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(bos);
			gzip.write(input.getBytes("utf-8"));
			gzip.finish();
			gzip.close();
			bytes = bos.toByteArray();
			bos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new BASE64Encoder().encodeBuffer(bytes);
	}

	/***
	 * 解压GZip
	 * 
	 * @return
	 */
	public static String unGZip(String input) {
		byte[] bytes = null;
		String out = "";
		ByteArrayInputStream bis = null;
		GZIPInputStream gzip = null;
		ByteArrayOutputStream baos = null;
		try {
			bis = new ByteArrayInputStream(
					new BASE64Decoder().decodeBuffer(input));
			gzip = new GZIPInputStream(bis);
			byte[] buf = new byte[1024];
			int num = -1;
			baos = new ByteArrayOutputStream();
			while ((num = gzip.read(buf, 0, buf.length)) != -1) {
				baos.write(buf, 0, num);
			}
			bytes = baos.toByteArray();
			baos.flush();
			baos.close();
			baos = null;
			gzip.close();
			gzip = null;
			bis.close();
			bis = null;
			out = new String(bytes, "utf-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (null != baos) {
				try {
					baos.close();
					baos = null;
				} catch (IOException e) {
				}

			}

			if (null != gzip) {
				try {
					gzip.close();
					gzip = null;
				} catch (IOException e) {
				}
			}

			if (null != bis) {
				try {
					bis.close();
					bis = null;
				} catch (IOException e) {
				}

			}

		}
		return out;
	}

	/**
	 * 对字符串进行Base64加密
	 * 
	 * @param str
	 * @return
	 */
	public static String getBase64(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = new BASE64Encoder().encode(b);
		}
		return s;
	}

	/**
	 * 对Base64加密后的字符串进行解密
	 * 
	 * @param s
	 * @return
	 */
	public static String getFromBase64(String s) {
		byte[] b = null;
		String result = null;
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				result = new String(b, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 从给定的字符串中截取指定首尾串之间的数据
	 * 
	 * @param context
	 * @param signStr
	 * @param quote
	 * @return
	 */
	public static String parseFormValue(String context, String signStr,
			String quote) {
		String ret = "";
		if (context.contains(signStr)) {
			ret = context
					.substring(context.indexOf(signStr) + signStr.length());

			if (ret.contains(quote)) {
				ret = ret.substring(0, ret.indexOf(quote));
			}
		}

		return ret;
	}

}
