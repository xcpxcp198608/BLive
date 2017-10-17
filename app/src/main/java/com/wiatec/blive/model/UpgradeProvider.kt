package com.wiatec.blive.model

import com.px.common.utils.Logger
import com.wiatec.blive.pojo.UpgradeInfo
import com.wiatec.blive.utils.RMaster
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by patrick on 13/10/2017.
 * create time : 5:22 PM
 */
class UpgradeProvider {

    fun checkUpgrade(loadListener: LoadListener<UpgradeInfo>){
        RMaster.retrofit
                .create(UpgradeService::class.java)
                .get()
                .enqueue(object :Callback<UpgradeInfo>{
                    override fun onResponse(call: Call<UpgradeInfo>?, response: Response<UpgradeInfo>?) {
                        val upgradeInfo = response!!.body()
                        if(upgradeInfo != null){
                            loadListener.onSuccess(true, upgradeInfo)
                        }else{
                            loadListener.onSuccess(false, upgradeInfo)
                        }
                    }

                    override fun onFailure(call: Call<UpgradeInfo>?, t: Throwable?) {
                        Logger.d(t!!.message)
                        loadListener.onSuccess(false, null)
                    }
        })
    }
}