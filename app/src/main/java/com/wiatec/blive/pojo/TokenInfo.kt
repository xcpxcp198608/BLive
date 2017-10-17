package com.wiatec.blive.pojo

class TokenInfo {

    var id: Int = 0
    var token: String? = null
    var createTime: String? = null
    var userId: Int = 0
    var userInfo: UserInfo? = null

    override fun toString(): String {
        return "TokenInfo{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", createTime='" + createTime + '\'' +
                ", userId=" + userId +
                ", userInfo=" + userInfo +
                '}'
    }
}
