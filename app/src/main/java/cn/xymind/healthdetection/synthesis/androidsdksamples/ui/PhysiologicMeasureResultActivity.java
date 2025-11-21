package cn.xymind.healthdetection.synthesis.androidsdksamples.ui;

import android.os.Bundle;
import android.view.View;
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

    private SegmentScoreMirrorView xlSsv, xzylSsv, xggnSsv, pfnlSsv, xxgbfxSsv, tzzsSsv, xzbfxSsv, jsylSsv, ssySsv, szySsv, zffxSsv, spo2Ssmv, calculated, emotionScore, aggressivity, v_gender, anxiety, vitality, suppression, fatigue, physiologyScore;

    private ImageView isAfIv;

    private TextView atrialFibrillationTv;
    private TextView tv_emotion;

    private boolean isExpanded = false;
    private boolean isExpanded2 = false;
    private boolean isExpanded3 = false;
    private boolean isExpanded4 = false;
    private boolean isExpanded5 = false;
    private boolean isExpanded6 = false;
    private boolean isExpanded7 = false;
    private boolean isExpanded8 = false;
    private boolean isExpanded9 = false;
    private boolean isExpanded10 = false;
    private boolean isExpanded11 = false;
    private boolean isExpanded12 = false;
    private boolean isExpanded13 = false;
    private boolean isExpanded14 = false;
    private boolean isExpanded15 = false;
    private boolean isExpanded16 = false;
    private boolean isExpanded17 = false;
    private boolean isExpanded18 = false;
    private TextView contentText,toggleText,contentText2,toggleText2,contentText3,toggleText3,contentText4,toggleText4,contentText5,toggleText5,contentText6,toggleText6,contentText7,toggleText7,contentText8,toggleText8,contentText9,toggleText9,contentText10,toggleText10,contentText11,toggleText11,contentText12,toggleText12,contentText13,toggleText13,contentText14,toggleText14,contentText15,toggleText15,contentText16,toggleText16,contentText17,toggleText17,contentText18,toggleText18;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physiologic_measure_result);
        initView();
        initData();

        contentText = findViewById(R.id.content);
        toggleText = findViewById(R.id.expand_toggle);
        contentText2 = findViewById(R.id.content2);
        toggleText2 = findViewById(R.id.expand_toggle2);
        contentText3 = findViewById(R.id.content3);
        toggleText3 = findViewById(R.id.expand_toggle3);
        contentText4 = findViewById(R.id.content4);
        toggleText4 = findViewById(R.id.expand_toggle4);
        contentText5 = findViewById(R.id.content5);
        toggleText5 = findViewById(R.id.expand_toggle5);
        contentText6 = findViewById(R.id.content6);
        toggleText6 = findViewById(R.id.expand_toggle6);
        contentText7 = findViewById(R.id.content7);
        toggleText7 = findViewById(R.id.expand_toggle7);
        contentText8 = findViewById(R.id.content8);
        toggleText8 = findViewById(R.id.expand_toggle8);
        contentText9 = findViewById(R.id.content9);
        toggleText9 = findViewById(R.id.expand_toggle9);
        contentText10 = findViewById(R.id.content10);
        toggleText10 = findViewById(R.id.expand_toggle10);
        contentText11 = findViewById(R.id.content11);
        toggleText11 = findViewById(R.id.expand_toggle11);
        contentText12 = findViewById(R.id.content12);
        toggleText12 = findViewById(R.id.expand_toggle12);
        contentText13 = findViewById(R.id.content13);
        toggleText13 = findViewById(R.id.expand_toggle13);
        contentText14 = findViewById(R.id.content14);
        toggleText14 = findViewById(R.id.expand_toggle14);
        contentText15 = findViewById(R.id.content15);
        toggleText15 = findViewById(R.id.expand_toggle15);
        contentText16 = findViewById(R.id.content16);
        toggleText16 = findViewById(R.id.expand_toggle16);
        contentText17 = findViewById(R.id.content17);
        toggleText17 = findViewById(R.id.expand_toggle17);
        contentText18 = findViewById(R.id.content18);
        toggleText18 = findViewById(R.id.expand_toggle18);

        //心率
        toggleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded = !isExpanded;

                if (isExpanded) {
                    contentText.setMaxLines(Integer.MAX_VALUE);
                    toggleText.setText("收起");
                } else {
                    contentText.setMaxLines(3);
                    toggleText.setText("...展开");
                }
            }
        });
        //心率变异性
        toggleText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded2 = !isExpanded2;

                if (isExpanded2) {
                    contentText2.setMaxLines(Integer.MAX_VALUE);
                    toggleText2.setText("收起");
                } else {
                    contentText2.setMaxLines(3);
                    toggleText2.setText("...展开");
                }
            }
        });
        //体重指数
        toggleText3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded3 = !isExpanded3;

                if (isExpanded3) {
                    contentText3.setMaxLines(Integer.MAX_VALUE);
                    toggleText3.setText("收起");
                } else {
                    contentText3.setMaxLines(3);
                    toggleText3.setText("...展开");
                }
            }
        });
        //皮肤年龄
        toggleText4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded4 = !isExpanded4;

                if (isExpanded4) {
                    contentText4.setMaxLines(Integer.MAX_VALUE);
                    toggleText4.setText("收起");
                } else {
                    contentText4.setMaxLines(3);
                    toggleText4.setText("...展开");
                }
            }
        });
        //血管功能
        toggleText5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded5 = !isExpanded5;

                if (isExpanded5) {
                    contentText5.setMaxLines(Integer.MAX_VALUE);
                    toggleText5.setText("收起");
                } else {
                    contentText5.setMaxLines(3);
                    toggleText5.setText("...展开");
                }
            }
        });
        //心脏压力
        toggleText6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded6 = !isExpanded6;

                if (isExpanded6) {
                    contentText6.setMaxLines(Integer.MAX_VALUE);
                    toggleText6.setText("收起");
                } else {
                    contentText6.setMaxLines(3);
                    toggleText6.setText("...展开");
                }
            }
        });
        //压力度
        toggleText7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded7 = !isExpanded7;

                if (isExpanded7) {
                    contentText7.setMaxLines(Integer.MAX_VALUE);
                    toggleText7.setText("收起");
                } else {
                    contentText7.setMaxLines(3);
                    toggleText7.setText("...展开");
                }
            }
        });
        //情绪指数
        toggleText8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded8 = !isExpanded8;

                if (isExpanded7) {
                    contentText8.setMaxLines(Integer.MAX_VALUE);
                    toggleText8.setText("收起");
                } else {
                    contentText8.setMaxLines(3);
                    toggleText8.setText("...展开");
                }
            }
        });
        //攻击性
        toggleText9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded9 = !isExpanded9;

                if (isExpanded9) {
                    contentText9.setMaxLines(Integer.MAX_VALUE);
                    toggleText9.setText("收起");
                } else {
                    contentText9.setMaxLines(3);
                    toggleText9.setText("...展开");
                }
            }
        });
        //焦虑度
        toggleText10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded10 = !isExpanded10;

                if (isExpanded10) {
                    contentText10.setMaxLines(Integer.MAX_VALUE);
                    toggleText10.setText("收起");
                } else {
                    contentText10.setMaxLines(3);
                    toggleText10.setText("...展开");
                }
            }
        });
        //活力度
        toggleText11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded11 = !isExpanded11;

                if (isExpanded11) {
                    contentText11.setMaxLines(Integer.MAX_VALUE);
                    toggleText11.setText("收起");
                } else {
                    contentText11.setMaxLines(3);
                    toggleText11.setText("...展开");
                }
            }
        });
        //抑郁度
        toggleText12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded12 = !isExpanded12;

                if (isExpanded12) {
                    contentText12.setMaxLines(Integer.MAX_VALUE);
                    toggleText12.setText("收起");
                } else {
                    contentText12.setMaxLines(3);
                    toggleText12.setText("...展开");
                }
            }
        });
        //疲劳度
        toggleText13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded13 = !isExpanded13;

                if (isExpanded13) {
                    contentText13.setMaxLines(Integer.MAX_VALUE);
                    toggleText13.setText("收起");
                } else {
                    contentText13.setMaxLines(3);
                    toggleText13.setText("...展开");
                }
            }
        });
        //心脏病风险
        toggleText14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded14 = !isExpanded14;

                if (isExpanded14) {
                    contentText14.setMaxLines(Integer.MAX_VALUE);
                    toggleText14.setText("收起");
                } else {
                    contentText14.setMaxLines(3);
                    toggleText14.setText("...展开");
                }
            }
        });
        //中风风险
        toggleText15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded15 = !isExpanded15;

                if (isExpanded15) {
                    contentText15.setMaxLines(Integer.MAX_VALUE);
                    toggleText15.setText("收起");
                } else {
                    contentText15.setMaxLines(3);
                    toggleText15.setText("...展开");
                }
            }
        });
        //心血管病风险
        toggleText16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded16 = !isExpanded16;

                if (isExpanded16) {
                    contentText16.setMaxLines(Integer.MAX_VALUE);
                    toggleText16.setText("收起");
                } else {
                    contentText16.setMaxLines(3);
                    toggleText16.setText("...展开");
                }
            }
        });
        //血氧含量
        toggleText17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded17 = !isExpanded17;

                if (isExpanded17) {
                    contentText17.setMaxLines(Integer.MAX_VALUE);
                    toggleText17.setText("收起");
                } else {
                    contentText17.setMaxLines(3);
                    toggleText17.setText("...展开");
                }
            }
        });
        //房颤
        toggleText18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded18 = !isExpanded18;

                if (isExpanded18) {
                    contentText18.setMaxLines(Integer.MAX_VALUE);
                    toggleText18.setText("收起");
                } else {
                    contentText18.setMaxLines(3);
                    toggleText18.setText("...展开");
                }
            }
        });
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
//        ssySsv = findViewById(R.id.v_ssy);
//        szySsv = findViewById(R.id.v_szy);
        zffxSsv = findViewById(R.id.v_zffx);
        spo2Ssmv = findViewById(R.id.spo2Ssmv);
        calculated = findViewById(R.id.calculated);
        emotionScore = findViewById(R.id.emotionScore);
        isAfIv = findViewById(R.id.isAfIv);
        atrialFibrillationTv = findViewById(R.id.atrialFibrillationTv);
        aggressivity = findViewById(R.id.aggressivity);
//        v_gender = findViewById(R.id.gender);
        anxiety = findViewById(R.id.anxiety);
        vitality = findViewById(R.id.vitality);
        suppression = findViewById(R.id.suppression);
        fatigue = findViewById(R.id.fatigue);
//        physiologyScore = findViewById(R.id.physiologyScore);
//        tv_emotion = findViewById(R.id.tv_emotion);
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
        double emotion = getIntent().getDoubleExtra("emotion", 0);

        double physiologyScoreReport = getIntent().getDoubleExtra("physiologyScoreReport", 0);
        double aggressivityReport = getIntent().getDoubleExtra("aggressivityReport", 0);
        double anxietyReport = getIntent().getDoubleExtra("anxietyReport", 0);
        double vitalityReport = getIntent().getDoubleExtra("vitalityReport", 0);
        double suppressionReport = getIntent().getDoubleExtra("suppressionReport", 0);
        double fatigueReport = getIntent().getDoubleExtra("fatigueReport", 0);
        int gender = getIntent().getIntExtra("gender", 0);
        //情绪
//        tv_emotion.setText("情绪指数：" + ValueUtil.doubleToString(emotion)
//                + "\r\n攻击性:" + ValueUtil.doubleToString(aggressivityReport)
//                + "\r\n性  别:" + (gender == 0 ? "男" : "女")
//                + "\r\n焦虑度:" + ValueUtil.doubleToString(anxietyReport)
//                + "\r\n活力度:" + ValueUtil.doubleToString(vitalityReport)
//                + "\r\n抑郁度:" + ValueUtil.doubleToString(suppressionReport)
//                + "\r\n疲劳度:" + ValueUtil.doubleToString(fatigueReport)
//        );

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

//        physiologyScore.setTvTitle("综合心健康风险");
//        physiologyScore.setTvValue(ValueUtil.doubleToString(physiologyScoreReport));
//        physiologyScore.setIvIcon(R.mipmap.icon_physiology);
//        physiologyScore.getIndicatorView().setCurrentValue((float) physiologyScoreReport);
//        physiologyScore.getIndicatorView().setMinFractionLen(1);
//        physiologyScore.getIndicatorView().setMinValue(50f);
//        physiologyScore.getIndicatorView().setMaxValue(100f);
//        physiologyScore.getIndicatorView().setDrawEndValue(true);
//        physiologyScore.getIndicatorView().setDrawStartValue(true);
//        physiologyScore.getIndicatorView().setPhases(
//                new IndicatorMirrorView.Phase[]{
//                        new IndicatorMirrorView.Phase(0f, 60f, getResources().getColor(R.color.color_red)),
//                        new IndicatorMirrorView.Phase(60f, 70f, getResources().getColor(R.color.color_orange)),
//                        new IndicatorMirrorView.Phase(70f, 80f, getResources().getColor(R.color.color_yellow)),
//                        new IndicatorMirrorView.Phase(80f, 90f, getResources().getColor(R.color.color_blue)),
//                        new IndicatorMirrorView.Phase(90f, 100f, getResources().getColor(R.color.color_green))}
//        );

        calculated.setTvTitle("压力度");
        calculated.setTvValue(ValueUtil.doubleToString(msi));
        calculated.setIvIcon(R.mipmap.icon_xinzfh);
        calculated.getIndicatorView().setCurrentValue((float) msi);
//        calculated.getIndicatorView().setMinFractionLen(1);
        calculated.getIndicatorView().setMinValue(0f);
        calculated.getIndicatorView().setMaxValue(100f);
        calculated.getIndicatorView().setDrawEndValue(true);
        calculated.getIndicatorView().setDrawStartValue(true);
        calculated.getIndicatorView().setPhases(
                new IndicatorMirrorView.Phase[]{
                        new IndicatorMirrorView.Phase(0f, 20f, getResources().getColor(R.color.color_green)),
                        new IndicatorMirrorView.Phase(20f, 40f, getResources().getColor(R.color.color_blue)),
                        new IndicatorMirrorView.Phase(40f, 60f, getResources().getColor(R.color.color_yellow)),
                        new IndicatorMirrorView.Phase(60f, 80f, getResources().getColor(R.color.color_orange)),
                        new IndicatorMirrorView.Phase(80f, 100f, getResources().getColor(R.color.color_red))}
        );

        emotionScore.setTvTitle("情绪指数");
        emotionScore.setTvValue(ValueUtil.doubleToString(emotion));
        emotionScore.setIvIcon(R.mipmap.icon_emotion);
        emotionScore.getIndicatorView().setCurrentValue((float) emotion);
//        emotionScore.getIndicatorView().setMinFractionLen(1);
        emotionScore.getIndicatorView().setMinValue(0f);
        emotionScore.getIndicatorView().setMaxValue(100f);
        emotionScore.getIndicatorView().setDrawEndValue(true);
        emotionScore.getIndicatorView().setDrawStartValue(true);
        emotionScore.getIndicatorView().setPhases(
                new IndicatorMirrorView.Phase[]{
                        new IndicatorMirrorView.Phase(0f, 20f, getResources().getColor(R.color.color_green)),
                        new IndicatorMirrorView.Phase(20f, 40f, getResources().getColor(R.color.color_blue)),
                        new IndicatorMirrorView.Phase(40f, 60f, getResources().getColor(R.color.color_yellow)),
                        new IndicatorMirrorView.Phase(60f, 80f, getResources().getColor(R.color.color_orange)),
                        new IndicatorMirrorView.Phase(80f, 100f, getResources().getColor(R.color.color_red))}
        );

        aggressivity.setTvTitle("攻击性");
        aggressivity.setTvValue(ValueUtil.doubleToString(aggressivityReport));
        aggressivity.setIvIcon(R.mipmap.icon_aggressivity);
        aggressivity.getIndicatorView().setCurrentValue((float) aggressivityReport);
//        aggressivity.getIndicatorView().setMinFractionLen(1);
        aggressivity.getIndicatorView().setMinValue(0f);
        aggressivity.getIndicatorView().setMaxValue(100f);
        aggressivity.getIndicatorView().setDrawEndValue(true);
        aggressivity.getIndicatorView().setDrawStartValue(true);
        aggressivity.getIndicatorView().setPhases(
                new IndicatorMirrorView.Phase[]{
                        new IndicatorMirrorView.Phase(0f, 20f, getResources().getColor(R.color.color_green)),
                        new IndicatorMirrorView.Phase(20f, 40f, getResources().getColor(R.color.color_blue)),
                        new IndicatorMirrorView.Phase(40f, 60f, getResources().getColor(R.color.color_yellow)),
                        new IndicatorMirrorView.Phase(60f, 80f, getResources().getColor(R.color.color_orange)),
                        new IndicatorMirrorView.Phase(80f, 100f, getResources().getColor(R.color.color_red))}
        );

        anxiety.setTvTitle("焦虑度");
        anxiety.setTvValue(ValueUtil.doubleToString(anxietyReport));
        anxiety.setIvIcon(R.mipmap.icon_anxiety);
        anxiety.getIndicatorView().setCurrentValue((float) anxietyReport);
//        anxiety.getIndicatorView().setMinFractionLen(1);
        anxiety.getIndicatorView().setMinValue(0f);
        anxiety.getIndicatorView().setMaxValue(100f);
        anxiety.getIndicatorView().setDrawEndValue(true);
        anxiety.getIndicatorView().setDrawStartValue(true);
        anxiety.getIndicatorView().setPhases(
                new IndicatorMirrorView.Phase[]{
                        new IndicatorMirrorView.Phase(0f, 20f, getResources().getColor(R.color.color_green)),
                        new IndicatorMirrorView.Phase(20f, 40f, getResources().getColor(R.color.color_blue)),
                        new IndicatorMirrorView.Phase(40f, 60f, getResources().getColor(R.color.color_yellow)),
                        new IndicatorMirrorView.Phase(60f, 80f, getResources().getColor(R.color.color_orange)),
                        new IndicatorMirrorView.Phase(80f, 100f, getResources().getColor(R.color.color_red))}
        );

        vitality.setTvTitle("活力度");
        vitality.setTvValue(ValueUtil.doubleToString(vitalityReport));
        vitality.setIvIcon(R.mipmap.icon_vitality);
        vitality.getIndicatorView().setCurrentValue((float) vitalityReport);
//        vitality.getIndicatorView().setMinFractionLen(1);
        vitality.getIndicatorView().setMinValue(0f);
        vitality.getIndicatorView().setMaxValue(100f);
        vitality.getIndicatorView().setDrawEndValue(true);
        vitality.getIndicatorView().setDrawStartValue(true);
        vitality.getIndicatorView().setPhases(
                new IndicatorMirrorView.Phase[]{
                        new IndicatorMirrorView.Phase(0f, 20f, getResources().getColor(R.color.color_red)),
                        new IndicatorMirrorView.Phase(20f, 40f, getResources().getColor(R.color.color_yellow)),
                        new IndicatorMirrorView.Phase(40f, 60f, getResources().getColor(R.color.color_green)),
                        new IndicatorMirrorView.Phase(60f, 80f, getResources().getColor(R.color.color_blue)),
                        new IndicatorMirrorView.Phase(80f, 100f, getResources().getColor(R.color.color_yellow))}
        );

        suppression.setTvTitle("抑郁度");
        suppression.setTvValue(ValueUtil.doubleToString(suppressionReport));
        suppression.setIvIcon(R.mipmap.icon_suppression);
        suppression.getIndicatorView().setCurrentValue((float) suppressionReport);
//        suppression.getIndicatorView().setMinFractionLen(1);
        suppression.getIndicatorView().setMinValue(0f);
        suppression.getIndicatorView().setMaxValue(100f);
        suppression.getIndicatorView().setDrawEndValue(true);
        suppression.getIndicatorView().setDrawStartValue(true);
        suppression.getIndicatorView().setPhases(
                new IndicatorMirrorView.Phase[]{
                        new IndicatorMirrorView.Phase(0f, 20f, getResources().getColor(R.color.color_green)),
                        new IndicatorMirrorView.Phase(20f, 40f, getResources().getColor(R.color.color_blue)),
                        new IndicatorMirrorView.Phase(40f, 60f, getResources().getColor(R.color.color_yellow)),
                        new IndicatorMirrorView.Phase(60f, 80f, getResources().getColor(R.color.color_orange)),
                        new IndicatorMirrorView.Phase(80f, 100f, getResources().getColor(R.color.color_red))}
        );

        fatigue.setTvTitle("疲劳度");
        fatigue.setTvValue(ValueUtil.doubleToString(fatigueReport));
        fatigue.setIvIcon(R.mipmap.icon_fatigue);
        fatigue.getIndicatorView().setCurrentValue((float) fatigueReport);
//        fatigue.getIndicatorView().setMinFractionLen(1);
        fatigue.getIndicatorView().setMinValue(0f);
        fatigue.getIndicatorView().setMaxValue(100f);
        fatigue.getIndicatorView().setDrawEndValue(true);
        fatigue.getIndicatorView().setDrawStartValue(true);
        fatigue.getIndicatorView().setPhases(
                new IndicatorMirrorView.Phase[]{
                        new IndicatorMirrorView.Phase(0f, 20f, getResources().getColor(R.color.color_green)),
                        new IndicatorMirrorView.Phase(20f, 40f, getResources().getColor(R.color.color_blue)),
                        new IndicatorMirrorView.Phase(40f, 60f, getResources().getColor(R.color.color_yellow)),
                        new IndicatorMirrorView.Phase(60f, 80f, getResources().getColor(R.color.color_orange)),
                        new IndicatorMirrorView.Phase(80f, 100f, getResources().getColor(R.color.color_red))}
        );

//        v_gender.setTvTitle("性别");
//        v_gender.setTvValue((gender == 0 ? "男" : "女"));
//        v_gender.setIvIcon(R.mipmap.icon_gender);
////        v_gender.getIndicatorView().setCurrentValue((gender == 0 ? "男" : "女"));
////        v_gender.getIndicatorView().setMinFractionLen(1);
////        v_gender.getIndicatorView().setMinValue(0f);
////        v_gender.getIndicatorView().setMaxValue(100f);
////        v_gender.getIndicatorView().setDrawEndValue(true);
////        v_gender.getIndicatorView().setDrawStartValue(true);
//        v_gender.getIndicatorView().setPhases(
//                new IndicatorMirrorView.Phase[]{
//                        new IndicatorMirrorView.Phase(0, 0, getResources().getColor(R.color.color_green))}
//        );

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

//        ssySsv.setTvTitle("收缩压");
//        ssySsv.setTvValue(Math.round(sys) + "");
//        ssySsv.setIvIcon(R.mipmap.icon_shousy);
//        ssySsv.getIndicatorView().setCurrentValue((float) sys);
//        ssySsv.getIndicatorView().setMinValue(70f);
//        ssySsv.getIndicatorView().setMaxValue(170f);
//        ssySsv.getIndicatorView().setDrawEndValue(true);
//        ssySsv.getIndicatorView().setDrawStartValue(true);
//        ssySsv.getIndicatorView().setPhases(
//                new IndicatorMirrorView.Phase[]{
//                        new IndicatorMirrorView.Phase(70f, 90f, getResources().getColor(R.color.color_yellow)),
//                        new IndicatorMirrorView.Phase(90f, 110f, getResources().getColor(R.color.color_green)),
//                        new IndicatorMirrorView.Phase(110f, 130f, getResources().getColor(R.color.color_blue)),
//                        new IndicatorMirrorView.Phase(130f, 140f, getResources().getColor(R.color.color_yellow)),
//                        new IndicatorMirrorView.Phase(140f, 170f, getResources().getColor(R.color.color_red))}
//        );

//        szySsv.setTvTitle("舒张压");
//        szySsv.setTvValue(Math.round(dia) + "");
//        szySsv.setIvIcon(R.mipmap.icon_shuzy);
//        szySsv.getIndicatorView().setCurrentValue((float) dia);
//        szySsv.getIndicatorView().setMinValue(50f);
//        szySsv.getIndicatorView().setMaxValue(100f);
//        szySsv.getIndicatorView().setDrawEndValue(true);
//        szySsv.getIndicatorView().setDrawStartValue(true);
//        szySsv.getIndicatorView().setPhases(
//                new IndicatorMirrorView.Phase[]{
//                        new IndicatorMirrorView.Phase(50f, 60f, getResources().getColor(R.color.color_yellow)),
//                        new IndicatorMirrorView.Phase(60f, 70f, getResources().getColor(R.color.color_green)),
//                        new IndicatorMirrorView.Phase(70f, 80f, getResources().getColor(R.color.color_blue)),
//                        new IndicatorMirrorView.Phase(80f, 90f, getResources().getColor(R.color.color_yellow)),
//                        new IndicatorMirrorView.Phase(90f, 100f, getResources().getColor(R.color.color_red))}
//        );

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
