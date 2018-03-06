package com.wiatec.blive.view.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.wiatec.blive.R
import com.wiatec.blive.adapter.LiveRecordsAdapter
import com.wiatec.blive.instance.KEY_PLAY_TYPE
import com.wiatec.blive.instance.KEY_PLAY_TYPE_LOCAL
import com.wiatec.blive.instance.KEY_URL
import com.wiatec.blive.pojo.LiveRecordsInfo
import com.wiatec.blive.presenter.RecordsFragmentPresenter
import com.wiatec.blive.view.activity.PlayActivity
import com.wiatec.blive.view.activity.PlayLocalActivity
import com.wiatec.blive.view.custom_view.BasicLinearItemDecoration
import kotlinx.android.synthetic.main.fragment_live_records.*

/**
 * Created by patrick on 30/10/2017.
 * create time : 4:14 PM
 */
class FragmentLiveRecords: BaseFragment<LiveRecords, RecordsFragmentPresenter>(), LiveRecords {

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var progressBar: ProgressBar? = null
    private var liveRecordsAdapter: LiveRecordsAdapter? = null

    override fun createPresenter(): RecordsFragmentPresenter = RecordsFragmentPresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_live_records, container, false)
        presenter!!.loadLiveRecords()
        progressBar = view.findViewById(R.id.progressBar) as ProgressBar
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        progressBar!!.visibility = View.VISIBLE
        swipeRefreshLayout!!.setOnRefreshListener { presenter!!.loadLiveRecords() }
        return view
    }

    override fun onLoadLiveRecords(execute: Boolean, liveRecordsInfoList: List<LiveRecordsInfo>?) {
        swipeRefreshLayout!!.isRefreshing = false
        progressBar!!.visibility = View.GONE
        if(execute && liveRecordsInfoList != null) {
            if(liveRecordsInfoList.isEmpty()){
                llNoData.visibility = View.VISIBLE
                if(liveRecordsAdapter != null){
                    liveRecordsAdapter!!.notifyChange(liveRecordsInfoList)
                }
                return
            }
            llNoData.visibility = View.GONE
            if(liveRecordsAdapter == null) {
                liveRecordsAdapter = LiveRecordsAdapter(liveRecordsInfoList)
                rcvLiveRecords.adapter = liveRecordsAdapter
                rcvLiveRecords.layoutManager = LinearLayoutManager(context)
                rcvLiveRecords.addItemDecoration(BasicLinearItemDecoration(2))
            }else{
                liveRecordsAdapter!!.notifyChange(liveRecordsInfoList)
            }
            liveRecordsAdapter!!.setOnItemClickListener { _, position ->
                playLiveRecord(liveRecordsInfoList[position])
            }
        }else{

        }
    }

    private fun playLiveRecord(liveRecordsInfo: LiveRecordsInfo){
        val intent = Intent(context, PlayLocalActivity::class.java)
        intent.putExtra(KEY_URL, liveRecordsInfo.playUrl)
        startActivity(intent)
    }
}