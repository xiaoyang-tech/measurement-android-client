package cn.xymind.healthdetection.synthesis.androidsdksamples.utils;

import java.text.DecimalFormat;

public class ValueUtil {
    /**
     * double转String,保留小数点后两位
     *
     * @param num
     * @return
     */
    public static String doubleToString(double num) {
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("#.##").format(num);
    }

    /**
     * float 保留两位有效数字
     *
     * @param num
     * @return
     */
    public static float floatToTwo(float num) {
        return (float) Math.round(num * 1000) / 1000;
    }
}
