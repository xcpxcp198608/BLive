package com.wiatec.blive.view.activity;

import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;

import com.wiatec.blive.R;


/**
 * DN
 */

public class RecorderActivity1 extends AppCompatActivity implements View.OnClickListener {

    private SurfaceView surfaceView;
    private ImageButton btStart;
    private boolean isPushing = false;
    private static final String URL = "rtmp://128.1.68.58:1939/live/BLIVE1";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder1);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        btStart = (ImageButton) findViewById(R.id.btStart);
        btStart.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btStart:
                if(isPushing){
                    stop();
                    btStart.setImageResource(R.drawable.ic_start_record);
                }else{
                    start();
                    btStart.setImageResource(R.drawable.ic_stop_record);
                }
                break;
            default:
                break;
        }
    }

    private void start(){

    }

    private void stop(){

    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();
    }
}
