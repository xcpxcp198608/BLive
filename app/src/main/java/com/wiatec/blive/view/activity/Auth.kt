package com.wiatec.blive.view.activity

import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.pojo.TokenInfo
import com.wiatec.blive.pojo.UserInfo

/**
 * Created by patrick on 16/10/2017.
 * create time : 5:26 PM
 */
interface Auth {
    fun signUp(execute: Boolean, resultInfo: ResultInfo<UserInfo>?)
    fun signIn(execute: Boolean, resultInfo: ResultInfo<TokenInfo>?)
    fun resetPassword(execute: Boolean, resultInfo: ResultInfo<UserInfo>?)
}