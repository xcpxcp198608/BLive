package com.wiatec.blive.presenter

import com.px.common.utils.Logger
import com.wiatec.blive.model.ChannelProvider
import com.wiatec.blive.model.LoadListener
import com.wiatec.blive.pojo.ChannelInfo
import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.view.activity.UserSettings
import java.io.File

/**
 * Created by patrick on 24/10/2017.
 * create time : 3:03 PM
 */
class UserSettingsPresenter(val userSettings: UserSettings): BasePresenter<UserSettings>() {

    private val channelProvider = ChannelProvider()

    fun uploadPreviewImage(file: File){
        channelProvider.uploadPreviewImage(file, object: LoadListener<ResultInfo<ChannelInfo>>{
            override fun onSuccess(execute: Boolean, t: ResultInfo<ChannelInfo>?) {
                userSettings.onUploadPreviewImage(execute, t)
            }

            override fun onFailure(e: String) {
            }
        })
    }


}