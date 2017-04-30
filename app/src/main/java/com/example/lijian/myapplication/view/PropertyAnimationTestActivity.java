package com.example.lijian.myapplication.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.example.lijian.myapplication.R;
import com.example.lijian.myapplication.custom.AddWidthPropertyButton;
import com.example.lijian.myapplication.custom.PropertyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LIJIAN on 2017/3/22.
 */

public class PropertyAnimationTestActivity extends AppCompatActivity implements View.OnClickListener,GestureDetector.OnGestureListener {
    Button mButton_number, mButton_color, mButton_together, mButton_translation, mButton_background, mButton_ball;
    AddWidthPropertyButton mButton_width;
    PropertyTextView mTextView;
    ImageView mIv_ball;
    @BindView(R.id.iv_top)
    ImageView ivTop;
    @BindView(R.id.iv_bottom)
    ImageView ivBottom;

    private GestureDetector mGstureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_animation_test);
        ButterKnife.bind(this);
        init();
    }

    int mStartColor, mEndColor;

    private void init() {
        mButton_number = (Button) findViewById(R.id.button1);
        mButton_color = (Button) findViewById(R.id.button2);
        mButton_together = (Button) findViewById(R.id.button3);
        mButton_translation = (Button) findViewById(R.id.button4);
        mButton_background = (Button) findViewById(R.id.button5);
        mButton_ball = (Button) findViewById(R.id.button6);
        mButton_width = (AddWidthPropertyButton) findViewById(R.id.button7);
        mTextView = (PropertyTextView) findViewById(R.id.textView);
        mIv_ball = (ImageView) findViewById(R.id.iv_ball);

        mGstureDetector = new GestureDetector(this,this);
        mStartColor = getResources().getColor(R.color.colorPrimary);
        mEndColor = getResources().getColor(R.color.colorAccent);
//        final ValueAnimator animator = getNumberAnimator();
//        final ObjectAnimator animator1= (ObjectAnimator) AnimatorInflater.loadAnimator(this,R.animator.animator);
//        animator1.setTarget(mTextView);
        mButton_number.setOnClickListener(this);
        mButton_color.setOnClickListener(this);
        mButton_together.setOnClickListener(this);
        mButton_translation.setOnClickListener(this);
        mButton_background.setOnClickListener(this);
        mButton_ball.setOnClickListener(this);
        mButton_width.setOnClickListener(this);
        mButton_width.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    @NonNull
    private AnimatorSet getNumberColorSet() {
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(getNumberAnimator(), getColorAnimator());
        animatorSet.setDuration(5000);
        animatorSet.setInterpolator(new LinearInterpolator());
        return animatorSet;
    }

    @NonNull
    private ValueAnimator getColorAnimator() {
        ValueAnimator colorAnimator = ValueAnimator.ofArgb(mStartColor, mEndColor);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = (int) animation.getAnimatedValue();
                mTextView.setTextColor(color);
            }
        });
        return colorAnimator;
    }

    @NonNull
    private ValueAnimator getNumberAnimator() {
        float startValue = 0f;
        float endValue = 187654.32f;
        final ValueAnimator animator = ValueAnimator.ofFloat(startValue, endValue);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mTextView.setText(String.format("%.2f", value));
            }
        });
        animator.setDuration(5000);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    private Animator getTranslationAnimator() {
        float start = mButton_number.getTranslationX();
        float end = getWindow().getDecorView().getWidth();
        ObjectAnimator leftToRight = ObjectAnimator.ofFloat(mButton_translation, "translationX", start, end, -end, start);
        leftToRight.setDuration(4000);
        return leftToRight;
    }

    private boolean isFirst = false;

    private Animator getBackgroundAnimator() {
        int startColor = mStartColor;
        int endColor = mEndColor;
        if ((isFirst = !isFirst) != true) {
            startColor = mEndColor;
            endColor = mStartColor;
        }
        ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofArgb(mButton_background, "backgroundColor", startColor, endColor);
        backgroundColorAnimator.setDuration(2000);
        backgroundColorAnimator.setEvaluator(new ArgbEvaluator());
        return backgroundColorAnimator;
    }

    private AnimatorSet mAnimatorSet = new AnimatorSet();

    private void getWidthAnimator() {
//        mButton_width.setPivotX(mButton_width.getWidth()/2);
//        mButton_width.setPivotY(mButton_width.getHeight()/2);
//        mButton_width.invalidate();
        mButton_width.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mButton_width.setVisibility(View.INVISIBLE);
        mButton_width.animate().x(mButton_width.getWidth() / 2).setDuration(0);
        mButton_width.setVisibility(View.VISIBLE);
        ObjectAnimator widthAnimator1 = ObjectAnimator.ofObject(mButton_width, "width", new TypeEvaluator() {
            @Override
            public Object evaluate(float fraction, Object startValue, Object endValue) {
                int startWidth = (int) startValue;
                int endWidth = (int) endValue;
                return (int) (startWidth + fraction * (endWidth - startWidth));
            }
        }, 0, mButton_width.getWidth());
//        widthAnimator1.setDuration(2000);

        ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(mButton_width, "translationX", mButton_width.getWidth() / 2, 0);
//        translateAnimator.setDuration(2000);
        mAnimatorSet.setDuration(2000);
        mAnimatorSet.playTogether(widthAnimator1, translateAnimator);
        mAnimatorSet.start();
//        ObjectAnimator widthAnimator2 = ObjectAnimator.ofFloat(mButton_width, "scaleX", 1f, 0.5f);
//        widthAnimator2.setDuration(2000);
//        return widthAnimator2;
    }

    private void startBallAnimator() {
        int windowHeight = getWindow().getDecorView().getHeight();
        int ballBottom = mIv_ball.getBottom();
        int ballHeight = mIv_ball.getHeight();
        int[] location = new int[2];
        mIv_ball.getLocationOnScreen(location);
        Log.e("TEST", "x:  " + location[0] + "   y:  " + location[1]);
        Log.e("TEST", "window height:  " + windowHeight + "     ballBottom:   " + ballBottom + "  ballHeight: " + ballHeight);
        mIv_ball.animate().y(windowHeight - location[1] - ballHeight).setDuration(5000).setInterpolator(new BounceInterpolator()).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                getNumberAnimator().start();
                break;
            case R.id.button2:
                getColorAnimator().start();
                break;
            case R.id.button3:
                getNumberColorSet().start();
                break;
            case R.id.button4:
                getTranslationAnimator().start();
                break;
            case R.id.button5:
                getBackgroundAnimator().start();
                break;
            case R.id.button6:
                startBallAnimator();
                break;
            case R.id.button7:
                getWidthAnimator();
                break;
        }

    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
