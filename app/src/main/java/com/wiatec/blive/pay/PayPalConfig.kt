package com.wiatec.blive.pay

import android.content.Context
import android.content.Intent
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalService

const val PRODUCTION_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION
//const val CLIENT_ID = "AWCnwwXwXADHirQ1CZzGJVftxo33UqGkTw80bHnwkWMU5uF4k4CRD9MCgz2luu-sF9Z-073HCfdxCmiK" //h live
const val CLIENT_ID = "AUIoEwXGBWT4jsSRICOU5HTTXrop7OFsEQrIyANkbwFxL982Yu2eSMkolwAa8E0zVslo6d2E7U0mWvvz" //j live
const val PAY_REQUEST_CODE = 0X1026

object PayPalConfig{

    val configuration = PayPalConfiguration()
            .environment(PRODUCTION_ENVIRONMENT)
            .clientId(CLIENT_ID)
            .acceptCreditCards(true)!!

    /**
     * 在需要调用支付页面的onCreate中启动paypal服务
     */
    fun startPayPalService(context: Context){
        val intent = Intent(context, PayPalService::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration)
        context.startService(intent)
    }

    /**
     * 在调用支付服务的页面的onDestroy中停止paypal服务
     */
    fun stopPayPalService(context: Context){
        context.stopService(Intent(context, PayPalService::class.java))
    }
}
