package com.wiatec.blive.task

import android.text.TextUtils
import com.px.common.http.Bean.DownloadInfo
import com.px.common.http.HttpMaster
import com.px.common.http.Listener.DownloadListener
import com.px.common.http.Listener.StringListener
import com.px.common.utils.CommonApplication
import com.px.common.utils.SPUtil
import com.wiatec.blive.instance.KEY_AUTH_ICON_PATH
import com.wiatec.blive.instance.KEY_AUTH_USER_ID
import com.wiatec.blive.model.AuthService
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
        val iconPath = SPUtil.get(KEY_AUTH_ICON_PATH, 0) as String
        if(!TextUtils.isEmpty(iconPath)){
            val file = File(iconPath)
            if(file.exists()){
                return
            }
        }
        RMaster.retrofit.create(AuthService::class.java)
                .user(userId)
                .enqueue(object : Callback<UserInfo>{
                    override fun onResponse(call: Call<UserInfo>?, response: Response<UserInfo>?) {
                        val userInfo = response!!.body() ?: return
                        downloadIcon(userInfo.icon!!)
                    }

                    override fun onFailure(call: Call<UserInfo>?, t: Throwable?) {

                    }
                })

    }

    private fun downloadIcon(url: String){
        HttpMaster.download(CommonApplication.context)
                .url(url)
                .path(CommonApplication.context.getExternalFilesDir("icon").absolutePath)
                .startDownload(object: DownloadListener{
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