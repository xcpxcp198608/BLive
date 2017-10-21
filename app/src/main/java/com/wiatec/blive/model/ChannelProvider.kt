package com.wiatec.blive.model

import com.px.common.utils.Logger
import com.wiatec.blive.pojo.ChannelInfo
import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.pojo.TokenInfo
import com.wiatec.blive.utils.RMaster
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                            Logger.d(resultInfo.toString())
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

}