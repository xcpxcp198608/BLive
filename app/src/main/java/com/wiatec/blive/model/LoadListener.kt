package com.wiatec.blive.model

/**
 * Created by patrick on 14/10/2017.
 * create time : 9:51 AM
 */
interface LoadListener<in T> {

    fun onSuccess(execute: Boolean, t: T?)
    fun onFailure(e: String)
}