package cn.xymind.healthdetection.synthesis.androidsdksamples;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;


public class ItemScoreView extends LinearLayout{

    private TextView tvTitle;
    private ScoreView vScore;

    public ItemScoreView(Context context) {
        this(context, null);
    }

    public ItemScoreView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemScoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_scl_total_item_score, this);
        setOrientation(VERTICAL);

        tvTitle = findViewById(R.id.tv_title);
        vScore = findViewById(R.id.v_score);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setScore(float score) {
        vScore.setScore(score);
    }

    public void setTooMany(boolean tooMany) {
        vScore.setTooMany(tooMany);
    }

    public void setScore(float[] scoreArray, int[] colorArray, float score) {
        vScore.setArray(scoreArray, colorArray, score);
    }

    public void setProgressBgColor(int color) {
        vScore.setProgressBgColor(color);
    }

    public int getTextColor() {
        return vScore.getTextColor();
    }
}
