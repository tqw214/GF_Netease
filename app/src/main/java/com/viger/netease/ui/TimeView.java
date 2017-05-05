package com.viger.netease.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/4/7.
 */

public class TimeView extends View {

    private TextPaint mTextPaint;
    private Paint circleP;
    private Paint arcP;
    private String text = "跳过";
    private int padding = 14;
    private int mViewWidth;
    private int mArcWidth = 6;
    private int inner;
    private int textWidth;
    private  RectF rectF;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private onFinishListener mListener;
    public interface onFinishListener {
        void onfinish();
        void onInto();
    }

    public void setOnFinishListener(onFinishListener listener) {
        this.mListener = listener;
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(25);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(Color.WHITE);
        textWidth = (int) mTextPaint.measureText(text);


        //内圆直径
        inner = textWidth + padding * 2;
        //外圆的直径
        mViewWidth = inner + mArcWidth * 2;

        circleP = new Paint();
        circleP.setAntiAlias(true);
        circleP.setDither(true);
        circleP.setColor(Color.BLUE);
        circleP.setStyle(Paint.Style.FILL);

        arcP = new Paint();
        arcP.setAntiAlias(true);
        arcP.setDither(true);
        arcP.setColor(Color.RED);
        arcP.setStyle(Paint.Style.STROKE);
        arcP.setStrokeWidth(mArcWidth);

        timer = new Timer();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mViewWidth, mViewWidth);
    }

    private int mDegrees = 0;
    private int space = 50;     //刷新频率
    private int hd;
    private Timer timer;

    public void setProgress(int time) {
        int total = time/space;    //总刷新的次数
        hd = 360 / total;  //每次刷新的弧度
        //timer.schedule(task, 1000, space);
        timer.schedule(task, 0, space);
    }

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    mDegrees += hd;
                    Log.i("tag",mDegrees + "");
                    invalidate();
                    if(mDegrees == 360) {
                        if(mListener != null) mListener.onfinish();
                        stop();
                    }
                }
            });
        }
    };

    public void stop() {
        if(task != null) {
            task.cancel();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawColor(Color.RED);
        canvas.drawCircle(mViewWidth/2, mViewWidth/2, inner/2, circleP);

        rectF = new RectF(mArcWidth/2,mArcWidth/2,mViewWidth-mArcWidth/2,mViewWidth-mArcWidth/2);
        canvas.save();
        canvas.rotate(-90, mViewWidth/2, mViewWidth/2);
        canvas.drawArc(rectF,0,mDegrees,false,arcP);
        canvas.restore();

        canvas.drawText(text, padding + mArcWidth, mViewWidth/2 - ((mTextPaint.descent()+mTextPaint.ascent())/2), mTextPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setAlpha(0.5f);
                break;
            case MotionEvent.ACTION_UP:
                setAlpha(1.0f);
                if(mListener != null) mListener.onInto();
                stop();
                break;
        }
        return true;
    }
}
