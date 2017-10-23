package com.wiatec.blive.view.activity

import com.wiatec.blive.pojo.*

/**
 * Created by patrick on 13/10/2017.
 * create time : 5:06 PM
 */
interface Splash {
    fun checkUpgrade(execute: Boolean, upgradeInfo: UpgradeInfo?)
    fun validateToken(execute: Boolean, resultInfo: ResultInfo<TokenInfo>?)
    fun getPush(execute: Boolean, pushInfo: PushInfo?)
    fun updateChannel(execute: Boolean, resultInfo: ResultInfo<ChannelInfo>?)
}