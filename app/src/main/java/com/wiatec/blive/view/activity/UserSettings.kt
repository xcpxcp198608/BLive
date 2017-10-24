package com.wiatec.blive.view.activity

import com.wiatec.blive.pojo.ChannelInfo
import com.wiatec.blive.pojo.ResultInfo

/**
 * Created by patrick on 24/10/2017.
 * create time : 3:00 PM
 */
interface UserSettings {

    fun onUploadPreviewImage(execute: Boolean, resultInfo: ResultInfo<ChannelInfo>?)
}