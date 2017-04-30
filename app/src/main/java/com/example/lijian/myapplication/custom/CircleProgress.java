package com.example.lijian.myapplication.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.SweepGradient;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.example.lijian.myapplication.R;

/**
 * Created by LIJIAN on 2017/4/22.
 */

public class CircleProgress extends View {
    private int mDefaultSize; //默认宽高
    private int mBackgroundColor;
    private int mForegroundColor;
    private Paint mPaint;
    private SweepGradient mSweepGradient;
    private int[] mColors = {Color.GREEN, Color.BLUE, Color.MAGENTA};
    private int mOuterRadius;
    private int mCenterX;
    private int mCenterY;
    private float mSin_1; //sin1度对应的弧度值
    private float mDotRadius;
    private Xfermode mXfermode_clear = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private int mPercent;
    private Bitmap mBitmap;
    private Canvas mCanvas;

    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Log.e("TEST", "CircleProgress()..........................");
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgress, defStyleAttr, defStyleRes);
        mBackgroundColor = typedArray.getColor(R.styleable.CircleProgress_backgroundColor, Color.GRAY);
        mForegroundColor = typedArray.getColor(R.styleable.CircleProgress_foregroundColor, Color.GREEN);
        typedArray.recycle();
        mDefaultSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, context.getResources().getDisplayMetrics());
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSin_1 = (float) Math.sin(Math.toRadians(1));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("TEST", "onSizeChanged()..........................");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("TEST", "onMeasure()..........................");

        //对Wrap_content的情况做处理（系统默认只有match_parent的效果）
        setMeasuredDimension(measureValue(widthMeasureSpec), measureValue(heightMeasureSpec));

        if (getWidth() > 0) {
            mOuterRadius = Math.min(getWidth(), getHeight()) / 2;
            mCenterX = getWidth() / 2;
            mCenterY = getHeight() / 2;
            mDotRadius = mSin_1 * mOuterRadius / (1 + mSin_1);

            mBitmap = Bitmap.createBitmap(mOuterRadius * 2, mOuterRadius * 2, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            mSweepGradient = new SweepGradient(mOuterRadius, mOuterRadius, mColors, null);
            Matrix matrix = new Matrix();
            matrix.postRotate(-90, mOuterRadius, mOuterRadius);
            mSweepGradient.setLocalMatrix(matrix);
        }
    }

    private int measureValue(int measureSpec) {
        int specSize = MeasureSpec.getSize(measureSpec);
        int specMode = MeasureSpec.getMode(measureSpec);
        int measureSize;
        switch (specMode) {
            case MeasureSpec.EXACTLY:
                measureSize = specSize;
                break;
            case MeasureSpec.AT_MOST:
                measureSize = Math.min(specSize, mDefaultSize);
                break;
            default:
                measureSize = mDefaultSize;
        }
        return measureSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap != null) {
            drawOutCircle(mCanvas);
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    /**
     * 绘制最外层圆
     */
    private void drawOutCircle(Canvas canvas) {
        Log.e("TEST", "drawOutCircle()..........................");
        //绘制之前清除上次绘制
        mPaint.setXfermode(mXfermode_clear);
        canvas.drawPaint(mPaint);
        mPaint.setXfermode(null);
        mPaint.setColor(mForegroundColor);
        //绘制已完成部分
        int count = 0;
        while (count++ < mPercent) {
            canvas.drawCircle(mCenterX, mDotRadius, mDotRadius, mPaint);
            canvas.rotate(3.6f, mOuterRadius, mOuterRadius);
            Log.e("TEST", "count0 :  " + count);
        }
        //绘制未完成部分圆圆点
        mPaint.setColor(mBackgroundColor);
        count--;
        while (count++ < 100) {
            Log.e("TEST", "count2 :  " + count);
//            canvas.drawCircle(mCenterX, mCenterY - mOuterRadius + mDotRadius, mDotRadius, mPaint);
            canvas.drawCircle(mCenterX, mDotRadius, mDotRadius, mPaint);
            canvas.rotate(3.6f, mOuterRadius, mOuterRadius);
        }
    }

    public int getPercent() {
        return mPercent;
    }

    public void setPercent(int percent) {
        mPercent = percent;
        postInvalidate();
    }
}
