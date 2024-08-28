package cn.xymind.healthdetection.synthesis.androidsdksamples.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.xiaoyang.measurement.abstraction.MeasurementReport;
import cn.xymind.healthdetection.synthesis.androidsdksamples.MeasureResult;
import cn.xymind.healthdetection.synthesis.androidsdksamples.R;
import cn.xymind.healthdetection.synthesis.androidsdksamples.app.DemoApp;
import cn.xymind.healthdetection.synthesis.androidsdksamples.views.XProgressDialog;
import cn.xymind.measurement_sdk_plugin.camera2.MeasurementCamera2;
import cn.xymind.measurement_sdk_plugin.core.FrameListener;
import cn.xymind.measurement_sdk_plugin.strategy.CameraStrategy;
import cn.xymind.measurement_sdk_plugin.view.AutoFitTextureView;
import cn.xymind.measurement_sdk_plugin.view.FaceAreaView;
import cn.xymind.measurementsdk.bean.ExceptionBean;
import cn.xymind.measurementsdk.listener.IMeasurementListener;
import cn.xymind.measurementsdk.strategy.Measurement;
import cn.xymind.measurementsdk.util.MyLog;

public class SwitchActivity extends AppCompatActivity implements IMeasurementListener, FrameListener {
    private static final String TAG = "SwitchActivity";
    private AutoFitTextureView texture_view;

    private FaceAreaView scopeView;

    private TextView tv_3s_count_down;

    private boolean isIncomingData = true;
    private boolean isRunning = false;

    private CountDownTimer timer_3;

    private XProgressDialog progressDialog;
    private Paint KeyPointsPaint = new Paint();

    private String measurementId;

    //camera2
    private CameraStrategy cameraStrategy;
    private Measurement measurement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);

        executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

        initView();
        initMeasurement();
    }


    private void initView() {
        texture_view = this.findViewById(R.id.texture_view);
        scopeView = this.findViewById(R.id.scopeView);


        tv_3s_count_down = this.findViewById(R.id.tv_3s_count_down);

        progressDialog = new XProgressDialog(SwitchActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);


        scopeView.setHeartBitmap(R.mipmap.icon_heart);
        scopeView.setImageCursorId(R.mipmap.icon_cursor);
        scopeView.setArrowResId(R.drawable.icon_jiantou);
    }

    private void initMeasurement() {

        cameraStrategy = new MeasurementCamera2(this, texture_view);
        cameraStrategy.setSize(640, 480);
        cameraStrategy.setFrameListener(this);

        measurement = new Measurement(DemoApp.config, DemoApp.featureExtractor);
        measurement.setMeasurementListener(this);


        KeyPointsPaint.setColor((Color.WHITE));
        KeyPointsPaint.setStyle(Paint.Style.FILL);
        KeyPointsPaint.setStrokeWidth(2);

        timer_3 = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_3s_count_down.setText(millisUntilFinished / 1000 + 1 + "");
            }

            @Override
            public void onFinish() {
                MyLog.d(TAG, "------------------------------------321倒计时结束，开始测量工作------------------------------------");
                if (isException) {
                    return;
                }

                //开始测量
                tv_3s_count_down.setVisibility(View.GONE);
                scopeView.setMessage("测量条件满足");

                scopeView.setStatus(FaceAreaView.STATUS_REGULAR_GREEN);
                scopeView.setRunningState(FaceAreaView.RUNNING_START);

                isIncomingData = true;
                measurement.start();
            }
        };

    }

    @Override
    public void onStarted(String measurementId) {
        MyLog.d(TAG, "InitMeasurement " + measurementId);
        this.measurementId = measurementId;
    }

    private int count = 0;

    @Override
    public void onFrameAvailable(byte[] frameData, long timestamp, int width, int height, int displayOrientation) {

        executor.execute(() -> {
            synchronized (this) {
                if (!isRunning) {
                    count++;
                    if (count % 12 != 0) {
                        return;
                    }
                    Map<String, ExceptionBean> warnings = measurement.validate(frameData, timestamp, width, height, displayOrientation);
                    if (warnings != null && !warnings.isEmpty()) {
                        for (Map.Entry<String, ExceptionBean> entry : warnings.entrySet()) {
                            ExceptionBean bean = entry.getValue();
                            MyLog.e(TAG, "测量前校验结果 Exception Key: " + entry.getKey() +
                                    ", Message: " + bean.getMsg() +
                                    ", Message CN: " + bean.getMsg_cn() +
                                    ", Level: " + bean.getLevel());
                        }

                        Map.Entry<String, ExceptionBean> bean = warnings.entrySet().iterator().next();
                        runOnUiThread(() -> {

                            scopeView.setMessage(bean.getValue().getMsg_cn());
                        });
                    } else {
                        MyLog.d(TAG, "------------------------------------开始测量工作-");
                        runOnUiThread(() -> {
                            scopeView.setMessage("测量条件满足");
                            isRunning = true;
                            if (!isException) {
                                tv_3s_count_down.setVisibility(View.VISIBLE);
                                timer_3.start();
                            }
                        });
                    }
                } else {

                    measurement.enqueue(frameData, timestamp, width, height, displayOrientation);
                }
            }
        });
    }

    private ThreadPoolExecutor executor;

    @Override
    public void onFrameProcessed(byte[] nv21, long timestamp, int width, int height) {
    }

    @Override
    public void onChunkReportGenerated(String measurementId, MeasurementReport.HrReport hrReport) {
        runOnUiThread(() -> {
            if (!hrReport.hasData()) {
                MyLog.e(TAG, "阶段性数据为空");
                return;
            }

            MeasurementReport.HrData hrData = hrReport.getData();
            double hrBpm = hrData.getHrBpm();
            MyLog.d(TAG, "心率：" + hrBpm + "   心率变异性：" + hrData.getHrv());
            BigDecimal hr = new BigDecimal(hrBpm);
            int value = hr.setScale(0, BigDecimal.ROUND_UP).intValue();

            scopeView.setHeartRate(value);
        });
    }

    @Override
    public void onCollected() {
        MyLog.d(TAG, "------------------------------------测量工作结束，等待汇总结果------------------------------------");
        //测量成功
        isIncomingData = false;
        runOnUiThread(() -> {
            progressDialog.setMessage("请稍候，我们正在努力为您计算中......");
            progressDialog.show();
            MyLog.d(TAG, "摄像头参数：" + cameraStrategy.getOption());
        });
    }

    @Override
    public void onInterrupted() {

    }

    @Override
    public void onWholeReportGenerated(String measurementId, MeasurementReport.Report measurementReport) {
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
            MyLog.d(TAG, "压力度报告code= " + calculatedReport.getCode());
            MyLog.d(TAG, "压力度报告data= " + calculatedReport.getData());
            MyLog.d(TAG, "压力度报告msg = " + calculatedReport.getMsg());
            //综合心健康风险报告
            MeasurementReport.SingleValueReport physiologyScoreReport = measurementReport.getPhysiologyScoreReport();
            MyLog.d(TAG, "综合心健康风险报告code= " + physiologyScoreReport.getCode());
            MyLog.d(TAG, "综合心健康风险报告data= " + physiologyScoreReport.getData());
            MyLog.d(TAG, "综合心健康风险报告msg = " + physiologyScoreReport.getMsg());
            //血压报告

            //攻击性报告
            MeasurementReport.SingleValueReport aggressivityReport = measurementReport.getAggressivityReport();
            MyLog.d(TAG, "攻击性报告code= " + aggressivityReport.getCode());
            MyLog.d(TAG, "攻击性报告data= " + aggressivityReport.getData());
            MyLog.d(TAG, "攻击性报告msg = " + aggressivityReport.getMsg());

            //焦虑度报告
            MeasurementReport.SingleValueReport anxietyReport = measurementReport.getAnxietyReport();
            MyLog.d(TAG, "焦虑度报告code= " + anxietyReport.getCode());
            MyLog.d(TAG, "焦虑度报告data= " + anxietyReport.getData());
            MyLog.d(TAG, "焦虑度报告msg = " + anxietyReport.getMsg());

            //活力度报告
            MeasurementReport.SingleValueReport vitalityReport = measurementReport.getVitalityReport();
            MyLog.d(TAG, "活力度报告code= " + vitalityReport.getCode());
            MyLog.d(TAG, "活力度报告data= " + vitalityReport.getData());
            MyLog.d(TAG, "活力度报告msg = " + vitalityReport.getMsg());

            //抑郁度报告
            MeasurementReport.SingleValueReport suppressionReport = measurementReport.getSuppressionReport();
            MyLog.d(TAG, "抑郁度报告code= " + suppressionReport.getCode());
            MyLog.d(TAG, "抑郁度报告data= " + suppressionReport.getData());
            MyLog.d(TAG, "抑郁度报告msg = " + suppressionReport.getMsg());

            //疲劳度报告
            MeasurementReport.SingleValueReport fatigueReport = measurementReport.getFatigueReport();
            MyLog.d(TAG, "疲劳度报告code= " + fatigueReport.getCode());
            MyLog.d(TAG, "疲劳度报告data= " + fatigueReport.getData());
            MyLog.d(TAG, "疲劳度报告msg = " + fatigueReport.getMsg());

            //情绪 report
            MeasurementReport.SingleValueReport emotionReport = measurementReport.getEmotionScoreReport();
            MyLog.d(TAG, "情绪data= " + emotionReport.getData());
            MyLog.d(TAG, "情绪msg = " + emotionReport.getMsg());
            MyLog.d(TAG, "情绪code= " + emotionReport.getCode());

            MeasureResult measureResult = new MeasureResult();
            measureResult.setMeasurementId(measurementId);
            if (hrReport != null) {
                MyLog.e(TAG, "心率= " + hrReport);
                if (hrReport.getCode() == 10100) {
                    //这里表示汇总结果心率不正常，直接取消汇总并弹窗
                    MyLog.e(TAG, "心率不正常 Code是：" + hrReport.getCode() + ",停止结果汇总，并弹窗：" + hrReport.getMsg());
                    new XPopup.Builder(SwitchActivity.this).dismissOnTouchOutside(false)
                            .asConfirm("测量失败", hrReport.getMsg(), "取消", "确定",
                                    this::finish, () -> {

                                    }, true).show();
                    return;
                } else {
                    MeasurementReport.HrData hrData = hrReport.getData();
                    MyLog.e(TAG, "心率=" + hrData.getHrBpm() + " 心率变异率=" + hrData.getHrv());
                    measureResult.sethR_BPM((float) hrData.getHrBpm());
                    measureResult.setHrv((float) hrData.getHrv());
                }
            }
            if (afReport != null) {
                MyLog.e(TAG, "房颤= " + afReport);
                if (afReport.getCode() == 10200) {
                    //房颤异常
                    MyLog.e(TAG, "房颤异常 Code是：" + afReport.getCode() + ",显示内容：" + afReport.getMsg());
                    measureResult.setIsAf(-100000);
                    measureResult.setAfMsg(afReport.getMsg());
                } else {
                    MyLog.e(TAG, "房颤=" + afReport.getData());
                    MyLog.e(TAG, "房颤值：" + afReport.getData());
                    measureResult.setIsAf((int) afReport.getData());
                }
            }
            if (spo2HReport != null) {
                MyLog.e(TAG, "血氧= " + spo2HReport);
                if (spo2HReport.getCode() == 30100) {
                    //血氧异常
                    MyLog.e(TAG, "血氧异常 Code是：" + spo2HReport.getCode() + ",显示内容：" + spo2HReport.getMsg());
                    measureResult.setSpo2h(-100000);
                    measureResult.setSpo2hMsg(spo2HReport.getMsg());
                } else {
                    MyLog.e(TAG, "血氧=" + spo2HReport.getData());
                    measureResult.setSpo2h(spo2HReport.getData());
                }
            }
            if (bpReport != null) {
                MyLog.e(TAG, "血压= " + bpReport);
                if (bpReport.getCode() == 20100) {
                    //这里表示汇总结果血压不正常，直接取消汇总并弹窗
                    MyLog.e(TAG, "血压不正常 Code是：" + bpReport.getCode() + ",停止结果汇总，并弹窗：" + bpReport.getMsg());
                    new XPopup.Builder(SwitchActivity.this).dismissOnTouchOutside(false)
                            .asConfirm("测量失败", bpReport.getMsg(), "取消", "确定",
                                    this::finish, () -> {

                                    }, true).show();
                    return;
                } else {
                    MyLog.e(TAG, "收缩压=" + bpReport.getData().getBpSystolic() + " 舒张压=" + bpReport.getData().getBpDiastolic());
                    measureResult.setbP_SYSTOLIC((float) bpReport.getData().getBpSystolic());
                    measureResult.setbP_DIASTOLIC((float) bpReport.getData().getBpDiastolic());
                }
            }
            if (essentialReport != null) {
                MyLog.e(TAG, "年龄Bmi= " + essentialReport);
                if (essentialReport.getCode() == 60100) {
                    MyLog.e(TAG, "年龄BMI异常 Code是：" + essentialReport.getCode() + ",显示内容：" + essentialReport.getMsg());
                    measureResult.setAge(-100000);
                    measureResult.setBmi(-100000);
                    measureResult.setAgeBmiMsg(essentialReport.getMsg());
                } else {
                    MyLog.e(TAG, "年龄=" + essentialReport.getData().getAge() + " bmi=" + essentialReport.getData().getBmi());
                    if (essentialReport.getData().getAge() > 0) {
                        measureResult.setAge(essentialReport.getData().getAge());
                    }
                    if (essentialReport.getData().getBmi() > 0) {
                        measureResult.setBmi((float) essentialReport.getData().getBmi());
                    }
                }
            }
            if (riskReport != null) {
                MyLog.e(TAG, "风险预测=" + riskReport);
                if (riskReport.getCode() == 40100) {
                    MyLog.e(TAG, "风险模型异常 Code是：" + riskReport.getCode() + ",显示内容：" + riskReport.getMsg());
                    measureResult.setbP_HEART_ATTACK(-100000);
                    measureResult.setbP_STROKE(-100000);
                    measureResult.setbP_CVD(-100000);
                    measureResult.setbP_PP(-100000);
                    measureResult.setbP_TAU(-100000);
                    measureResult.setRiskMsg(riskReport.getMsg());
                } else {
                    MyLog.e(TAG, "风险预测=" + " 心脏病风险=" + riskReport.getData().getBpHeartAttack()
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
                MyLog.e(TAG, "计算报告=" + calculatedReport);
                measureResult.setMsi(calculatedReport.getData());
                if (calculatedReport.getCode() == 80000) {
                    MyLog.d(TAG, "计算报告所有指标成功");
                } else if (calculatedReport.getCode() == 80010) {
                    MyLog.d(TAG, "计算报告分数计算失败");
                } else if (calculatedReport.getCode() == 80020) {
                    MyLog.d(TAG, "计算报告心理压力计算失败");
                } else if (calculatedReport.getCode() == 81000) {
                    MyLog.d(TAG, "计算报告全部指标都计算失败");
                }
                MyLog.e(TAG, "总分=" + healthScoreReport.getData());
                measureResult.setHealthScore(healthScoreReport.getData());
            }

            MyLog.d(TAG, "=================血氧仪测量结束====================");
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
    public void onStateUpdated(Map<String, ExceptionBean> warnings) {

        runOnUiThread(() -> {
            if (warnings.isEmpty()) {
                scopeView.setMessage("测量条件满足");
                scopeView.setStatus(FaceAreaView.STATUS_REGULAR_GREEN);
                return;
            }

            for (Map.Entry<String, ExceptionBean> entry : warnings.entrySet()) {
                ExceptionBean bean = entry.getValue();
                MyLog.d(TAG, "Exception Key: " + entry.getKey() +
                        ", Message: " + bean.getMsg() +
                        ", Message CN: " + bean.getMsg_cn() +
                        ", Level: " + bean.getLevel());
            }
            Map.Entry<String, ExceptionBean> bean = warnings.entrySet().iterator().next();

            scopeView.setMessage(bean.getValue().getMsg_cn());
        });
    }

    private boolean isException = false;

    @Override
    public void onCrashed(ExceptionBean exception) {
        MyLog.e(TAG, "人脸识别异常333：" + exception.getMsg_cn());

        Log.e(TAG, "onException: " + exception.getMsg_cn() + " level: " + exception.getLevel());
        runOnUiThread(() -> {
            progressDialog.dismiss();
            scopeView.setMessage(exception.getMsg_cn());
        });
        if (exception.getLevel().equals("warning")) {
            return;
        }
        runOnUiThread(() -> scopeView.setRunningState(FaceAreaView.RUNNING_STOP));
        isException = true;
        MyLog.e(TAG, "  msg:" + exception.getMsg_cn());
        isIncomingData = false;
        if (cameraStrategy != null) {
            cameraStrategy.stop();
        }

        new XPopup.Builder(SwitchActivity.this).dismissOnTouchOutside(false)
                .asConfirm("测量失败", exception.getMsg_cn(), "取消", "确定",
                        this::finish, () -> {

                        }, true).show();

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
        if (scopeView != null) {
            scopeView.stopAnimation();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (cameraStrategy != null) {
            cameraStrategy.stop();
        }
        timer_3.cancel();
        executor.shutdownNow();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyLog.i(TAG, "按下了back键   onBackPressed()");
        if (cameraStrategy != null) {
            cameraStrategy.destroy();
            cameraStrategy = null;
        }
        if (measurement != null) {
            measurement.interrupt();
            measurement = null;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyLog.i(TAG, "执行activity  的onDestroy()");
        if (cameraStrategy != null) {
            cameraStrategy.destroy();
            cameraStrategy = null;
        }

        if (measurement != null) {
            measurement.interrupt();
            measurement = null;
        }
    }
}