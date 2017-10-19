package com.wiatec.blive.view.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.px.common.adapter.BaseRecycleAdapter
import com.px.common.utils.Logger
import com.px.common.utils.NetUtil
import com.wiatec.blive.R
import com.wiatec.blive.adapter.LiveChannelAdapter
import com.wiatec.blive.instance.KEY_URL
import com.wiatec.blive.pojo.ChannelInfo
import com.wiatec.blive.presenter.LiveFragmentPresenter
import com.wiatec.blive.view.activity.PlayActivity
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.fragment_live.*

/**
 * Created by patrick on 24/05/2017.
 * create time : 3:49 PM
 */
class FragmentLiveChannel : BaseFragment<LiveChannel, LiveFragmentPresenter>(), LiveChannel {

    private var liveChannelAdapter: LiveChannelAdapter? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun createPresenter(): LiveFragmentPresenter = LiveFragmentPresenter(this)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_live , container , false)
        presenter!!.listChannel()
        swipeRefreshLayout = view!!.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        swipeRefreshLayout!!.setOnRefreshListener { presenter!!.listChannel() }
        return view
    }

    override fun listChannel(execute: Boolean, channelInfoList: List<ChannelInfo>?) {
        swipeRefreshLayout!!.isRefreshing = false
        if(execute && channelInfoList != null){
            if(liveChannelAdapter == null) {
                liveChannelAdapter = LiveChannelAdapter(channelInfoList)
            }else{
                liveChannelAdapter!!.notifyChange(channelInfoList)
            }
            rcvLive.adapter = liveChannelAdapter
            rcvLive.layoutManager = LinearLayoutManager(context)
            liveChannelAdapter!!.setOnItemClickListener { _, position ->
                val channelInfo = channelInfoList[position]
                playLiveChannel(channelInfo)
            }
        }else{
            if(NetUtil.isConnected()) {
                showErrorSnackNotice("Data obtain failed")
            }else{
                showErrorSnackNotice("network connection error")
            }
        }
    }

    private fun playLiveChannel(channelInfo: ChannelInfo){
        val intent = Intent(context, PlayActivity::class.java)
        intent.putExtra(KEY_URL, channelInfo.url)
        startActivity(intent)
    }

    private fun showErrorSnackNotice(message: String){
        val snackBar = Snackbar.make(linearLayout, message, Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundResource(R.color.colorBlue1)
        val tvContent = snackBar.view.findViewById(R.id.snackbar_text) as TextView
        tvContent.setTextColor(Color.rgb(255, 64, 129))
        snackBar.show()
    }
}