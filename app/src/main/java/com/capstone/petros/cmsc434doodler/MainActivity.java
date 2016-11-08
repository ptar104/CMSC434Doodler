package com.capstone.petros.cmsc434doodler;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public boolean shouldDraw = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Size
        SeekBar seekBarSize = (SeekBar)findViewById(R.id.seekBarSize);
        seekBarSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                DoodleView dv = (DoodleView)findViewById(R.id.doodleView);
                dv.paintDoodle.setStrokeWidth(i);
                TextView tv = (TextView)findViewById(R.id.textViewSize);
                tv.setText("Size: "+(i+1));

                PreviewView pv = (PreviewView)findViewById(R.id.previewView);
                pv.drawPath();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Opacity
        SeekBar seekBarOpacity = (SeekBar)findViewById(R.id.seekBarOpacity);
        seekBarOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                DoodleView dv = (DoodleView)findViewById(R.id.doodleView);
                int red = ((SeekBar)findViewById(R.id.seekBarRed)).getProgress();
                int green = ((SeekBar)findViewById(R.id.seekBarGreen)).getProgress();
                int blue = ((SeekBar)findViewById(R.id.seekBarBlue)).getProgress();
                dv.paintDoodle.setARGB(i, red, green, blue);
                TextView tv = (TextView)findViewById(R.id.textViewOpacity);
                tv.setText("Opacity: "+(int)((i/255.0)*100)+"%");

                PreviewView pv = (PreviewView)findViewById(R.id.previewView);
                pv.drawPath();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Color: red
        SeekBar seekBarRed = (SeekBar)findViewById(R.id.seekBarRed);
        seekBarRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                DoodleView dv = (DoodleView)findViewById(R.id.doodleView);
                int alpha = ((SeekBar)findViewById(R.id.seekBarOpacity)).getProgress();
                int green = ((SeekBar)findViewById(R.id.seekBarGreen)).getProgress();
                int blue = ((SeekBar)findViewById(R.id.seekBarBlue)).getProgress();
                dv.paintDoodle.setARGB(alpha, i, green, blue);
                TextView tv = (TextView)findViewById(R.id.textViewRed);
                tv.setText("R: "+i);

                PreviewView pv = (PreviewView)findViewById(R.id.previewView);
                pv.drawPath();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Color: green
        SeekBar seekBarGreen = (SeekBar)findViewById(R.id.seekBarGreen);
        seekBarGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                DoodleView dv = (DoodleView)findViewById(R.id.doodleView);
                int alpha = ((SeekBar)findViewById(R.id.seekBarOpacity)).getProgress();
                int red = ((SeekBar)findViewById(R.id.seekBarRed)).getProgress();
                int blue = ((SeekBar)findViewById(R.id.seekBarBlue)).getProgress();
                dv.paintDoodle.setARGB(alpha, red, i, blue);
                TextView tv = (TextView)findViewById(R.id.textViewGreen);
                tv.setText("G: "+i);

                PreviewView pv = (PreviewView)findViewById(R.id.previewView);
                pv.drawPath();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Color: blue
        SeekBar seekBarBlue = (SeekBar)findViewById(R.id.seekBarBlue);
        seekBarBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                DoodleView dv = (DoodleView)findViewById(R.id.doodleView);
                int alpha = ((SeekBar)findViewById(R.id.seekBarOpacity)).getProgress();
                int red = ((SeekBar)findViewById(R.id.seekBarRed)).getProgress();
                int green = ((SeekBar)findViewById(R.id.seekBarGreen)).getProgress();
                dv.paintDoodle.setARGB(alpha, red, green, i);
                TextView tv = (TextView)findViewById(R.id.textViewBlue);
                tv.setText("B: "+i);

                PreviewView pv = (PreviewView)findViewById(R.id.previewView);
                pv.drawPath();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    public void onClickClear(View button){
        DoodleView dv = (DoodleView)findViewById(R.id.doodleView);
        dv.clearDrawing();
    }

    public void onClickUp(View button){
        View controlPanel = findViewById(R.id.controlPanel);
        View previewPanel = findViewById(R.id.previewPanel);
        View tintOverlay = findViewById(R.id.tintOverlay);
        View buttonUp = findViewById(R.id.buttonUp);
        controlPanel.setVisibility(View.VISIBLE);
        previewPanel.setVisibility(View.VISIBLE);
        tintOverlay.setVisibility(View.VISIBLE);
        buttonUp.setVisibility(View.INVISIBLE);
        shouldDraw = false;

        PreviewView pv = (PreviewView)findViewById(R.id.previewView);
        pv.drawPath();
    }

    public void onClickDown(View button){
        View controlPanel = findViewById(R.id.controlPanel);
        View previewPanel = findViewById(R.id.previewPanel);
        View tintOverlay = findViewById(R.id.tintOverlay);
        View buttonUp = findViewById(R.id.buttonUp);
        controlPanel.setVisibility(View.INVISIBLE);
        previewPanel.setVisibility(View.INVISIBLE);
        tintOverlay.setVisibility(View.INVISIBLE);
        buttonUp.setVisibility(View.VISIBLE);
        shouldDraw = true;
    }

    public void onClickUndo(View button){
        if(shouldDraw){
            DoodleView dv = (DoodleView)findViewById(R.id.doodleView);
            dv.undo();
        }
    }

    public void onClickRedo(View button){
        if(shouldDraw){
            DoodleView dv = (DoodleView)findViewById(R.id.doodleView);
            dv.redo();
        }
    }

    public void onClickSave(View button){
        DoodleView dv = (DoodleView)findViewById(R.id.doodleView);
        dv.save();
    }

    public void onClickLoad(View button){
        DoodleView dv = (DoodleView)findViewById(R.id.doodleView);
        dv.load();
    }

}
