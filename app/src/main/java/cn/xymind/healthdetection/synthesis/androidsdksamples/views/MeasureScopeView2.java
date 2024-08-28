package cn.xymind.healthdetection.synthesis.androidsdksamples.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;

import java.util.Timer;
import java.util.TimerTask;

import cn.xymind.healthdetection.synthesis.androidsdksamples.R;


/**
 * created by xingxing on 2021/3/30 21:32
 * Email：zhangwenxing1716@163.com
 */
public class MeasureScopeView2 extends View {
    public static final String TAG = "MeasureScopeView";
    public static final int STATUS_REGULAR_GREEN = 0;
    public static final int STATUS_ABNORMAL_RED = 1;
    private int status = STATUS_ABNORMAL_RED;
    private boolean isMeasuring = false;
    private int timeCircleRadius;//倒计时的秒数半径
    private int circleWidth = 0;
    private int progressWidth = 0;
    private int topMargin = 0;
    private long measureTime = 30 * 1000;
    private long startTime = 0;
    private double progressPercent;
    private double measurementDurationSecs = 30.0;
//    private Timer mMeasureTimer;

    private Paint mPaint;
    private Paint mTextPaint;
    private Paint transPaint;
    private ImageView imageView;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private OnMeasureTimeListener onMeasureTimeListener;
    private int i = 0;
    private int measureType = 0;

    protected boolean showSecond = true;

    private Timer mMeasureTimer;

    public MeasureScopeView2(Context context) {
        super(context);
        init();
    }

    public MeasureScopeView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MeasureScopeView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //    @Override
    protected void init() {
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(ConvertUtils.sp2px(12));
        mTextPaint.setColor(Color.WHITE);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        circleWidth = ConvertUtils.dp2px(3);
        topMargin = ConvertUtils.dp2px(140);
        progressWidth = ConvertUtils.dp2px(4);
        timeCircleRadius = ConvertUtils.dp2px(12);

        transPaint = new Paint();
        transPaint.setColor(Color.TRANSPARENT);
        transPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        setWillNotDraw(false);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    //    @Override
    public void setStatus(int s) {
        this.status = s;
        post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        if (width == 0 || height == 0) {
            return;
        }

        int radius;

        if (width > height) {
            radius = (int) (height * 0.8f / 2);
        } else {
            radius = (int) (width * 0.8f / 2);
        }

        mPaint.setStyle(Paint.Style.FILL);
        canvas.save();
        Path path = new Path();
        path.addCircle(width / 2f, topMargin + radius, radius, Path.Direction.CW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas.clipOutPath(path);
            canvas.drawColor(getResources().getColor(R.color.bgColor));
        } else {
            canvas.drawColor(getResources().getColor(R.color.bgColor));
            canvas.drawCircle(width / 2f, topMargin + radius, radius, transPaint);
        }
        canvas.restore();

        /***** 绘制细圆圈 ****/
        if (isMeasuring) {
            mPaint.setColor(getResources().getColor(R.color.defaultCircleColor));
        } else {
            if (status == STATUS_REGULAR_GREEN) {
                mPaint.setColor(getResources().getColor(R.color.regularColor));
            } else if (status == STATUS_ABNORMAL_RED) {
                mPaint.setColor(getResources().getColor(R.color.abnormalColor));
            }
        }
        mPaint.setStrokeWidth(ConvertUtils.dp2px(1));
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(width / 2f, topMargin + radius, radius, mPaint);


        float angle;
        long time;
        if (measureType == 1) {
            if (isMeasuring) {
                time = System.currentTimeMillis() - startTime > measureTime ? measureTime : System.currentTimeMillis() - startTime;
                angle = (time + 0f) / measureTime * 360;
            } else {
                time = 0;
                angle = 0;
            }
        } else {
            if (isMeasuring) {
                time = System.currentTimeMillis() - startTime > measureTime ? measureTime : System.currentTimeMillis() - startTime;
                angle = (time + 0f) / measureTime * 360;
                angle = (float) (360 * progressPercent / 100.0f);
            } else {
                time = 0;
                angle = 0;
            }
        }

        //绘制倒计时秒数的小圆圈
        int cx = (int) (Math.sin(Math.toRadians(angle)) * radius + width / 2f);
        int cy = (int) (radius - Math.cos(Math.toRadians(angle)) * radius + topMargin);
        canvas.drawCircle(cx, cy, timeCircleRadius + ConvertUtils.dp2px(1), mPaint);

        if (status == STATUS_REGULAR_GREEN) {
            mPaint.setColor(getResources().getColor(R.color.regularColor));
        } else if (status == STATUS_ABNORMAL_RED) {
            mPaint.setColor(getResources().getColor(R.color.abnormalColor));
        }

        if (imageView1 != null && imageView2 != null && imageView3 != null) {
            FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) imageView1.getLayoutParams();
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) imageView2.getLayoutParams();
            FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) imageView3.getLayoutParams();

            layoutParams1.width = width;
            layoutParams1.height = width;
            layoutParams1.topMargin = topMargin + radius - width / 2;
            imageView1.setLayoutParams(layoutParams1);

            layoutParams2.width = width;
            layoutParams2.height = width;
            layoutParams2.topMargin = topMargin + radius - width / 2;
            imageView2.setLayoutParams(layoutParams2);

            layoutParams3.width = width;
            layoutParams3.height = width;
            layoutParams3.topMargin = topMargin + radius - width / 2;
            imageView3.setLayoutParams(layoutParams3);

            if (i == 0) {
                RotateAnimation rotate1 = new RotateAnimation(0f, 360f, width / 2, width / 2);
                RotateAnimation rotate2 = new RotateAnimation(0f, 360f, width / 2, width / 2);
                RotateAnimation rotate3 = new RotateAnimation(0f, 360f, width / 2, width / 2);

                rotate1.setInterpolator(new LinearInterpolator());
                rotate1.setDuration(15 * 1000);//设置动画持续周期
                rotate1.setRepeatCount(-1);//设置重复次数
                rotate1.setFillAfter(true);//动画执行完后是否停留在执行完的状态

                rotate2.setInterpolator(new LinearInterpolator());
                rotate2.setDuration(15 * 1000);//设置动画持续周期
                rotate2.setRepeatCount(-1);//设置重复次数
                rotate2.setFillAfter(true);//动画执行完后是否停留在执行完的状态

                rotate3.setInterpolator(new LinearInterpolator());
                rotate3.setDuration(30 * 1000);//设置动画持续周期
                rotate3.setRepeatCount(-1);//设置重复次数
                rotate3.setFillAfter(true);//动画执行完后是否停留在执行完的状态

                imageView1.startAnimation(rotate1);
                imageView2.startAnimation(rotate2);
                imageView3.startAnimation(rotate3);
            }

            i = 1;
        }

        mPaint.setStrokeWidth(progressWidth);
        RectF rectF = new RectF(width / 2f - radius, topMargin, width / 2f + radius, topMargin + 2 * radius);
        canvas.drawArc(rectF, -90, angle, false, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cx, cy, timeCircleRadius, mPaint);

        Rect rect = new Rect();
        float timeSecond = (measureTime - time) / 1000f;
        if (measureType == 0) {
            timeSecond = (int) (measurementDurationSecs - measurementDurationSecs * progressPercent / 100);
        }
        String timeStr = String.valueOf((int) Math.ceil(timeSecond));
        mTextPaint.getTextBounds(timeStr, 0, timeStr.length(), rect);
        if (onMeasureTimeListener != null) {
            onMeasureTimeListener.getCountDownTime((int) timeSecond);
        }
        if (showSecond) {
            canvas.drawText(timeStr, cx - mTextPaint.measureText(timeStr) / 2f, cy + rect.height() / 2f, mTextPaint);
        }
    }

    public void onMeasureStart() {
        if (isMeasuring) {
            return;
        }
        isMeasuring = true;
        startTime = System.currentTimeMillis();
    }

    public void onMeasureStart(int measureType) {
        try {
            this.measureType = measureType;
            if (isMeasuring) {
                return;
            }
            isMeasuring = true;
            startTime = System.currentTimeMillis();
            mMeasureTimer = new Timer();
            mMeasureTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isMeasuring) {
                        return;
                    }
                    post(new Runnable() {
                        @Override
                        public void run() {
                            invalidate();
                        }
                    });
                }
            }, 40, 40);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //    @Override
    public void onMeasureStop() {
        if (!isMeasuring) {
            return;
        }
        isMeasuring = false;
//        mMeasureTimer.cancel();
//        mMeasureTimer = null;
        post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    public void onMeasureStop2() {
        if (!isMeasuring) {
            return;
        }
        isMeasuring = false;
        mMeasureTimer.cancel();
        mMeasureTimer = null;
        post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    //    @Override
    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setImageView1(ImageView imageView1) {
        this.imageView1 = imageView1;
    }

    public void setImageView2(ImageView imageView2) {
        this.imageView2 = imageView2;
    }

    public void setImageView3(ImageView imageView3) {
        this.imageView3 = imageView3;
    }

    public interface OnMeasureTimeListener {
        void getCountDownTime(int time);
    }

    public void setOnMeasureTimeListener(OnMeasureTimeListener onMeasureTimeListener) {
        this.onMeasureTimeListener = onMeasureTimeListener;
    }

    public void setMeasurementProgress(double var1) {
        this.progressPercent = Math.min((double) var1, 100.0D);
        this.postInvalidate();
    }

    public void setShowSecond(boolean showSecond) {
        this.showSecond = showSecond;
    }

    public void setMeasureTime(int time) {
        measureTime = time * 1000L;
    }

    public void setDurationSecs(double time) {
        measurementDurationSecs = time;
    }
}
