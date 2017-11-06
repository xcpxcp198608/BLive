package com.wiatec.blive.model

import com.px.common.utils.Logger
import com.wiatec.blive.pay.PayResultInfo
import com.wiatec.blive.pojo.ChannelInfo
import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.utils.RMaster
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Path

/**
 * Created by patrick on 06/11/2017.
 * create time : 2:34 PM
 */
class PayProvider {

    fun validatePay1(payerId: Int, publisherId: Int,
                          loadListener: LoadListener<ResultInfo<PayResultInfo>>){
        RMaster.retrofit.create(PayService::class.java)
                .validate1(payerId, publisherId)
                .enqueue(object : Callback<ResultInfo<PayResultInfo>> {
                    override fun onResponse(call: Call<ResultInfo<PayResultInfo>>?, response: Response<ResultInfo<PayResultInfo>>?) {
                        Logger.d(response.toString())
                        if(response == null) {
                            loadListener.onSuccess(false, null)
                            return
                        }
                        val resultInfo = response.body()
                        if(resultInfo != null){
                            loadListener.onSuccess(true, resultInfo)
                        }else{
                            loadListener.onSuccess(false, null)
                        }
                    }

                    override fun onFailure(call: Call<ResultInfo<PayResultInfo>>?, t: Throwable?) {
                        if (t?.message != null) Logger.d(t.message)
                        loadListener.onSuccess(false, null)
                    }
                })
    }

    fun validatePay(payerId: Int, publisherId: Int, paymentId: String,
                    loadListener: LoadListener<ResultInfo<PayResultInfo>>){
        RMaster.retrofit.create(PayService::class.java)
                .validate(payerId, publisherId, paymentId)
                .enqueue(object : Callback<ResultInfo<PayResultInfo>> {
                    override fun onResponse(call: Call<ResultInfo<PayResultInfo>>?, response: Response<ResultInfo<PayResultInfo>>?) {
                        if(response == null) {
                            loadListener.onSuccess(false, null)
                            return
                        }
                        val resultInfo = response.body()
                        if(resultInfo != null){
                            loadListener.onSuccess(true, resultInfo)
                        }else{
                            loadListener.onSuccess(false, null)
                        }
                    }

                    override fun onFailure(call: Call<ResultInfo<PayResultInfo>>?, t: Throwable?) {
                        if (t?.message != null) Logger.d(t.message)
                        loadListener.onSuccess(false, null)
                    }
                })
    }
}