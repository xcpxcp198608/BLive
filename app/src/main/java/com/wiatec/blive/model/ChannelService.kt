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

    /**
     * get all available channel list
     */
    @GET("channel/")
    fun listChannel(): Call<List<ChannelInfo>>

    /**
     * update channel title and message
     * params: title, message, user id
     */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT("channel/update/1")
    fun updateChannelTitleAndMessage(@Body channelInfo: ChannelInfo): Call<ResultInfo<ChannelInfo>>

    /**
     * update channel price
     * params: price, user id
     */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT("channel/update/4")
    fun updatePrice(@Body channelInfo: ChannelInfo): Call<ResultInfo<ChannelInfo>>

    /**
     * update channel status
     * params: activate: 1->activate 0->deactivate, user id
     */
    @PUT("channel/status/{activate}/{userId}")
    fun updateChannelStatus(@Path("activate") activate:Int,  @Path("userId")userId: Int):
            Call<ResultInfo<ChannelInfo>>

}