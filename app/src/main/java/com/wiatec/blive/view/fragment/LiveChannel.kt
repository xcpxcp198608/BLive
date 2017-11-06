package com.wiatec.blive.view.fragment

import com.wiatec.blive.pay.PayResultInfo
import com.wiatec.blive.pojo.ChannelInfo
import com.wiatec.blive.pojo.ResultInfo

/**
 * Created by patrick on 17/10/2017.
 * create time : 5:20 PM
 */
interface LiveChannel {
    fun onListChannel(execute: Boolean, channelInfoList: List<ChannelInfo>?)
    fun onValidatePay(execute: Boolean, resultInfo: ResultInfo<PayResultInfo>?)
}