package com.lab.zongjx.projfinal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyrecyclerView extends RecyclerView {
    public MyrecyclerView(Context context){
        super(context);
    }
    public MyrecyclerView(Context context, AttributeSet attrs){super(context, attrs);}     //Constructor that is called when inflating a view from XML
    public MyrecyclerView(Context context, AttributeSet attrs, int defStyle){super(context, attrs, defStyle);}     //Perform inflation from XML and apply a class-specific base style

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}