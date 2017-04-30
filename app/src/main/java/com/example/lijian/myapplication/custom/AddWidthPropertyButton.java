package com.example.lijian.myapplication.custom;


import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by LIJIAN on 2017/3/23.
 */

public class AddWidthPropertyButton extends AppCompatButton {
    public AddWidthPropertyButton(Context context) {
        super(context);
    }

    public AddWidthPropertyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AddWidthPropertyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setWidth(int width) {
        getLayoutParams().width = width;
        requestLayout();
    }
}
