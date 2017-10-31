package com.wiatec.blive.pojo

/**
 * Created by patrick on 30/10/2017.
 * create time : 4:19 PM
 */
class LiveRecordsInfo {
    var id: Int = 0
    var title: String? = null
    var playUrl: String? = null
    var preview: String? = null
    var modifyTime: String? = null

    override fun toString(): String =
            "LiveRecordsInfo(id=$id, title=$title, playUrl=$playUrl, preview=$preview, modifyTime=$modifyTime)"


}