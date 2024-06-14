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
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

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
import cn.xymind.measurement_sdk_plugin.strategy.CameraStrategy;
import cn.xymind.measurement_sdk_plugin.view.AutoFitTextureView;
import cn.xymind.measurementsdk.bean.ExceptionBean;
import cn.xymind.measurementsdk.enums.CameraType;
import cn.xymind.measurementsdk.enums.RecognitionType;
import cn.xymind.measurementsdk.listener.FrameListener;
import cn.xymind.measurementsdk.listener.IMeasurementListener;
import cn.xymind.measurementsdk.strategy.FaceRecognitionStrategy;
import cn.xymind.measurementsdk.strategy.MNNFaceRecognitionStrategy;
import cn.xymind.measurementsdk.strategy.MediapipeFaceRecognitionStrategy;
import cn.xymind.measurementsdk.util.ImageUtil;
import cn.xymind.measurementsdk.util.MyLog;
import cn.xymind.measurementsdk.util.Constants;

public class SwitchActivity extends AppCompatActivity implements IMeasurementListener, FrameListener {

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

    //camera2\

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
        cameraStrategy.setFrameListener(this);
        switch (recognitionType) {
            case MNN:
                faceRecognitionStrategy = new MNNFaceRecognitionStrategy(DemoApp.config);
                break;
            case MEDIAPIPE:
                faceRecognitionStrategy = new MediapipeFaceRecognitionStrategy(DemoApp.config, DemoApp.faceLandmarkerHelper);
                break;
        }

        faceRecognitionStrategy.setMeasurementListener(this);

        //初始化测量工具
        //必须线创建测量用户对象
//        MeasurementRequiredData requiredData = new MeasurementRequiredData();
//        requiredData.setWeight(80);
//        requiredData.setHeight(170);
//        requiredData.setAge(30);
//        requiredData.setGender(1);


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

//    @Override
//    public void onFaceLandkarkers(float[] reports, int width, int height) {
//        face_landmarks.setResult(reports, height, width);
//    }

    @Override
    public void onCreated(String measurementId) {
        MyLog.d(Constants.TAG, "InitMeasurement " + measurementId);
        this.measurementId = measurementId;
    }

//    @Override
//    public void onFaceDetected(FaceDetectionReport[] reports, int width, int height) {
//
//    }


    @Override
    public void onFrameAvailable(byte[] frameData, long timestamp, int width, int height, int displayOrientation) {
//        byte[] bytes3 = ImageUtil.nv21ToBitmapBytes(frameData, width, height);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes3, 0, bytes3.length);
//
//
//        runOnUiThread(() -> testIv.setImageBitmap(bitmap));
        faceRecognitionStrategy.processFrame(frameData, timestamp, width, height, displayOrientation);
    }

    @Override
    public void onFrameProcessed(byte[] nv21, long timestamp, int width, int height) {
        byte[] bytes3 = ImageUtil.nv21ToBitmapBytes(nv21, width, height);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes3, 0, bytes3.length);


        runOnUiThread(() -> testIv.setImageBitmap(bitmap));

        //保存视频帧
        //width 视频宽
        //height 视频高
        //nv21 视频数据
//        VideoRecorderUtil.getInstance().setWH(width, height, nv21);
//        try {
//            if (nv21 == null){
//                MyLog.e(StringConstans.TAG,"nv21 is null");
//            }
//            if(videoFrameQueue == null){
//
//                MyLog.e(StringConstans.TAG,"videoFrameQueue is null");
//            }
//
//            if(videoFrameQueue.size() > 1000){
//
//                MyLog.e(StringConstans.TAG,"videoFrameQueue size is " + videoFrameQueue.size());
//            }
//            videoWidth = width;
//            videoHeight = height;
//            videoFrameQueue.put(nv21);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public void chunkReportGenerated(String measurementId, MeasurementReport.HrReport hrReport) {
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

//    @Override
//    public void onDataCollected() {
//
//    }

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
    public void wholeReportGenerated(String measurementId, MeasurementReport.Report measurementReport) {
        try {
            runOnUiThread(() -> progressDialog.dismiss());

            MeasurementReport.HrReport hrReport = measurementReport.getHrReport();
            hrReport.getExplanation().getHrv().getAdvicesList();//建议集合
            MeasurementReport.SingleValueReport afReport = measurementReport.getAfReport();
            MeasurementReport.SingleValueReport spo2HReport = measurementReport.getSpo2HReport();
            MeasurementReport.BpReport bpReport = measurementReport.getBpReport();
            MeasurementReport.EssentialReport essentialReport = measurementReport.getEssentialReport();
            MeasurementReport.RiskReport riskReport = measurementReport.getRiskReport();
            MeasurementReport.SingleValueReport healthScoreReport = measurementReport.getPhysiologyScoreReport();

            //压力度报告
            MeasurementReport.SingleValueReport calculatedReport = measurementReport.getMsiReport();
            MyLog.d(Constants.TAG, "压力度报告code= " + calculatedReport.getCode());
            MyLog.d(Constants.TAG, "压力度报告data= " + calculatedReport.getData());
            MyLog.d(Constants.TAG, "压力度报告msg = " + calculatedReport.getMsg());
            //综合心健康风险报告
            MeasurementReport.SingleValueReport physiologyScoreReport = measurementReport.getPhysiologyScoreReport();
            MyLog.d(Constants.TAG, "综合心健康风险报告code= " + physiologyScoreReport.getCode());
            MyLog.d(Constants.TAG, "综合心健康风险报告data= " + physiologyScoreReport.getData());
            MyLog.d(Constants.TAG, "综合心健康风险报告msg = " + physiologyScoreReport.getMsg());
            //血压报告

            //攻击性报告
            MeasurementReport.SingleValueReport aggressivityReport = measurementReport.getAggressivityReport();
            MyLog.d(Constants.TAG, "攻击性报告code= " + aggressivityReport.getCode());
            MyLog.d(Constants.TAG, "攻击性报告data= " + aggressivityReport.getData());
            MyLog.d(Constants.TAG, "攻击性报告msg = " + aggressivityReport.getMsg());

            //焦虑度报告
            MeasurementReport.SingleValueReport anxietyReport = measurementReport.getAnxietyReport();
            MyLog.d(Constants.TAG, "焦虑度报告code= " + anxietyReport.getCode());
            MyLog.d(Constants.TAG, "焦虑度报告data= " + anxietyReport.getData());
            MyLog.d(Constants.TAG, "焦虑度报告msg = " + anxietyReport.getMsg());

            //活力度报告
            MeasurementReport.SingleValueReport vitalityReport = measurementReport.getVitalityReport();
            MyLog.d(Constants.TAG, "活力度报告code= " + vitalityReport.getCode());
            MyLog.d(Constants.TAG, "活力度报告data= " + vitalityReport.getData());
            MyLog.d(Constants.TAG, "活力度报告msg = " + vitalityReport.getMsg());

            //抑郁度报告
            MeasurementReport.SingleValueReport suppressionReport = measurementReport.getSuppressionReport();
            MyLog.d(Constants.TAG, "抑郁度报告code= " + suppressionReport.getCode());
            MyLog.d(Constants.TAG, "抑郁度报告data= " + suppressionReport.getData());
            MyLog.d(Constants.TAG, "抑郁度报告msg = " + suppressionReport.getMsg());

            //疲劳度报告
            MeasurementReport.SingleValueReport fatigueReport = measurementReport.getFatigueReport();
            MyLog.d(Constants.TAG, "疲劳度报告code= " + fatigueReport.getCode());
            MyLog.d(Constants.TAG, "疲劳度报告data= " + fatigueReport.getData());
            MyLog.d(Constants.TAG, "疲劳度报告msg = " + fatigueReport.getMsg());

            //情绪 report
            MeasurementReport.SingleValueReport emotionReport = measurementReport.getEmotionScoreReport();
            MyLog.d(Constants.TAG, "情绪data= " + emotionReport.getData());
            MyLog.d(Constants.TAG, "情绪msg = " + emotionReport.getMsg());
            MyLog.d(Constants.TAG, "情绪code= " + emotionReport.getCode());

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
                    measureResult.setIsAf((int) afReport.getData());
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
            if (calculatedReport != null) {
                MyLog.e(Constants.TAG, "计算报告=" + calculatedReport);
                measureResult.setMsi(calculatedReport.getData());
                if (calculatedReport.getCode() == 80000) {
                    MyLog.d(Constants.TAG, "计算报告所有指标成功");
                } else if (calculatedReport.getCode() == 80010) {
                    MyLog.d(Constants.TAG, "计算报告分数计算失败");
                } else if (calculatedReport.getCode() == 80020) {
                    MyLog.d(Constants.TAG, "计算报告心理压力计算失败");
                } else if (calculatedReport.getCode() == 81000) {
                    MyLog.d(Constants.TAG, "计算报告全部指标都计算失败");
                }
                MyLog.e(Constants.TAG, "总分=" + healthScoreReport.getData());
                measureResult.setHealthScore(healthScoreReport.getData());
            }
//            if (healthScoreReport != null) {
//                MyLog.e(StringConstans.TAG, "总分=" + healthScoreReport.getData());
//                measureResult.setHealthScore(healthScoreReport.getData());
//            }


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
                    .putExtra("emotion", emotionReport.getData())
                    .putExtra("physiologyScoreReport", physiologyScoreReport.getData())
                    .putExtra("aggressivityReport", aggressivityReport.getData())
                    .putExtra("anxietyReport", anxietyReport.getData())
                    .putExtra("vitalityReport", vitalityReport.getData())
                    .putExtra("suppressionReport", suppressionReport.getData())
                    .putExtra("fatigueReport", fatigueReport.getData())
                    .putExtra("gender", essentialReport.getData().getGenderValue())
            );
            SwitchActivity.this.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onException(ExceptionBean exception) {
        Log.e(Constants.TAG, "onException: " + exception.getMsg_cn() + " level: " + exception.getLevel());
        if (exception.getLevel().equals( "warning")){
            runOnUiThread(() -> tv_bottom_note.setText(exception.getMsg_cn()));
            return;
        }
        if (isIncomingData) {
            MyLog.e(Constants.TAG, "  msg:" + exception.getMsg_cn());
            stopMeasurement();
            isIncomingData = false;
//            camera2Helper.interrupt();
            if (cameraStrategy != null) {
                cameraStrategy.stop();
            }

            new XPopup.Builder(SwitchActivity.this).dismissOnTouchOutside(false)
                    .asConfirm("测量失败", exception.getMsg_cn(), "取消", "确定",
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