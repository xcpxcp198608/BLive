package com.wiatec.blive.task

import android.text.TextUtils
import com.px.common.constant.CommonApplication
import com.px.common.http.HttpMaster
import com.px.common.http.listener.DownloadListener
import com.px.common.http.pojo.DownloadInfo
import com.px.common.utils.FileUtil
import com.px.common.utils.SPUtil
import com.wiatec.blive.instance.KEY_AUTH_ICON_PATH
import com.wiatec.blive.instance.KEY_AUTH_ICON_URL
import com.wiatec.blive.instance.KEY_AUTH_PREVIEW_URL
import com.wiatec.blive.instance.KEY_AUTH_USER_ID
import com.wiatec.blive.model.UserService
import com.wiatec.blive.pojo.UserInfo
import com.wiatec.blive.utils.RMaster
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

/**
 * Created by patrick on 24/10/2017.
 * create time : 12:07 AM
 */
class DownloadUserIcon: Runnable {

    override fun run() {
        execute()
    }

    private fun execute(){
        val userId = SPUtil.get(KEY_AUTH_USER_ID, 0) as Int
        if(userId <= 0 )return
        val iconPath = SPUtil.get(KEY_AUTH_ICON_PATH, "") as String
        if(!TextUtils.isEmpty(iconPath)){
            val file = File(iconPath)
            if(file.exists()){
                return
            }
        }
        RMaster.retrofit.create(UserService::class.java)
                .getUserInfo(userId)
                .enqueue(object : Callback<UserInfo>{
                    override fun onResponse(call: Call<UserInfo>?, response: Response<UserInfo>?) {
                        val userInfo = response!!.body() ?: return
                        SPUtil.put(KEY_AUTH_ICON_URL, userInfo.icon!!)
                        SPUtil.put(KEY_AUTH_PREVIEW_URL, userInfo.channelInfo!!.preview!!)
                        downloadIcon(userInfo.icon!!)
                    }

                    override fun onFailure(call: Call<UserInfo>?, t: Throwable?) {

                    }
                })

    }

    private fun downloadIcon(url: String){
        HttpMaster.download(CommonApplication.context)
                .url(url)
                .path(FileUtil.getPathWith("icon"))
                .startDownload(object: DownloadListener {
                    override fun onPending(downloadInfo: DownloadInfo?) {
                    }

                    override fun onStart(downloadInfo: DownloadInfo?) {
                    }

                    override fun onPause(downloadInfo: DownloadInfo?) {
                    }

                    override fun onProgress(downloadInfo: DownloadInfo?) {
                    }

                    override fun onFinished(downloadInfo: DownloadInfo?) {
                        SPUtil.put(KEY_AUTH_ICON_PATH, downloadInfo!!.path + "/" +downloadInfo.name)
                    }

                    override fun onCancel(downloadInfo: DownloadInfo?) {
                    }

                    override fun onError(downloadInfo: DownloadInfo?) {
                    }
                })
    }
}