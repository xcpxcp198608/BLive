package com.wiatec.blive.view.activity;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.px.kotlin.utils.Logger;
import com.wiatec.blive.R;

import java.io.IOException;

/**
 * with media recorder
 */

public class MediaRecorderActivity extends AppCompatActivity implements SurfaceHolder.Callback , View.OnClickListener{

    private String RECORDER_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Movies/";
    private MediaRecorder mediaRecorder;
    private Camera camera;

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Button btStart, btStop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_recorder);
        initLayout();
    }

    private void initLayout() {
        surfaceView = (SurfaceView) findViewById(R.id.sf_preview);
        btStart = (Button) findViewById(R.id.bt_start);
        btStop = (Button) findViewById(R.id.bt_stop);
        btStart.setOnClickListener(this);
        btStop.setOnClickListener(this);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setKeepScreenOn(true);
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void initCamera(){
        if(camera == null){
            camera = Camera.open();
        }
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            if(camera != null ){
                camera.release();
                camera = null;
            }
        }
    }

    private void startRecorder(){
        if(mediaRecorder == null){
            mediaRecorder = new MediaRecorder();
        }
        stopPreview();
        initCamera();
        camera.unlock();
        mediaRecorder.setCamera(camera);
        mediaRecorder.reset();
//        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//        mediaRecorder.setVideoSize(480, 640);
//        mediaRecorder.setVideoFrameRate(20);
        mediaRecorder.setOrientationHint(90);
        String fileName = System.currentTimeMillis()+".mp4";
        Logger.INSTANCE.d(RECORDER_PATH + fileName);
        mediaRecorder.setOutputFile(RECORDER_PATH + fileName);
        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void stopRecorder(){
        if(mediaRecorder != null){
            mediaRecorder.stop();
            mediaRecorder.release();
            camera.lock();
            mediaRecorder = null;
        }
    }

    private void startPreview(){
        if(camera != null && surfaceHolder != null){
            camera.startPreview();
        }
    }

    private void stopPreview(){
        if(camera != null ) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initCamera();
        startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopPreview();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_start:
                startRecorder();
                break;
            case R.id.bt_stop:
                stopRecorder();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPreview();
        stopRecorder();
    }
}
