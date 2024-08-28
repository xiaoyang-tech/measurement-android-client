package cn.xymind.healthdetection.synthesis.androidsdksamples.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import cn.xymind.healthdetection.synthesis.androidsdksamples.R;


public class SegmentScoreMirrorView extends LinearLayout {

    private TextView tvValue;
    private TextView tvTitle;
    private IndicatorMirrorView indicatorView;
    private ImageView ivIcon;
    public SegmentScoreMirrorView(Context context) {
        this(context, null);
    }

    public SegmentScoreMirrorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SegmentScoreMirrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_score_item, this);
        tvValue = findViewById(R.id.itemValueTv);
        tvTitle = findViewById(R.id.itemTitleTv);
        ivIcon = findViewById(R.id.ivIcon);
        indicatorView = findViewById(R.id.Idv);
    }

    public void setTvValue(String score) {
        tvValue.setText(score);
    }


    public void setTvTitle(String title) {
        tvTitle.setText(title);
    }

    public IndicatorMirrorView getIndicatorView() {
        return indicatorView;
    }

    public void setIndicatorView(IndicatorMirrorView indicatorView) {
        this.indicatorView = indicatorView;
    }

    public void setIvIcon(int resource){
        ivIcon.setImageResource(resource);
    }
}
