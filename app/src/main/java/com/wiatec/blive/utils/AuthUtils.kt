package com.wiatec.blive.utils

import android.text.TextUtils
import com.px.common.utils.SPUtil
import com.wiatec.blive.instance.KEY_AUTH_TOKEN

/**
 * Created by patrick on 24/10/2017.
 * create time : 4:18 PM
 */
object AuthUtils {

    fun isSignin(): Boolean{
        val token = SPUtil.get(KEY_AUTH_TOKEN, "") as String
        return !TextUtils.isEmpty(token)
    }
}