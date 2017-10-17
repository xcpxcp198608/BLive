package com.wiatec.blive.pojo

class ChannelInfo {

    var id: Int = 0
    var name: String? = null
    var url: String? = null
    var category: String? = null
    var isAvailable: Boolean = false
    /**
     * 1:default live
     */
    var type: Int = 0
    var startTime: String? = null
    var userId: Int = 0

    override fun toString(): String {
        return "ChannelInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", category='" + category + '\'' +
                ", available=" + isAvailable +
                ", type=" + type +
                ", startTime='" + startTime + '\'' +
                ", userId=" + userId +
                '}'
    }
}
