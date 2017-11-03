package com.wiatec.blive.view.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.px.common.utils.*

import com.wiatec.blive.R
import com.wiatec.blive.instance.*
import com.wiatec.blive.pojo.*
import com.wiatec.blive.presenter.AuthPresenter
import com.wiatec.blive.utils.WindowUtil
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.layout_reset_password.*
import kotlinx.android.synthetic.main.layout_sign_in.*
import kotlinx.android.synthetic.main.layout_sign_up.*
import kotlinx.android.synthetic.main.tool_bar_main.*

class AuthActivity : BaseActivity<Auth, AuthPresenter>(), Auth, View.OnClickListener{

    private var isSignUpShow = false
    private var isForgotShow = false

    override fun createPresenter(): AuthPresenter = AuthPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        initToolBar()
        initView()
    }

    private fun initView(){
        val username = SPUtil.get(KEY_AUTH_USERNAME, "") as String
        if(!TextUtils.isEmpty(username)){
            etUsernameSignIn.setText(username)
            etUsernameSignIn.setSelection(username.length)
        }
        tvForgot.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        tvSignUp.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        tvSignUp.setOnClickListener(this)
        tvForgot.setOnClickListener(this)
        btSignUp.setOnClickListener(this)
        btSignIn.setOnClickListener(this)
        btReset.setOnClickListener(this)
    }

    private fun initToolBar() {
        val paddingTop = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT )
            WindowUtil.getStatusBarHeight(this) else 0
        toolBarMain.setPadding(0, paddingTop, 0, 0)
        toolBarMain.title = getString(R.string.signin)
        toolBarMain.setTitleTextColor(Color.WHITE)
        toolBarMain.inflateMenu(R.menu.menu_skip)
        toolBarMain.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.item_skip -> jumpToMain()
            }
            true
        }
    }

    override fun onClick(v: View?) {
        SysUtil.closeKeybord(etPasswordSignIn, this@AuthActivity)
        when (v!!.id){
            R.id.tvSignUp -> {
                toolBarMain.menu.getItem(0).isVisible = false
                layoutSignIn.visibility = View.GONE
                layoutSignUp.visibility = View.VISIBLE
                toolBarMain.title = getString(R.string.signup)
                btSignUp.isEnabled = true
                progressBarSignUp.visibility = View.GONE
                isSignUpShow = true
                toolBarMain.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
                toolBarMain.setNavigationOnClickListener {
                    showSignInFromSignUp()
                }
            }
            R.id.tvForgot -> {
                toolBarMain.menu.getItem(0).isVisible = false
                layoutSignIn.visibility = View.GONE
                layoutResetPassword.visibility = View.VISIBLE
                toolBarMain.title = getString(R.string.reset)
                btReset.isEnabled = true
                isForgotShow = true
                toolBarMain.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
                toolBarMain.setNavigationOnClickListener {
                    showSignInFromReset()
                }
            }
            R.id.btSignUp -> {
                val userInfo = checkSignUpInput()
                if(userInfo != null) {
                    presenter!!.signUp(userInfo)
                    disableButton(btSignUp, progressBarSignUp)
                }
            }
            R.id.btSignIn -> {
                val userInfo = checkSignInInput()
                if(userInfo != null) {
                    presenter!!.signIn(userInfo)
                    disableButton(btSignIn, progressBarSignIn)
                }
            }
            R.id.btReset -> {
                val userInfo = checkResetInput()
                if(userInfo != null) {
                    presenter!!.resetPassword(userInfo)
                    disableButton(btReset, progressBarReset)
                }
            }
        }
    }

    private fun showSignInFromSignUp(){
        layoutSignIn.visibility = View.VISIBLE
        layoutSignUp.visibility = View.GONE
        progressBarSignIn.visibility = View.GONE
        toolBarMain.navigationIcon = null
        toolBarMain.title = getString(R.string.signin)
        toolBarMain.menu.getItem(0).isVisible = true
        isSignUpShow = false
        val username = SPUtil.get(KEY_AUTH_USERNAME, "") as String
        if(!TextUtils.isEmpty(username)){
            etPasswordSignIn.setText(username)
            etUsernameSignIn.setSelection(username.length)
        }
    }

    private fun showSignInFromReset(){
        layoutSignIn.visibility = View.VISIBLE
        layoutResetPassword.visibility = View.GONE
        progressBarSignIn.visibility = View.GONE
        toolBarMain.navigationIcon = null
        toolBarMain.title = getString(R.string.signin)
        toolBarMain.menu.getItem(0).isVisible = true
        isForgotShow = false
        val username = SPUtil.get(KEY_AUTH_USERNAME, "") as String
        if(!TextUtils.isEmpty(username)){
            etPasswordSignIn.setText(username)
            etUsernameSignIn.setSelection(username.length)
        }
    }

    private fun checkSignUpInput(): UserInfo?{
        val username = etUsernameSignUp.text.toString()
        val password = etPasswordSignUp.text.toString()
        val password1 = etPasswordSignUp1.text.toString()
        val email = etEmailSignUp.text.toString()
        val phone = etPhoneSignUp.text.toString()
        if(TextUtils.isEmpty(username) || username.length < 6 || username.length > 20){
            showInputErrorNotice("username input error")
            return null
        }
        if(TextUtils.isEmpty(password) || password.length < 6 || username.length > 20){
            showInputErrorNotice("password input error")
            return null
        }
        if(TextUtils.isEmpty(password1) || password1.length < 6 || username.length > 20){
            showInputErrorNotice("password input error")
            return null
        }
        if(password != password1){
            showInputErrorNotice("password not match")
            return null
        }
        if(TextUtils.isEmpty(email)){
            showInputErrorNotice("email no input")
            return null
        }
        if(!RegularUtil.validateEmail(email)){
            showInputErrorNotice("email format error")
            return null
        }
        if(TextUtils.isEmpty(phone) || phone.length < 10){
            showInputErrorNotice("cell phone input error")
            return null
        }
        return UserInfo(username, password, email, phone)
    }

    private fun checkSignInInput(): UserInfo? {
        val username = etUsernameSignIn.text.toString()
        val password = etPasswordSignIn.text.toString()
        if(TextUtils.isEmpty(username) || username.length < 6 || username.length > 20){
            showInputErrorNotice("username input error")
            return null
        }
        if(TextUtils.isEmpty(password) || password.length < 6 || username.length > 20){
            showInputErrorNotice("password input error")
            return null
        }
        return UserInfo(username, password)
    }

    private fun checkResetInput(): UserInfo?{
        val username = etUsernameReset.text.toString()
        val email = etEmailReset.text.toString()
        if(TextUtils.isEmpty(username) || username.length < 6 || username.length > 20){
            showInputErrorNotice("username input error")
            return null
        }
        if(TextUtils.isEmpty(email)){
            showInputErrorNotice("email no input")
            return null
        }
        if(!RegularUtil.validateEmail(email)){
            showInputErrorNotice("email format error")
            return null
        }
        return UserInfo(username, "", email, "")
    }

    private fun showInputErrorNotice(message: String){
        val snackBar = Snackbar.make(constraintLayout, message, Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundResource(R.color.colorBlue1)
        val tvContent = snackBar.view.findViewById(R.id.snackbar_text) as TextView
        tvContent.setTextColor(Color.rgb(255, 64, 129))
        snackBar.show()
    }

    override fun signUp(execute: Boolean, resultInfo: ResultInfo<UserInfo>?) {
        enableButton(btSignUp, progressBarSignUp)
        if(execute && resultInfo != null){
            if(resultInfo.code == ResultInfo.CODE_OK){
                if(resultInfo.t != null) {
                    SPUtil.put(KEY_AUTH_USERNAME, resultInfo.t!!.username)
                }
                EmojiToast.show(resultInfo.message, EmojiToast.EMOJI_SMILE)
                showSignInFromSignUp()
            }else {
                EmojiToast.show(resultInfo.message, EmojiToast.EMOJI_SAD)
            }
        }else{
            EmojiToast.show("signup server error", EmojiToast.EMOJI_SAD)
        }
    }

    override fun signIn(execute: Boolean, resultInfo: ResultInfo<TokenInfo>?) {
        if(execute && resultInfo != null){
            if(resultInfo.code == ResultInfo.CODE_OK){
                if(resultInfo.t != null) {
                    SPUtil.put(KEY_AUTH_TOKEN, resultInfo.t!!.token)
                    if(resultInfo.t!!.userInfo != null) {
                        SPUtil.put(KEY_AUTH_USERNAME, resultInfo.t!!.userInfo!!.username)
                        SPUtil.put(KEY_AUTH_USER_ID, resultInfo.t!!.userId)
                        SPUtil.put(KEY_AUTH_ICON_URL, resultInfo.t!!.userInfo!!.icon)
                    }
                }
                presenter!!.getPush(resultInfo.t!!.userInfo!!.username!!, RTMP_TOKEN)
            }else {
                enableButton(btSignIn, progressBarSignIn)
                EmojiToast.show(resultInfo.message, EmojiToast.EMOJI_SAD)
            }
        }else{
            enableButton(btSignIn, progressBarSignIn)
            EmojiToast.show("signin server error", EmojiToast.EMOJI_SAD)
        }
    }

    override fun getPush(execute: Boolean, pushInfo: PushInfo?) {
        if(execute && pushInfo != null) {
            SPUtil.put(KEY_AUTH_PUSH_URL, pushInfo.data!!.push_full_url)
            val userId = SPUtil.get(KEY_AUTH_USER_ID, 0) as Int
            if(userId == 0){
                EmojiToast.show("signin server error", EmojiToast.EMOJI_SAD)
            }else {
                presenter!!.updateChannel(ChannelInfo(pushInfo.data!!.push_full_url!!,
                        pushInfo.data!!.play_url!!, userId))
            }
        }else{
            enableButton(btSignIn, progressBarSignIn)
            EmojiToast.show("live server error", EmojiToast.EMOJI_SAD)
        }
    }

    override fun updateChannel(execute: Boolean, resultInfo: ResultInfo<ChannelInfo>?) {
        enableButton(btSignIn, progressBarSignIn)
        if(execute && resultInfo != null) {
            if(resultInfo.code != ResultInfo.CODE_OK){
                EmojiToast.show(resultInfo.message, EmojiToast.EMOJI_SAD)
            }
            val channelInfo  = resultInfo.t
            if(channelInfo != null) {
                SPUtil.put(KEY_CHANNEL_ID, channelInfo.id.toString())
            }
        }else{
            EmojiToast.show("signin server error", EmojiToast.EMOJI_SAD)
        }
        jumpToMain()
    }

    override fun resetPassword(execute: Boolean, resultInfo: ResultInfo<UserInfo>?) {
        enableButton(btReset, progressBarReset)
        if(execute && resultInfo != null){
            if(resultInfo.code == ResultInfo.CODE_OK){
                EmojiToast.show(resultInfo.message, EmojiToast.EMOJI_SMILE)
                showSignInFromReset()
            }else {
                EmojiToast.show(resultInfo.message, EmojiToast.EMOJI_SAD)
            }
        }else{
            EmojiToast.show("reset failure", EmojiToast.EMOJI_SAD)
        }
    }

    private fun enableButton(bt: Button, pb: ProgressBar){
        pb.visibility = View.GONE
        bt.isEnabled = true
        bt.setBackgroundResource(R.drawable.bg_bt_auth)
    }

    private fun disableButton(bt: Button, pb: ProgressBar){
        bt.isEnabled = false
        bt.setBackgroundResource(R.drawable.bg_bt_auth_disabled)
        pb.visibility = View.VISIBLE
    }

    private fun jumpToMain(){
        startActivity(Intent(this@AuthActivity, MainActivity::class.java))
        finish()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(event!!.keyCode == KeyEvent.KEYCODE_BACK){
            if(isSignUpShow){
                showSignInFromSignUp()
                return true
            }else if (isForgotShow){
                showSignInFromReset()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
