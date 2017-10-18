package com.wiatec.blive.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alex.livertmppushsdk.FdkAacEncode;
import com.alex.livertmppushsdk.RtmpSessionManager;
import com.alex.livertmppushsdk.SWVideoEncoder;
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

public class Recorder1Activity extends Activity {

    private final String LOG_TAG = "Recorder";
    private final static int ID_RTMP_PUSH_START = 100;
    private final static int ID_RTMP_PUSH_EXIT = 101;
    private final int WIDTH_DEF = 480;
    private final int HEIGHT_DEF = 640;
    private final int FRAMERATE_DEF = 20;
    private final int BITRATE_DEF = 800 * 1000;
    private final int SAMPLE_RATE_DEF = 22050;
    private final int CHANNEL_NUMBER_DEF = 2;
    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_AUDIO = 1;
    private final boolean DEBUG_ENABLE = false;

    private String rtmpUrl = "rtmp://us3.protv.company:1939/liveChannel/BVISION3";
    private PowerManager.WakeLock wakeLock;
    private DataOutputStream outputStream = null;
    private AudioRecord audioRecorder = null;
    private byte[] recorderBuffer = null;
    private FdkAacEncode fdkaacEnc = null;
    private int fdkaacHandle = 0;
    public SurfaceView surfaceView = null;
    private Camera mCamera = null;
    private boolean isFront = true;
    private SWVideoEncoder swEncH264 = null;
    private int degrees = 0;
    private int recorderBufferSize = 0;
    private ImageButton ibtSwitchCamera = null;
    private boolean startFlag = false;
    private int cameraCodecType = android.graphics.ImageFormat.NV21;
    private byte[] yuvNV21 = new byte[WIDTH_DEF * HEIGHT_DEF * 3 / 2];
    private byte[] yuvEdit = new byte[WIDTH_DEF * HEIGHT_DEF * 3 / 2];
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
                newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        initAll();
    }

    private void initAll() {
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        int iNewWidth = (int) (height * 3.0 / 4.0);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        int iPos = width - iNewWidth;
        layoutParams.setMargins(iPos, 0, 0, 0);
        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceViewEx);
        surfaceView.getHolder().setFixedSize(HEIGHT_DEF, WIDTH_DEF);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(new SurfaceCallBack());
        surfaceView.setLayoutParams(layoutParams);
        initAudioRecord();
        ibtSwitchCamera = (ImageButton) findViewById(R.id.SwitchCamerabutton);
        ibtSwitchCamera.setOnClickListener(_switchCameraOnClickedEvent);
        rtmpStartMessage();//开始推流
    }

    protected void onResume() {
        super.onResume();
        wakeLock.acquire();
    }

    protected void onPause() {
        super.onPause();
        wakeLock.release();
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
        int result = 0;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    private Camera.PreviewCallback _previewCallback = new Camera.PreviewCallback() {

        @Override
        public void onPreviewFrame(byte[] YUV, Camera currentCamera) {
            if (!startFlag) {
                return;
            }
            byte[] yuv420 = null;
            if (cameraCodecType == android.graphics.ImageFormat.YV12) {
                yuv420 = new byte[YUV.length];
                swEncH264.swapYV12toI420_Ex(YUV, yuv420, HEIGHT_DEF, WIDTH_DEF);
            } else if (cameraCodecType == android.graphics.ImageFormat.NV21) {
                yuv420 = swEncH264.swapNV21toI420(YUV, HEIGHT_DEF, WIDTH_DEF);
            }
            if (yuv420 == null) {
                return;
            }
            if (!startFlag) {
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
        Camera.Parameters p = mCamera.getParameters();
        Size prevewSize = p.getPreviewSize();
        Log.i(LOG_TAG, "Original Width:" + prevewSize.width + ", height:" + prevewSize.height);
        List<Size> PreviewSizeList = p.getSupportedPreviewSizes();
        List<Integer> PreviewFormats = p.getSupportedPreviewFormats();
        Log.i(LOG_TAG, "Listing all supported icon sizes");
        for (Size size : PreviewSizeList) {
            Log.i(LOG_TAG, "  w: " + size.width + ", h: " + size.height);
        }
        Log.i(LOG_TAG, "Listing all supported icon formats");
        Integer iNV21Flag = 0;
        Integer iYV12Flag = 0;
        for (Integer yuvFormat : PreviewFormats) {
            Log.i(LOG_TAG, "icon formats:" + yuvFormat);
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
        p.setPreviewSize(HEIGHT_DEF, WIDTH_DEF);
        p.setPreviewFormat(cameraCodecType);
        p.setPreviewFrameRate(FRAMERATE_DEF);

        mCamera.setDisplayOrientation(degrees);
        p.setRotation(degrees);
        mCamera.setPreviewCallback(_previewCallback);
        mCamera.setParameters(p);
        try {
            mCamera.setPreviewDisplay(surfaceView.getHolder());
        } catch (Exception e) {
            return;
        }
        mCamera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
        mCamera.startPreview();
    }

    private final class SurfaceCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
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

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            degrees = getDisplayOrientation(getDisplayRotation(), 0);
            if (mCamera != null) {
                initCamera();
                return;
            }
            if (Build.VERSION.SDK_INT>22){
                if (ContextCompat.checkSelfPermission(Recorder1Activity.this,
                        Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    //先判断有没有权限 ，没有就在这里进行权限的申请
                    ActivityCompat.requestPermissions(Recorder1Activity.this,
                            new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA);

                }else {
                    mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                    initCamera();
                }
            }else {
                mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                initCamera();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    private void start() {
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
        rtmpSessionMgr.Start(rtmpUrl);
        int iFormat = cameraCodecType;
        swEncH264 = new SWVideoEncoder(WIDTH_DEF, HEIGHT_DEF, FRAMERATE_DEF, BITRATE_DEF);
        swEncH264.start(iFormat);
        startFlag = true;
        h264EncoderThread = new Thread(h264Runnable);
        h264EncoderThread.setPriority(Thread.MAX_PRIORITY);
        h264EncoderThread.start();
        if (Build.VERSION.SDK_INT>22){
            if (ContextCompat.checkSelfPermission(Recorder1Activity.this,
                    Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
                //先判断有没有权限 ，没有就在这里进行权限的申请
                ActivityCompat.requestPermissions(Recorder1Activity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_AUDIO);
            }else {
                audioRecorder.startRecording();
                aacEncoderThread = new Thread(aacEncoderRunnable);
                aacEncoderThread.setPriority(Thread.MAX_PRIORITY);
                aacEncoderThread.start();
            }
        }else {
            audioRecorder.startRecording();
            aacEncoderThread = new Thread(aacEncoderRunnable);
            aacEncoderThread.setPriority(Thread.MAX_PRIORITY);
            aacEncoderThread.start();
        }
    }

    private void stop() {
        startFlag = false;
        if(aacEncoderThread != null) aacEncoderThread.interrupt();
        h264EncoderThread.interrupt();
        audioRecorder.stop();
        swEncH264.stop();
        rtmpSessionMgr.Stop();
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

    private OnClickListener _switchCameraOnClickedEvent = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if (mCamera == null) {
                return;
            }
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            if (isFront) {
                mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            } else {
                mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            }
            isFront = !isFront;
            initCamera();
        }
    };

    private void initAudioRecord() {
        recorderBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_DEF,
                AudioFormat.CHANNEL_CONFIGURATION_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);
        audioRecorder = new AudioRecord(AudioSource.MIC,
                SAMPLE_RATE_DEF, AudioFormat.CHANNEL_CONFIGURATION_STEREO,
                AudioFormat.ENCODING_PCM_16BIT, recorderBufferSize);
        recorderBuffer = new byte[recorderBufferSize];

        fdkaacEnc = new FdkAacEncode();
        fdkaacHandle = fdkaacEnc.FdkAacInit(SAMPLE_RATE_DEF, CHANNEL_NUMBER_DEF);
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ID_RTMP_PUSH_START: {
                    start();
                    break;
                }
            }
        }
    };

    private void rtmpStartMessage() {
        Message msg = new Message();
        msg.what = ID_RTMP_PUSH_START;
        Bundle b = new Bundle();
        b.putInt("ret", 0);
        msg.setData(b);
        mHandler.sendMessage(msg);
    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExitPublishDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showExitPublishDialog(){
        new MaterialDialog.Builder(Recorder1Activity.this)
                .title(R.string.notice)
                .content(R.string.stop_publish)
                .positiveText(R.string.confirm)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
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
                        if (startFlag) {
                            stop();
                        }
                        finish();
                    }
                })
                .show();
    }

    private Runnable h264Runnable = new Runnable() {
        @Override
        public void run() {
            while (!h264EncoderThread.interrupted() && startFlag) {
                int iSize = yuvQueue.size();
                if (iSize > 0) {
                    yuvQueueLock.lock();
                    byte[] yuvData = yuvQueue.poll();
                    if (iSize > 9) {
                        Log.i(LOG_TAG, "###YUV Queue len=" + yuvQueue.size() + ", YUV length=" + yuvData.length);
                    }
                    yuvQueueLock.unlock();
                    if (yuvData == null) {
                        continue;
                    }
                    if (isFront) {
                        yuvEdit = swEncH264.YUV420pRotate270(yuvData, HEIGHT_DEF, WIDTH_DEF);
                    } else {
                        yuvEdit = swEncH264.YUV420pRotate90(yuvData, HEIGHT_DEF, WIDTH_DEF);
                    }
                    byte[] h264Data = swEncH264.EncoderH264(yuvEdit);
                    if (h264Data != null) {
                        rtmpSessionMgr.InsertVideoData(h264Data);
                        if (DEBUG_ENABLE) {
                            try {
                                outputStream.write(h264Data);
                                int iH264Len = h264Data.length;
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

            while (!aacEncoderThread.interrupted() && startFlag) {
                int iPCMLen = audioRecorder.read(recorderBuffer, 0, recorderBuffer.length); // Fill buffer
                if ((iPCMLen != audioRecorder.ERROR_BAD_VALUE) && (iPCMLen != 0)) {
                    if (fdkaacHandle != 0) {
                        byte[] aacBuffer = fdkaacEnc.FdkAacEncode(fdkaacHandle, recorderBuffer);
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
                    Log.i(LOG_TAG, "######fail to get PCM data");
                }
                try {
                    Thread.sleep(lSleepTime / 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.i(LOG_TAG, "AAC Encoder Thread ended ......");
        }
    };
}
