package com.wiatec.blive.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.wiatec.blive.R
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by patrick on 17/10/2017.
 * create time : 5:40 PM
 */
class LiveRecordsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var tvTitle: TextView = itemView.findViewById(R.id.tvTitle) as TextView
    var tvModifyTime: TextView = itemView.findViewById(R.id.tvModifyTime) as TextView
    var ivPreview: ImageView = itemView.findViewById(R.id.ivPreview) as ImageView

}