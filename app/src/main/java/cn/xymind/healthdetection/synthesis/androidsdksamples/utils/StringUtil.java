package cn.xymind.healthdetection.synthesis.androidsdksamples.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * created by xingxing on 18/10/1 18:41
 * Email：zhangwenxing1716@@163.com
 */
public class StringUtil {

    public static String formatBytes(long size) {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);

        if (size < 1024 * 1024) {
            float kb = (int) (size / 1024);
            return format.format(kb) + " KB";
        } else {
            float mb = size / (1024 * 1024f);
            return format.format(mb) + " MB";
        }
    }


    public static String getFileNameByUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }

        int index = url.lastIndexOf("/");
        if (index == -1) {
            return "";
        }

        return url.substring(index, url.length());
    }

    /**
     * 格式化小数
     *
     * @param number
     * @param len    小数保留位数
     * @return
     */
    public static String formatNumber(double number, int len) {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(len);

        return format.format(number);
    }

    public static String compareNumber(BigDecimal number){
        if (!"".equals(number) && number != null){
            if (new BigDecimal(number.intValue()).compareTo(number)==0){
                //整数
                return String.valueOf(number.intValue());
            }else {
                //小数
                return String.valueOf(number);
            }
        }
        return "";
    }

    /**
     * 将小数格式化成百分数
     *
     * @param num
     * @return
     */
    public static String format2Percent(double num) {
        NumberFormat formater = NumberFormat.getPercentInstance();
        formater.setMaximumIntegerDigits(3);
        formater.setMaximumFractionDigits(2);
//        formater.setMinimumFractionDigits(0);

        return formater.format(num);
    }

    public static boolean isContainsChar(String txt) {
        if (TextUtils.isEmpty(txt)) {
            return false;
        }

        for (int i = 0; i < txt.length(); i++) {
            char c = txt.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                return true;
            }
        }

        return false;
    }

    public static boolean isContainsNumber(String txt) {
        if (TextUtils.isEmpty(txt)) {
            return false;
        }

        for (int i = 0; i < txt.length(); i++) {
            char c = txt.charAt(i);
            if ((c >= '0' && c <= '9')) {
                return true;
            }
        }

        return false;
    }
}
