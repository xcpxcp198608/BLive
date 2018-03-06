package com.wiatec.blive.view.activity

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.CompoundButton
import android.widget.TextView
import com.px.common.utils.EmojiToast
import com.px.common.utils.Logger
import com.wiatec.blive.R
import com.wiatec.blive.instance.KEY_URL
import kotlinx.android.synthetic.main.activity_play.*

class PlayLocalActivity : AppCompatActivity() {

    private var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_play_local)
        url = intent.getStringExtra(KEY_URL)
        if(TextUtils.isEmpty(url)){
            EmojiToast.show("play path error", EmojiToast.EMOJI_SAD)
        }
        play(url)
    }

    private fun play(url: String){
        Logger.d(url)
        videoView.setVideoURI(Uri.parse(url))
        videoView.setOnPreparedListener {
            progressBar.visibility = View.GONE
            videoView.start()
        }
        videoView.setOnErrorListener { _, _, _ ->
            progressBar.visibility = View.GONE
            play(url)
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView.stopPlayback()
    }

}
