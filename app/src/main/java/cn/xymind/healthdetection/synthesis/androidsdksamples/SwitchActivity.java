package cn.xymind.healthdetection.synthesis.androidsdksamples;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.mnnkit.entity.FaceDetectionReport;
import com.alibaba.android.mnnkit.utils.Constants;
import com.google.mediapipe.framework.image.BitmapImageBuilder;
import com.google.mediapipe.framework.image.MPImage;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;


import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.concurrent.LinkedBlockingQueue;

import cn.xiaoyang.measurement.abstraction.MeasurementReport;
import cn.xymind.healthdetection.synthesis.androidsdksamples.app.DemoApp;
import cn.xymind.measurement_sdk_plugin.camera1.Camera1Config;
import cn.xymind.measurement_sdk_plugin.camera1.MeasurementCamera1;
import cn.xymind.measurement_sdk_plugin.camera2.MeasurementCamera2;
import cn.xymind.measurementsdk.bean.IMeasurementException;
import cn.xymind.measurementsdk.bean.MeasurementRequiredData;
import cn.xymind.measurementsdk.enums.CameraType;
import cn.xymind.measurementsdk.enums.RecognitionType;
import cn.xymind.measurementsdk.listener.IMeasurementListener;
import cn.xymind.measurement_sdk_plugin.strategy.CameraStrategy;
import cn.xymind.measurementsdk.strategy.FaceRecognitionStrategy;
import cn.xymind.measurementsdk.strategy.MNNFaceRecognitionStrategy;
import cn.xymind.measurementsdk.strategy.MediapipeFaceRecognitionStrategy;
import cn.xymind.measurementsdk.util.ImageUtil;
import cn.xymind.measurementsdk.util.MyLog;
import cn.xymind.measurement_sdk_plugin.view.AutoFitTextureView;

public class SwitchActivity extends AppCompatActivity implements IMeasurementListener {

    private AutoFitTextureView texture_view;

    private MeasureScopeView2 scopeView;

    private ImageView iv_measurement_rotation1;
    private ImageView iv_measurement_rotation2;
    private ImageView iv_measurement_rotation3;
    private ImageView testIv;


    private TextView tv_bottom_note2;
    private TextView tv_bottom_note;
    private TextView tv_3s_count_down;
    private TextView tv_heart_bmp;
    private TextView brightnessValueTv;
    private boolean isIncomingData = true;

    private CountDownTimer timer_3;
    private CountDownTimer timer_5;

    private XProgressDialog progressDialog;
    private Paint KeyPointsPaint = new Paint();

    private String measurementId;


    private CameraType cameraType;
    private RecognitionType recognitionType;

    private CameraStrategy cameraStrategy;
    private FaceRecognitionStrategy faceRecognitionStrategy;

    private LinkedBlockingQueue<byte[]> videoFrameQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);

        videoFrameQueue = new LinkedBlockingQueue<>(1000);

        if (getIntent().getExtras() != null) {
            cameraType = CameraType.valueOf(getIntent().getStringExtra("cameraType"));
            recognitionType = RecognitionType.valueOf(getIntent().getStringExtra("recognitionType"));

            MyLog.d(Constants.TAG, "相机选择：" + cameraType.name());
            MyLog.d(Constants.TAG, "测量选择：" + recognitionType.name());
        }

        initView();
        initMeasurement();
    }


    private void initView() {
        texture_view = this.findViewById(R.id.texture_view);
        scopeView = this.findViewById(R.id.scopeView);
        iv_measurement_rotation1 = this.findViewById(R.id.iv_measurement_rotation1);
        iv_measurement_rotation2 = this.findViewById(R.id.iv_measurement_rotation2);
        iv_measurement_rotation3 = this.findViewById(R.id.iv_measurement_rotation3);
        tv_bottom_note2 = this.findViewById(R.id.tv_bottom_note2);
        tv_bottom_note = this.findViewById(R.id.tv_bottom_note);
        tv_3s_count_down = this.findViewById(R.id.tv_3s_count_down);
        tv_heart_bmp = this.findViewById(R.id.tv_heart_bmp);
        testIv = this.findViewById(R.id.testIv);
        brightnessValueTv = this.findViewById(R.id.brightnessValueTv);

        progressDialog = new XProgressDialog(SwitchActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void initMeasurement() {
        setHeartRateAnimation();
        //初始化相机
        switch (cameraType) {
            case CAMERA1:
                cameraStrategy = new MeasurementCamera1(new Camera1Config(this, texture_view));
                break;
            case CAMERA2:
                cameraStrategy = new MeasurementCamera2(this, texture_view);
                break;
        }
        cameraStrategy.setSize(640, 480);
        MeasurementRequiredData requiredData = new MeasurementRequiredData();
        requiredData.setWeight(80);
        requiredData.setHeight(170);
        requiredData.setAge(30);
        requiredData.setGender(1);
        switch (recognitionType) {
            case MNN:
                faceRecognitionStrategy = new MNNFaceRecognitionStrategy(requiredData,DemoApp.config);
                break;
            case MEDIAPIPE:
                faceRecognitionStrategy = new MediapipeFaceRecognitionStrategy(requiredData,DemoApp.config, DemoApp.faceLandmarkerHelper);
                break;
        }

        faceRecognitionStrategy.setMeasurementListener(SwitchActivity.this);

        scopeView.setMeasureTime(15);
        scopeView.setDurationSecs(15.0);
        scopeView.setStatus(MeasureScopeView2.STATUS_REGULAR_GREEN);

        KeyPointsPaint.setColor((Color.WHITE));
        KeyPointsPaint.setStyle(Paint.Style.FILL);
        KeyPointsPaint.setStrokeWidth(2);

        iv_measurement_rotation1.setVisibility(View.VISIBLE);
        iv_measurement_rotation2.setVisibility(View.VISIBLE);
        iv_measurement_rotation3.setVisibility(View.VISIBLE);
        scopeView.setImageView1(iv_measurement_rotation1);
        scopeView.setImageView2(iv_measurement_rotation2);
        scopeView.setImageView3(iv_measurement_rotation3);
        timer_3 = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_3s_count_down.setText(millisUntilFinished / 1000 + 1 + "");

                MPImage mpImage = new BitmapImageBuilder(convertImageToNV21("gpu.jpg")).build();
                DemoApp.faceLandmarkerHelper.detectAsync(mpImage, SystemClock.uptimeMillis());
            }

            @Override
            public void onFinish() {
                MyLog.d(Constants.TAG, "------------------------------------321倒计时结束，开始测量工作------------------------------------");
                //开始测量
                tv_3s_count_down.setVisibility(View.GONE);
                tv_bottom_note.setText("测量条件满足");
                tv_bottom_note2.setText("测量过程中请保持静止");

                tv_heart_bmp.setVisibility(View.VISIBLE);
                isIncomingData = true;
                scopeView.onMeasureStart(1);


                cameraStrategy.setFrameListener((frameData, timestamp, width, height, displayOrientation) ->
                        faceRecognitionStrategy.processFrame(frameData, timestamp, width, height, displayOrientation));
                faceRecognitionStrategy.start();

//                initSaveVideo();
            }
        };

        tv_3s_count_down.setVisibility(View.VISIBLE);
        timer_3.start();
    }

    private Bitmap convertImageToNV21(String filename) {
        try {
            // Read the image from assets
            InputStream is = getAssets().open(filename);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();

            // Resize the bitmap to 50x50
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, true);
            return resizedBitmap; // Replace with the actual NV21 byte array
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 设置心率动画
     */
    private void setHeartRateAnimation() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(tv_heart_bmp, "scaleX", 1, 1.1f);
        scaleX.setRepeatCount(-1);
        scaleX.setRepeatMode(ObjectAnimator.RESTART);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(tv_heart_bmp, "scaleY", 1, 1.1f);
        scaleY.setRepeatCount(-1);
        scaleY.setRepeatMode(ObjectAnimator.RESTART);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(900);
        animatorSet.setInterpolator(new AnticipateInterpolator());
        animatorSet.play(scaleX).with(scaleY);
        animatorSet.start();
    }

    @Override
    public void onFaceLandkarkers(float[] floats, int i, int i1) {

    }

    @Override
    public void onCreated(String measurementId) {
        MyLog.d(Constants.TAG, "InitMeasurement " + measurementId);
        this.measurementId = measurementId;
    }

    @Override
    public void onFaceDetected(FaceDetectionReport[] faceDetectionReports, int i, int i1) {

    }

    @Override
    public void onFrameProcessed(byte[] nv21, long timestamp, int width, int height) {
        byte[] bytes3 = ImageUtil.nv21ToBitmapBytes(nv21, width, height);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes3, 0, bytes3.length);


        runOnUiThread(() -> testIv.setImageBitmap(bitmap));
    }

    @Override
    public void onChunkProcessed(String measurementId, MeasurementReport.HrReport hrReport) {
        runOnUiThread(() -> {
            if (!hrReport.hasData()) {
                MyLog.e(Constants.TAG, "阶段性数据为空");
                return;
            }

            MeasurementReport.HrData hrData = hrReport.getData();
            double hrBpm = hrData.getHrBpm();
            MyLog.d(Constants.TAG, "心率：" + hrBpm + "   心率变异性：" + hrData.getHrv());
            BigDecimal hr = new BigDecimal(hrBpm);
            int value = hr.setScale(0, BigDecimal.ROUND_UP).intValue();
            tv_heart_bmp.setText(Integer.toString(value));
        });
    }

    @Override
    public void onDataCollected() {

    }

    @Override
    public void onFinished() {
        MyLog.d(Constants.TAG, "------------------------------------测量工作结束，等待汇总结果------------------------------------");
        //测量成功
        isIncomingData = false;
        runOnUiThread(() -> {
            progressDialog.setMessage("请稍候，我们正在努力为您计算中......");
            progressDialog.show();
        });

        //视频录制完成
//        VideoRecorderUtil.getInstance().setMeasureIsSuccess(true);
//        saveVideoFrame();
    }

    @Override
    public void onReportProcessed(String measurementId, MeasurementReport.Report measurementReport) {
        try {
            runOnUiThread(() -> progressDialog.dismiss());

            MeasurementReport.HrReport hrReport = measurementReport.getHrReport();
            hrReport.getExplanation().getHrv().getAdvicesList();//建议集合
            MeasurementReport.AfReport afReport = measurementReport.getAfReport();
            MeasurementReport.Spo2hReport spo2HReport = measurementReport.getSpo2HReport();
            MeasurementReport.BpReport bpReport = measurementReport.getBpReport();
            MeasurementReport.EssentialReport essentialReport = measurementReport.getEssentialReport();
            MeasurementReport.RiskReport riskReport = measurementReport.getRiskReport();
            MeasurementReport.HealthScoreReport healthScoreReport = measurementReport.getHealthScoreReport();


            MeasureResult measureResult = new MeasureResult();
            measureResult.setMeasurementId(measurementId);
            if (hrReport != null) {
                MyLog.e(Constants.TAG, "心率= " + hrReport);
                if (hrReport.getCode() == 10100) {
                    //这里表示汇总结果心率不正常，直接取消汇总并弹窗
                    MyLog.e(Constants.TAG, "心率不正常 Code是：" + hrReport.getCode() + ",停止结果汇总，并弹窗：" + hrReport.getMsg());
                    new XPopup.Builder(SwitchActivity.this).dismissOnTouchOutside(false)
                            .asConfirm("测量失败", hrReport.getMsg(), "取消", "确定",
                                    new OnConfirmListener() {
                                        @Override
                                        public void onConfirm() {
                                            finish();
                                        }
                                    }, new OnCancelListener() {
                                        @Override
                                        public void onCancel() {

                                        }
                                    }, true).show();
                    return;
                } else {
                    MeasurementReport.HrData hrData = hrReport.getData();
                    MyLog.e(Constants.TAG, "心率=" + hrData.getHrBpm() + " 心率变异率=" + hrData.getHrv());
                    measureResult.sethR_BPM((float) hrData.getHrBpm());
                    measureResult.setHrv((float) hrData.getHrv());
                }
            }
            if (afReport != null) {
                MyLog.e(Constants.TAG, "房颤= " + afReport);
                if (afReport.getCode() == 10200) {
                    //房颤异常
                    MyLog.e(Constants.TAG, "房颤异常 Code是：" + afReport.getCode() + ",显示内容：" + afReport.getMsg());
                    measureResult.setIsAf(-100000);
                    measureResult.setAfMsg(afReport.getMsg());
                } else {
                    MyLog.e(Constants.TAG, "房颤=" + afReport.getData());
                    MyLog.e(Constants.TAG, "房颤值：" + afReport.getData());
                    measureResult.setIsAf((int) afReport.getDataValue());
                }
            }
            if (spo2HReport != null) {
                MyLog.e(Constants.TAG, "血氧= " + spo2HReport);
                if (spo2HReport.getCode() == 30100) {
                    //血氧异常
                    MyLog.e(Constants.TAG, "血氧异常 Code是：" + spo2HReport.getCode() + ",显示内容：" + spo2HReport.getMsg());
                    measureResult.setSpo2h(-100000);
                    measureResult.setSpo2hMsg(spo2HReport.getMsg());
                } else {
                    MyLog.e(Constants.TAG, "血氧=" + spo2HReport.getData());
                    measureResult.setSpo2h(spo2HReport.getData());
                }
            }
            if (bpReport != null) {
                MyLog.e(Constants.TAG, "血压= " + bpReport);
                if (bpReport.getCode() == 20100) {
                    //这里表示汇总结果血压不正常，直接取消汇总并弹窗
                    MyLog.e(Constants.TAG, "血压不正常 Code是：" + bpReport.getCode() + ",停止结果汇总，并弹窗：" + bpReport.getMsg());
                    new XPopup.Builder(SwitchActivity.this).dismissOnTouchOutside(false)
                            .asConfirm("测量失败", bpReport.getMsg(), "取消", "确定",
                                    new OnConfirmListener() {
                                        @Override
                                        public void onConfirm() {
                                            finish();
                                        }
                                    }, new OnCancelListener() {
                                        @Override
                                        public void onCancel() {

                                        }
                                    }, true).show();
                    return;
                } else {
                    MyLog.e(Constants.TAG, "收缩压=" + bpReport.getData().getBpSystolic() + " 舒张压=" + bpReport.getData().getBpDiastolic());
                    measureResult.setbP_SYSTOLIC((float) bpReport.getData().getBpSystolic());
                    measureResult.setbP_DIASTOLIC((float) bpReport.getData().getBpDiastolic());
                }
            }
            if (essentialReport != null) {
                MyLog.e(Constants.TAG, "年龄Bmi= " + essentialReport);
                if (essentialReport.getCode() == 60100) {
                    MyLog.e(Constants.TAG, "年龄BMI异常 Code是：" + essentialReport.getCode() + ",显示内容：" + essentialReport.getMsg());
                    measureResult.setAge(-100000);
                    measureResult.setBmi(-100000);
                    measureResult.setAgeBmiMsg(essentialReport.getMsg());
                } else {
                    MyLog.e(Constants.TAG, "年龄=" + essentialReport.getData().getAge() + " bmi=" + essentialReport.getData().getBmi());
                    if (essentialReport.getData().getAge() > 0) {
                        measureResult.setAge(essentialReport.getData().getAge());
                    }
                    if (essentialReport.getData().getBmi() > 0) {
                        measureResult.setBmi((float) essentialReport.getData().getBmi());
                    }
                }
            }
            if (riskReport != null) {
                MyLog.e(Constants.TAG, "风险预测=" + riskReport);
                if (riskReport.getCode() == 40100) {
                    MyLog.e(Constants.TAG, "风险模型异常 Code是：" + riskReport.getCode() + ",显示内容：" + riskReport.getMsg());
                    measureResult.setbP_HEART_ATTACK(-100000);
                    measureResult.setbP_STROKE(-100000);
                    measureResult.setbP_CVD(-100000);
                    measureResult.setbP_PP(-100000);
                    measureResult.setbP_TAU(-100000);
                    measureResult.setRiskMsg(riskReport.getMsg());
                } else {
                    MyLog.e(Constants.TAG, "风险预测=" + " 心脏病风险=" + riskReport.getData().getBpHeartAttack()
                            + " 中风风险=" + riskReport.getData().getBpStroke()
                            + " 心血管病风险=" + riskReport.getData().getBpCvd()
                            + " 心脏压力=" + riskReport.getData().getBpPp()
                            + " 血管弹性=" + riskReport.getData().getBpTau());
                    measureResult.setbP_HEART_ATTACK((float) riskReport.getData().getBpHeartAttack());
                    measureResult.setbP_STROKE((float) riskReport.getData().getBpStroke());
                    measureResult.setbP_CVD((float) riskReport.getData().getBpCvd());
                    measureResult.setbP_PP((float) riskReport.getData().getBpPp());
                    measureResult.setbP_TAU((float) riskReport.getData().getBpTau());
                }
            }
            if (healthScoreReport != null) {
                MyLog.e(Constants.TAG, "总分=" + healthScoreReport.getData());
                measureResult.setHealthScore(healthScoreReport.getData());
            }


            MyLog.d(Constants.TAG, "=================血氧仪测量结束====================");
            startActivity(new Intent(SwitchActivity.this, PhysiologicMeasureResultActivity.class)
                    .putExtra("measurementId", SwitchActivity.this.measurementId)
                    .putExtra("hr", measureResult.gethR_BPM())
                    .putExtra("hrv", measureResult.getHrv())
                    .putExtra("isAf", measureResult.getIsAf())
                    .putExtra("isAfMsg", measureResult.getAfMsg())
                    .putExtra("spo2h", measureResult.getSpo2h())
                    .putExtra("sys", measureResult.getbP_SYSTOLIC())
                    .putExtra("dia", measureResult.getbP_DIASTOLIC())
                    .putExtra("age", measureResult.getAge())
                    .putExtra("bmi", measureResult.getBmi())
                    .putExtra("heart_attack_risk", measureResult.getbP_HEART_ATTACK())
                    .putExtra("stroke_risk", measureResult.getbP_STROKE())
                    .putExtra("cardiovascular_risk", measureResult.getbP_CVD())
                    .putExtra("cardiac_workload", measureResult.getbP_PP())
                    .putExtra("healthScore", measureResult.getHealthScore())
                    .putExtra("vascular_capacity", measureResult.getbP_TAU())
                    .putExtra("msi", measureResult.getMsi())

                    .putExtra("gender", essentialReport.getData().getGenderValue())
            );
            SwitchActivity.this.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onException(IMeasurementException exception) {
        if (isIncomingData) {
            MyLog.e(Constants.TAG, "onException code:" + exception.getCode() + "  msg:" + exception.getMessage());
            stopMeasurement();
            isIncomingData = false;
//            camera2Helper.interrupt();
            if (cameraStrategy != null) {
                cameraStrategy.stop();
            }

            new XPopup.Builder(SwitchActivity.this).dismissOnTouchOutside(false)
                    .asConfirm("测量失败", exception.getMessage(), "取消", "确定",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    finish();
                                }
                            }, new OnCancelListener() {
                                @Override
                                public void onCancel() {

                                }
                            }, true).show();
        }
    }

    public void stopMeasurement() {
        scopeView.onMeasureStop2();
    }


    @Override
    protected void onResume() {
        super.onResume();
        cameraStrategy.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraStrategy != null) {
            cameraStrategy.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (cameraStrategy != null) {
            cameraStrategy.stop();
        }
        timer_3.cancel();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyLog.i(Constants.TAG, "按下了back键   onBackPressed()");
        if (cameraStrategy != null) {
            cameraStrategy.destroy();
            cameraStrategy = null;
        }
        if (faceRecognitionStrategy != null) {
            faceRecognitionStrategy.onDestroy();
            faceRecognitionStrategy = null;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyLog.i(Constants.TAG, "执行activity  的onDestroy()");
        if (cameraStrategy != null) {
            cameraStrategy.destroy();
            cameraStrategy = null;
        }
        if (faceRecognitionStrategy != null) {

            faceRecognitionStrategy.onDestroy();
            faceRecognitionStrategy = null;
        }
        videoFrameQueue.clear();
    }
}