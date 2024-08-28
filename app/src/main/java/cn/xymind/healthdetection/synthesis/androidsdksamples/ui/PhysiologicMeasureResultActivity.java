package cn.xymind.healthdetection.synthesis.androidsdksamples.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ColorUtils;

import cn.xymind.healthdetection.synthesis.androidsdksamples.R;
import cn.xymind.healthdetection.synthesis.androidsdksamples.utils.ValueUtil;
import cn.xymind.healthdetection.synthesis.androidsdksamples.views.IndicatorMirrorView;
import cn.xymind.healthdetection.synthesis.androidsdksamples.views.ItemScoreView;
import cn.xymind.healthdetection.synthesis.androidsdksamples.views.SegmentScoreMirrorView;


/**
 * (正常测量结果页面)
 */

public class PhysiologicMeasureResultActivity extends AppCompatActivity {

    private static final int color_red = ColorUtils.getColor(R.color.color_red);// 红
    private static final int color_yellow = ColorUtils.getColor(R.color.color_yellow);//黄
    private static final int color_blue = ColorUtils.getColor(R.color.color_blue);//蓝
    private static final int color_green = ColorUtils.getColor(R.color.color_green);//绿
    private static final int color_orange = ColorUtils.getColor(R.color.color_green);// 橙

    private ItemScoreView healthScoreView;

    private SegmentScoreMirrorView xlSsv, xzylSsv, xggnSsv, pfnlSsv, xxgbfxSsv, tzzsSsv, xzbfxSsv, jsylSsv, ssySsv, szySsv, zffxSsv, spo2Ssmv, calculated;

    private ImageView isAfIv;

    private TextView atrialFibrillationTv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physiologic_measure_result);
        initView();
        initData();
    }

    private void initView() {
        healthScoreView = findViewById(R.id.v_standard_score);
        xlSsv = findViewById(R.id.v_xl);
        xzylSsv = findViewById(R.id.v_xzyl);
        xggnSsv = findViewById(R.id.v_xggn);
        pfnlSsv = findViewById(R.id.v_pfnl);
        xxgbfxSsv = findViewById(R.id.v_xxgbfx);
        tzzsSsv = findViewById(R.id.v_tzzs);
        xzbfxSsv = findViewById(R.id.v_xzbfx);
        jsylSsv = findViewById(R.id.v_jsyl);
        ssySsv = findViewById(R.id.v_ssy);
        szySsv = findViewById(R.id.v_szy);
        zffxSsv = findViewById(R.id.v_zffx);
        spo2Ssmv = findViewById(R.id.spo2Ssmv);
        calculated = findViewById(R.id.calculated);
        isAfIv = findViewById(R.id.isAfIv);
        atrialFibrillationTv = findViewById(R.id.atrialFibrillationTv);

    }

    private void initData() {

        double hr = getIntent().getFloatExtra("hr", 0);
        double hrv = getIntent().getFloatExtra("hrv", 0);
        int isAF = getIntent().getIntExtra("isAf", 0);
        String isAfMsg = getIntent().getStringExtra("isAfMsg");
        double spo2h = getIntent().getDoubleExtra("spo2h", 0);
        double sys = getIntent().getFloatExtra("sys", 0);
        double dia = getIntent().getFloatExtra("dia", 0);
        int age = getIntent().getIntExtra("age", 0);
        double bmi = getIntent().getFloatExtra("bmi", 0);
        double heart_attack_risk = getIntent().getFloatExtra("heart_attack_risk", 0);
        double stroke_risk = getIntent().getFloatExtra("stroke_risk", 0);
        double cardiovascular_risk = getIntent().getFloatExtra("cardiovascular_risk", 0);
        double cardiac_workload = getIntent().getFloatExtra("cardiac_workload", 0);
        double vascular_capacity = getIntent().getFloatExtra("vascular_capacity", 0);
        double healthScore = getIntent().getDoubleExtra("healthScore", 0);
        double msi = getIntent().getDoubleExtra("msi", 0);

        //房颤
        isAfIv.setImageResource(R.mipmap.icon_xinlv);

        if (isAF == -100000) {
            atrialFibrillationTv.setText(isAfMsg);
            atrialFibrillationTv.setTextColor(getResources().getColor(R.color.color_red));
        } else {
            if (isAF == 1) {
                atrialFibrillationTv.setText("发作");
                atrialFibrillationTv.setTextColor(getResources().getColor(R.color.color_red));
            } else {
                atrialFibrillationTv.setText("未发作");
                atrialFibrillationTv.setTextColor(getResources().getColor(R.color.color_green));
            }
        }

        calculated.setTvTitle("压力度");
        calculated.setTvValue(ValueUtil.doubleToString(msi));
        calculated.setIvIcon(R.mipmap.icon_xinzfh);
        calculated.getIndicatorView().setCurrentValue((float) msi);
        calculated.getIndicatorView().setMinFractionLen(1);
        calculated.getIndicatorView().setMinValue(1f);
        calculated.getIndicatorView().setMaxValue(5.9f);
        calculated.getIndicatorView().setDrawEndValue(true);
        calculated.getIndicatorView().setDrawStartValue(true);
        calculated.getIndicatorView().setPhases(
                new IndicatorMirrorView.Phase[]{
                        new IndicatorMirrorView.Phase(1.0f, 2.0f, getResources().getColor(R.color.color_green)),
                        new IndicatorMirrorView.Phase(2.0f, 3.0f, getResources().getColor(R.color.color_blue)),
                        new IndicatorMirrorView.Phase(3.0f, 4.0f, getResources().getColor(R.color.color_yellow)),
                        new IndicatorMirrorView.Phase(4.0f, 5.0f, getResources().getColor(R.color.color_orange)),
                        new IndicatorMirrorView.Phase(5.0f, 5.9f, getResources().getColor(R.color.color_red))}
        );

        spo2Ssmv.setTvTitle("血氧含量\n");
        spo2Ssmv.setTvValue((int) spo2h + "%");
        spo2Ssmv.getIndicatorView().setCurrentValue((float) spo2h);
        spo2Ssmv.getIndicatorView().setMinValue(60f);
        spo2Ssmv.getIndicatorView().setMaxValue(100f);
        spo2Ssmv.getIndicatorView().setDrawEndValue(true);
        spo2Ssmv.getIndicatorView().setDrawStartValue(true);
        spo2Ssmv.getIndicatorView().setPhases(new IndicatorMirrorView.Phase[]{
                new IndicatorMirrorView.Phase(60f, 93f, getResources().getColor(R.color.color_yellow)),
                new IndicatorMirrorView.Phase(93f, 100f, getResources().getColor(R.color.color_green))});
        spo2Ssmv.setIvIcon(R.mipmap.icon_spo2);

        xlSsv.setTvTitle("心率\n");
        xlSsv.setTvValue(Math.round(hr) + "");
        xlSsv.getIndicatorView().setCurrentValue((float) hr);
        xlSsv.getIndicatorView().setMinValue(40f);
        xlSsv.getIndicatorView().setMaxValue(140f);
        xlSsv.getIndicatorView().setDrawEndValue(true);
        xlSsv.getIndicatorView().setDrawStartValue(true);
        xlSsv.getIndicatorView().setPhases(new IndicatorMirrorView.Phase[]{
                new IndicatorMirrorView.Phase(0f, 60f, getResources().getColor(R.color.color_yellow)),
                new IndicatorMirrorView.Phase(60f, 100f, getResources().getColor(R.color.color_green)),
                new IndicatorMirrorView.Phase(100f, 140f, getResources().getColor(R.color.color_yellow))});
        xlSsv.setIvIcon(R.mipmap.icon_xinlv);

        //心脏压力
        xzylSsv.setTvTitle("心脏压力");
        xzylSsv.setTvValue(ValueUtil.doubleToString(cardiac_workload));
        xzylSsv.getIndicatorView().setCurrentValue((float) cardiac_workload);
        xzylSsv.getIndicatorView().setMinFractionLen(1);
        xzylSsv.getIndicatorView().setMinValue(3.5f);
        xzylSsv.getIndicatorView().setMaxValue(4.5f);
        xzylSsv.getIndicatorView().setDrawEndValue(true);
        xzylSsv.getIndicatorView().setDrawStartValue(true);
        xzylSsv.getIndicatorView().setPhases(new IndicatorMirrorView.Phase[]{
                new IndicatorMirrorView.Phase(3.5f, 3.8f, getResources().getColor(R.color.color_green)),
                new IndicatorMirrorView.Phase(3.8f, 3.93f, getResources().getColor(R.color.color_blue)),
                new IndicatorMirrorView.Phase(3.93f, 4.06f, getResources().getColor(R.color.color_yellow)),
                new IndicatorMirrorView.Phase(4.06f, 4.2f, getResources().getColor(R.color.color_orange)),
                new IndicatorMirrorView.Phase(4.2f, 4.5f, getResources().getColor(R.color.color_red))});
        xzylSsv.setIvIcon(R.mipmap.icon_xinlyl);

        xggnSsv.setTvTitle("血管功能");
        xggnSsv.setTvValue(ValueUtil.doubleToString(vascular_capacity));
        xggnSsv.getIndicatorView().setCurrentValue((float) vascular_capacity);
        xggnSsv.getIndicatorView().setMinFractionLen(1);
        xggnSsv.getIndicatorView().setMinValue(0f);
        xggnSsv.getIndicatorView().setMaxValue(3f);
        xggnSsv.getIndicatorView().setDrawEndValue(true);
        xggnSsv.getIndicatorView().setDrawStartValue(true);
        xggnSsv.getIndicatorView().setPhases(
                new IndicatorMirrorView.Phase[]{
                        new IndicatorMirrorView.Phase(0f, 0.8f, getResources().getColor(R.color.color_red)),
                        new IndicatorMirrorView.Phase(0.8f, 1.2f, getResources().getColor(R.color.color_orange)),
                        new IndicatorMirrorView.Phase(1.2f, 1.6f, getResources().getColor(R.color.color_yellow)),
                        new IndicatorMirrorView.Phase(1.6f, 2.2f, getResources().getColor(R.color.color_blue)),
                        new IndicatorMirrorView.Phase(2.2f, 3f, getResources().getColor(R.color.color_green))}
        );
        xggnSsv.setIvIcon(R.mipmap.icon_xueggn);

        pfnlSsv.setTvTitle("皮肤年龄");
        pfnlSsv.setTvValue(ValueUtil.doubleToString(age));
        pfnlSsv.setIvIcon(R.mipmap.icon_age);
        pfnlSsv.getIndicatorView().setCurrentValue(age);
        pfnlSsv.getIndicatorView().setMinValue(1f);
        pfnlSsv.getIndicatorView().setMaxValue(150f);
        pfnlSsv.getIndicatorView().setDrawEndValue(true);
        pfnlSsv.getIndicatorView().setDrawStartValue(true);
        pfnlSsv.getIndicatorView().setPhases(
                new IndicatorMirrorView.Phase[]{
                        new IndicatorMirrorView.Phase(1f, 150f, getResources().getColor(R.color.color_blue)),
                }
        );

        xxgbfxSsv.setTvTitle("心血管病风险");
        xxgbfxSsv.setTvValue(ValueUtil.doubleToString((cardiovascular_risk * 100)) + "%");
        xxgbfxSsv.setIvIcon(R.mipmap.icon_xinxgb);
        xxgbfxSsv.getIndicatorView().setCurrentValue((float) cardiovascular_risk);
        xxgbfxSsv.getIndicatorView().setMinValue(0f);
        xxgbfxSsv.getIndicatorView().setMaxValue(15f);
        xxgbfxSsv.getIndicatorView().setDrawEndValue(true);
        xxgbfxSsv.getIndicatorView().setDrawStartValue(true);
        xxgbfxSsv.getIndicatorView().setPhases(
                new IndicatorMirrorView.Phase[]{
                        new IndicatorMirrorView.Phase(0.0f, 3.0f, getResources().getColor(R.color.color_green)),
                        new IndicatorMirrorView.Phase(3.0f, 6.0f, getResources().getColor(R.color.color_blue)),
                        new IndicatorMirrorView.Phase(6.0f, 9.0f, getResources().getColor(R.color.color_yellow)),
                        new IndicatorMirrorView.Phase(9.0f, 12.0f, getResources().getColor(R.color.color_orange)),
                        new IndicatorMirrorView.Phase(12.0f, 15.0f, getResources().getColor(R.color.color_red))}
        );

        tzzsSsv.setTvTitle("体重指数");
        tzzsSsv.setTvValue(ValueUtil.doubleToString(bmi));
        tzzsSsv.setIvIcon(R.mipmap.icon_tizz);
        tzzsSsv.getIndicatorView().setCurrentValue((float) bmi);
        tzzsSsv.getIndicatorView().setMinValue(12f);
        tzzsSsv.getIndicatorView().setMaxValue(45f);
        tzzsSsv.getIndicatorView().setDrawEndValue(true);
        tzzsSsv.getIndicatorView().setDrawStartValue(true);
        tzzsSsv.getIndicatorView().setPhases(
                new IndicatorMirrorView.Phase[]{
                        new IndicatorMirrorView.Phase(12f, 18.5f, getResources().getColor(R.color.color_yellow)),
                        new IndicatorMirrorView.Phase(18.5f, 25f, getResources().getColor(R.color.color_green)),
                        new IndicatorMirrorView.Phase(25f, 30f, getResources().getColor(R.color.color_yellow)),
                        new IndicatorMirrorView.Phase(30f, 35f, getResources().getColor(R.color.color_orange)),
                        new IndicatorMirrorView.Phase(35f, 45f, getResources().getColor(R.color.color_red))}
        );

        xzbfxSsv.setTvTitle("心脏病风险");
        xzbfxSsv.setTvValue(ValueUtil.doubleToString((heart_attack_risk * 100)) + "%");
        xzbfxSsv.setIvIcon(R.mipmap.icon_xinzbfx);
        xzbfxSsv.getIndicatorView().setCurrentValue((float) heart_attack_risk);
        xzbfxSsv.getIndicatorView().setMinFractionLen(1);
        xzbfxSsv.getIndicatorView().setMinValue(0f);
        xzbfxSsv.getIndicatorView().setMaxValue(7.5f);
        xzbfxSsv.getIndicatorView().setDrawEndValue(true);
        xzbfxSsv.getIndicatorView().setDrawStartValue(true);
        xzbfxSsv.getIndicatorView().setPhases(
                new IndicatorMirrorView.Phase[]{
                        new IndicatorMirrorView.Phase(0f, 1.5f, getResources().getColor(R.color.color_green)),
                        new IndicatorMirrorView.Phase(1.5f, 3f, getResources().getColor(R.color.color_blue)),
                        new IndicatorMirrorView.Phase(3f, 4.5f, getResources().getColor(R.color.color_yellow)),
                        new IndicatorMirrorView.Phase(4.5f, 6f, getResources().getColor(R.color.color_orange)),
                        new IndicatorMirrorView.Phase(6f, 7.5f, getResources().getColor(R.color.color_red))}
        );

        jsylSsv.setTvTitle("心率变异性");
        jsylSsv.setTvValue(ValueUtil.doubleToString(hrv));
        jsylSsv.setIvIcon(R.mipmap.icon_xinzfh);
        jsylSsv.getIndicatorView().setCurrentValue((float) hrv);
        jsylSsv.getIndicatorView().setMinFractionLen(1);
        jsylSsv.getIndicatorView().setMinValue(0f);
        jsylSsv.getIndicatorView().setMaxValue(300f);
        jsylSsv.getIndicatorView().setDrawEndValue(true);
        jsylSsv.getIndicatorView().setDrawStartValue(true);
        jsylSsv.getIndicatorView().setPhases(
                new IndicatorMirrorView.Phase[]{
                        new IndicatorMirrorView.Phase(0.0f, 50.0f, getResources().getColor(R.color.color_yellow)),
                        new IndicatorMirrorView.Phase(50.0f, 200.0f, getResources().getColor(R.color.color_green)),
                        new IndicatorMirrorView.Phase(200.0f, 300.0f, getResources().getColor(R.color.color_yellow))}
        );

        ssySsv.setTvTitle("收缩压");
        ssySsv.setTvValue(Math.round(sys) + "");
        ssySsv.setIvIcon(R.mipmap.icon_shousy);
        ssySsv.getIndicatorView().setCurrentValue((float) sys);
        ssySsv.getIndicatorView().setMinValue(70f);
        ssySsv.getIndicatorView().setMaxValue(170f);
        ssySsv.getIndicatorView().setDrawEndValue(true);
        ssySsv.getIndicatorView().setDrawStartValue(true);
        ssySsv.getIndicatorView().setPhases(
                new IndicatorMirrorView.Phase[]{
                        new IndicatorMirrorView.Phase(70f, 90f, getResources().getColor(R.color.color_yellow)),
                        new IndicatorMirrorView.Phase(90f, 110f, getResources().getColor(R.color.color_green)),
                        new IndicatorMirrorView.Phase(110f, 130f, getResources().getColor(R.color.color_blue)),
                        new IndicatorMirrorView.Phase(130f, 140f, getResources().getColor(R.color.color_yellow)),
                        new IndicatorMirrorView.Phase(140f, 170f, getResources().getColor(R.color.color_red))}
        );

        szySsv.setTvTitle("舒张压");
        szySsv.setTvValue(Math.round(dia) + "");
        szySsv.setIvIcon(R.mipmap.icon_shuzy);
        szySsv.getIndicatorView().setCurrentValue((float) dia);
        szySsv.getIndicatorView().setMinValue(50f);
        szySsv.getIndicatorView().setMaxValue(100f);
        szySsv.getIndicatorView().setDrawEndValue(true);
        szySsv.getIndicatorView().setDrawStartValue(true);
        szySsv.getIndicatorView().setPhases(
                new IndicatorMirrorView.Phase[]{
                        new IndicatorMirrorView.Phase(50f, 60f, getResources().getColor(R.color.color_yellow)),
                        new IndicatorMirrorView.Phase(60f, 70f, getResources().getColor(R.color.color_green)),
                        new IndicatorMirrorView.Phase(70f, 80f, getResources().getColor(R.color.color_blue)),
                        new IndicatorMirrorView.Phase(80f, 90f, getResources().getColor(R.color.color_yellow)),
                        new IndicatorMirrorView.Phase(90f, 100f, getResources().getColor(R.color.color_red))}
        );

        zffxSsv.setTvTitle("中风风险\n");
        zffxSsv.setTvValue(ValueUtil.doubleToString((stroke_risk * 100)) + "%");
        zffxSsv.setIvIcon(R.mipmap.icon_zhongffx);
        zffxSsv.getIndicatorView().setCurrentValue((float) stroke_risk);
        zffxSsv.getIndicatorView().setMinFractionLen(1);
        zffxSsv.getIndicatorView().setMinValue(0f);
        zffxSsv.getIndicatorView().setMaxValue(7.5f);
        zffxSsv.getIndicatorView().setDrawEndValue(true);
        zffxSsv.getIndicatorView().setDrawStartValue(true);
        zffxSsv.getIndicatorView().setPhases(
                new IndicatorMirrorView.Phase[]{
                        new IndicatorMirrorView.Phase(0f, 1.5f, getResources().getColor(R.color.color_green)),
                        new IndicatorMirrorView.Phase(1.5f, 3f, getResources().getColor(R.color.color_blue)),
                        new IndicatorMirrorView.Phase(3f, 4.5f, getResources().getColor(R.color.color_yellow)),
                        new IndicatorMirrorView.Phase(4.5f, 6f, getResources().getColor(R.color.color_orange)),
                        new IndicatorMirrorView.Phase(6f, 7.5f, getResources().getColor(R.color.color_red))}
        );

        healthScoreView.setScore(new float[]{0, 20, 40, 60, 80, 100}, new int[]{color_red, color_orange, color_yellow, color_blue, color_green}, (float) healthScore);
        healthScoreView.setScore((float) healthScore);
        healthScoreView.setTitle("生理综合分");
    }
}
