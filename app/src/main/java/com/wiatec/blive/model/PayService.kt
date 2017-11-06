package com.wiatec.blive.model

import com.wiatec.blive.pay.PayResultInfo
import com.wiatec.blive.pojo.ResultInfo
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by patrick on 06/11/2017.
 * create time : 2:30 PM
 */
interface PayService {

    @POST("pay/verify/{payerId}/{publisherId}/{paymentId}")
    fun validate(@Path("payerId") payerId: Int,
                 @Path("publisherId") publisherId: Int,
                 @Path("paymentId") paymentId: String): Call<ResultInfo<PayResultInfo>>

    @POST("pay/verify/{payerId}/{publisherId}")
    fun validate1(@Path("payerId") payerId: Int,
                 @Path("publisherId") publisherId: Int): Call<ResultInfo<PayResultInfo>>
}