package com.wiatec.blive.view.activity;

import android.content.Context;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;

import com.ksyun.media.streamer.capture.CameraCapture;
import com.ksyun.media.streamer.kit.KSYStreamer;
import com.ksyun.media.streamer.kit.StreamerConstants;
import com.wiatec.blive.R;


/**
 * DN
 */

public class RecorderActivity1 extends AppCompatActivity implements View.OnClickListener {

    private GLSurfaceView surfaceView;
    private KSYStreamer ksyStreamer;

    private ImageButton btStart;
    private boolean isPushing = false;
    private static final String URL = "rtmp://128.1.68.58:1939/live/BLIVE1";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder1);
        surfaceView = (GLSurfaceView) findViewById(R.id.surfaceView);
        btStart = (ImageButton) findViewById(R.id.btStart);
        btStart.setOnClickListener(this);

        initStream();
    }

    private void initStream(){
        ksyStreamer = new KSYStreamer(this);
        ksyStreamer.setDisplayPreview(surfaceView);
        ksyStreamer.setUrl(URL);
        ksyStreamer.setPreviewResolution(480,0);
        ksyStreamer.setTargetResolution(480,0);
        ksyStreamer.setPreviewFps(15);
        ksyStreamer.setTargetFps(15);
        ksyStreamer.setVideoBitrate(600,800,400);
        ksyStreamer.setAudioSampleRate(44100);
        ksyStreamer.setAudioBitrate(48);
        ksyStreamer.setEncodeMethod(StreamerConstants.ENCODE_METHOD_SOFTWARE);
        ksyStreamer.setRotateDegrees(90);
        ksyStreamer.setCameraFacing(CameraCapture.FACING_BACK);
        ksyStreamer.setOnErrorListener(new KSYStreamer.OnErrorListener() {
            @Override
            public void onError(int i, int i1, int i2) {
                Log.d("----px----", "ERROR:" + i + "/" + i1 +"/" + i2);
            }
        });
        ksyStreamer.setOnInfoListener(new KSYStreamer.OnInfoListener() {
            @Override
            public void onInfo(int i, int i1, int i2) {
                Log.d("----px----", "INFO:" + i + "/" + i1 +"/" + i2);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        ksyStreamer.startStream();
        isPushing = true;
    }

    private void stop(){
        ksyStreamer.stopStream();
        isPushing = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        ksyStreamer.onPause();
        isPushing = false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isPushing = false;
        ksyStreamer.setOnLogEventListener(null);
        ksyStreamer.release();
    }
}
