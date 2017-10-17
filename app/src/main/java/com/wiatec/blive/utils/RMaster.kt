package com.wiatec.blive.utils

import com.wiatec.blive.instance.BASE_URL
import com.wiatec.blive.model.UpgradeService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by patrick on 14/10/2017.
 * create time : 9:28 AM
 */
object RMaster {

     val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
}