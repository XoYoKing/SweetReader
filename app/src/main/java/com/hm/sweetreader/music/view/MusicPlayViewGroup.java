package com.hm.sweetreader.music.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

/**
 * project：SweetReader
 * author： FLY
 * date：   2016/7/6
 * time：   10:58
 * purpose：
 */
public class MusicPlayViewGroup extends RelativeLayout {

    private Context context;
    private MusicPlayView playView;
    private ObjectAnimator anim;
    //是否是继续播放
//    private boolean isPause = false;
//    private float offerX = 0;

    public MusicPlayViewGroup(Context context) {
        this(context, null);
    }

    public MusicPlayViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public MusicPlayViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        playView = new MusicPlayView(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(CENTER_IN_PARENT);
        addView(playView, layoutParams);
    }

    /**
     * 设置圆心图片
     *
     * @param background
     */
    public void setBackground(Bitmap background) {
        playView.setBackground(background);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void startAnimation() {
        anim = ObjectAnimator.ofFloat(playView, "rotationZ", 0, 360.0F)//
                .setDuration(10000);//
        anim.setRepeatCount(Animation.INFINITE);//Animation.INFINITE
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                playView.setRotation(cVal);
            }
        });

    }

    public void stopAnimation() {
//        offerX = anim.getCurrentPlayTime();
        if (anim != null) {
            anim.cancel();
            anim = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void pauseAnimation() {
//        offerX = anim.getCurrentPlayTime();
        if (anim != null)
            anim.pause();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void reStartAnimation() {

        if (anim != null && anim.isPaused()) {
            anim.resume();
            return;
        }
        startAnimation();
    }
}
