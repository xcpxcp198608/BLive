package com.wiatec.blive.view.activity

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.px.common.utils.EmojiToast
import com.px.common.utils.Logger

import com.wiatec.blive.R
import com.wiatec.blive.instance.KEY_CHANNEL_MESSAGE
import com.wiatec.blive.instance.KEY_PLAY_TYPE
import com.wiatec.blive.instance.KEY_PLAY_TYPE_LOCAL
import com.wiatec.blive.instance.KEY_URL
import kotlinx.android.synthetic.main.activity_play.*

class PlayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_play)
        val url = intent.getStringExtra(KEY_URL)
        val message = intent.getStringExtra(KEY_CHANNEL_MESSAGE)
        val type = intent.getStringExtra(KEY_PLAY_TYPE)
        if(!TextUtils.isEmpty(message)){
            tvChannelMessage.text = message
            tvChannelMessage.visibility = View.VISIBLE
        }
        play(url, type)
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

    override fun onDestroy() {
        super.onDestroy()
        videoView.stopPlayback()
    }
}
