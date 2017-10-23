package com.wiatec.blive.presenter

import com.px.common.utils.AppUtil
import com.px.common.utils.Logger
import com.px.common.utils.SPUtil
import com.wiatec.blive.instance.KEY_AUTH_USERNAME
import com.wiatec.blive.instance.RTMP_TOKEN
import com.wiatec.blive.model.*
import com.wiatec.blive.pojo.*
import com.wiatec.blive.view.activity.Splash

/**
 * Created by patrick on 13/10/2017.
 * create time : 4:57 PM
 */
class SplashPresenter(private var splash: Splash): BasePresenter<Splash>() {

    private val upgradeProvider = UpgradeProvider()
    private val tokenProvider = TokenProvider()
    private val authProvider: AuthProvider = AuthProvider()
    private val channelProvider: ChannelProvider = ChannelProvider()

    fun checkUpgrade(){
        upgradeProvider.checkUpgrade(object: LoadListener<UpgradeInfo>{

            override fun onSuccess(execute: Boolean, t: UpgradeInfo?) {
                if(execute){
                    if(!AppUtil.isNeedUpgrade(t!!.code)){
                        splash.checkUpgrade(false, t)
                        return
                    }
                }
                splash.checkUpgrade(execute, t)
            }

            override fun onFailure(e: String) {
            }
        })
    }

    fun validateToken(token: String){
        tokenProvider.validate(token, object: LoadListener<ResultInfo<TokenInfo>>{

            override fun onSuccess(execute: Boolean, t: ResultInfo<TokenInfo>?) {
                if(execute){
                    if(!AppUtil.isNeedUpgrade(t!!.code)){
                        splash.validateToken(false, t)
                        return
                    }
                }
                splash.validateToken(execute, t)
            }

            override fun onFailure(e: String) {
            }
        })
    }

    fun getPush(){
        val username = SPUtil.get(KEY_AUTH_USERNAME, "") as String
        authProvider.getPush(username, RTMP_TOKEN, object : LoadListener<PushInfo>{
            override fun onSuccess(execute: Boolean, t: PushInfo?) {
                splash.getPush(execute, t)
            }

            override fun onFailure(e: String) {
            }
        })
    }

    fun updateChannel(channelInfo: ChannelInfo){
        channelProvider.updateChannel(channelInfo, object : LoadListener<ResultInfo<ChannelInfo>>{
            override fun onSuccess(execute: Boolean, t: ResultInfo<ChannelInfo>?) {
                splash.updateChannel(execute, t)
            }

            override fun onFailure(e: String) {

            }
        })
    }
}