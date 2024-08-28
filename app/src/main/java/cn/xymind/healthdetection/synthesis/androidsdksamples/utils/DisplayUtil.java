package cn.xymind.healthdetection.synthesis.androidsdksamples.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DisplayUtil {
    /**
     * dip转换成px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转换成dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        java.lang.reflect.Field field = null;
        int x = 0;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);

            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (statusBarHeight == 0) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
        }

        return statusBarHeight;
    }

    public static double getWidthInScreen(Context context, int width) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;         // 屏幕宽度（像素）
//        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
//        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
//        int screenHeight = (int) (height / density);// 屏幕高度(dp)

        return width / densityDpi * 2.54;
    }

    /**
     * 获取Bitmap的亮度流明值
     *
     * @param bitmap 计算的图片
     * @return 流明值
     */
    public static double getBrightness(Bitmap bitmap) {
        if (bitmap == null) {
            return 0.0;
        }
        // 计算亮度流明值
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int totalPixels = width * height;
        int[] pixels = new int[totalPixels];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int sumR = 0, sumG = 0, sumB = 0;
        for (int i = 0; i < totalPixels; i++) {
            int pixel = pixels[i];
            sumR += Color.red(pixel);
            sumG += Color.green(pixel);
            sumB += Color.blue(pixel);
        }
        double avgR = (double) sumR / totalPixels;
        double avgG = (double) sumG / totalPixels;
        double avgB = (double) sumB / totalPixels;
        double Y = 0.2126 * avgR + 0.7152 * avgG + 0.0722 * avgB;

        return Y;
    }

}
