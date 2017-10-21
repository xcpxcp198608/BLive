package com.wiatec.blive.view.activity

import android.content.Context
import android.content.res.Configuration
import android.hardware.Camera
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import com.afollestad.materialdialogs.MaterialDialog
import com.github.faucamp.simplertmp.RtmpHandler
import com.px.common.utils.Logger

import com.wiatec.blive.R
import kotlinx.android.synthetic.main.activity_push.*
import net.ossrs.yasea.SrsEncodeHandler
import net.ossrs.yasea.SrsPublisher
import net.ossrs.yasea.SrsRecordHandler
import java.io.IOException
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.net.SocketException
import com.px.common.utils.EmojiToast
import com.px.common.utils.SPUtil
import com.seu.magicfilter.utils.MagicFilterType
import com.wiatec.blive.animator.Rotation
import com.wiatec.blive.instance.*
import com.wiatec.blive.pojo.ChannelInfo
import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.presenter.PushPresenter


class PushActivity : BaseActivity<Push, PushPresenter>(), Push, View.OnClickListener, RtmpHandler.RtmpListener,
        SrsRecordHandler.SrsRecordListener, SrsEncodeHandler.SrsEncodeListener {

    private var pushUrl = ""
    private var isPushing = false
    private var publisher: SrsPublisher? = null
    private var displayOrientationListener: DisplayOrientationListener? = null

    override fun createPresenter(): PushPresenter = PushPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_push)
        initView()
    }

    private fun initView() {
        displayOrientationListener = DisplayOrientationListener(this)
        if (displayOrientationListener!!.canDetectOrientation()) {
            displayOrientationListener!!.enable()
        } else {
            displayOrientationListener!!.disable()
        }
        ibtStart.isEnabled = false
        btConfirm.setOnClickListener(this)
        ibtStart.setOnClickListener(this)
        ibtSwitchCamera.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        pushUrl = intent.getStringExtra(KEY_URL)
        if(TextUtils.isEmpty(pushUrl)) pushUrl = TEST_PUSH_URL
        initPublish()
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
        publisher!!.switchCameraFilter(MagicFilterType.SUNRISE)
        publisher!!.startCamera()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btConfirm -> {
                var name = etChannelTitle.text.toString()
                if(TextUtils.isEmpty(name)){
                    name = SPUtil.get(KEY_AUTH_USERNAME, "") as String
                }
                val userId = SPUtil.get(KEY_AUTH_USER_ID, 0) as Int
                presenter!!.updateChannelName(ChannelInfo(name, userId))
            }
            R.id.ibtStart -> {
                if(isPushing){
                    stopPush()
                    ibtStart.setBackgroundResource(R.drawable.bg_bt_play)
                }else{
                    startPush()
                    ibtStart.setBackgroundResource(R.drawable.bg_bt_pause)
                }
            }
            R.id.ibtSwitchCamera -> {
                Rotation.r180(ibtSwitchCamera)
                if(publisher != null) {
                    publisher!!.switchCameraFace((publisher!!.camraId + 1) % Camera.getNumberOfCameras())
                }
            }
        }
    }

    override fun updateChannelName(execute: Boolean, resultInfo: ResultInfo<ChannelInfo>?) {
        if(execute && resultInfo != null){
            Logger.d(resultInfo.toString())
            if(resultInfo.code == ResultInfo.CODE_OK){
                llSetting.visibility = View.GONE
                ibtStart.isEnabled = true
            }else{
                EmojiToast.show(resultInfo.message, EmojiToast.EMOJI_SAD)
            }
        }else{
            EmojiToast.show("server error", EmojiToast.EMOJI_SAD)
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

    override fun onStop() {
        super.onStop()
        stopPush()
    }

    override fun onDestroy() {
        super.onDestroy()
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
        presenter!!.updateChannelStatus(ACTIVATE)
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
        presenter!!.updateChannelStatus(DEACTIVATE)
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(isPushing) {
                showExitPublishDialog()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun showExitPublishDialog(){
        MaterialDialog.Builder(this@PushActivity)
                .title(R.string.notice)
                .content(R.string.stop_publish)
                .positiveText(R.string.confirm)
                .onPositive { dialog, which ->
                    stopPush()
                    displayOrientationListener!!.disable()
                    finish()
                }
                .show()
    }

}
