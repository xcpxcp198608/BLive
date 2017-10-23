package com.wiatec.blive.model

import com.wiatec.blive.pojo.ChannelInfo
import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.pojo.TokenInfo
import com.wiatec.blive.pojo.UserInfo
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by patrick on 16/10/2017.
 * create time : 5:34 PM
 */
interface AuthService {

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("user/signup")
    fun signUp(@Body userInfo: UserInfo): Call<ResultInfo<UserInfo>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("user/signin")
    fun signIn(@Body userInfo: UserInfo): Call<ResultInfo<TokenInfo>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("user/validate")
    fun validate(@Body userInfo: UserInfo): Call<ResultInfo<UserInfo>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("user/reset")
    fun resetPassword(@Body userInfo: UserInfo): Call<ResultInfo<UserInfo>>

    @POST("user/{userId}")
    fun user(@Path("userId") userId: Int): Call<UserInfo>

}