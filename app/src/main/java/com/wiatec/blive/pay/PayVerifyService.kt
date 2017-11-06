package com.wiatec.blive.pay

import com.wiatec.blive.pojo.ResultInfo
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by patrick on 01/11/2017.
 * create time : 9:56 AM
 */
interface PayVerifyService {

    @POST("pay/verify/{paymentId}")
    fun verify(@Path("paymentId") paymentId: String): Call<ResultInfo<PayResultInfo>>
}