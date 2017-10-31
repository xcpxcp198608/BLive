package com.wiatec.blive.adapter

import android.graphics.Bitmap
import android.view.View
import com.px.common.adapter.BaseRecycleAdapter
import com.px.common.image.ImageMaster
import com.px.common.utils.Logger
import com.px.common.utils.MediaUtil
import com.wiatec.blive.R
import com.wiatec.blive.pojo.LiveRecordsInfo

/**
 * Created by patrick on 17/10/2017.
 * create time : 5:40 PM
 */
class LiveRecordsAdapter(private var liveRecordsList: List<LiveRecordsInfo>):
        BaseRecycleAdapter<LiveRecordsViewHolder>() {

    override fun setLayoutId(): Int = R.layout.item_live_records

    override fun createHolder(view: View?): LiveRecordsViewHolder = LiveRecordsViewHolder(view!!)

    override fun bindHolder(holderLiveRecords: LiveRecordsViewHolder?, position: Int) {
        try {
            val liveRecordsInfo = liveRecordsList[position]
            if(holderLiveRecords != null) {
                holderLiveRecords.tvTitle.text = liveRecordsInfo.title
                holderLiveRecords.tvModifyTime.text = liveRecordsInfo.modifyTime
                holderLiveRecords.ivPreview
                        .setImageBitmap(MediaUtil.getVideoThumbnail(liveRecordsInfo.playUrl))
            }
        }catch (e: Exception){
            Logger.d(e.message)
        }
    }

    override fun getItemCounts(): Int = liveRecordsList.size

    fun notifyChange(liveRecordsList: List<LiveRecordsInfo>){
        this.liveRecordsList = liveRecordsList
        notifyDataSetChanged()
    }
}