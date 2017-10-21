package com.wiatec.blive.view.activity

import com.wiatec.blive.pojo.ChannelInfo
import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.pojo.TokenInfo

/**
 * Created by patrick on 21/10/2017.
 * create time : 11:40 AM
 */
interface Push {

    fun updateChannelName(execute: Boolean, resultInfo: ResultInfo<ChannelInfo>?)
}