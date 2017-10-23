package com.wiatec.blive.view.activity

import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.pojo.UserInfo

/**
 * Created by patrick on 13/10/2017.
 * create time : 5:15 PM
 */
interface Main {

    fun uploadIconCallBack(execute: Boolean, resultInfo: ResultInfo<UserInfo>?)
}