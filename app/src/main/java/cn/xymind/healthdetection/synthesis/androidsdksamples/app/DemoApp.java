package cn.xymind.healthdetection.synthesis.androidsdksamples.app;

import android.app.Application;

import com.tencent.mmkv.MMKV;


import cn.xiaoyang.measurement.abstraction.Category;
import cn.xymind.measurementsdk.config.IMeasurementConfig;
import cn.xymind.measurementsdk.config.MeasurementConfig;
import cn.xymind.measurementsdk.enums.ServiceType;
import cn.xymind.measurementsdk.facelandmarker.FaceLandmarkerHelper;
import cn.xymind.measurementsdk.facelandmarker.InitCallback;
import cn.xymind.measurementsdk.util.Constants;
import cn.xymind.measurementsdk.util.MyLog;

public class DemoApp extends Application {

    public static IMeasurementConfig config;
    public static FaceLandmarkerHelper faceLandmarkerHelper = null;

    @Override
    public void onCreate() {
        super.onCreate();

        config = new MeasurementConfig(getApplicationContext(), "id", "key", ServiceType.PUBLIC.getValue());
        config.setMinMeasurementDuration(15000);
        config.setMinFramesCnt(257);
        config.setMeasurementCategory(Category.MeasurementCategory.All);

        initFace();
    }

    private void initFace() {
        new Thread(() -> {
            try {
                DemoApp.faceLandmarkerHelper = new FaceLandmarkerHelper(this, new InitCallback() {
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
