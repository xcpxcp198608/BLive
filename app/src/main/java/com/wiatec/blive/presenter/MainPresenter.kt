package com.wiatec.blive.presenter

import com.px.common.utils.SPUtil
import com.wiatec.blive.instance.KEY_AUTH_USER_ID
import com.wiatec.blive.model.AuthProvider
import com.wiatec.blive.model.ChannelProvider
import com.wiatec.blive.model.LoadListener
import com.wiatec.blive.pojo.ChannelInfo
import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.pojo.UserInfo
import com.wiatec.blive.view.activity.Main
import java.io.File

/**
 * Created by patrick on 24/05/2017.
 * create time : 3:14 PM
 */
class MainPresenter(var main: Main): BasePresenter<Main>() {

    private val authProvider: AuthProvider = AuthProvider()
    private val channelProvider: ChannelProvider = ChannelProvider()

    fun deactivateChannel(){

    }

    fun uploadIcon(file: File){
        authProvider.uploadIcon(file, object : LoadListener<ResultInfo<UserInfo>>{
            override fun onSuccess(execute: Boolean, t: ResultInfo<UserInfo>?) {
                main.uploadIconCallBack(execute, t)
            }

            override fun onFailure(e: String) {
            }
        })
    }

    fun updateChannelStatus(activate: Int){
        val userId = SPUtil.get(KEY_AUTH_USER_ID, 0) as Int
        channelProvider.updateChannelStatus(activate, userId, object : LoadListener<ResultInfo<ChannelInfo>>{
            override fun onSuccess(execute: Boolean, t: ResultInfo<ChannelInfo>?) {

            }

            override fun onFailure(e: String) {

            }
        })
    }
}