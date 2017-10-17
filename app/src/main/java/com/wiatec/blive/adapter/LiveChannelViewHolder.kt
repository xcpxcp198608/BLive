package com.wiatec.blive.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.wiatec.blive.R
import kotlinx.android.synthetic.main.item_live_channel.*

/**
 * Created by patrick on 17/10/2017.
 * create time : 5:40 PM
 */
class LiveChannelViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var tvName: TextView = itemView.findViewById(R.id.tvName) as TextView
    var ivIcon: ImageView = itemView.findViewById(R.id.ivIcon) as ImageView

}