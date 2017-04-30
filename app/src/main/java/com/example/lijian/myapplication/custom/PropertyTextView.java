package com.example.lijian.myapplication.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by LIJIAN on 2017/3/23.
 */

public class PropertyTextView extends AppCompatTextView {
    public PropertyTextView(Context context) {
        super(context);
    }

    public PropertyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PropertyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public void setText(float value){
//       setText(String.format("%.0f",value));
//    }

}
