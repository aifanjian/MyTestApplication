package com.example.lijian.myapplication.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.example.lijian.myapplication.R;

import static android.R.attr.centerX;

/**
 * 自定义进度条
 * 注意事项
 * 1、画布高度为圆的直径，画布宽度为进度条宽度+圆的直径，（因为因为圆在左右两端时要多占用半个圆的宽度）
 * 2、进度条起始位置 左：圆半径值，右：进度条+圆半径值，上：（画布高-进度条高）/2 下：进度条高度
 * 3、圆的中心点为主进度条的终点位置
 * 4、进度文字的起始坐标点计算问题。
 */

public class WithNumberProgressBar extends View {
    private static final String TAG = WithNumberProgressBar.class.getSimpleName();
    private int mPrimaryColor;
    private int mSecondColor;
    private int mCircleRadius;
    private int mFondSize;
    private int mProgressBarHeight;
    private int mProgressBarWidth;
    private Paint mPaint;
    private int mProgress;
    private int mDefaultWidth;
    private int mDefaultHeight;
    private int mHeight;
    private int mWidth;
    private int mTextWidth;
    private int mTextHeight;
    private int mTextBaseLineY;
    private Paint.FontMetrics mFontMetrics;

    public WithNumberProgressBar(Context context) {
        this(context, null);
    }

    public WithNumberProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WithNumberProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public WithNumberProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WithNumberProgressBar, defStyleAttr, defStyleRes);
        mPrimaryColor = typedArray.getColor(R.styleable.WithNumberProgressBar_primaryProgressColor,
                context.getResources().getColor(R.color.colorAccent));
        mSecondColor = typedArray.getColor(R.styleable.WithNumberProgressBar_secondProgressColor,
                context.getResources().getColor(R.color.colorPrimary));

        mFondSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, context.getResources().getDisplayMetrics());
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mFondSize);
        mFontMetrics = mPaint.getFontMetrics();
        mTextHeight = (int) (mFontMetrics.bottom - mFontMetrics.top);
        mTextWidth = (int) mPaint.measureText("100");
        mProgressBarHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());
        mCircleRadius = Math.max(mTextHeight, mTextWidth) / 2 + 2;
        mProgressBarWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources().getDisplayMetrics());
        mDefaultWidth = mProgressBarWidth + mCircleRadius * 2;
        mDefaultHeight = mCircleRadius * 2;
    }

    public void setProgress(int progress) {
        mProgress = progress;
        postInvalidate();
    }

    public int getProgress() {
        return mProgress;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG, "onMeasure..............");
        setMeasuredDimension(measureSize(widthMeasureSpec, mDefaultWidth), measureSize(heightMeasureSpec, mDefaultHeight));
    }

    private int measureSize(int measureSpec, int defaultSize) {
        int measureSize = MeasureSpec.getSize(measureSpec);
        int measureMode = MeasureSpec.getMode(measureSpec);
        switch (measureMode) {
            case MeasureSpec.EXACTLY:
                return measureSize;
            case MeasureSpec.AT_MOST:
                return Math.min(measureSize, defaultSize);
            default:
                return defaultSize;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(TAG, "onSizeChanged............getWidth:  " + getWidth() + "getHeight:  " + getHeight());
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        Log.e(TAG, "getMeasuredWidth:  " + getMeasuredWidth() + "  getMeasureHeight: " + getMeasuredHeight());
        mProgressBarWidth = mWidth - mCircleRadius * 2;
        mTextBaseLineY = (int) (mHeight / 2 + mTextHeight / 2 - mFontMetrics.bottom);
    }

    private Xfermode mXfermode_clear = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

    /**
     * 绘制步骤：
     * 1、绘制进度部分
     * 2、绘制背景部分
     * 3、绘制小圆圈
     * 4、绘制进度文字
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 每次重绘之前要清空原来的
//        mPaint.setXfermode(mXfermode_clear);
//        canvas.drawPaint(mPaint);
//        mPaint.setXfermode(null);
        Log.e(TAG, "onDraw...........progress;  " + mProgress);
        mPaint.setColor(mPrimaryColor);
        float mProgressRight = mCircleRadius + mProgressBarWidth * mProgress / 100;
        float mProgressTop = (mHeight - mProgressBarHeight) / 2;
        //绘制主进度
        canvas.drawRoundRect(
                mCircleRadius,
                mProgressTop,
                mProgressRight,
                mProgressTop + mProgressBarHeight,
                mProgressBarHeight / 2,
                mProgressBarHeight / 2,
                mPaint);
        //绘制次进度
        mPaint.setColor(mSecondColor);
        canvas.drawRoundRect(
                mProgressRight,
                mProgressTop,
                mCircleRadius + mProgressBarWidth,
                mProgressTop + mProgressBarHeight,
                mProgressBarHeight / 2,
                mProgressBarHeight / 2,
                mPaint);
        //绘制圆点
        mPaint.setColor(mPrimaryColor);
        canvas.drawCircle(mProgressRight, mHeight / 2, mCircleRadius, mPaint);
        //绘制进度文字
        mPaint.setColor(mSecondColor);
        //让进度文字居中处理
        canvas.drawText(String.valueOf(mProgress), mProgressRight, mTextBaseLineY, mPaint);
    }
}

