package com.wiatec.blive.presenter

import com.wiatec.blive.model.LiveRecordsProvider
import com.wiatec.blive.model.LoadListener
import com.wiatec.blive.pojo.LiveRecordsInfo
import com.wiatec.blive.view.fragment.LiveRecords

/**
 * Created by patrick on 30/10/2017.
 * create time : 4:15 PM
 */
class RecordsFragmentPresenter(val liveRecords: LiveRecords) : BasePresenter<LiveRecords>() {

    private val liveRecordProvider: LiveRecordsProvider = LiveRecordsProvider()

    fun loadLiveRecords(){
        liveRecordProvider.loadLiveRecords(object : LoadListener<List<LiveRecordsInfo>>{
            override fun onSuccess(execute: Boolean, t: List<LiveRecordsInfo>?) {
                liveRecords.onLoadLiveRecords(execute, t)
            }

            override fun onFailure(e: String) {

            }
        })
    }
}