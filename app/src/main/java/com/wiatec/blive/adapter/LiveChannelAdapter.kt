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
                holderLive.tvTitle.text = channelInfo.title
                holderLive.tvUserName.text = channelInfo.userInfo!!.username
                holderLive.tvStartTime.text = channelInfo.startTime!!.substring(0,
                        channelInfo.startTime!!.length -2)
                if(channelInfo.price <= 0) {
                    holderLive.tvPrice.visibility = View.GONE
                }else {
                    holderLive.tvPrice.text = "$" + channelInfo.price.toString()
                    holderLive.tvPrice.visibility = View.VISIBLE
                }
                ImageMaster.load(channelInfo.userInfo!!.icon, holderLive.civIcon,
                        R.drawable.img_holder_icon,
                        R.drawable.img_error_icon)
                ImageMaster.load(channelInfo.preview, holderLive.ivPreview,
                        R.drawable.img_holder_preview,
                        R.drawable.img_error_preview)
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