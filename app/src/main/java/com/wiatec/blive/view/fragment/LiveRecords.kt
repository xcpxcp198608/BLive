package com.wiatec.blive.view.fragment

import com.wiatec.blive.pojo.LiveRecordsInfo

/**
 * Created by patrick on 30/10/2017.
 * create time : 4:14 PM
 */
interface LiveRecords {

    fun onLoadLiveRecords(execute: Boolean, liveRecordsInfoList: List<LiveRecordsInfo>?)
}