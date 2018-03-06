package com.wiatec.blive.presenter

import com.wiatec.blive.model.UserProvider
import com.wiatec.blive.model.ChannelProvider
import com.wiatec.blive.model.LoadListener
import com.wiatec.blive.pojo.ChannelInfo
import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.pojo.UserInfo
import com.wiatec.blive.view.activity.UserSettings
import java.io.File

/**
 * Created by patrick on 24/10/2017.
 * create time : 3:03 PM
 */
class UserSettingsPresenter(val userSettings: UserSettings): BasePresenter<UserSettings>() {

    private val channelProvider = ChannelProvider()
    private val authProvider = UserProvider()

    fun uploadPreviewImage(file: File){
        channelProvider.uploadPreviewImage(file, object: LoadListener<ResultInfo<ChannelInfo>>{
            override fun onSuccess(execute: Boolean, t: ResultInfo<ChannelInfo>?) {
                userSettings.onUploadPreviewImage(execute, t)
            }

            override fun onFailure(e: String) {
            }
        })
    }

    fun loadUserInfo(){
        authProvider.getUserInfo(object : LoadListener<UserInfo>{
            override fun onSuccess(execute: Boolean, t: UserInfo?) {
                userSettings.onLoadUserInfo(execute, t)
            }

            override fun onFailure(e: String) {

            }
        })
    }

    fun updatePrice(channelInfo: ChannelInfo){
        channelProvider.updatePrice(channelInfo, object : LoadListener<ResultInfo<ChannelInfo>>{
            override fun onSuccess(execute: Boolean, t: ResultInfo<ChannelInfo>?) {
                userSettings.onUpdatePrice(execute, t)
            }

            override fun onFailure(e: String) {
            }
        })
    }


}