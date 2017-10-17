package com.wiatec.blive.model

import com.wiatec.blive.pojo.ChannelInfo
import com.wiatec.blive.pojo.ResultInfo
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by patrick on 17/10/2017.
 * create time : 4:48 PM
 */
interface ChannelService {

    @GET("channel/")
    fun listChannel(): Call<List<ChannelInfo>>
}