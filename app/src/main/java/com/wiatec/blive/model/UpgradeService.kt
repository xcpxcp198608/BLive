package com.wiatec.blive.model

import com.wiatec.blive.pojo.UpgradeInfo
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by patrick on 14/10/2017.
 * create time : 9:20 AM
 */
interface UpgradeService {

    @GET("upgrade/")
    fun get(): Call<UpgradeInfo>
}