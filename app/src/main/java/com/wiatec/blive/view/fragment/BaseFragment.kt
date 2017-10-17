package com.wiatec.blive.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wiatec.blive.presenter.BasePresenter

/**
 * Created by patrick on 24/05/2017.
 * create time : 1:53 PM
 */
abstract class BaseFragment<V, T: BasePresenter<V>> : Fragment(){

    abstract fun createPresenter(): T
    protected var presenter: T? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        presenter = createPresenter()
        presenter!!.attachView(this as V)
    }

    override fun onDetach() {
        super.onDetach()
        presenter!!.detachView()
    }
}