package com.wiatec.blive.pojo

/**
 * Created by patrick on 21/10/2017.
 * create time : 10:02 AM
 */

class PushInfo {

    var error_code: Int = 0
    var error_msg: String? = null
    var token: String? = null
    var data: Data? = null

    override fun toString(): String {
        return "PushInfo{" +
                "error_code=" + error_code +
                ", error_msg='" + error_msg + '\'' +
                ", token='" + token + '\'' +
                ", data=" + data +
                '}'
    }

    inner class Data {
        var username: String? = null
        var push_url: String? = null
        var push_key: String? = null
        var push_full_url: String? = null
        var play_url: String? = null

        override fun toString(): String {
            return "Data{" +
                    "username='" + username + '\'' +
                    ", push_url='" + push_url + '\'' +
                    ", push_key='" + push_key + '\'' +
                    ", push_full_url='" + push_full_url + '\'' +
                    ", play_url='" + play_url + '\'' +
                    '}'
        }
    }
}
