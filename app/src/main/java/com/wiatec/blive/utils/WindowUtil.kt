package com.wiatec.blive.utils

import android.content.Context

/**
 * Created by patrick on 29/05/2017.
 * create time : 9:35 AM
 */
object WindowUtil{

    fun getStatusBarHeight (context: Context): Int{
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0 ) return context.resources.getDimensionPixelSize(resId) else return 0
    }
}