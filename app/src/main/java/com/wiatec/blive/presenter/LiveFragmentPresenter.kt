package com.wiatec.blive.presenter

import com.wiatec.blive.model.ChannelProvider
import com.wiatec.blive.model.LoadListener
import com.wiatec.blive.pojo.ChannelInfo
import com.wiatec.blive.view.fragment.LiveChannel

/**
 * Created by patrick on 17/10/2017.
 * create time : 5:17 PM
 */
class LiveFragmentPresenter(val liveChannel: LiveChannel): BasePresenter<LiveChannel>(){

    private val channelProvider = ChannelProvider()

    fun listChannel(){
        channelProvider.listChannel(object: LoadListener<List<ChannelInfo>>{
            override fun onSuccess(execute: Boolean, t: List<ChannelInfo>?) {
                liveChannel.listChannel(execute, t)
            }

            override fun onFailure(e: String) {
            }
        })
    }
}