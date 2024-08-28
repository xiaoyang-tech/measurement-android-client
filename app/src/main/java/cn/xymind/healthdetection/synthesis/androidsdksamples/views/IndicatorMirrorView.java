package cn.xymind.healthdetection.synthesis.androidsdksamples.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ColorUtils;

import java.text.NumberFormat;

import cn.xymind.healthdetection.synthesis.androidsdksamples.R;
import cn.xymind.healthdetection.synthesis.androidsdksamples.utils.DisplayUtil;
import cn.xymind.healthdetection.synthesis.androidsdksamples.utils.StringUtil;


public class IndicatorMirrorView extends View {

    private int verticalPadding = 4;
    private int horizontalPadding = 14;
    private int valueSize = 2;

    private int defaultColor = 0xFF29D372;
    private int valueColor = 0xFF2D2D2D;

    private int valueTextSize = 14;
    private float maxValue = 100f;
    private float minValue = 0;
    private float value = 0;
    private Phase[] phases;

    private boolean drawStartValue = false;
    private boolean drawEndValue = false;

    private NumberFormat valueFormat;

    private int fractionLen = 0;

    public IndicatorMirrorView(Context context) {
        super(context);
        init();
    }

    public IndicatorMirrorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndicatorMirrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        verticalPadding = DisplayUtil.dip2px(getContext(), verticalPadding);
        horizontalPadding = DisplayUtil.dip2px(getContext(), horizontalPadding);
        valueTextSize = DisplayUtil.sp2px(getContext(), valueTextSize);
        valueSize = DisplayUtil.dip2px(getContext(), valueSize);

        valueFormat =NumberFormat.getNumberInstance();
        valueFormat.setMaximumFractionDigits(fractionLen);
        valueFormat.setMinimumFractionDigits(fractionLen);
    }

    public void setPhases(Phase[] ps) {
        this.phases = ps;
        post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    public void setMaxValue(float max) {
        this.maxValue = max;
    }

    public void setMinValue(float min) {
        this.minValue = min;
    }

    public void setCurrentValue(float v) {
        this.value = v;
    }

    public void setDrawStartValue(boolean b){
        this.drawStartValue = b;
    }

    public void setDrawEndValue(boolean b){
        this.drawEndValue = b;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0) {
            return;
        }

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        paint.setColor(defaultColor);
        paint.setStyle(Paint.Style.FILL);

        int barHeight = (int) (height * (1f / 3));

        canvas.drawRect(horizontalPadding, verticalPadding, width - horizontalPadding, barHeight + verticalPadding, paint);

        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(valueColor);
        textPaint.setTextSize(valueTextSize);

        //各个阶段值
        if (phases == null || phases.length == 0) {
            String mv = valueFormat.format(minValue);
            float valueWidth = textPaint.measureText(mv);
            canvas.drawText(mv, horizontalPadding - valueWidth / 2f, barHeight + verticalPadding + valueTextSize, textPaint);
        } else {
            for (int i = 0; i < phases.length; i++) {

                float left = (phases[i].startValue - minValue) / (maxValue - minValue) * (width - horizontalPadding * 2) + horizontalPadding;
                float right = (phases[i].endValue - minValue) / (maxValue - minValue) * (width - horizontalPadding * 2) + horizontalPadding;
                paint.setColor(phases[i].color);

                canvas.drawRect(left, verticalPadding, right, barHeight + verticalPadding, paint);

                if (i > 0) {
                    String startValue = valueFormat.format(phases[i].startValue);
                    canvas.drawText(startValue, left - textPaint.measureText(startValue) / 2f, barHeight + verticalPadding + valueTextSize + DisplayUtil.dip2px(getContext(), 2), textPaint);
                }
            }

            if (drawStartValue) {
                String value = valueFormat.format(minValue);
                canvas.drawText(value, horizontalPadding - textPaint.measureText(value) / 2f, barHeight + verticalPadding + valueTextSize + DisplayUtil.dip2px(getContext(), 2), textPaint);
            }

            if (drawEndValue) {
                String value = valueFormat.format(maxValue);
                canvas.drawText(value, width - horizontalPadding - textPaint.measureText(value) / 2f, barHeight + verticalPadding + valueTextSize + DisplayUtil.dip2px(getContext(), 2), textPaint);

            }

        }

        float x = (value - minValue) / (maxValue - minValue) * (width - horizontalPadding * 2) + horizontalPadding;
        if (x < horizontalPadding) {
            x = horizontalPadding;
        } else if (x > width - horizontalPadding) {
            x = width - horizontalPadding;
        }

        paint.setColor(valueColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(valueSize);
        canvas.drawCircle(x, barHeight / 2f + verticalPadding, barHeight / 2f + valueSize / 2f, paint);

    }

    public void setMinFractionLen(int len){
        this.fractionLen = len;
        valueFormat =NumberFormat.getNumberInstance();
        valueFormat.setMaximumFractionDigits(fractionLen);
        valueFormat.setMinimumFractionDigits(fractionLen);
    }

    public static class Phase {
        private float startValue;
        private float endValue;
        private int color;

        public Phase() {
        }

        public Phase(float startValue, float endValue, int color) {
            this.startValue = startValue;
            this.endValue = endValue;
            this.color = color;
        }

        public float getStartValue() {
            return startValue;
        }

        public void setStartValue(float startValue) {
            this.startValue = startValue;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public float getEndValue() {
            return endValue;
        }

        public void setEndValue(float endValue) {
            this.endValue = endValue;
        }

    }
    public int getTextColor() {
//        MyLog.e("字体颜色==" + scorePaint.getColor());
//        return scorePaint.getColor();
        return color(value);
    }



    private int color(float itemScore) {
        int color = phases[0].color;
        //判断值是否超出显示范围，超出则直接标红
        if (itemScore < phases[0].startValue || itemScore > phases[phases.length-1].endValue) {
            color = ColorUtils.getColor(R.color.color_red);
        } else {
            for (int index = 0; index < phases.length; index++) {
                if (itemScore < phases[index].endValue && itemScore >= phases[0].startValue&& itemScore > phases[index].startValue) {
                    color = phases[index].color;
                    break;
                }
                if (itemScore == phases[phases.length - 1].endValue) {
                    color =phases[phases.length - 1].color;
                    break;
                }
            }
        }
        return color;
    }
    public static void main(String[] args) {
        System.out.println("格式化：" + StringUtil.formatNumber(2.0f, 1));
    }
}
