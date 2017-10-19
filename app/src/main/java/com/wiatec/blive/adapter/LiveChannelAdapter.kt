package com.wiatec.blive.adapter

import android.view.View
import com.px.common.adapter.BaseRecycleAdapter
import com.px.common.image.ImageMaster
import com.px.common.utils.Logger
import com.wiatec.blive.R
import com.wiatec.blive.pojo.ChannelInfo

/**
 * Created by patrick on 17/10/2017.
 * create time : 5:40 PM
 */
class LiveChannelAdapter(private var channelList: List<ChannelInfo>):
        BaseRecycleAdapter<LiveChannelViewHolder>() {

    override fun setLayoutId(): Int = R.layout.item_live_channel

    override fun createHolder(view: View?): LiveChannelViewHolder = LiveChannelViewHolder(view!!)

    override fun bindHolder(holderLive: LiveChannelViewHolder?, position: Int) {
        try {
            val channelInfo = channelList[position]
            if(holderLive != null) {
                holderLive.tvTitle.text = channelInfo.name
                holderLive.tvUserName.text = channelInfo.userInfo!!.username
                holderLive.tvStartTime.text = channelInfo.startTime!!.substring(0,
                        channelInfo.startTime!!.length -2)
                ImageMaster.load(channelInfo.preview, holderLive.ivPreview,
                        R.drawable.holder1,
                        R.drawable.holder1)
            }
        }catch (e: Exception){
            Logger.d(e.message)
        }
    }

    override fun getItemCounts(): Int = channelList.size

    fun notifyChange(channelList: List<ChannelInfo>){
        this.channelList = channelList
        notifyDataSetChanged()
    }
}