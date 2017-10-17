package com.wiatec.blive.view.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View

import com.wiatec.blive.R
import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.pojo.UserInfo
import com.wiatec.blive.presenter.AuthPresenter
import com.wiatec.blive.utils.WindowUtil
import kotlinx.android.synthetic.main.activity_auth.*
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
        initView()
        initToolBar()
    }

    private fun initView(){
        tvForgot.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        tvSignUp.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        tvSignUp.setOnClickListener(this)
        tvForgot.setOnClickListener(this)
    }

    private fun initToolBar() {
        val paddingTop = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT )
            WindowUtil.getStatusBarHeight(this) else 0
        toolBarMain.setPadding(0, paddingTop, 0, 0)
        toolBarMain.title = getString(R.string.app_name)
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
        when (v!!.id){
            R.id.tvSignUp -> {
                layoutSignIn.visibility = View.GONE
                layoutSignUp.visibility = View.VISIBLE
                isSignUpShow = true
                toolBarMain.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp)
                toolBarMain.setNavigationOnClickListener {
                    layoutSignIn.visibility = View.VISIBLE
                    layoutSignUp.visibility = View.GONE
                    toolBarMain.navigationIcon = null
                    isSignUpShow = false
                }
            }
            R.id.tvForgot -> {
                layoutSignIn.visibility = View.GONE
                layoutResetPassword.visibility = View.VISIBLE
                isForgotShow = true
                toolBarMain.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp)
                toolBarMain.setNavigationOnClickListener {
                    layoutSignIn.visibility = View.VISIBLE
                    layoutResetPassword.visibility = View.GONE
                    toolBarMain.navigationIcon = null
                    isForgotShow = false
                }
            }
            R.id.btSignUp -> {
                val userInfo = checkSignUpInput()
                if(userInfo != null) presenter!!.signUp(userInfo)
            }
            R.id.btSignIn -> {

            }
            R.id.btReset -> {

            }
        }
    }

    private fun checkSignUpInput(): UserInfo?{
        val username = etUsernameSignUp.text.trim().toString()
        val password = etPasswordSignUp.text.trim().toString()
        val password1 = etPasswordSignUp1.text.trim().toString()
        val email = etEmailSignUp.text.trim().toString()
        val phone = etPhoneSignUp.text.trim().toString()
        if(TextUtils.isEmpty(username) || username.length < 6){

            return null
        }
        if(TextUtils.isEmpty(password) || password.length < 6){

            return null
        }
        if(TextUtils.isEmpty(password1) || password1.length < 6){

            return null
        }
        if(password != password1){

            return null
        }
        if(TextUtils.isEmpty(email)){

            return null
        }
        if(TextUtils.isEmpty(phone)){

            return null
        }
        return UserInfo(username, password, email, phone)
    }

    private fun checkSignInInput(){
        
    }

    private fun checkResetInput(){

    }

    override fun signUp(execute: Boolean, resultInfo: ResultInfo<UserInfo>?) {
    }

    override fun signIn(execute: Boolean, resultInfo: ResultInfo<UserInfo>?) {
    }

    override fun validate(execute: Boolean, resultInfo: ResultInfo<UserInfo>?) {
    }

    override fun resetPassword(execute: Boolean, resultInfo: ResultInfo<UserInfo>?) {
    }

    private fun jumpToMain(){
        startActivity(Intent(this@AuthActivity, MainActivity::class.java))
        finish()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(event!!.keyCode == KeyEvent.KEYCODE_BACK){
            if(isSignUpShow){
                layoutSignIn.visibility = View.VISIBLE
                layoutSignUp.visibility = View.GONE
                toolBarMain.navigationIcon = null
                isSignUpShow = false
                return true
            }else if (isForgotShow){
                layoutSignIn.visibility = View.VISIBLE
                layoutResetPassword.visibility = View.GONE
                toolBarMain.navigationIcon = null
                isForgotShow = false
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
