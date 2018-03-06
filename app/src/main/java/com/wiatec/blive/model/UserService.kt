package com.wiatec.blive.model

import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.pojo.TokenInfo
import com.wiatec.blive.pojo.UserInfo
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by patrick on 16/10/2017.
 * create time : 5:34 PM
 */
interface UserService {

    @POST("user/signup")
    fun signUp(@Field("username") username: String,
               @Field("password") password: String,
               @Field("email") email: String,
               @Field("phone") phone: String): Call<ResultInfo<UserInfo>>

    @FormUrlEncoded
    @POST("user/signin")
    fun signIn(@Field("username") username: String, @Field("password") password: String): Call<ResultInfo<TokenInfo>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("user/validate")
    fun validate(@Body userInfo: UserInfo): Call<ResultInfo<UserInfo>>

    @FormUrlEncoded
    @POST("user/reset")
    fun resetPassword(@Field("username") username: String, @Field("email") email: String): Call<ResultInfo<UserInfo>>

    @POST("user/{userId}")
    fun getUserInfo(@Path("userId") userId: Int): Call<UserInfo>

}