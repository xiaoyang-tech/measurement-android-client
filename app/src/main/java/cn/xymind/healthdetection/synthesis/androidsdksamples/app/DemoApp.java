package cn.xymind.healthdetection.synthesis.androidsdksamples.app;

import android.app.Application;

import com.alibaba.android.mnnkit.utils.Constants;
import com.tencent.mmkv.MMKV;

import cn.xymind.measurementsdk.bean.IMeasurementConfig;
import cn.xymind.measurementsdk.bean.MeasurementConfig;
import cn.xymind.measurementsdk.enums.ServiceType;
import cn.xymind.measurementsdk.facelandmarker.FaceLandmarkerHelper;
import cn.xymind.measurementsdk.facelandmarker.InitCallback;
import cn.xymind.measurementsdk.util.MyLog;

public class DemoApp extends Application {

    public static IMeasurementConfig config;
    public static FaceLandmarkerHelper faceLandmarkerHelper = null;

    @Override
    public void onCreate() {
        super.onCreate();


        MMKV.initialize(this);
        config = new MeasurementConfig(getApplicationContext(), "3a12dfa8e04354567abadae17842e617", "3a12dfa8e0436cd26887f41069321b4e", ServiceType.PUBLIC.getValue());


        initFace();
    }

    private void initFace() {
        new Thread(() -> {
            try {
                DemoApp.faceLandmarkerHelper = new FaceLandmarkerHelper(this, new InitCallback() {
                    @Override
                    public void onInitialized() {

                        MyLog.d(Constants.TAG, "Mediapipe 初始化成功");

                    }

                    @Override
                    public void onError(Exception e) {
                        MyLog.d(Constants.TAG, "Mediapipe 初始化失败");
                    }
                });
            } catch (Exception e) {
                MyLog.e(Constants.TAG, "Mediapipe 初始化失败" + e.getMessage());
            }
        }).start();

    }
}
