package com.ysl.utils;

import com.hankcs.hanlp.HanLP;

/**
 * 中文繁简互转
 */
public final class ChineseUtil {

    /**
     * 简体转繁体
     * 
     * @param simpStr
     *            简体字符串
     * @return 繁体字符串
     */
    public static String simp2Trad(String simpStr) {
        return HanLP.convertToTraditionalChinese(simpStr);
    }

    /**
     * 繁体转简体
     * 
     * @param tradStr
     *            繁体字符串
     * @return 简体字符串
     */
    public static String tradToSimp(String tradStr) {
        return HanLP.convertToSimplifiedChinese(tradStr);
    }

    /**
     * 繁体转简体，一个字一个字的转
     * 
     * @param tradStr
     *            繁体字符串
     * @return 简体字符串
     */
    public static String tradToSimpByChar(String tradStr) {
        StringBuilder buf = new StringBuilder();
        int len = tradStr.length();
        for (int i = 0; i < len; i++) {
            String simplifiedStr = HanLP.convertToSimplifiedChinese(tradStr.substring( i, i + 1));
            buf.append(simplifiedStr);
        }
        return buf.toString();
    }

    private ChineseUtil() {
    }
}