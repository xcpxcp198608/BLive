package com.wiatec.blive.presenter

import com.px.common.utils.AppUtil
import com.px.common.utils.Logger
import com.wiatec.blive.model.LoadListener
import com.wiatec.blive.model.UpgradeProvider
import com.wiatec.blive.pojo.UpgradeInfo
import com.wiatec.blive.view.activity.Splash

/**
 * Created by patrick on 13/10/2017.
 * create time : 4:57 PM
 */
class SplashPresenter(private var splash: Splash): BasePresenter<Splash>() {

    private val upgradeProvider = UpgradeProvider()

    fun checkUpgrade(){
        upgradeProvider.load(object: LoadListener<UpgradeInfo>{

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
}