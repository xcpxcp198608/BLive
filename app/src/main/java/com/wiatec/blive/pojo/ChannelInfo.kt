package com.wiatec.blive.pojo

class ChannelInfo {

    var id: Int = 0
    var title: String? = null
    var message: String? = null
    var url: String? = null
    var playUrl: String? = null
    var preview: String? = null
    var category: String? = null
    var isAvailable: Boolean = false
    /**
     * 1:default liveChannel
     */
    var type: Int = 0
    var price: Float = 0f
    var startTime: String? = null
    var userId: Int = 0
    var userInfo: UserInfo? = null

    constructor() {}

    constructor(userId: Int) {
        this.userId = userId
    }

    constructor(title: String, userId: Int) {
        this.title = title
        this.userId = userId
    }

    constructor(title: String, message: String, userId: Int, holder:Int) {
        this.title = title
        this.userId = userId
        this.message = message
    }

    constructor(url: String, playUrl: String, userId: Int) {
        this.url = url
        this.playUrl = playUrl
        this.userId = userId
    }

    override fun toString(): String {
        return "ChannelInfo(id=$id, title=$title, message=$message, url=$url, playUrl=$playUrl, preview=$preview, category=$category, isAvailable=$isAvailable, type=$type, price=$price, startTime=$startTime, userId=$userId, userInfo=$userInfo)"
    }


}
