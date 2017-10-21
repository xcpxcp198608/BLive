package com.wiatec.blive.pojo

class ChannelInfo {

    var id: Int = 0
    var name: String? = null
    var url: String? = null
    var playUrl: String? = null
    var preview: String? = null
    var category: String? = null
    var isAvailable: Boolean = false
    /**
     * 1:default liveChannel
     */
    var type: Int = 0
    var startTime: String? = null
    var userId: Int = 0
    var userInfo: UserInfo? = null

    constructor() {}

    constructor(userId: Int) {
        this.userId = userId
    }

    constructor(name: String, userId: Int) {
        this.name = name
        this.userId = userId
    }

    constructor(url: String, playUrl: String, userId: Int) {
        this.url = url
        this.playUrl = playUrl
        this.userId = userId
    }

    override fun toString(): String {
        return "ChannelInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", playUrl='" + playUrl + '\'' +
                ", preview='" + preview + '\'' +
                ", category='" + category + '\'' +
                ", available=" + isAvailable +
                ", type=" + type +
                ", startTime='" + startTime + '\'' +
                ", userId=" + userId +
                ", userInfo=" + userInfo +
                '}'
    }
}
