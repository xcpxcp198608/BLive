package com.wiatec.blive.presenter

import com.px.common.utils.SPUtil
import com.wiatec.blive.instance.KEY_AUTH_USER_ID
import com.wiatec.blive.model.ChannelProvider
import com.wiatec.blive.model.LoadListener
import com.wiatec.blive.pojo.ChannelInfo
import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.view.activity.Push

/**
 * Created by patrick on 21/10/2017.
 * create time : 3:15 PM
 */
class PushPresenter(val push: Push): BasePresenter<Push>() {

    private val channelProvider = ChannelProvider()

    fun updateChannelTitleAndMessage(channelInfo: ChannelInfo){
        channelProvider.updateChannelTitleAndMessage(channelInfo, object : LoadListener<ResultInfo<ChannelInfo>>{
            override fun onSuccess(execute: Boolean, t: ResultInfo<ChannelInfo>?) {
                push.updateChannelName(execute, t)
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