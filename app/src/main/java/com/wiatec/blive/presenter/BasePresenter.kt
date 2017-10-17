package com.wiatec.blive.presenter

import java.lang.ref.WeakReference

/**
 * Created by patrick on 13/10/2017.
 * create time : 4:57 PM
 */
abstract class BasePresenter<V> {

    private var presenter = null
    private var weakReference: WeakReference<V>? = null

    fun attachView(v: V){
        if(weakReference == null){
            weakReference = WeakReference(v)
        }
    }

    fun detachView(){
        if(weakReference != null){
            weakReference!!.clear()
            weakReference = null
        }
    }

}