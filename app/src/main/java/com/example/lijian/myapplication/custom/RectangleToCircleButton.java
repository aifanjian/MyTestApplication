package com.example.lijian.myapplication.custom;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.example.lijian.myapplication.R;

import static android.R.attr.value;

/**
 * Created by LIJIAN on 2017/5/6.
 */

public class RectangleToCircleButton extends View {
    private static final int DURATION_ANIMATION = 1000;
    private int mDefaultWidth;
    private int mDefaultHeight;
    private int mWidth;
    private int mHeight;
    private int mBackground;
    private int mTextColor;
    private int mTextSize;
    private String mText = "";
    private Paint mPaint;
    private RectF mRect;
    private DashPathEffect mPathEffect;
    private Paint mDashPaint;
    private Path mDashPath;
    private PathMeasure mDashPathMeasure;
    private float[] mIntervals;
    private Paint mTextPaint;

    private ValueAnimator mAnimator_corner;
    private ValueAnimator mAnimator_width;
    private ValueAnimator mAnimator_dash;
    private AnimatorSet mAnimatorSet;
    private AnimatorSet mAnimatorSet_reverse;

    private int mMaxMoveDistance;//由长方形转变为圆形时，两端向中间靠拢的最大距离
    private int mCurrentMoveDistance;
    private float mCurrentCorner;//由长方形转变为圆角矩形时的圆角角度值

    private int mTextBaseline;

    public RectangleToCircleButton(Context context) {
        this(context, null);
    }

    public RectangleToCircleButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.customViewStyle);
    }

    public RectangleToCircleButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.style.RectangleToCircleButtonStyle18);
    }

    public RectangleToCircleButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mDefaultHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
        mDefaultWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics());
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RectangleToCircleButton, defStyleAttr, defStyleRes);
        mBackground = typedArray.getColor(R.styleable.RectangleToCircleButton_backgroundColor, context.getResources().getColor(R.color.colorPrimary));
        mTextColor = typedArray.getColor(R.styleable.RectangleToCircleButton_android_textColor, context.getResources().getColor(R.color.colorAccent));
        mTextSize = (int) typedArray.getDimension(R.styleable.RectangleToCircleButton_android_textSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics()));
        Log.e("TEST", "TextSize:  " + mTextSize);
        mText = typedArray.getString(R.styleable.RectangleToCircleButton_android_text);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mBackground);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mDashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDashPaint.setColor(mTextColor);
        mDashPaint.setStyle(Paint.Style.STROKE);
        mDashPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
        mRect = new RectF();
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet_reverse = new AnimatorSet();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        Log.e("TEST", "onMeasure.........");
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

    /**
     * intervals是一个float数组，且其长度必须是偶数且>=2，指定了多少长度的实线之后再画多少长度的空白。例如
     * new DashPathEffect(new float[] { 8, 10, 8, 10}, 0);
     * 指定了绘制8px的实线,再绘制10px的透明,再绘制8px的实线,再绘制10px的透明,
     * 依次重复来绘制达到path对象的长度。
     * <p>
     * phase参数指定了绘制的虚线相对了起始地址（Path起点）的取余偏移（对路径总长度）。
     * <p>
     * new DashPathEffect(new float[] { 8, 10, 8, 10}, 0);
     * 这时偏移为0，先绘制实线，再绘制透明。
     * <p>
     * new DashPathEffect(new float[] { 8, 10, 8, 10}, 8);
     * 这时偏移为8，先绘制了透明，再绘制了实线.(实线被偏移过去了)
     * 可是通过不断地递增/递减来改变phase的值，达到一个路径自身不断循环移动的动画效果。
     *
     * @param w
     * @param h
     * @param oldW
     * @param oldH
     */

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        Log.e("TEST", "onSizeChanged.........");
        mWidth = getWidth();
        mHeight = getHeight();
        mMaxMoveDistance = (mWidth - mHeight) >> 1;

        mDashPath = new Path();
        mDashPath.moveTo(mWidth / 2 - mHeight / 4, mHeight / 2);
        mDashPath.lineTo(mWidth / 2, mHeight / 3 * 2);
        mDashPath.lineTo(mWidth / 2 + mHeight / 4, mHeight / 3);
        mDashPathMeasure = new PathMeasure(mDashPath, true);
        mIntervals = new float[]{mDashPathMeasure.getLength(), mDashPathMeasure.getLength()};
//        mDashPaint.setPathEffect(new DashPathEffect(new float[]{mDashPathMeasure.getLength(), mDashPathMeasure.getLength()}, 0));
//        initAnimator();
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextBaseline = (int) (mHeight / 2 + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);
    }

    private int mStartCorner = 0;
    private int mEndCorner = mHeight >> 1;
    private int mStartMoveDistance = 0;
    private int mEndMoveDistance = mMaxMoveDistance;
    private int mStartDash = 1;
    private int mEndDash = 0;
    private boolean mIsDrawDash;

    private void initAnimator() {
//        mAnimatorSet.setDuration(DURATION_ANIMATION);
        mAnimator_corner = ValueAnimator.ofInt(mStartCorner, mEndCorner);
        mAnimator_corner.setDuration(DURATION_ANIMATION);
        mAnimator_corner.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentCorner = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mAnimator_width = ValueAnimator.ofInt(mStartMoveDistance, mEndMoveDistance);
        mAnimator_width.setDuration(DURATION_ANIMATION);
        mAnimator_width.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentMoveDistance = (int) animation.getAnimatedValue();
                //alpha: 0-255
                mTextPaint.setAlpha(255 - (mCurrentMoveDistance * 255 / mMaxMoveDistance));
                postInvalidate();
            }
        });

        mAnimator_dash = ValueAnimator.ofFloat(mStartDash, mEndDash);
        mAnimator_dash.setDuration(DURATION_ANIMATION);
        mAnimator_dash.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mIsDrawDash = true;
                float value = (float) animation.getAnimatedValue();
                mPathEffect = new DashPathEffect(mIntervals, mDashPathMeasure.getLength() * value);
                mDashPaint.setPathEffect(mPathEffect);
                postInvalidate();
            }
        });
        mAnimatorSet.play(mAnimator_width).with(mAnimator_corner).before(mAnimator_dash);
        mAnimatorSet_reverse.play(mAnimator_width).with(mAnimator_corner).after(mAnimator_dash);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Log.e("TEST", "onDraw.................");
        drawRect(canvas);
        canvas.drawText(mText, mWidth / 2, mTextBaseline, mTextPaint);
        if (mIsDrawDash) {
            canvas.drawPath(mDashPath, mDashPaint);
        }
    }


    private void drawRect(Canvas canvas) {
        mRect.left = mCurrentMoveDistance;
        mRect.top = 0;
        mRect.right = mWidth - mCurrentMoveDistance;
        mRect.bottom = mHeight;
        canvas.drawRoundRect(mRect, mCurrentCorner, mCurrentCorner, mPaint);

//        Log.e("TEST", "drawRect:  left  " + mRect.left + "  right:  " + mRect.right + " corner: " + mCurrentCorner);
    }

    private boolean mIsFirst;

    public void startAnimation() {
        mIsFirst = !mIsFirst;
        if (mIsFirst == true) {
            mStartCorner = 0;
            mEndCorner = mHeight >> 1;
            mStartMoveDistance = 0;
            mEndMoveDistance = mMaxMoveDistance;
            mStartDash = 1;
            mEndDash = 0;
        } else {
            mStartMoveDistance = mMaxMoveDistance;
            mEndMoveDistance = 0;
            mStartCorner = mHeight >> 1;
            mEndCorner = 0;
            mStartDash = 0;
            mEndDash = 1;
        }
        initAnimator();
        mAnimatorSet.resume();
        if (mIsFirst) {
            mAnimatorSet.start();
        } else {
            mAnimatorSet_reverse.start();
        }
    }
}
