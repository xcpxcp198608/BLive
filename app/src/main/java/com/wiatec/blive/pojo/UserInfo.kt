package com.wiatec.blive.pojo

class UserInfo {

    var id: Int = 0
    var username: String? = null
    var password: String? = null
    var email: String? = null
    var phone: String? = null
    var isStatus: Boolean = false
    var registerTime: String? = null
    var channelInfoList: List<ChannelInfo>? = null

    constructor() {}

    constructor(username: String) {
        this.username = username
    }

    constructor(username: String, password: String) {
        this.username = username
        this.password = password
    }

    constructor(username: String, password: String, email: String, phone: String) {
        this.username = username
        this.password = password
        this.email = email
        this.phone = phone
    }

    override fun toString(): String {
        return "UserInfo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", status=" + isStatus +
                ", registerTime='" + registerTime + '\'' +
                ", channelInfoList=" + channelInfoList +
                '}'
    }
}
