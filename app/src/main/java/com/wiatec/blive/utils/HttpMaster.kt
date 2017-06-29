package com.px.kotlin.utils

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

/**
 * Created by patrick on 22/05/2017.
 * create time : 5:06 PM
 */
class HttpMaster(val url:String){

    interface OnLoadListener{
        fun onSuccess(s:String)
    }

    fun execute(onLoadListener: OnLoadListener) {
        doAsync {
            val result = URL(url).readText()
            uiThread {  onLoadListener.onSuccess(result) }
        }
    }
}