package com.wiatec.blive.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.px.common.http.HttpMaster
import com.px.common.http.Listener.StringListener
import com.px.common.utils.Logger
import com.px.common.utils.SPUtil
import com.wiatec.blive.instance.*
import com.wiatec.blive.pojo.*
import com.wiatec.blive.utils.RMaster
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

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

    fun uploadIcon(file: File, loadListener: LoadListener<ResultInfo<UserInfo>>){
        val userId = SPUtil.get(KEY_AUTH_USER_ID, 0) as Int
        if(userId <= 0 ) return
        HttpMaster.upload(BASE_URL + "user/upload/" + userId)
                .file(file)
                .enqueue(object:StringListener(){
                    override fun onSuccess(s: String?) {
                        if(s == null){
                            loadListener.onSuccess(false, null)
                            return
                        }
                        val resultInfo: ResultInfo<UserInfo> = Gson().fromJson(s,
                                object: TypeToken<ResultInfo<UserInfo>>(){}.type)
                        loadListener.onSuccess(true, resultInfo)
                    }

                    override fun onFailure(e: String?) {
                        Logger.d(e)
                        loadListener.onSuccess(false, null)
                    }
                })
    }

    fun updateUserInfo(){
        val userId = SPUtil.get(KEY_AUTH_USER_ID, 0) as Int
        if(userId <= 0 ) return
        RMaster.retrofit.create(AuthService::class.java)
                .user(userId)
                .enqueue(object : Callback<UserInfo>{
                    override fun onResponse(call: Call<UserInfo>?, response: Response<UserInfo>?) {
                        if(response == null) return
                        val userInfo = response.body()
                        if(userInfo != null){
                            SPUtil.put(KEY_AUTH_USERNAME, userInfo.username)
                            SPUtil.put(KEY_AUTH_ICON_URL, userInfo.icon)
                            SPUtil.put(KEY_AUTH_PREVIEW_URL, userInfo.channelInfo!!.preview)
                            SPUtil.put(KEY_AUTH_PUSH_URL, userInfo.channelInfo!!.url)
                        }
                    }

                    override fun onFailure(call: Call<UserInfo>?, t: Throwable?) {
                        if (t?.message != null) Logger.d(t.message)
                    }
                })
    }

}