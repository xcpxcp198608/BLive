package com.wiatec.blive.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wiatec.blive.R

/**
 * Created by patrick on 24/05/2017.
 * create time : 3:49 PM
 */
class FragmentLive : BaseFragment(){

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_live , container , false)
        return view
    }
}