package com.wiatec.blive.pay

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import java.math.BigDecimal
import com.paypal.android.sdk.d
import com.paypal.android.sdk.payments.PaymentConfirmation
import com.px.common.utils.Logger
import org.json.JSONException


/**
 * Created by patrick on 31/10/2017.
 * create time : 10:16 AM
 */


object PayPalManager {

    /**
     * activity pay
     */
    fun pay(activity: Activity, payInfo: PayInfo){
        val payment = PayPalPayment( BigDecimal(payInfo.price.toString()),
                payInfo.currency,
                payInfo.description,
                PayPalPayment.PAYMENT_INTENT_SALE)
        val intent = Intent(activity, PaymentActivity::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, PayPalConfig.configuration)
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)
        activity.startActivityForResult(intent, PAY_REQUEST_CODE)
    }

    /**
     * fragment pay
     */
    fun pay(fragment: Fragment, payInfo: PayInfo){
        try {
            val payment = PayPalPayment(BigDecimal(payInfo.price.toString()),
                    payInfo.currency,
                    payInfo.description,
                    PayPalPayment.PAYMENT_INTENT_SALE)
            val intent = Intent(fragment.context, PaymentActivity::class.java)
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, PayPalConfig.configuration)
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)
            fragment.startActivityForResult(intent, PAY_REQUEST_CODE)
        }catch (e: Exception){
            Logger.d(e.message)
        }
    }

    /**
     * 支付页面的onActivityResult中调用，支付页面需要实现OnPayResultListener接口，在实现方法中处理回调逻辑
     */
    fun payResult(requestCode: Int, resultCode: Int, data: Intent?,
                  onPayResultListener: OnPayResultListener) {
        if (requestCode == PAY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                onPayResultListener.customerCancel("user cancel pay")
                return
            }
            if (resultCode == Activity.RESULT_OK) {
                try {
                    val paymentConfirmation = data!!.getParcelableExtra<PaymentConfirmation>(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                    if (paymentConfirmation != null) {
                        val paymentId = paymentConfirmation.toJSONObject().getJSONObject("response").getString("id")
                        Logger.d(paymentId)
                        if (!TextUtils.isEmpty(paymentId)) {
                            onPayResultListener.paySuccess(paymentId)
                        }
                        // 保留订单付款信息
                        //savePaymentInfo();
                        // 展示成功界面
                        //showSuccessful();
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        }
    }

    interface OnPayResultListener {
        fun paySuccess(paymentId: String)
        fun customerCancel(error: String)
    }
}