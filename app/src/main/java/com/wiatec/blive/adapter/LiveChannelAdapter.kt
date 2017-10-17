package com.wiatec.blive.adapter

import android.view.View
import com.px.common.adapter.BaseRecycleAdapter
import com.px.common.image.ImageMaster
import com.wiatec.blive.R
import com.wiatec.blive.pojo.ChannelInfo

/**
 * Created by patrick on 17/10/2017.
 * create time : 5:40 PM
 */
class LiveChannelAdapter(private val channelList: List<ChannelInfo>): BaseRecycleAdapter<LiveChannelViewHolder>() {

    override fun setLayoutId(): Int = R.layout.item_live_channel

    override fun createHolder(view: View?): LiveChannelViewHolder = LiveChannelViewHolder(view!!)

    override fun bindHolder(holderLive: LiveChannelViewHolder?, position: Int) {
        val channelInfo = channelList[position]
        if(holderLive != null) {
            holderLive.tvName.text = channelInfo.name
            ImageMaster.load(channelInfo.icon, holderLive.ivIcon,
                    R.drawable.holder,
                    R.drawable.holder)
        }
    }

    override fun getItemCounts(): Int = channelList.size
}