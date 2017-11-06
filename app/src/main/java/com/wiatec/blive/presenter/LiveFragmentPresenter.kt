package com.wiatec.blive.presenter

import android.text.TextUtils
import com.wiatec.blive.model.ChannelProvider
import com.wiatec.blive.model.LoadListener
import com.wiatec.blive.model.PayProvider
import com.wiatec.blive.pay.PayResultInfo
import com.wiatec.blive.pojo.ChannelInfo
import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.view.fragment.LiveChannel

/**
 * Created by patrick on 17/10/2017.
 * create time : 5:17 PM
 */
class LiveFragmentPresenter(val liveChannel: LiveChannel): BasePresenter<LiveChannel>(){

    private val channelProvider = ChannelProvider()
    private val payProvider = PayProvider()

    fun listChannel(){
        channelProvider.listChannel(object: LoadListener<List<ChannelInfo>>{
            override fun onSuccess(execute: Boolean, t: List<ChannelInfo>?) {
                liveChannel.onListChannel(execute, t)
            }

            override fun onFailure(e: String) {
            }
        })
    }

    fun validatePay(payerId: Int, publisherId: Int, paymentId: String){
        if(TextUtils.isEmpty(paymentId)){
            payProvider.validatePay1(payerId, publisherId, object : LoadListener<ResultInfo<PayResultInfo>> {
                override fun onSuccess(execute: Boolean, t: ResultInfo<PayResultInfo>?) {
                    liveChannel.onValidatePay(execute, t)
                }

                override fun onFailure(e: String) {

                }
            })
        }else {
            payProvider.validatePay(payerId, publisherId, paymentId, object : LoadListener<ResultInfo<PayResultInfo>> {
                override fun onSuccess(execute: Boolean, t: ResultInfo<PayResultInfo>?) {
                    liveChannel.onValidatePay(execute, t)
                }

                override fun onFailure(e: String) {

                }
            })
        }
    }
}