package com.wiatec.blive.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.paypal.android.sdk.payments.PaymentActivity
import com.paypal.android.sdk.payments.PaymentConfirmation
import com.px.common.utils.EmojiToast
import com.px.common.utils.Logger
import com.px.common.utils.NetUtil
import com.px.common.utils.SPUtil
import com.wiatec.blive.R
import com.wiatec.blive.adapter.LiveChannelAdapter
import com.wiatec.blive.instance.*
import com.wiatec.blive.pay.PayResultInfo
import com.wiatec.blive.pay.PayInfo
import com.wiatec.blive.pay.PayPalManager
import com.wiatec.blive.pojo.ChannelInfo
import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.presenter.LiveFragmentPresenter
import com.wiatec.blive.view.activity.AuthActivity
import com.wiatec.blive.view.activity.PlayActivity
import com.wiatec.blive.view.custom_view.BasicLinearItemDecoration
import kotlinx.android.synthetic.main.fragment_live.*
import java.io.Serializable


/**
 * Created by patrick on 24/05/2017.
 * create time : 3:49 PM
 */
class FragmentLiveChannel : BaseFragment<LiveChannel, LiveFragmentPresenter>(), LiveChannel,
        PayPalManager.OnPayResultListener {

    private var liveChannelAdapter: LiveChannelAdapter? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var progressBar: ProgressBar? = null
    private var activity: Activity? = null
    private var mChannelInfo: ChannelInfo? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = context as Activity?
    }

    override fun createPresenter(): LiveFragmentPresenter = LiveFragmentPresenter(this)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_live , container , false)
        presenter!!.listChannel()
        progressBar = view!!.findViewById(R.id.progressBar) as ProgressBar
        progressBar!!.visibility = View.VISIBLE
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        swipeRefreshLayout!!.setOnRefreshListener { presenter!!.listChannel() }
        return view
    }

    override fun onListChannel(execute: Boolean, channelInfoList: List<ChannelInfo>?) {
        swipeRefreshLayout!!.isRefreshing = false
        progressBar!!.visibility = View.GONE
        if(execute && channelInfoList != null){
            if(channelInfoList.isEmpty()){
                llNoData.visibility = View.VISIBLE
                if(liveChannelAdapter != null){
                    liveChannelAdapter!!.notifyChange(channelInfoList)
                }
                return
            }
            llNoData.visibility = View.GONE
            llNoNetwork.visibility = View.GONE
            if(liveChannelAdapter == null) {
                liveChannelAdapter = LiveChannelAdapter(channelInfoList)
                rcvLive.adapter = liveChannelAdapter
                rcvLive.layoutManager = LinearLayoutManager(context)
                rcvLive.addItemDecoration(BasicLinearItemDecoration(2))
            }else{
                liveChannelAdapter!!.notifyChange(channelInfoList)
            }
            liveChannelAdapter!!.setOnItemClickListener { _, position ->
                val channelInfo = channelInfoList[position]
                mChannelInfo = channelInfo
                playChannel(channelInfo)
            }
        }else{
            if(NetUtil.isConnected()) {
                showErrorSnackNotice("Data obtain failed")
                llNoNetwork.visibility = View.GONE
                llNoData.visibility = View.VISIBLE
            }else{
                showErrorSnackNotice("network connection error")
                llNoData.visibility = View.GONE
                llNoNetwork.visibility = View.VISIBLE
            }
        }
    }

    private fun playChannel(channelInfo: ChannelInfo){
        if(channelInfo.price <= 0){
            playLiveChannel(channelInfo)
            return
        }
        val username = SPUtil.get(KEY_AUTH_USERNAME, "") as String
        if(TextUtils.isEmpty(username)){
            EmojiToast.show("you have no sign in", EmojiToast.EMOJI_SAD)
            jumpToAuth()
        }
        presenter!!.validatePay(username, channelInfo.userId, "")
    }

    override fun onValidatePay(execute: Boolean, resultInfo: ResultInfo<PayResultInfo>?) {
        if(execute && resultInfo != null){
            if(resultInfo.code == 200){
                if(mChannelInfo != null) {
                    playLiveChannel(mChannelInfo!!)
                }
            }else{
                showPayDialog(PayInfo(mChannelInfo!!.price, "USD", mChannelInfo!!.title!!))
                EmojiToast.show(resultInfo.message, EmojiToast.EMOJI_SAD)
            }
        }else{
            EmojiToast.show("net communication error", EmojiToast.EMOJI_SAD)
        }
    }

    private fun showPayDialog(payInfo: PayInfo){
        MaterialDialog.Builder(context)
                .title(getString(R.string.notice))
                .content("you will pay "+
                        payInfo.price + " "+
                        payInfo.currency + " for " +
                        payInfo.description)
                .positiveText(getString(R.string.confirm))
                .negativeText(getString(R.string.cancel))
                .onPositive { dialog, which ->
                    PayPalManager.pay(this@FragmentLiveChannel, payInfo)
                    dialog.dismiss()
                }
                .onNegative { dialog, which ->
                    dialog.dismiss()
                }
                .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        PayPalManager.payResult(requestCode, resultCode, data, this)
    }

    override fun paySuccess(paymentId: String) {
        Logger.d(paymentId)
        val username = SPUtil.get(KEY_AUTH_USERNAME, "") as String
        if(TextUtils.isEmpty(username)){
            EmojiToast.show("you have no sign in", EmojiToast.EMOJI_SAD)
            jumpToAuth()
        }
        presenter!!.validatePay(username, mChannelInfo!!.userId, paymentId)
    }

    override fun customerCancel(error: String) {
        Logger.d(error)
        EmojiToast.show(error, EmojiToast.EMOJI_SAD)
    }

    private fun playLiveChannel(channelInfo: ChannelInfo){
        val intent = Intent(context, PlayActivity::class.java)
        intent.putExtra("channelInfo", channelInfo as Serializable)
        intent.putExtra(KEY_PLAY_TYPE, "")
        startActivity(intent)
    }

    private fun showErrorSnackNotice(message: String){
        if(linearLayout == null) return
        val snackBar = Snackbar.make(linearLayout, message, Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundResource(R.color.colorBlue1)
        val tvContent = snackBar.view.findViewById(R.id.snackbar_text) as TextView
        tvContent.setTextColor(Color.rgb(255, 64, 129))
        snackBar.show()
    }

    private fun jumpToAuth(){
        val intent = Intent(context , AuthActivity::class.java)
        startActivity(intent)
        activity!!.finish()
    }
}