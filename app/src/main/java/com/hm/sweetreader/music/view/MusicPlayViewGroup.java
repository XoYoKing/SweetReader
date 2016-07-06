package com.hm.sweetreader.music.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
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
    private boolean isFirst = true;
    private float offerX = 0;

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

//    public void initAnimation() {
//
//        anim = ObjectAnimator.ofFloat(playView, "rotationZ", 0.0F, 360.0F)//
//                .setDuration(10000);//
//        anim.setRepeatCount(0);//Animation.INFINITE
//        anim.setInterpolator(new LinearInterpolator());
//    }

    public void startAnimation() {
//        playView.setRotation();
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
        offerX =anim.getCurrentPlayTime();
        anim.cancel();
    }
    public void reStartAnimation(){
        if (anim==null){
            startAnimation();
            return;
        }
        anim = ObjectAnimator.ofFloat(playView, "rotationZ", offerX/10000*360, 360.0F)//
                .setDuration((int)(10000-offerX));//
        anim.setRepeatCount(0);//Animation.INFINITE
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                playView.setRotation(cVal);
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animation.cancel();
                startAnimation();
            }
        });
    }

//    public void initAnimation() {
//        playView.initAnimation();
//
//    }

//    public void startAnimation() {
//        if (playView != null) {
//            playView.startAnimation();
//        }
//    }

//    public void stopAnimation() {
//        if (playView != null) {
//            playView.stopAnimation();
//        }
//    }

}
