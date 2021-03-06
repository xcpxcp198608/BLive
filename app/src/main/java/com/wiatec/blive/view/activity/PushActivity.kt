package com.wiatec.blive.view.activity

import android.content.res.Configuration
import android.hardware.Camera
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.webkit.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.px.common.utils.TimeUtil
import com.seu.magicfilter.utils.MagicFilterType
import com.wiatec.blive.animator.Rotation
import com.wiatec.blive.instance.*
import com.wiatec.blive.pojo.ChannelInfo
import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.presenter.PushPresenter
import kotlinx.android.synthetic.main.activity_push.*


class PushActivity : BaseActivity<Push, PushPresenter>(), Push, View.OnClickListener,
        AdapterView.OnItemSelectedListener, RtmpHandler.RtmpListener,
        SrsRecordHandler.SrsRecordListener, SrsEncodeHandler.SrsEncodeListener {

    private var pushUrl = ""
    private var isPushing = false
    private var publisher: SrsPublisher? = null
    private var recordPath = ""
    private var isRecording = false
    private var isJSLoaded = false

    override fun createPresenter(): PushPresenter = PushPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_push)
        recordPath = getExternalFilesDir("record").absolutePath
        initView()
        initSpinner()
        initWebView()
    }

    private fun initView() {
        ibtStart.isEnabled = false
        ibtRecord.isEnabled = false
        btConfirm.setOnClickListener(this)
        ibtStart.setOnClickListener(this)
        ibtSwitchCamera.setOnClickListener(this)
        ibtRecord.setOnClickListener(this)
    }

    private fun initSpinner(){
        val resolutionsAdapter = ArrayAdapter<String>(this, R.layout.spinner_item,
                resources.getStringArray(R.array.resolutions))
        resolutionsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spResolution.adapter =resolutionsAdapter
        spResolution.onItemSelectedListener = this
        val filtersAdapter = ArrayAdapter<String>(this, R.layout.spinner_item,
                resources.getStringArray(R.array.filters))
        filtersAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spFilters.adapter = filtersAdapter
        spFilters.onItemSelectedListener = this
    }

    override fun onStart() {
        super.onStart()
        pushUrl = SPUtil.get(KEY_AUTH_PUSH_URL, "") as String
        if(TextUtils.isEmpty(pushUrl)){
            EmojiToast.show(getString(R.string.push_url_error), EmojiToast.EMOJI_SAD)
            return
        }
        initPublish()
    }

    private fun initPublish(){
        publisher = SrsPublisher(srsCameraView)
        publisher!!.setEncodeHandler(SrsEncodeHandler(this))
        publisher!!.setRtmpHandler(RtmpHandler(this))
        publisher!!.setRecordHandler(SrsRecordHandler(this))
        publisher!!.setPreviewResolution(720, 480)
        publisher!!.setOutputResolution(480, 240)
        publisher!!.setScreenOrientation(Configuration.ORIENTATION_LANDSCAPE)
        publisher!!.setVideoHDMode()
        publisher!!.switchCameraFace(Camera.CameraInfo.CAMERA_FACING_BACK)
        publisher!!.startCamera()
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
            isPushing = false
            isRecording = false
        }
    }

    private fun startRecord(){
        if(TextUtils.isEmpty(title)){
            title = SPUtil.get(KEY_AUTH_USERNAME, "") as String
        }
        recordPath += "/" + title + "_" + System.currentTimeMillis() + ".mp4"
        if(publisher!!.startRecord(recordPath)) {
            isRecording = true
        }
    }

    private fun stopRecord(){
        publisher!!.stopRecord()
        isRecording = false
    }

    override fun onStop() {
        super.onStop()
        stopPush()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseWebView()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btConfirm -> {
                var title = etChannelTitle.text.toString()
                var message = etChannelMessage.text.toString()
                if(TextUtils.isEmpty(title)){
                    title = SPUtil.get(KEY_AUTH_USERNAME, "") as String
                }
                if(TextUtils.isEmpty(message)){
                    message = ""
                }
                val userId = SPUtil.get(KEY_AUTH_USER_ID, 0) as Int
                presenter!!.updateChannelName(ChannelInfo(title, message, userId, holder = 0))
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
            R.id.ibtRecord -> {
                if(isRecording){
                    stopRecord()
                    ibtRecord.setBackgroundResource(R.drawable.bg_bt_record)
                }else{
                    startRecord()
                    ibtRecord.setBackgroundResource(R.drawable.bg_bt_record_stop)
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(parent!!.id){
            R.id.spResolution -> {
                when(position){
                    0 -> publisher!!.setOutputResolution(480, 240) //18s
                    1 -> publisher!!.setOutputResolution(640, 360) //25s
                    2 -> publisher!!.setOutputResolution(720, 480)
                }
            }
            R.id.spFilters -> {
                when(position){
                    0 -> publisher!!.switchCameraFilter(MagicFilterType.NONE)
                    1 -> publisher!!.switchCameraFilter(MagicFilterType.COOL)
                    2 -> publisher!!.switchCameraFilter(MagicFilterType.WARM)
                    3 -> publisher!!.switchCameraFilter(MagicFilterType.SUNRISE)
                    4 -> publisher!!.switchCameraFilter(MagicFilterType.SUNSET)
                    5 -> publisher!!.switchCameraFilter(MagicFilterType.BEAUTY)
                    6 -> publisher!!.switchCameraFilter(MagicFilterType.EARLYBIRD)
                    7 -> publisher!!.switchCameraFilter(MagicFilterType.EVERGREEN)
                    8 -> publisher!!.switchCameraFilter(MagicFilterType.ROMANCE)
                    9 -> publisher!!.switchCameraFilter(MagicFilterType.TENDER)
                    10 -> publisher!!.switchCameraFilter(MagicFilterType.VALENCIA)
                    11 -> publisher!!.switchCameraFilter(MagicFilterType.WALDEN)
                }
            }
        }
    }

    override fun updateChannelName(execute: Boolean, resultInfo: ResultInfo<ChannelInfo>?) {
        if(execute && resultInfo != null){
            if(resultInfo.code == ResultInfo.CODE_OK){
                llSetting.visibility = View.GONE
                ibtStart.isEnabled = true
            }else{
                EmojiToast.show(resultInfo.message, EmojiToast.EMOJI_SAD)
            }
        }else{
            EmojiToast.show("network unstable", EmojiToast.EMOJI_SAD)
        }
    }

    override fun onNetworkWeak() {
//        EmojiToast.show("network weak", EmojiToast.EMOJI_SAD)
    }

    override fun onNetworkResume() {
        EmojiToast.show("network unstable", EmojiToast.EMOJI_SAD)
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
        EmojiToast.show("record start", EmojiToast.EMOJI_SMILE)
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
        ibtRecord.isEnabled = true
    }

    override fun onRtmpVideoStreaming() {
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
            Logger.i(String.format("Video bitrate: %d kbps", rate))
        } else {
            Logger.i(String.format("Video bitrate: %d bps", rate))
        }
    }

    override fun onRtmpAudioBitrateChanged(bitrate: Double) {
        val rate = bitrate.toInt()
        if (rate / 1000 > 0) {
            Logger.i(String.format("Audio bitrate: %d kbps", rate))
        } else {
            Logger.i(String.format("Audio bitrate: %d bps", rate))
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
            EmojiToast.showLong(e.message, EmojiToast.EMOJI_SAD)
            Logger.d(e.message)
            publisher!!.stopPublish()
            publisher!!.stopRecord()
        } catch (e1: Exception) {
            Logger.d(e1.message)
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
                .onPositive { _, _ ->
                    stopPush()
                    finish()
                }
                .show()
    }

    class MyWebViewClient: WebViewClient(){
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean = true
    }

    private fun initWebView(){
        webView.setWebViewClient(MyWebViewClient())
        webView.setBackgroundColor(0)
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        webSettings.allowFileAccess = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.loadsImagesAutomatically = true
        webSettings.defaultTextEncodingName = "utf-8"
        webView.setWebChromeClient(WebChromeClient())
        loadWebView()
    }

    private fun loadWebView(){
        isJSLoaded = false
        webView.loadUrl("http://blive.protv.company:8804/html/danmu.html")
        webView.setWebChromeClient(object: WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if(newProgress >= 100){
                    if(!isJSLoaded) {
                        val channel = SPUtil.get(KEY_CHANNEL_ID, "") as String
                        webView.loadUrl("javascript:showDanMu('$channel')")
                        isJSLoaded = true
                    }
                }
            }
        })
    }

    private fun releaseWebView(){
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            webView.clearHistory()
            (webView.parent as ViewGroup).removeView(webView)
            webView.destroy()
        }
    }

}
