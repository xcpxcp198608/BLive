package com.wiatec.blive.pay

import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.utils.RMaster
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by patrick on 01/11/2017.
 * create time : 9:52 AM
 */
class PayVerify {

    fun verify(paymentId: String){
        RMaster.retrofit.create(PayVerifyService::class.java)
                .verify(paymentId)
                .enqueue(object: Callback<ResultInfo<PayResultInfo>>{
                    override fun onResponse(call: Call<ResultInfo<PayResultInfo>>?, response: Response<ResultInfo<PayResultInfo>>?) {
                    }

                    override fun onFailure(call: Call<ResultInfo<PayResultInfo>>?, t: Throwable?) {
                    }
                })
    }
}