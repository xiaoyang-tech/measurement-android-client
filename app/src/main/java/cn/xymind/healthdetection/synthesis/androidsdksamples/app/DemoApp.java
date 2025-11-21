package cn.xymind.healthdetection.synthesis.androidsdksamples.app;

import android.app.Application;

import cn.xiaoyang.measurement.abstraction.Category;
import cn.xymind.measurementsdk.config.IMeasurementConfig;
import cn.xymind.measurementsdk.config.MeasurementConfig;
import cn.xymind.measurementsdk.facelandmarker.FeatureExtractor;
import cn.xymind.measurementsdk.facelandmarker.InitCallback;
import cn.xymind.measurementsdk.util.Constants;
import cn.xymind.measurementsdk.util.MyLog;

public class DemoApp extends Application {

    public static IMeasurementConfig config;
    public static FeatureExtractor featureExtractor = null;

    @Override
    public void onCreate() {
        super.onCreate();

        config = new MeasurementConfig(getApplicationContext(), "3a19c03d822a3967ca511e966e677af2", "3a19c03d822aedbecc778214106b614d");
        config.setMeasurementDuration(15000);
        preload();
    }

    private void preload() {
        new Thread(() -> {
            try {
                DemoApp.featureExtractor = new FeatureExtractor(this, new InitCallback() {
                    @Override
                    public void onInitialized() {

                        MyLog.d(cn.xymind.measurementsdk.util.Constants.TAG, "Mediapipe 初始化成功");

                    }

                    @Override
                    public void onError(Exception e) {
                        MyLog.d(cn.xymind.measurementsdk.util.Constants.TAG, "Mediapipe 初始化失败");
                    }
                });
            } catch (Exception e) {
                MyLog.e(Constants.TAG, "Mediapipe 初始化失败" + e.getMessage());
            }
        }).start();
    }
}
