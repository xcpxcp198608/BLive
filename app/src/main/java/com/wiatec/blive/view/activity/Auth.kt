package com.wiatec.blive.view.activity

import com.wiatec.blive.pojo.*

/**
 * Created by patrick on 16/10/2017.
 * create time : 5:26 PM
 */
interface Auth {
    fun signUp(execute: Boolean, resultInfo: ResultInfo<UserInfo>?)
    fun signIn(execute: Boolean, resultInfo: ResultInfo<TokenInfo>?)
    fun resetPassword(execute: Boolean, resultInfo: ResultInfo<UserInfo>?)
    fun getPush(execute: Boolean, pushInfo: PushInfo?)
    fun updateChannel(execute: Boolean, resultInfo: ResultInfo<ChannelInfo>?)
}