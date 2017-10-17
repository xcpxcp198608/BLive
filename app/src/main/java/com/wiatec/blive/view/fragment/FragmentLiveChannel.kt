package com.wiatec.blive.view.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.px.common.utils.Logger
import com.wiatec.blive.R
import com.wiatec.blive.adapter.LiveChannelAdapter
import com.wiatec.blive.pojo.ChannelInfo
import com.wiatec.blive.presenter.LiveFragmentPresenter
import kotlinx.android.synthetic.main.fragment_live.*

/**
 * Created by patrick on 24/05/2017.
 * create time : 3:49 PM
 */
class FragmentLiveChannel : BaseFragment<LiveChannel, LiveFragmentPresenter>(), LiveChannel {

    override fun createPresenter(): LiveFragmentPresenter = LiveFragmentPresenter(this)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_live , container , false)
        presenter!!.listChannel()
        return view
    }

    override fun listChannel(execute: Boolean, channelInfoList: List<ChannelInfo>?) {
        if(execute && channelInfoList != null){
            Logger.d(channelInfoList.toString())
            val liveChannelAdapter = LiveChannelAdapter(channelInfoList)
            rcvLive.adapter = liveChannelAdapter
            rcvLive.layoutManager = LinearLayoutManager(context)
        }
    }
}