package com.wiatec.blive.pay

import android.content.Context
import android.content.Intent
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalService

const val SANDBOX_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX
const val PRODUCTION_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION
//const val CLIENT_ID = "Aa1B25_Mfr9Hzf18kOungjSCZaj3k6_b4-3sMpnX8HxfBY65eJfP3E0t2qO90-Nue7VAQpgJAk-ncudZ"
const val CLIENT_ID = "AYXBO1fwYWfX15Sg4KjgzFS9Mkf_XCIpZtkivQ7PY80BV4PzD6xND_pWZbarz25HxI1CdvPe2Ls6D1yA"
const val PAY_REQUEST_CODE = 0X1206

object PayPalConfig{

    val configuration = PayPalConfiguration()
        .environment(SANDBOX_ENVIRONMENT)
        .clientId(CLIENT_ID)!!

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
