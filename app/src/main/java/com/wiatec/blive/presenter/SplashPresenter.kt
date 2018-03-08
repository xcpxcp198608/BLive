package com.wiatec.blive.presenter

import com.px.common.utils.AppUtil
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
    private val userProvider: UserProvider = UserProvider()

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
                splash.validateToken(execute, t)
            }

            override fun onFailure(e: String) {
            }
        })
    }


    fun getUserInfo(){
        userProvider.getUserInfo(null)
    }
}