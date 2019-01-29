package com.example.android.exotelcaller.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.exotelcaller.R;

public class DialPad extends RelativeLayout {

    private String mChar;
    private String mLetters;

    public DialPad(Context context, AttributeSet attrs) {
        super(context, attrs);
        initButton(attrs);
    }

    public DialPad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initButton(attrs);
    }

    public DialPad(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initButton(attrs);
    }

    private void initButton(AttributeSet attrs) {
        LayoutInflater inflater;
        if (getContext() instanceof Activity) {
            inflater = ((Activity)getContext()).getLayoutInflater();
        } else {
            inflater = LayoutInflater.from(getContext());
        }

        inflater.inflate(R.layout.dial_pad, this, true);
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.NumpadButton, 0, 0);
        try {
            mChar = arr.getString(R.styleable.NumpadButton_number);
            mLetters = arr.getString(R.styleable.NumpadButton_letters);
        } finally {
            arr.recycle();
        }
        ((TextView)findViewById(R.id.dialpad_number)).setText(mChar);
        ((TextView)findViewById(R.id.dialpad_letters)).setText(mLetters);
    }
}
