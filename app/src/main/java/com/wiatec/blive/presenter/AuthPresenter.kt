package com.wiatec.blive.presenter

import com.wiatec.blive.model.AuthProvider
import com.wiatec.blive.model.LoadListener
import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.pojo.TokenInfo
import com.wiatec.blive.pojo.UserInfo
import com.wiatec.blive.view.activity.Auth

/**
 * Created by patrick on 16/10/2017.
 * create time : 5:27 PM
 */
class AuthPresenter(val auth: Auth): BasePresenter<Auth>(){

    private val authProvider: AuthProvider = AuthProvider()

    fun signUp(userInfo: UserInfo){
        authProvider.signUp(userInfo, object: LoadListener<ResultInfo<UserInfo>>{
            override fun onSuccess(execute: Boolean, t: ResultInfo<UserInfo>?) {
                if(t == null) return
                auth.signUp(execute, t)
            }

            override fun onFailure(e: String) {
            }
        })
    }

    fun signIn(userInfo: UserInfo){
        authProvider.signIn(userInfo, object: LoadListener<ResultInfo<TokenInfo>>{
            override fun onSuccess(execute: Boolean, t: ResultInfo<TokenInfo>?) {
                if(t == null) return
                auth.signIn(execute, t)
            }

            override fun onFailure(e: String) {
            }
        })
    }

    fun resetPassword(userInfo: UserInfo){
        authProvider.resetPassword(userInfo, object: LoadListener<ResultInfo<UserInfo>>{
            override fun onSuccess(execute: Boolean, t: ResultInfo<UserInfo>?) {
                if(t == null) return
                auth.resetPassword(execute, t)
            }

            override fun onFailure(e: String) {
            }
        })
    }
}