package com.wiatec.blive.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.wiatec.blive.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_live_channel.*

/**
 * Created by patrick on 17/10/2017.
 * create time : 5:40 PM
 */
class LiveChannelViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var civIcon: CircleImageView = itemView.findViewById(R.id.civIcon) as CircleImageView
    var tvTitle: TextView = itemView.findViewById(R.id.tvTitle) as TextView
    var tvUserName: TextView = itemView.findViewById(R.id.tvUsername) as TextView
    var tvStartTime: TextView = itemView.findViewById(R.id.tvStartTime) as TextView
    var ivPreview: ImageView = itemView.findViewById(R.id.ivPreview) as ImageView
    var tvPrice: TextView = itemView.findViewById(R.id.tvPrice) as TextView

}