package com.vpineda.duinocontrol.app.classes.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by vpineda1996 on 2015-03-26.
 */
public class ServoView extends View {

    public ServoView(Context context) {
        super(context);
    }

    public ServoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ServoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ServoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


}
