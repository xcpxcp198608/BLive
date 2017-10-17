package com.wiatec.blive.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.wiatec.blive.presenter.BasePresenter

/**
 * Created by patrick on 24/05/2017.
 * create time : 1:45 PM
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseActivity<V , T : BasePresenter<V>> : AppCompatActivity(){

    protected var presenter: T? = null
    abstract fun createPresenter(): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = createPresenter()
        if(presenter != null) presenter!!.attachView(this as V)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(presenter != null) presenter!!.detachView()
    }
}