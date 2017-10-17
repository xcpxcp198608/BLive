package com.wiatec.blive.model

import com.wiatec.blive.pojo.ResultInfo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by patrick on 16/10/2017.
 * create time : 5:34 PM
 */
interface AuthService {

    @POST("user/signup")
    fun signUp(@Body userInfo: UserInfo): Call<ResultInfo<UserInfo>>

    @POST("user/signin")
    fun signIn(@Body userInfo: UserInfo): Call<ResultInfo<UserInfo>>

    @POST("user/validate")
    fun validate(@Body userInfo: UserInfo): Call<ResultInfo<UserInfo>>

    @POST("user/reset")
    fun resetPassword(@Body userInfo: UserInfo): Call<ResultInfo<UserInfo>>
}