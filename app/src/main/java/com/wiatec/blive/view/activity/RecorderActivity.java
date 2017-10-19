package com.wiatec.blive.view.activity;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alex.livertmppushsdk.FdkAacEncode;
import com.alex.livertmppushsdk.RtmpSessionManager;
import com.alex.livertmppushsdk.SWVideoEncoder;
import com.px.common.utils.Logger;
import com.wiatec.blive.R;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RecorderActivity extends Activity implements OnClickListener {

    private static final int ID_RTMP_PUSH_START = 100;
    private static final int ID_RTMP_PUSH_EXIT = 101;
    private static final int WIDTH = 360;
    private static final int HEIGHT = 640;
    private static final int FRAME_RATE_DEF = 20;
    private static final int BITRATE_DEF = 800 * 1000;
    private static final int SAMPLE_RATE_DEF = 22050;
    private static final int CHANNEL_NUMBER_DEF = 2;
    private static final boolean DEBUG_ENABLE = false;
    private static final String DEFAULT_PUSH_URL = "rtmp://us3.protv.company:1939/live/BVISION4";

    private String pushUrl;
    private PowerManager.WakeLock wakeLock;
    private DataOutputStream outputStream = null;
    private AudioRecord audioRecorder = null;
    private byte[] recorderBuffer = null;
    private FdkAacEncode fdkAacEnc = null;
    private int fdkAacHandle = 0;
    private Camera mCamera = null;
    private SurfaceView surfaceView = null;
    private SWVideoEncoder swEncH264 = null;
    private boolean isFront = false;
    private boolean isHorizontal = false;
    private boolean isPushing = false;
    private int degrees = 0;
    private int cameraCodecType = android.graphics.ImageFormat.NV21;
//    private byte[] yuvNV21 = new byte[WIDTH * HEIGHT * 3 / 2];
    private byte[] yuvEdit = new byte[WIDTH * HEIGHT * 3 / 2];
    private RtmpSessionManager rtmpSessionMgr = null;
    private Queue<byte[]> yuvQueue = new LinkedList<>();
    private Lock yuvQueueLock = new ReentrantLock();
    private Thread aacEncoderThread = null;
    private Thread h264EncoderThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).
                newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "recorder");
        pushUrl = getIntent().getStringExtra("url");
        if(TextUtils.isEmpty(pushUrl)) pushUrl = DEFAULT_PUSH_URL;
    }

    @Override
    protected void onStart() {
        super.onStart();
        initAll();
    }

    protected void onResume() {
        super.onResume();
        wakeLock.acquire();
    }

    protected void onPause() {
        super.onPause();
        wakeLock.release();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPush();
    }

    private void initAll() {
        ImageButton ibtSwitchCamera = (ImageButton) findViewById(R.id.ibtSwitchCamera);
        ibtSwitchCamera.setOnClickListener(this);
        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);
        surfaceView.getHolder().setFixedSize(HEIGHT, WIDTH);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(new SurfaceCallBack());
        initAudioRecord();
        startPush();
    }

    private void initAudioRecord() {
        int recorderBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_DEF,
                AudioFormat.CHANNEL_CONFIGURATION_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);
        audioRecorder = new AudioRecord(AudioSource.MIC,
                SAMPLE_RATE_DEF, AudioFormat.CHANNEL_CONFIGURATION_STEREO,
                AudioFormat.ENCODING_PCM_16BIT, recorderBufferSize);
        recorderBuffer = new byte[recorderBufferSize];
        fdkAacEnc = new FdkAacEncode();
        fdkAacHandle = fdkAacEnc.FdkAacInit(SAMPLE_RATE_DEF, CHANNEL_NUMBER_DEF);
    }

    private void startPush() {
        if (DEBUG_ENABLE) {
            File saveDir = Environment.getExternalStorageDirectory();
            String strFilename = saveDir + "/aaa.h264";
            try {
                outputStream = new DataOutputStream(new FileOutputStream(strFilename));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        rtmpSessionMgr = new RtmpSessionManager();
        rtmpSessionMgr.Start(pushUrl);
        int iFormat = cameraCodecType;
        swEncH264 = new SWVideoEncoder(WIDTH, HEIGHT, FRAME_RATE_DEF, BITRATE_DEF);
        swEncH264.start(iFormat);
        isPushing = true;
        h264EncoderThread = new Thread(h264Runnable);
        h264EncoderThread.setPriority(Thread.MAX_PRIORITY);
        h264EncoderThread.start();
        audioRecorder.startRecording();
        aacEncoderThread = new Thread(aacEncoderRunnable);
        aacEncoderThread.setPriority(Thread.MAX_PRIORITY);
        aacEncoderThread.start();
    }

    private void stopPush() {
        isPushing = false;
        if(aacEncoderThread != null) aacEncoderThread.interrupt();
        if(h264EncoderThread != null)h264EncoderThread.interrupt();
        if(audioRecorder != null)audioRecorder.stop();
        if(swEncH264 != null)swEncH264.stop();
        if(rtmpSessionMgr != null)rtmpSessionMgr.Stop();
        yuvQueueLock.lock();
        yuvQueue.clear();
        yuvQueueLock.unlock();
        if (DEBUG_ENABLE) {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int getDisplayRotation() {
        int i = getWindowManager().getDefaultDisplay().getRotation();
        switch (i) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }

    private int getDisplayOrientation(int degrees, int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {

        @Override
        public void onPreviewFrame(byte[] YUV, Camera currentCamera) {
            if (!isPushing) {
                return;
            }
            byte[] yuv420 = null;
            if (cameraCodecType == android.graphics.ImageFormat.YV12) {
                yuv420 = new byte[YUV.length];
                swEncH264.swapYV12toI420_Ex(YUV, yuv420, HEIGHT, WIDTH);
            } else if (cameraCodecType == android.graphics.ImageFormat.NV21) {
                yuv420 = swEncH264.swapNV21toI420(YUV, HEIGHT, WIDTH);
            }
            if (yuv420 == null) {
                return;
            }
            if (!isPushing) {
                return;
            }
            yuvQueueLock.lock();
            if (yuvQueue.size() > 1) {
                yuvQueue.clear();
            }
            yuvQueue.offer(yuv420);
            yuvQueueLock.unlock();
        }
    };

    public void initCamera() {
        if(mCamera == null ) mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        Camera.Parameters p = mCamera.getParameters();
        Size previewSize = p.getPreviewSize();
        Logger.d("Original Width:" + previewSize.width + ", height:" + previewSize.height);
        List<Size> PreviewSizeList = p.getSupportedPreviewSizes();
        List<Integer> PreviewFormats = p.getSupportedPreviewFormats();
        for (Size size : PreviewSizeList) {
            Logger.d("  w: " + size.width + ", h: " + size.height);
        }
        Integer iNV21Flag = 0;
        Integer iYV12Flag = 0;
        for (Integer yuvFormat : PreviewFormats) {
            Logger.d("icon formats:" + yuvFormat);
            if (yuvFormat == android.graphics.ImageFormat.YV12) {
                iYV12Flag = android.graphics.ImageFormat.YV12;
            }
            if (yuvFormat == android.graphics.ImageFormat.NV21) {
                iNV21Flag = android.graphics.ImageFormat.NV21;
            }
        }
        if (iNV21Flag != 0) {
            cameraCodecType = iNV21Flag;
        } else if (iYV12Flag != 0) {
            cameraCodecType = iYV12Flag;
        }
        p.setPreviewSize(HEIGHT, WIDTH);
        p.setPreviewFormat(cameraCodecType);
        p.setPreviewFrameRate(FRAME_RATE_DEF);
        try {
            mCamera.setDisplayOrientation(degrees);
            p.setRotation(degrees);
            mCamera.setPreviewCallback(previewCallback);
            mCamera.setParameters(p);
            mCamera.setPreviewDisplay(surfaceView.getHolder());
            mCamera.cancelAutoFocus();
            mCamera.startPreview();
        } catch (Exception e) {
            Logger.d(e.getMessage());
        }
    }

    private final class SurfaceCallBack implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            degrees = getDisplayOrientation(getDisplayRotation(), 0);
            initCamera();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if(mCamera!= null) {
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (success) {
                            initCamera();
                            camera.cancelAutoFocus();
                        }
                    }
                });
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibtSwitchCamera:
                switchCamera();
                break;
        }
    }

    private void switchCamera(){
        if (mCamera == null) return;
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        mCamera = isFront ? Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK):
                Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        isFront = !isFront;
        initCamera();
    }

    private void releaseCamera(){
        if (mCamera != null) {
            try {
                mCamera.setPreviewCallback(null);
                mCamera.setPreviewDisplay(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExitPublishDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showExitPublishDialog(){
        new MaterialDialog.Builder(RecorderActivity.this)
                .title(R.string.notice)
                .content(R.string.stop_publish)
                .positiveText(R.string.confirm)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        releaseCamera();
                        if (isPushing) stopPush();
                        finish();
                    }
                })
                .show();
    }

    private Runnable h264Runnable = new Runnable() {
        @Override
        public void run() {
            while (!h264EncoderThread.interrupted() && isPushing) {
                int iSize = yuvQueue.size();
                if (iSize > 0) {
                    yuvQueueLock.lock();
                    byte[] yuvData = yuvQueue.poll();
                    if (iSize > 9) {
                        Logger.d("###YUV Queue len=" + yuvQueue.size() + ", YUV length=" + yuvData.length);
                    }
                    yuvQueueLock.unlock();
                    if (yuvData == null) {
                        continue;
                    }
                    if (isFront) {
                        yuvEdit = swEncH264.YUV420pRotate270(yuvData, HEIGHT, WIDTH);
                    } else {
                        yuvEdit = swEncH264.YUV420pRotate90(yuvData, HEIGHT, WIDTH);
                    }
                    byte[] h264Data = swEncH264.EncoderH264(yuvEdit);
                    if (h264Data != null) {
                        rtmpSessionMgr.InsertVideoData(h264Data);
                        if (DEBUG_ENABLE) {
                            try {
                                outputStream.write(h264Data);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            yuvQueue.clear();
        }
    };


    private Runnable aacEncoderRunnable = new Runnable() {
        @Override
        public void run() {
            DataOutputStream outputStream = null;
            if (DEBUG_ENABLE) {
                File saveDir = Environment.getExternalStorageDirectory();
                String strFilename = saveDir + "/aaa.aac";
                try {
                    outputStream = new DataOutputStream(new FileOutputStream(strFilename));
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }

            long lSleepTime = SAMPLE_RATE_DEF * 16 * 2 / recorderBuffer.length;

            while (!aacEncoderThread.interrupted() && isPushing) {
                int iPCMLen = audioRecorder.read(recorderBuffer, 0, recorderBuffer.length); // Fill buffer
                if ((iPCMLen != AudioRecord.ERROR_BAD_VALUE) && (iPCMLen != 0)) {
                    if (fdkAacHandle != 0) {
                        byte[] aacBuffer = fdkAacEnc.FdkAacEncode(fdkAacHandle, recorderBuffer);
                        if (aacBuffer != null) {
                            rtmpSessionMgr.InsertAudioData(aacBuffer);
                            if (DEBUG_ENABLE) {
                                try {
                                    outputStream.write(aacBuffer);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else {
                    Logger.d("######fail to get PCM data");
                }
                try {
                    Thread.sleep(lSleepTime / 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Logger.d("AAC Encoder Thread ended ......");
        }
    };
}
