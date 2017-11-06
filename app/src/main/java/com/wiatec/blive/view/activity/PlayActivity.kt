package com.wiatec.blive.view.activity

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.view.inputmethod.EditorInfo
import android.webkit.*
import android.widget.CompoundButton
import com.px.common.http.HttpMaster
import com.px.common.http.Listener.StringListener
import com.px.common.utils.Logger

import com.wiatec.blive.R
import com.wiatec.blive.instance.KEY_PLAY_TYPE
import com.wiatec.blive.instance.KEY_PLAY_TYPE_LOCAL
import kotlinx.android.synthetic.main.activity_play.*
import android.widget.TextView
import com.wiatec.blive.pojo.ChannelInfo


class PlayActivity : AppCompatActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private var url: String = ""
    private var channelInfo: ChannelInfo? = null
    private var channel = ""
    private var isJSLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_play)
        channelInfo = intent.getSerializableExtra("channelInfo") as ChannelInfo?
        if(channelInfo == null) return
        url = channelInfo!!.playUrl!!
        channel = channelInfo!!.id.toString()
        val message = channelInfo!!.message
        val type = intent.getStringExtra(KEY_PLAY_TYPE)
        if(!TextUtils.isEmpty(message)){
            tvChannelMessage.text = message
            tvChannelMessage.visibility = View.VISIBLE
        }
        ibtSend.setOnClickListener(this)
        switchDanMu.setOnCheckedChangeListener(this)
        etMessage.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEND){
                sendGoEasyMessage()
                return@OnEditorActionListener true
            }
            false
        })
        play(url, type)
        initWebView()
    }

    override fun onStart() {
        super.onStart()
        if(switchDanMu.isChecked){
            loadWebView()
        }
    }

    private fun play(url: String, type: String){
        Logger.d(url)
        if(TextUtils.isEmpty(type)) {
            videoView.setVideoPath(url)
        }else if(type == KEY_PLAY_TYPE_LOCAL){
            videoView.setVideoURI(Uri.parse(url))
        }
        videoView.setOnPreparedListener {
            progressBar.visibility = View.GONE
            videoView.start()
        }
        videoView.setOnErrorListener { _, _, _ ->
            progressBar.visibility = View.GONE
            play(url, type)
            true
        }
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

    }

    private fun loadWebView(){
        isJSLoaded = false
        webView.loadUrl("http://blive.protv.company:8804/html/danmu.html")
        webView.setWebChromeClient(object: WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if(newProgress >= 100){
                    if(!isJSLoaded) {
                        Logger.d(newProgress.toString())
                        webView.loadUrl("javascript:showDanMu('$channel')")
                        isJSLoaded = true
                    }
                }
            }
        })
    }

    private fun unloadWebView(){
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            isJSLoaded = false
        }
    }

    private fun releaseWebView(){
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            isJSLoaded = false
            webView.clearHistory()
            (webView.parent as ViewGroup).removeView(webView)
            webView.destroy()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView.stopPlayback()
        releaseWebView()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.ibtSend -> {
                sendGoEasyMessage()
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if(buttonView!!.id == R.id.switchDanMu){
            if(isChecked){
                webView.visibility = View.VISIBLE
                etMessage.visibility = View.VISIBLE
                ibtSend.visibility = View.VISIBLE
                loadWebView()
            }else{
                webView.visibility = View.GONE
                etMessage.visibility = View.GONE
                ibtSend.visibility = View.GONE
                unloadWebView()
            }
        }
    }

    private fun sendGoEasyMessage(){
        val message = etMessage.text.toString()
        if(TextUtils.isEmpty(message)) {
            return
        }
        HttpMaster.post("http://rest-hangzhou.goeasy.io/publish")
                .parames("appkey", "BC-6a9b6c468c894389881bc1df7d90cddb")
                .parames("channel", channel)
                .parames("content", message)
                .enqueue(object : StringListener(){
                    override fun onSuccess(s: String?) {
                        etMessage.setText("")
                    }

                    override fun onFailure(e: String?) {
                    }
                })
    }
}
