package com.wiatec.blive.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.px.common.http.HttpMaster
import com.px.common.http.Listener.StringListener
import com.px.common.utils.Logger
import com.px.common.utils.SPUtil
import com.wiatec.blive.instance.BASE_URL
import com.wiatec.blive.instance.KEY_AUTH_USER_ID
import com.wiatec.blive.pojo.ChannelInfo
import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.pojo.TokenInfo
import com.wiatec.blive.pojo.UserInfo
import com.wiatec.blive.utils.RMaster
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

/**
 * Created by patrick on 16/10/2017.
 * create time : 5:29 PM
 */
class ChannelProvider {

    fun listChannel(loadListener: LoadListener<List<ChannelInfo>>){
        RMaster.retrofit.create(ChannelService::class.java)
                .listChannel()
                .enqueue(object : Callback<List<ChannelInfo>> {
                    override fun onFailure(call: Call<List<ChannelInfo>>?, t: Throwable?) {
                        if (t?.message != null) Logger.d(t.message)
                        loadListener.onSuccess(false, null)
                    }

                    override fun onResponse(call: Call<List<ChannelInfo>>?, response: Response<List<ChannelInfo>>?) {
                        if(response == null) return
                        val resultInfo = response.body()
                        if(resultInfo != null){
                            loadListener.onSuccess(true, resultInfo)
                        }else{
                            loadListener.onSuccess(false, null)
                        }
                    }
                })
    }

    fun updateChannel(channelInfo: ChannelInfo, loadListener: LoadListener<ResultInfo<ChannelInfo>>){
        RMaster.retrofit.create(ChannelService::class.java)
                .updateChannel(channelInfo)
                .enqueue(object : Callback<ResultInfo<ChannelInfo>>{
                    override fun onResponse(call: Call<ResultInfo<ChannelInfo>>?, response: Response<ResultInfo<ChannelInfo>>?) {
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

                    override fun onFailure(call: Call<ResultInfo<ChannelInfo>>?, t: Throwable?) {
                        if (t?.message != null) Logger.d(t.message)
                        loadListener.onSuccess(false, null)
                    }
                })
    }

    fun updateChannelName(channelInfo: ChannelInfo, loadListener: LoadListener<ResultInfo<ChannelInfo>>){
        RMaster.retrofit.create(ChannelService::class.java)
                .updateChannelName(channelInfo)
                .enqueue(object : Callback<ResultInfo<ChannelInfo>>{
                    override fun onResponse(call: Call<ResultInfo<ChannelInfo>>?, response: Response<ResultInfo<ChannelInfo>>?) {
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

                    override fun onFailure(call: Call<ResultInfo<ChannelInfo>>?, t: Throwable?) {
                        if (t?.message != null) Logger.d(t.message)
                        loadListener.onSuccess(false, null)
                    }
                })
    }

    fun updateChannelStatus(activate: Int, userId: Int, loadListener: LoadListener<ResultInfo<ChannelInfo>>){
        RMaster.retrofit.create(ChannelService::class.java)
                .updateChannelStatus(activate, userId)
                .enqueue(object : Callback<ResultInfo<ChannelInfo>>{
                    override fun onResponse(call: Call<ResultInfo<ChannelInfo>>?, response: Response<ResultInfo<ChannelInfo>>?) {
                        if(response == null) {
                            loadListener.onSuccess(false, null)
                            return
                        }
                        val resultInfo = response.body()
                        if(resultInfo != null){
//                            Logger.d(resultInfo.toString())
                            loadListener.onSuccess(true, resultInfo)
                        }else{
                            loadListener.onSuccess(false, null)
                        }
                    }

                    override fun onFailure(call: Call<ResultInfo<ChannelInfo>>?, t: Throwable?) {
                        if (t?.message != null) Logger.d(t.message)
                        loadListener.onSuccess(false, null)
                    }
                })
    }

    fun uploadPreviewImage(file: File, loadListener: LoadListener<ResultInfo<ChannelInfo>>){
        val userId = SPUtil.get(KEY_AUTH_USER_ID, 0) as Int
        if(userId <= 0 ) {
            loadListener.onSuccess(false, null)
            return
        }
        HttpMaster.upload(BASE_URL + "channel/upload/" + userId)
                .file(file)
                .enqueue(object: StringListener(){
                    override fun onSuccess(s: String?) {
                        if(s == null){
                            loadListener.onSuccess(false, null)
                            return
                        }
                        val resultInfo: ResultInfo<ChannelInfo> = Gson().fromJson(s,
                                object: TypeToken<ResultInfo<ChannelInfo>>(){}.type)
                        loadListener.onSuccess(true, resultInfo)
                    }

                    override fun onFailure(e: String?) {
                        Logger.d(e)
                        loadListener.onSuccess(false, null)
                    }
                })
    }

}