package com.hm.sweetreader.music.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.hm.sweetreader.R;
import com.hm.sweetreader.ScreenUtils;

/**
 * project：SweetReader
 * author： FLY
 * date：   2016/7/5
 * time：   16:48
 * purpose：实现音乐播放主界面圆盘
 */
public class MusicPlayView extends View {

    private int width, height;
    private Paint paintWai, paintNei;
    private Context context;
    private Bitmap bitmap;

//    private RotateAnimation animation;

    public MusicPlayView(Context context) {
//        super(context);
        this(context, null);

    }

    public MusicPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MusicPlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heighMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heighSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize - getPaddingRight() - getPaddingLeft();
        } else {
            width = getWidth();
        }
        if (heighMode == MeasureSpec.EXACTLY) {
            height = heighSize;
        } else {
            height = getHeight();
        }
        //保证是正方形区域
        if (width > height) {
            width = height;
        } else {
            height = width;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPaint();
        drawCircle(canvas);
    }

    private void initPaint() {
        paintWai = new Paint();
//        paintWai.setColor(Color.BLACK);
        paintWai.setStyle(Paint.Style.STROKE);
        paintWai.setAntiAlias(true);
        paintWai.setStrokeWidth(10);

        paintNei = new Paint();
//        paintNei.setColor(Color.BLACK);
        paintNei.setStyle(Paint.Style.STROKE);
        paintNei.setAntiAlias(true);
        paintNei.setStrokeWidth(10);
    }

    public void setBackground(Bitmap background) {
        this.bitmap = background;
    }

    private void drawCircle(Canvas canvas) {
        int center = getWidth() / 2;
        int innerCircle = ScreenUtils.dip2px(context, 75); //设置内圆半径
//        int ringWidth = ScreenUtils.dip2px(context, 5); //设置圆环宽度
        int ringWidth = width / 2 - innerCircle - 50;
        //绘制内圆
//        paintWai.setARGB(155, 167, 190, 206);
//        paintWai.setStrokeWidth(2);
//        canvas.drawCircle(center,center, innerCircle, paintWai);

        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        }
        //画图片，就是贴图

//        Rect rect=new Rect(0,0,144,144);
        RectF rectF = new RectF(center-innerCircle, center-innerCircle, center+innerCircle, center+innerCircle);
        canvas.drawBitmap(bitmap, null, rectF, paintNei);

        //绘制圆环
        paintWai.setARGB(255, 212, 225, 233);
        paintWai.setStrokeWidth(ringWidth);
        canvas.drawCircle(center, center, innerCircle + 1 + ringWidth / 2, paintWai);

        //绘制外圆
//        paintNei.setARGB(155, 167, 190, 206);
//        paintNei.setStrokeWidth(2);
//        canvas.drawCircle(center,center, innerCircle+ringWidth, paintNei);


    }

//    /**
//     * 开始动画 设置动画时长 单位 毫秒
//     */
//    public void initAnimation() {
//        animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
//                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        animation.setFillBefore(true);
//        animation.setDuration(Contents.MUSIC_TIEM);//设置动画持续时间
//        animation.setInterpolator(new LinearInterpolator());//不停顿
//        /** 常用方法 */
//        animation.setRepeatCount(Animation.INFINITE);//设置重复次数
//        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
//        //animation.setStartOffset(long startOffset);//执行前的等待时间
//
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                Log.e("123", "on start");
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                Log.e("123", "on end");
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//                Log.e("123", "on repeat");
//            }
//        });
////        playView.setAnimation(animation);
//        /** 开始动画 */
////        animation.startNow();
//    }
}
