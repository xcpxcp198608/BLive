package com.wiatec.blive.view.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.px.common.utils.EmojiToast
import com.px.common.utils.Logger

import com.wiatec.blive.R
import com.wiatec.blive.instance.KEY_URL
import kotlinx.android.synthetic.main.activity_play.*

class PlayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_play)
        val url = intent.getStringExtra(KEY_URL)
        play(url)
    }

    private fun play(url: String){
        Logger.d(url)
        videoView.setVideoPath(url)
        videoView.setOnPreparedListener {
            progressBar.visibility = View.GONE
            videoView.start()
        }
        videoView.setOnErrorListener { _, _, _ ->
            progressBar.visibility = View.GONE
            EmojiToast.show("missing live information, press back and try again", EmojiToast.EMOJI_SAD)
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView.stopPlayback()
    }
}
