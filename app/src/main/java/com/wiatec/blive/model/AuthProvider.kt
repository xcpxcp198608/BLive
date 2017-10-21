package com.wiatec.blive.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.px.common.http.HttpMaster
import com.px.common.http.Listener.StringListener
import com.px.common.utils.Logger
import com.wiatec.blive.instance.RTMP_TOKEN_URL
import com.wiatec.blive.pojo.*
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
                        if(response == null) {
                            loadListener.onSuccess(false, null)
                            return
                        }
                        val resultInfo = response.body()
                        if(resultInfo != null){
                            loadListener.onSuccess(true, resultInfo)
                        }else{
                            loadListener.onSuccess(false, null)
                        }
                    }
                })
    }

    fun signIn(userInfo: UserInfo, loadListener: LoadListener<ResultInfo<TokenInfo>>){
        RMaster.retrofit.create(AuthService::class.java)
                .signIn(userInfo)
                .enqueue(object : Callback<ResultInfo<TokenInfo>>{
                    override fun onFailure(call: Call<ResultInfo<TokenInfo>>?, t: Throwable?) {
                        if (t?.message != null) Logger.d(t.message)
                        loadListener.onSuccess(false, null)
                    }

                    override fun onResponse(call: Call<ResultInfo<TokenInfo>>?, response: Response<ResultInfo<TokenInfo>>?) {
                        if(response == null) {
                            loadListener.onSuccess(false, null)
                            return
                        }
                        val resultInfo = response.body()
                        if(resultInfo != null){
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
                        if(response == null) {
                            loadListener.onSuccess(false, null)
                            return
                        }
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
                        if(response == null) {
                            loadListener.onSuccess(false, null)
                            return
                        }
                        val resultInfo = response.body()
                        if(resultInfo != null){
                            loadListener.onSuccess(true, resultInfo)
                        }else{
                            loadListener.onSuccess(false, null)
                        }
                    }
                })
    }

    fun getPush(username: String, token: String, loadListener: LoadListener<PushInfo>){
        val url = "$RTMP_TOKEN_URL?username=$username&token=$token"
        HttpMaster.get(url)
                .enqueue(object : StringListener(){
                    override fun onSuccess(s: String?) {
                        if(s == null){
                            loadListener.onSuccess(false, null)
                            return
                        }
                        val pushInfo: PushInfo = Gson().fromJson(s, object: TypeToken<PushInfo>(){}.type)
                        loadListener.onSuccess(true, pushInfo)
                    }

                    override fun onFailure(e: String?) {
                        loadListener.onSuccess(false, null)
                    }
                })
    }

}