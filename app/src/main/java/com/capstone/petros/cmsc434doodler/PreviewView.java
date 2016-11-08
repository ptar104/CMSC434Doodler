package com.capstone.petros.cmsc434doodler;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;

/**
 * Created by peterkoutras on 11/6/16.
 */

public class PreviewView extends View {

    public Paint paintDoodle = new Paint();
    private Path path = new Path();

    public PreviewView(Context context) {
        super(context);
        init(null, 0);
    }

    public PreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PreviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        paintDoodle.setAntiAlias(true);
        paintDoodle.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawPath(path, paintDoodle);
    }

    public void drawPath(){
        path.moveTo(10, (getHeight()*3)/4);
        path.lineTo(getWidth()-10, getHeight()/4);

        paintDoodle.setStrokeWidth(((SeekBar) ((Activity)getContext()).findViewById(R.id.seekBarSize)).getProgress());
        int alpha = ((SeekBar) ((Activity)getContext()).findViewById(R.id.seekBarOpacity)).getProgress();
        int red = ((SeekBar) ((Activity)getContext()).findViewById(R.id.seekBarRed)).getProgress();
        int green = ((SeekBar) ((Activity)getContext()).findViewById(R.id.seekBarGreen)).getProgress();
        int blue = ((SeekBar) ((Activity)getContext()).findViewById(R.id.seekBarBlue)).getProgress();
        paintDoodle.setARGB(alpha, red, green, blue);
        invalidate();
    }


}
