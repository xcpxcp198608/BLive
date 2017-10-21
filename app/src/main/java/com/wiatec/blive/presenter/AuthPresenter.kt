package com.wiatec.blive.presenter

import com.wiatec.blive.model.AuthProvider
import com.wiatec.blive.model.ChannelProvider
import com.wiatec.blive.model.LoadListener
import com.wiatec.blive.pojo.*
import com.wiatec.blive.view.activity.Auth

/**
 * Created by patrick on 16/10/2017.
 * create time : 5:27 PM
 */
class AuthPresenter(val auth: Auth): BasePresenter<Auth>(){

    private val authProvider: AuthProvider = AuthProvider()
    private val channelProvider: ChannelProvider = ChannelProvider()

    fun signUp(userInfo: UserInfo){
        authProvider.signUp(userInfo, object: LoadListener<ResultInfo<UserInfo>>{
            override fun onSuccess(execute: Boolean, t: ResultInfo<UserInfo>?) {
                auth.signUp(execute, t)
            }

            override fun onFailure(e: String) {
            }
        })
    }

    fun signIn(userInfo: UserInfo){
        authProvider.signIn(userInfo, object: LoadListener<ResultInfo<TokenInfo>>{
            override fun onSuccess(execute: Boolean, t: ResultInfo<TokenInfo>?) {
                auth.signIn(execute, t)
            }

            override fun onFailure(e: String) {
            }
        })
    }

    fun resetPassword(userInfo: UserInfo){
        authProvider.resetPassword(userInfo, object: LoadListener<ResultInfo<UserInfo>>{
            override fun onSuccess(execute: Boolean, t: ResultInfo<UserInfo>?) {
                auth.resetPassword(execute, t)
            }

            override fun onFailure(e: String) {
            }
        })
    }

    fun getPush(username: String, token: String){
        authProvider.getPush(username, token, object : LoadListener<PushInfo>{
            override fun onSuccess(execute: Boolean, t: PushInfo?) {
                auth.getPush(execute, t)
            }

            override fun onFailure(e: String) {
            }
        })
    }

    fun updateChannel(channelInfo: ChannelInfo){
        channelProvider.updateChannel(channelInfo, object : LoadListener<ResultInfo<ChannelInfo>>{
            override fun onSuccess(execute: Boolean, t: ResultInfo<ChannelInfo>?) {
                auth.updateChannel(execute, t)
            }

            override fun onFailure(e: String) {

            }
        })
    }

}