package cn.xymind.healthdetection.synthesis.androidsdksamples;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ConvertUtils;

import cn.xymind.measurementsdk.util.StringUtil;


public class ScoreView extends View {

    private static final String TAG = "ScoreView";
    private int startAngle = 120;
    private int endAngle = 420;
    private int defaultWidth = ConvertUtils.dp2px(330);
    private int defaultHeight = ConvertUtils.dp2px(330);

    private boolean enableAnim = false, tooMany = false, outerText = false;
    private float[] scoreArray;
    private int[] colorArray;
    private float minScore, maxScore;

    private Point centerPoint;

    private Paint outerLinePaint, outDotPaint, outLineTextPaint;
    private int outerCircleScoreTextColor = ColorUtils.getColor(R.color.outer_circle_score_text_color);
    private int outerCircleStrokeWidth = 20;
    private int outerCircleStrokeHeight = 20;
    private int outerCircleRadius = 100;
    private int outerCircleDotRadius = 4;
    private int outerCircleScoreTextSize = 16;

    private Paint innerCirclePaint, innerProgressCirclePaint, innerCircleProgressDotPaint;
    private RectF innerCircleRectF;
    private int innerCircleBgColor = ColorUtils.getColor(R.color.inner_circle_bg_color);
    //    private int innerProgressCircleBgColor = ColorUtils.getColor(R.color.inner_progress_circle_bg_color);
    private int innerProgressCircleBgColor = -1;
    private int innerCircleStrokeWidth = 20;
    private int innerCircleRadius = 100;
    private int innerProgressDotRadius = 20;

    private Paint scoreTextPaint, scoreUnitTextPaint;
    private int scoreTextColor = ColorUtils.getColor(R.color.inner_progress_circle_bg_color);
    private int scoreUnitTextColor = ColorUtils.getColor(R.color.inner_circle_bg_color);
    private int scoreTextSize = 50;
    private int scoreUnitTextSize = 20;
    private float score;

    private int innerCircleProgressSweepAngle;

    private float mValueOffset;

    private boolean isShowScore = true;

    public ScoreView(Context context) {
        this(context, null);
    }

    public ScoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        centerPoint = new Point();
        innerCircleRectF = new RectF();

        initAttrs(context, attrs);
        initPaint();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScoreView);

        enableAnim = typedArray.getBoolean(R.styleable.ScoreView_enableAnim, enableAnim);
        tooMany = typedArray.getBoolean(R.styleable.ScoreView_tooMany, tooMany);
        outerText = typedArray.getBoolean(R.styleable.ScoreView_outerText, outerText);

        outerCircleRadius = typedArray.getDimensionPixelSize(R.styleable.ScoreView_outer_circle_radius, outerCircleRadius);
        outerCircleStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.ScoreView_outer_circle_stroke_width, outerCircleStrokeWidth);
        outerCircleStrokeHeight = typedArray.getDimensionPixelSize(R.styleable.ScoreView_outer_circle_stroke_height, outerCircleStrokeHeight);
        outerCircleDotRadius = typedArray.getDimensionPixelSize(R.styleable.ScoreView_outer_circle_dot_radius, outerCircleDotRadius);
        outerCircleScoreTextColor = typedArray.getColor(R.styleable.ScoreView_outer_circle_score_text_color, outerCircleScoreTextColor);
        outerCircleScoreTextSize = typedArray.getDimensionPixelSize(R.styleable.ScoreView_outer_circle_score_text_size, outerCircleScoreTextSize);

        innerCircleBgColor = typedArray.getColor(R.styleable.ScoreView_inner_circle_bg_color, innerCircleBgColor);
        innerProgressCircleBgColor = typedArray.getColor(R.styleable.ScoreView_inner_circle_progress_bg_color, innerProgressCircleBgColor);
        innerCircleStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.ScoreView_inner_circle_stroke_width, innerCircleStrokeWidth);
        innerCircleRadius = typedArray.getDimensionPixelSize(R.styleable.ScoreView_inner_circle_radius, innerCircleRadius);
        innerProgressDotRadius = typedArray.getDimensionPixelSize(R.styleable.ScoreView_inner_circle_dot_radius, innerProgressDotRadius);

        scoreTextColor = typedArray.getColor(R.styleable.ScoreView_score_text_color, scoreTextColor);
        scoreTextSize = typedArray.getDimensionPixelSize(R.styleable.ScoreView_score_text_size, scoreTextSize);
        scoreUnitTextColor = typedArray.getColor(R.styleable.ScoreView_score_unit_text_color, scoreUnitTextColor);
        scoreUnitTextSize = typedArray.getDimensionPixelSize(R.styleable.ScoreView_score_unit_text_size, scoreUnitTextSize);


        typedArray.recycle();
    }

    private void initPaint() {
        outerLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outerLinePaint.setStyle(Paint.Style.STROKE);
        outerLinePaint.setStrokeWidth(outerCircleStrokeWidth);
        outerLinePaint.setColor(Color.BLACK);

        outDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outDotPaint.setColor(Color.BLACK);
        outDotPaint.setStyle(Paint.Style.FILL);

        outLineTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        outLineTextPaint.setColor(outerCircleScoreTextColor);
        outLineTextPaint.setTextSize(outerCircleScoreTextSize);
        outLineTextPaint.setTextAlign(Paint.Align.CENTER);

        innerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        innerCirclePaint.setStyle(Paint.Style.STROKE);
        innerCirclePaint.setStrokeWidth(innerCircleStrokeWidth);
        innerCirclePaint.setColor(innerCircleBgColor);

        innerProgressCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        innerProgressCirclePaint.setStyle(Paint.Style.STROKE);
        innerProgressCirclePaint.setStrokeWidth(innerCircleStrokeWidth);
        innerProgressCirclePaint.setColor(innerProgressCircleBgColor);
        innerProgressCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        scoreTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        scoreTextPaint.setTextSize(scoreTextSize);
        scoreTextPaint.setColor(scoreTextColor);
        scoreTextPaint.setFakeBoldText(true);
        scoreTextPaint.setTextAlign(Paint.Align.CENTER);

        scoreUnitTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        scoreUnitTextPaint.setTextSize(scoreUnitTextSize);
        scoreUnitTextPaint.setColor(scoreUnitTextColor);
        scoreUnitTextPaint.setFakeBoldText(true);
        scoreUnitTextPaint.setTextAlign(Paint.Align.CENTER);

        innerCircleProgressDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        innerCircleProgressDotPaint.setColor(Color.WHITE);
        innerCircleProgressDotPaint.setStyle(Paint.Style.FILL);
    }

    private int measureView(int measureSpec, int defaultSize) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("ScoreView","onSizeChanged  w=" + w + "  h=" + h + "   oldW=" + oldw + "   oldH=" + oldh);

        int realWidth = w - getPaddingLeft() - getPaddingRight();
        int realHeight = h - getPaddingTop() - getPaddingBottom();

        centerPoint.x = realWidth / 2 + getPaddingLeft();
        centerPoint.y = realHeight / 2 + getPaddingTop();

//        centerPoint.x = w / 2;
//        centerPoint.y = h / 2;

        if (innerCircleRadius < 0) {
            innerCircleRadius = (int) (getWidth() * 0.28f);
        }

        innerCircleRectF.left = -innerCircleRadius;
        innerCircleRectF.top = -innerCircleRadius;
        innerCircleRectF.right = +innerCircleRadius;
        innerCircleRectF.bottom = +innerCircleRadius;

        mValueOffset = getBaselineOffsetFromY(scoreTextPaint);
    }

    public void setProgressBgColor(int color) {
        this.innerProgressCircleBgColor = color;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG,"onMeasure");
        setMeasuredDimension(measureView(widthMeasureSpec, defaultWidth),
                measureView(heightMeasureSpec, defaultHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        canvas.translate(centerPoint.x, centerPoint.y);

        drawOuterCircle(canvas);
        drawInnerCircle(canvas);
        if (isShowScore) {
            drawScore(canvas);
        }
    }

    /**
     * 设置是否显示分数
     *
     * @param isShowScore
     */
    public void setIsShowScore(boolean isShowScore) {
        this.isShowScore = isShowScore;
        invalidate();
    }

    private void drawOuterCircle(Canvas canvas) {
        if (scoreArray == null || colorArray == null || scoreArray.length != colorArray.length + 1) {
            return;
        }

        if (outerCircleRadius <= 0) {
            outerCircleRadius = (int) (getWidth() * 0.36f);
        }

        int totalSweep = endAngle - startAngle;
        float totalScore = maxScore - minScore;
        int i = 0;
        for (float itemScore = minScore; itemScore <= maxScore; itemScore = itemScore + 0.5f) {
            double itemAngle = ((itemScore - minScore) * totalSweep * 1.0f / totalScore + startAngle) * Math.PI / 180;
//            MyLog.e("itemScore=" + itemScore + "   itemAngle=" + itemAngle + "   间隔=" + ((itemScore - minScore) * totalSweep * 1.0f / totalScore));
            int color = color(itemScore);
            if (line(itemScore)) {
                float startX = (float) (outerCircleRadius * Math.cos(itemAngle));
                float startY = (float) (outerCircleRadius * Math.sin(itemAngle));
                float stopX = (float) ((outerCircleRadius + outerCircleStrokeHeight) * Math.cos(itemAngle));
                float stopY = (float) ((outerCircleRadius + outerCircleStrokeHeight) * Math.sin(itemAngle));

                outerLinePaint.setColor(color);
//                MyLog.e("itemScore=" + itemScore + "   itemAngle=" + itemAngle + "   color=" + color);
                canvas.drawLine(startX, startY, stopX, stopY, outerLinePaint);

                if (outerText) {
                    float tx = 0.0f;
                    float ty = 0.0f;
                    if (scoreArray[i] == minScore || scoreArray[i] == maxScore) {
                        tx = (float) ((outerCircleRadius + outerCircleStrokeHeight + ConvertUtils.dp2px(16)) * Math.cos(itemAngle));
                        ty = (float) ((outerCircleRadius + outerCircleStrokeHeight + outLineTextPaint.getTextSize()) * Math.sin(itemAngle));
                    } else {
                        tx = (float) ((outerCircleRadius + outerCircleStrokeHeight + ConvertUtils.dp2px(12)) * Math.cos(itemAngle));
                        ty = (float) ((outerCircleRadius + outerCircleStrokeHeight + outLineTextPaint.getTextSize() / 2) * Math.sin(itemAngle));
                    }

                    int aa = (int) itemScore;
                    if (itemScore == aa) {
                        //小数部分是0的取整
                        canvas.drawText((int) itemScore + "", tx, ty, outLineTextPaint);
                    } else {
                        canvas.drawText(itemScore + "", tx, ty, outLineTextPaint);
                    }
                }
                i++;
            } else {
                if (tooMany) {
                    if (itemScore % 5 == 0) {
                        double cx = outerCircleRadius * Math.cos(itemAngle);
                        double cy = outerCircleRadius * Math.sin(itemAngle);
                        outDotPaint.setColor(color);
//                MyLog.e("a=" + a + " cx=" + cx + "  cy=" + cy + "  radius=" + innerCircleRectF.width());
                        canvas.drawCircle((float) cx, (float) cy, outerCircleDotRadius, outDotPaint);
                    }
                } else {
                    double cx = outerCircleRadius * Math.cos(itemAngle);
                    double cy = outerCircleRadius * Math.sin(itemAngle);
                    outDotPaint.setColor(color);
//                MyLog.e("a=" + a + " cx=" + cx + "  cy=" + cy + "  radius=" + innerCircleRectF.width());
                    canvas.drawCircle((float) cx, (float) cy, outerCircleDotRadius, outDotPaint);
                }
            }
        }
    }

    private void drawInnerCircle(Canvas canvas) {
        if (scoreArray == null || colorArray == null || scoreArray.length != colorArray.length + 1) {
            return;
        }

//        if (innerCircleRadius < 0) {
//            innerCircleRadius = (int) (getWidth() * 0.28f);
//        }
//        MyLog.e("innerCircleProgressSweepAngle=" + innerCircleProgressSweepAngle);
        // 内部圆环
        canvas.drawCircle(0, 0, innerCircleRadius, innerCirclePaint);

        drawInnerCircleProgress(canvas);

        if (innerCircleProgressSweepAngle > 0) {
            double a = 1.0 * (startAngle + innerCircleProgressSweepAngle) / 180 * Math.PI;
            double cx = innerCircleRectF.width() / 2 * Math.cos(a);
            double cy = innerCircleRectF.width() / 2 * Math.sin(a);
//            MyLog.e("a=" + a + " cx=" + cx + "  cy=" + cy + "  radius=" + innerCircleRectF.width());
            canvas.drawCircle((float) cx, (float) cy, innerProgressDotRadius, innerCircleProgressDotPaint);
        }
    }

    private void drawInnerCircleProgress(Canvas canvas) {
        // 内部进度圆弧
        if (innerProgressCircleBgColor == -1) {
            innerProgressCirclePaint.setColor(color(score));
        } else {
            innerProgressCirclePaint.setColor(innerProgressCircleBgColor);
        }

        canvas.drawArc(innerCircleRectF, startAngle, innerCircleProgressSweepAngle, false, innerProgressCirclePaint);
    }

    private void drawScore(Canvas canvas) {
        if (scoreArray == null || colorArray == null || scoreArray.length != colorArray.length + 1) {
            return;
        }

        if (innerProgressCircleBgColor == -1) {
            scoreTextPaint.setColor(color(score));
        } else {
            scoreTextPaint.setColor(innerProgressCircleBgColor);
        }

//        String str = Utils.doubleToString(score);
        String str = StringUtil.formatNumber(score, 2);//保留两位小数
        Log.i(getClass().getSimpleName(), "score:" + str);
//        scoreTextPaint.setColor(color(score));
        canvas.drawText(str, 0, mValueOffset, scoreTextPaint);
//        canvas.drawText("SCORE", 0, mValueOffset + measureHeight(scoreTextPaint, str), scoreUnitTextPaint);
        canvas.drawText("SCORE", 0, mValueOffset + measureHeight(scoreUnitTextPaint, "SCORE") + ConvertUtils.dp2px(10), scoreUnitTextPaint);
    }

    public int measureHeight(Paint paint, String str) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        int w = rect.width();
        int h = rect.height();
        return h;
    }

    public void setOuterText(boolean outerText) {
        this.outerText = outerText;
        invalidate();
    }

    public int measureHeight(TextPaint paint) {
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        int textHeight = ~fm.top - (~fm.top - ~fm.ascent) - (fm.bottom - fm.descent);
        return textHeight;
    }

    public int measureWidth(String text, Paint paint) {
        return (int) paint.measureText(text);
    }

    public void setTooMany(boolean tooMany) {
        this.tooMany = tooMany;
    }

    public void setArray(float[] scoreArray, int[] colorArray, float score) {
        array(scoreArray, colorArray, score);
        invalidate();
        requestLayout();
    }

    public void array(float[] scoreArray, int[] colorArray, float score) {
        if (scoreArray == null || colorArray == null || scoreArray.length != colorArray.length + 1) {
            throw new IllegalArgumentException("分数段和颜色段不对应");
        }
        this.scoreArray = scoreArray;
        this.colorArray = colorArray;
        this.score = score;
        minScore = scoreArray[0];
        maxScore = scoreArray[scoreArray.length - 1];
    }

    public void setScore(float score2) {
        if (enableAnim) {
            setWithAnim(score2);
        } else {
            int totalSweep = endAngle - startAngle;
            this.score = score2;
            updateScoreText(totalSweep);
        }
    }

    private void setWithAnim(float score2) {
        int totalSweep = endAngle - startAngle;

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(minScore, score2);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                score = (float) animation.getAnimatedValue();
                updateScoreText(totalSweep);
            }
        });
        valueAnimator.start();
    }

    private void updateScoreText(int totalSweep) {
//        MyLog.e("updateScoreText  score=" + score + "    aa==" + (score - minScore) / (maxScore - minScore));
//        MyLog.e("updateScoreText  minScore=" + minScore + "    maxScore==" + maxScore);
        if (score < minScore) {
            innerCircleProgressSweepAngle = 0;
        } else if (score > maxScore) {
            innerCircleProgressSweepAngle = totalSweep;
        } else {
            innerCircleProgressSweepAngle = (int) ((score - minScore) / (maxScore - minScore) * totalSweep);
        }
//        MyLog.e("updateScoreText  innerCircleProgressSweepAngle=" + innerCircleProgressSweepAngle);
        invalidate();
    }

    public int getTextColor() {
        return color(score);
    }

    private int color(float itemScore) {
        int color = colorArray[0];
        for (int index = 0; index < scoreArray.length; index++) {
            if (itemScore < scoreArray[index] && itemScore >= scoreArray[0]) {
                int colorIndex = index - 1;
                color = colorArray[colorIndex];
                break;
            }
            if (itemScore == scoreArray[scoreArray.length - 1]) {
                color = colorArray[colorArray.length - 1];
                break;
            }
        }
        return color;
    }

    private boolean line(float itemScore) {
        if (scoreArray == null) {
            return false;
        }
        for (float score : scoreArray) {
            if (score == itemScore) {
                return true;
            }
        }
        return false;
    }

    private float getBaselineOffsetFromY(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return ((Math.abs(fontMetrics.ascent) - fontMetrics.descent)) / 2;
    }

}
