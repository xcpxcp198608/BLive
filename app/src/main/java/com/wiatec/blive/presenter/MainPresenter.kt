package com.wiatec.blive.presenter

import com.wiatec.blive.model.AuthProvider
import com.wiatec.blive.model.LoadListener
import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.pojo.UserInfo
import com.wiatec.blive.view.activity.Main
import java.io.File

/**
 * Created by patrick on 24/05/2017.
 * create time : 3:14 PM
 */
class MainPresenter(var main: Main): BasePresenter<Main>() {

    private val authProvider: AuthProvider = AuthProvider()

    fun deactivateChannel(){

    }

    fun uploadIcon(file: File){
        authProvider.uploadIcon(file, object : LoadListener<ResultInfo<UserInfo>>{
            override fun onSuccess(execute: Boolean, t: ResultInfo<UserInfo>?) {
                main.uploadIconCallBack(execute, t)
            }

            override fun onFailure(e: String) {
            }
        })
    }
}