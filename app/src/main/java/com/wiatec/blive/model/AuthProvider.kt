package com.wiatec.blive.model

import com.px.common.utils.Logger
import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.pojo.UserInfo
import com.wiatec.blive.utils.RMaster
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by patrick on 16/10/2017.
 * create time : 5:29 PM
 */
class AuthProvider{

    fun signUp(userInfo: UserInfo, loadListener: LoadListener<ResultInfo<UserInfo>>){
        RMaster.retrofit.create(AuthService::class.java)
                .signUp(userInfo)
                .enqueue(object : Callback<ResultInfo<UserInfo>>{
                    override fun onFailure(call: Call<ResultInfo<UserInfo>>?, t: Throwable?) {
                        if (t?.message != null) Logger.d(t.message)
                        loadListener.onSuccess(false, null)
                    }

                    override fun onResponse(call: Call<ResultInfo<UserInfo>>?, response: Response<ResultInfo<UserInfo>>?) {
                        if(response == null) return
                        val resultInfo = response.body()
                        if(resultInfo != null){
                            Logger.d(resultInfo.toString())
                            loadListener.onSuccess(true, resultInfo)
                        }else{
                            loadListener.onSuccess(false, null)
                        }
                    }
                })
    }

    fun signIn(userInfo: UserInfo, loadListener: LoadListener<ResultInfo<UserInfo>>){
        RMaster.retrofit.create(AuthService::class.java)
                .signIn(userInfo)
                .enqueue(object : Callback<ResultInfo<UserInfo>>{
                    override fun onFailure(call: Call<ResultInfo<UserInfo>>?, t: Throwable?) {
                        if (t?.message != null) Logger.d(t.message)
                        loadListener.onSuccess(false, null)
                    }

                    override fun onResponse(call: Call<ResultInfo<UserInfo>>?, response: Response<ResultInfo<UserInfo>>?) {
                        if(response == null) return
                        val resultInfo = response.body()
                        if(resultInfo != null){
                            Logger.d(resultInfo.toString())
                            loadListener.onSuccess(true, resultInfo)
                        }else{
                            loadListener.onSuccess(false, null)
                        }
                    }
                })
    }

    fun validate(userInfo: UserInfo, loadListener: LoadListener<ResultInfo<UserInfo>>){
        RMaster.retrofit.create(AuthService::class.java)
                .validate(userInfo)
                .enqueue(object : Callback<ResultInfo<UserInfo>>{
                    override fun onFailure(call: Call<ResultInfo<UserInfo>>?, t: Throwable?) {
                        if (t?.message != null) Logger.d(t.message)
                        loadListener.onSuccess(false, null)
                    }

                    override fun onResponse(call: Call<ResultInfo<UserInfo>>?, response: Response<ResultInfo<UserInfo>>?) {
                        if(response == null) return
                        val resultInfo = response.body()
                        if(resultInfo != null){
                            Logger.d(resultInfo.toString())
                            loadListener.onSuccess(true, resultInfo)
                        }else{
                            loadListener.onSuccess(false, null)
                        }
                    }
                })
    }

    fun resetPassword(userInfo: UserInfo, loadListener: LoadListener<ResultInfo<UserInfo>>){
        RMaster.retrofit.create(AuthService::class.java)
                .resetPassword(userInfo)
                .enqueue(object : Callback<ResultInfo<UserInfo>>{
                    override fun onFailure(call: Call<ResultInfo<UserInfo>>?, t: Throwable?) {
                        if (t?.message != null) Logger.d(t.message)
                        loadListener.onSuccess(false, null)
                    }

                    override fun onResponse(call: Call<ResultInfo<UserInfo>>?, response: Response<ResultInfo<UserInfo>>?) {
                        if(response == null) return
                        val resultInfo = response.body()
                        if(resultInfo != null){
                            Logger.d(resultInfo.toString())
                            loadListener.onSuccess(true, resultInfo)
                        }else{
                            loadListener.onSuccess(false, null)
                        }
                    }
                })
    }
}