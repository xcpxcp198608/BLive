package com.wiatec.blive.model

import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.pojo.TokenInfo
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by patrick on 17/10/2017.
 * create time : 4:20 PM
 */
interface TokenService {

    @FormUrlEncoded
    @POST("token/validate")
    fun validate(@Field("token") token: String): Call<ResultInfo<TokenInfo>>
}