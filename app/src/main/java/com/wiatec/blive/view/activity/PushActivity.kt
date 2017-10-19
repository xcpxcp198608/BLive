package com.wiatec.blive.view.activity

import android.content.Context
import android.content.res.Configuration
import android.hardware.Camera
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.OrientationEventListener
import android.view.View
import android.view.WindowManager
import com.github.faucamp.simplertmp.RtmpHandler
import com.px.common.utils.Logger

import com.wiatec.blive.R
import com.wiatec.blive.instance.DEFAULT_PUSH_URL
import com.wiatec.blive.instance.KEY_URL
import kotlinx.android.synthetic.main.activity_push.*
import net.ossrs.yasea.SrsEncodeHandler
import net.ossrs.yasea.SrsPublisher
import net.ossrs.yasea.SrsRecordHandler
import java.io.IOException
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.net.SocketException
import com.px.common.utils.EmojiToast


class PushActivity : AppCompatActivity(), View.OnClickListener, RtmpHandler.RtmpListener,
        SrsRecordHandler.SrsRecordListener, SrsEncodeHandler.SrsEncodeListener {

    private var pushUrl = ""
    private var isPushing = false
    private var publisher: SrsPublisher? = null
    private var displayOrientationListener: DisplayOrientationListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_push)
        pushUrl = intent.getStringExtra(KEY_URL)
        if(TextUtils.isEmpty(pushUrl)) pushUrl = DEFAULT_PUSH_URL
        initView()
        initPublish()
    }

    private fun initView() {
        displayOrientationListener = DisplayOrientationListener(this)
        if (displayOrientationListener!!.canDetectOrientation()) {
            displayOrientationListener!!.enable()
        } else {
            displayOrientationListener!!.disable()
        }
        ibtStart.setOnClickListener(this)
        ibtSwitchCamera.setOnClickListener(this)
    }

    private fun initPublish(){
        publisher = SrsPublisher(srsCameraView)
        publisher!!.setEncodeHandler(SrsEncodeHandler(this))
        publisher!!.setRtmpHandler(RtmpHandler(this))
        publisher!!.setRecordHandler(SrsRecordHandler(this))
        publisher!!.setPreviewResolution(1280, 720)
        publisher!!.setOutputResolution(720, 480)
        publisher!!.setScreenOrientation(Configuration.ORIENTATION_LANDSCAPE)
        publisher!!.setVideoHDMode()
        publisher!!.switchCameraFace(Camera.CameraInfo.CAMERA_FACING_BACK)
        publisher!!.startCamera()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.ibtStart -> {
                if(isPushing){
                    stopPush()
                    ibtStart.setBackgroundResource(R.drawable.ic_play_circle_outline_pink_400_48dp)
                }else{
                    startPush()
                    ibtStart.setBackgroundResource(R.drawable.ic_pause_circle_outline_pink_400_48dp)
                }
            }
            R.id.ibtSwitchCamera -> {
                if(publisher != null) {
                    publisher!!.switchCameraFace((publisher!!.camraId + 1) % Camera.getNumberOfCameras())
                }
            }
        }
    }

    private fun startPush(){
        if(publisher != null) {
            publisher!!.startPublish(pushUrl)
            publisher!!.startCamera()
            isPushing = true
        }
    }

    private fun stopPush(){
        if(publisher != null) {
            publisher!!.stopPublish()
            publisher!!.stopRecord()
            publisher!!.stopEncode()
            isPushing = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopPush()
        displayOrientationListener!!.disable()
    }

    override fun onNetworkWeak() {
//        EmojiToast.show("network weak", EmojiToast.EMOJI_SAD)
    }

    override fun onNetworkResume() {
        EmojiToast.show("network resume", EmojiToast.EMOJI_SAD)
    }

    override fun onEncodeIllegalArgumentException(e: IllegalArgumentException?) {
        handleException(e!!)
    }

    override fun onRecordPause() {
        Logger.d("onRecordPause")
    }

    override fun onRecordResume() {
        Logger.d("onRecordResume")
    }

    override fun onRecordStarted(msg: String?) {
        EmojiToast.show("start", EmojiToast.EMOJI_SMILE)
    }

    override fun onRecordFinished(msg: String?) {
        Logger.d("onRecordFinished->" + msg)
    }

    override fun onRecordIllegalArgumentException(e: IllegalArgumentException?) {
        handleException(e!!)
    }

    override fun onRecordIOException(e: IOException?) {
        handleException(e!!)
    }

    override fun onRtmpConnecting(msg: String?) {
        Logger.d("onRtmpConnecting")
    }

    override fun onRtmpConnected(msg: String?) {
        Logger.d("onRtmpConnected")
    }

    override fun onRtmpVideoStreaming() {
        val r: Int = displayOrientationListener!!.currentOrientation
    }

    override fun onRtmpAudioStreaming() {
    }

    override fun onRtmpStopped() {
        Logger.d("onRtmpStopped")
    }

    override fun onRtmpDisconnected() {
        Logger.d("onRtmpDisconnected")
    }

    override fun onRtmpVideoFpsChanged(fps: Double) {
        Logger.d(String.format("Output Fps: %f", fps))
    }

    override fun onRtmpVideoBitrateChanged(bitrate: Double) {
        val rate = bitrate.toInt()
        if (rate / 1000 > 0) {
            Logger.d(String.format("Video bitrate: %d kbps", rate))
        } else {
            Logger.d(String.format("Video bitrate: %d bps", rate))
        }
    }

    override fun onRtmpAudioBitrateChanged(bitrate: Double) {
        val rate = bitrate.toInt()
        if (rate / 1000 > 0) {
            Logger.d(String.format("Audio bitrate: %d kbps", rate))
        } else {
            Logger.d(String.format("Audio bitrate: %d bps", rate))
        }
    }

    override fun onRtmpSocketException(e: SocketException?) {
        handleException(e!!)
    }

    override fun onRtmpIOException(e: IOException?) {
        handleException(e!!)
    }

    override fun onRtmpIllegalArgumentException(e: IllegalArgumentException?) {
        handleException(e!!)
    }

    override fun onRtmpIllegalStateException(e: IllegalStateException?) {
        handleException(e!!)
    }

    private fun handleException(e: Exception) {
        try {
            EmojiToast.show(e.message, EmojiToast.EMOJI_SAD)
            publisher!!.stopPublish()
            publisher!!.stopRecord()
        } catch (e1: Exception) {
            Logger.d(e1.message)
        }

    }

    private class DisplayOrientationListener(context: Context) : OrientationEventListener(context) {

        var currentOrientation = 0

        override fun onOrientationChanged(orientation: Int) {
            if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                return
            }
            if (orientation > 350 || orientation < 10) {
                currentOrientation = 0
            } else if (orientation in 81..99) {
                currentOrientation = 90
            } else if (orientation in 171..189) {
                currentOrientation = 180
            } else if (orientation in 261..279) {
                currentOrientation = 270
            }
        }
    }

}
