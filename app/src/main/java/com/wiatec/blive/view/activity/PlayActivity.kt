package com.wiatec.blive.view.activity

import android.content.Context
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.ScrollView
import com.px.common.utils.Logger

import com.wiatec.blive.R
import com.wiatec.blive.instance.KEY_PLAY_TYPE
import com.wiatec.blive.instance.KEY_PLAY_TYPE_LOCAL
import kotlinx.android.synthetic.main.activity_play.*
import android.widget.TextView
import com.px.common.utils.SPUtil
import com.wiatec.blive.instance.KEY_AUTH_USER_ID
import com.wiatec.blive.instance.WS_URL
import com.wiatec.blive.pojo.ChannelInfo
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.StringBuilder
import java.net.URI
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager


class PlayActivity : AppCompatActivity(), View.OnClickListener {

    private var url: String = ""
    private var groupId: String = ""
    private var channelInfo: ChannelInfo? = null
    private var webSocketClient: WebSocketClient? = null
    private var stringBuilder: StringBuilder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_play)
        channelInfo = intent.getSerializableExtra("channelInfo") as ChannelInfo?
        if(channelInfo == null) return
        url = channelInfo!!.playUrl!!
        groupId = channelInfo!!.userId.toString()
        val message = channelInfo!!.message
        val type = intent.getStringExtra(KEY_PLAY_TYPE)
        if(!TextUtils.isEmpty(message)){
            tvChannelMessage.text = message
            tvChannelMessage.visibility = View.VISIBLE
        }
        ibtEdit.setOnClickListener(this)
        etMessage.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEND){
                sendComment()
                return@OnEditorActionListener true
            }
            false
        })
        play(url, type)
    }

    override fun onStart() {
        super.onStart()
        initWS()
    }

    private fun play(url: String, type: String){
        progressBar.visibility = View.VISIBLE
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

    override fun onDestroy() {
        super.onDestroy()
        videoView.stopPlayback()
        closeWS()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.ibtEdit -> {
                etMessage.visibility = View.VISIBLE
                etMessage.isFocusable = true
                etMessage.isFocusableInTouchMode = true
                etMessage.requestFocus()
                val inputManager = etMessage.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.showSoftInput(etMessage, 0)
            }
        }
    }

    private fun initWS() {
        val userId = SPUtil.get(KEY_AUTH_USER_ID, 0) as Int
        webSocketClient = object : WebSocketClient(URI(WS_URL + groupId + "/" + userId)) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Logger.d("ws on open")
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Logger.d("ws on close")
            }

            override fun onMessage(message: String?) {
                Logger.d("ws on message " + message)
                val msg = handler.obtainMessage()
                msg.what = 1
                msg.obj = message
                handler.sendMessage(msg)
            }

            override fun onError(ex: java.lang.Exception?) {
                Logger.d("ws on error " + ex?.message)
            }
        }
        webSocketClient?.connect()
    }

    private val handler = object: Handler(Looper.getMainLooper()){

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if(msg?.what == 1){
                val comment = msg.obj as String
                showComment(comment)
            }
        }
    }

    private fun closeWS(){
        if(webSocketClient != null){
            webSocketClient?.close()
        }
    }

    private fun showComment(comment: String){
        if(stringBuilder == null) {
            stringBuilder = StringBuilder()
        }
        stringBuilder!!.append("\r\n")
        stringBuilder!!.append(comment)
        tvComment.text = stringBuilder.toString()
        scrollView.fullScroll(ScrollView.FOCUS_DOWN)
    }

    private fun sendComment(){
        val message = etMessage.text.toString()
        if(TextUtils.isEmpty(message)){
            return
        }
        if(webSocketClient != null){
            webSocketClient!!.send("1/$groupId/$message")
        }
        etMessage.setText("")
        etMessage.visibility = View.GONE
    }

}
