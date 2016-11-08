package com.capstone.petros.cmsc434doodler;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by peterkoutras on 11/2/16.
 */

public class DoodleView extends View{

    private final int END_OF_PATHS = 0;
    private final int NEXT_LINE = 1;
    private final int NEXT_PATH = 2;

    private Stack<Paint> paintStack = new Stack<Paint>();
    private Stack<Path> pathStack = new Stack<Path>();
    private Stack<ArrayList<Integer>> pathPointsStack = new Stack<ArrayList<Integer>>();

    private Stack<Paint> redoPaintStack = new Stack<Paint>();
    private Stack<Path> redoPathStack = new Stack<Path>();
    private Stack<ArrayList<Integer>> redoPathPointsStack = new Stack<ArrayList<Integer>>();

    private boolean shouldSave = false;

    public Paint paintDoodle = new Paint();
    private Path path = new Path();
    private ArrayList<Integer> pathPoints = new ArrayList<Integer>();


    public DoodleView(Context context) {
        super(context);
        init(null, 0);
    }

    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DoodleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        paintDoodle.setColor(Color.BLACK);
        paintDoodle.setAntiAlias(true);
        paintDoodle.setStyle(Paint.Style.STROKE);
        paintDoodle.setStrokeWidth(19);
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        for(int i = 0; i<paintStack.size(); i++)
            canvas.drawPath(pathStack.get(i), paintStack.get(i));
        canvas.drawPath(path, paintDoodle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        if(!((MainActivity)getContext()).shouldDraw){
            return true;
        }
        float x = motionEvent.getX(), y = motionEvent.getY();

        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                shouldSave = false;
                path.moveTo(x,y);
                pathPoints.add((int)x);
                pathPoints.add((int)y);
                break;
            case MotionEvent.ACTION_MOVE:
                shouldSave = true;
                path.lineTo(x,y);
                pathPoints.add((int)x);
                pathPoints.add((int)y);
                redoPaintStack.clear();
                redoPathStack.clear();
                redoPathPointsStack.clear();
                break;
            case MotionEvent.ACTION_UP:
                if(shouldSave) {
                    paintStack.push(paintDoodle);
                    pathStack.push(path);
                    pathPointsStack.push(pathPoints);
                }
                paintDoodle = new Paint();
                path = new Path();
                pathPoints = new ArrayList<Integer>();
                paintDoodle.setAntiAlias(true);
                paintDoodle.setStyle(Paint.Style.STROKE);
                paintDoodle.setStrokeWidth(((SeekBar) ((Activity)getContext()).findViewById(R.id.seekBarSize)).getProgress());
                int alpha = ((SeekBar) ((Activity)getContext()).findViewById(R.id.seekBarOpacity)).getProgress();
                int red = ((SeekBar) ((Activity)getContext()).findViewById(R.id.seekBarRed)).getProgress();
                int green = ((SeekBar) ((Activity)getContext()).findViewById(R.id.seekBarGreen)).getProgress();
                int blue = ((SeekBar) ((Activity)getContext()).findViewById(R.id.seekBarBlue)).getProgress();
                paintDoodle.setARGB(alpha, red, green, blue);
                break;
        }

        invalidate();
        return true;
    }

    public void clearDrawing(){
        paintStack.clear();
        pathStack.clear();
        pathPointsStack.clear();
        redoPathPointsStack.clear();
        redoPathStack.clear();
        redoPaintStack.clear();
        paintDoodle = new Paint();
        path = new Path();
        pathPoints = new ArrayList<Integer>();
        paintDoodle.setAntiAlias(true);
        paintDoodle.setStyle(Paint.Style.STROKE);
        paintDoodle.setStrokeWidth(((SeekBar) ((Activity)getContext()).findViewById(R.id.seekBarSize)).getProgress());
        int alpha = ((SeekBar) ((Activity)getContext()).findViewById(R.id.seekBarOpacity)).getProgress();
        int red = ((SeekBar) ((Activity)getContext()).findViewById(R.id.seekBarRed)).getProgress();
        int green = ((SeekBar) ((Activity)getContext()).findViewById(R.id.seekBarGreen)).getProgress();
        int blue = ((SeekBar) ((Activity)getContext()).findViewById(R.id.seekBarBlue)).getProgress();
        paintDoodle.setARGB(alpha, red, green, blue);
        invalidate();
    }

    public void undo(){
        if(!paintStack.isEmpty()) {
            redoPaintStack.push(paintStack.pop());
            redoPathStack.push(pathStack.pop());
            redoPathPointsStack.push(pathPointsStack.pop());
        }
        invalidate();
    }

    public void redo(){
        if(!redoPaintStack.isEmpty()) {
            paintStack.push(redoPaintStack.pop());
            pathStack.push(redoPathStack.pop());
            pathPointsStack.push(redoPathPointsStack.pop());
        }
        invalidate();
    }

    public void save(){
        //File file = new File(getContext().getFilesDir(), "imageFile");
        try {
            FileOutputStream outputStream = getContext().openFileOutput("com_petros_cmsc434doodler_imageFile", Context.MODE_PRIVATE);
            for(int i = 0; i<pathStack.size(); i++){
                outputStream.write((int)paintStack.get(i).getStrokeWidth()); //One byte is enough, max if 50
                outputStream.write(paintStack.get(i).getAlpha());
                //Red, Green, Blue
                outputStream.write((paintStack.get(i).getColor() & 0xFF0000) >> (4*4));
                outputStream.write((paintStack.get(i).getColor() & 0xFF00) >> (2*4));
                outputStream.write(paintStack.get(i).getColor() & 0xFF);

                outputStream.write(NEXT_LINE);
                for(int j = 0; j < pathPointsStack.get(i).size(); j+=2){
                    //First point - have to save points byte at a time.
                    outputStream.write(pathPointsStack.get(i).get(j) >> (6*4));
                    outputStream.write(pathPointsStack.get(i).get(j) >> (4*4));
                    outputStream.write(pathPointsStack.get(i).get(j) >> (2*4));
                    outputStream.write(pathPointsStack.get(i).get(j));
                    //Second point
                    outputStream.write(pathPointsStack.get(i).get(j+1) >> (6*4));
                    outputStream.write(pathPointsStack.get(i).get(j+1) >> (4*4));
                    outputStream.write(pathPointsStack.get(i).get(j+1) >> (2*4));
                    outputStream.write(pathPointsStack.get(i).get(j+1));
                    if(j+2 == pathPointsStack.get(i).size()){
                        outputStream.write(NEXT_PATH);
                    }
                    else{
                        outputStream.write(NEXT_LINE);
                    }
                }
            }
            outputStream.write(END_OF_PATHS);
            outputStream.close();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error saving the file", Toast.LENGTH_SHORT).show();
        }
    }

    public void load(){
        clearDrawing();
        try{
            FileInputStream inputStream = getContext().openFileInput("com_petros_cmsc434doodler_imageFile");
            int input = inputStream.read();
            while(input != END_OF_PATHS){
                Paint loadPaint = new Paint();
                Path loadPath = new Path();
                ArrayList<Integer> loadPoints = new ArrayList<Integer>();
                loadPaint.setAntiAlias(true);
                loadPaint.setStyle(Paint.Style.STROKE);
                loadPaint.setStrokeWidth(input);
                loadPaint.setARGB(inputStream.read(), inputStream.read(),
                        inputStream.read(), inputStream.read());
                input = inputStream.read();
                while(input != NEXT_PATH){
                    //First point...
                    int x = 0;
                    x |= inputStream.read();
                    x <<= 8;
                    x |= inputStream.read();
                    x <<= 8;
                    x |= inputStream.read();
                    x <<= 8;
                    x |= inputStream.read();

                    //Second point...
                    int y = 0;
                    y |= inputStream.read();
                    y <<= 8;
                    y |= inputStream.read();
                    y <<= 8;
                    y |= inputStream.read();
                    y <<= 8;
                    y |= inputStream.read();

                    if(loadPoints.isEmpty()){
                        loadPath.moveTo(x,y);
                    }
                    else{
                        loadPath.lineTo(x,y);
                    }
                    loadPoints.add(x);
                    loadPoints.add(y);
                    input = inputStream.read();
                }
                paintStack.push(loadPaint);
                pathStack.push(loadPath);
                pathPointsStack.push(loadPoints);
                input = inputStream.read();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error saving the file", Toast.LENGTH_SHORT).show();
        }
        invalidate();
    }

}
