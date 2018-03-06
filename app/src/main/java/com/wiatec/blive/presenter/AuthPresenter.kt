package com.wiatec.blive.presenter

import com.wiatec.blive.model.UserProvider
import com.wiatec.blive.model.ChannelProvider
import com.wiatec.blive.model.LoadListener
import com.wiatec.blive.pojo.*
import com.wiatec.blive.view.activity.Auth

/**
 * Created by patrick on 16/10/2017.
 * create time : 5:27 PM
 */
class AuthPresenter(val auth: Auth): BasePresenter<Auth>(){

    private val userProvider: UserProvider = UserProvider()

    fun signUp(userInfo: UserInfo){
        userProvider.signUp(userInfo, object: LoadListener<ResultInfo<UserInfo>>{
            override fun onSuccess(execute: Boolean, t: ResultInfo<UserInfo>?) {
                auth.signUp(execute, t)
            }

            override fun onFailure(e: String) {
            }
        })
    }

    fun signIn(userInfo: UserInfo){
        userProvider.signIn(userInfo, object: LoadListener<ResultInfo<TokenInfo>>{
            override fun onSuccess(execute: Boolean, t: ResultInfo<TokenInfo>?) {
                auth.signIn(execute, t)
            }

            override fun onFailure(e: String) {
            }
        })
    }

    fun resetPassword(userInfo: UserInfo){
        userProvider.resetPassword(userInfo, object: LoadListener<ResultInfo<UserInfo>>{
            override fun onSuccess(execute: Boolean, t: ResultInfo<UserInfo>?) {
                auth.resetPassword(execute, t)
            }

            override fun onFailure(e: String) {
            }
        })
    }


}