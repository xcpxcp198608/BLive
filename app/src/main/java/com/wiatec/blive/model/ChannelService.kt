package com.wiatec.blive.model

import com.wiatec.blive.pojo.ChannelInfo
import com.wiatec.blive.pojo.ResultInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*
import java.io.File

/**
 * Created by patrick on 17/10/2017.
 * create time : 4:48 PM
 */
interface ChannelService {

    @GET("channel/")
    fun listChannel(): Call<List<ChannelInfo>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT("channel/update")
    fun updateChannel(@Body channelInfo: ChannelInfo): Call<ResultInfo<ChannelInfo>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT("channel/title")
    fun updateChannelName(@Body channelInfo: ChannelInfo): Call<ResultInfo<ChannelInfo>>

    @PUT("channel/status/{activate}/{userId}")
    fun updateChannelStatus(@Path("activate") activate:Int,  @Path("userId")userId: Int):
            Call<ResultInfo<ChannelInfo>>
}