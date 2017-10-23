package com.wiatec.blive.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.view.WindowManager
import com.afollestad.materialdialogs.MaterialDialog
import com.px.common.http.Bean.DownloadInfo
import com.px.common.http.HttpMaster
import com.px.common.http.Listener.DownloadListener
import com.px.common.utils.*

import com.wiatec.blive.R
import com.wiatec.blive.instance.*
import com.wiatec.blive.pojo.*
import com.wiatec.blive.presenter.SplashPresenter


class SplashActivity : BaseActivity<Splash, SplashPresenter>(), Splash {

    override fun createPresenter(): SplashPresenter = SplashPresenter(this@SplashActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
        presenter!!.checkUpgrade()
    }

    override fun checkUpgrade(execute: Boolean, upgradeInfo: UpgradeInfo?) {
        if(execute && upgradeInfo != null){
            showUpgradeDialog(upgradeInfo)
        }else{
            if(upgradeInfo != null) {
                FileUtil.delete(FileUtil.getDownloadPath(), upgradeInfo.name)
            }
            checkAuth()
        }
    }

    private fun showUpgradeDialog(upgradeInfo: UpgradeInfo){
        MaterialDialog.Builder(this)
                .title(getString(R.string.upgrade))
                .content(upgradeInfo.info)
                .positiveText("upgrade")
                .cancelable(false)
                .onPositive { dialog, which ->
                    showDownloadDialog(upgradeInfo)
                    dialog.dismiss()
                }
                .show()
    }

    private fun showDownloadDialog(upgradeInfo: UpgradeInfo){
        val progressDialog = MaterialDialog.Builder(this)
                .cancelable(false)
                .title("Downloading")
                .content("wait download finish")
                .progress(false, 100)
                .progressIndeterminateStyle(true)
                .show()
        FileUtil.delete(FileUtil.getDownloadPath(), upgradeInfo.name)
        HttpMaster.download(this@SplashActivity)
                .name(upgradeInfo.name)
                .path(FileUtil.getDownloadPath())
                .url(upgradeInfo.url)
                .startDownload(object : DownloadListener{
                    override fun onPending(downloadInfo: DownloadInfo?) {
                    }

                    override fun onStart(downloadInfo: DownloadInfo?) {
                        progressDialog.setProgress(downloadInfo!!.progress)
                    }

                    override fun onPause(downloadInfo: DownloadInfo?) {
                    }

                    override fun onProgress(downloadInfo: DownloadInfo?) {
                        progressDialog.setProgress(downloadInfo!!.progress)
                    }

                    override fun onFinished(downloadInfo: DownloadInfo?) {
                        progressDialog.setProgress(100)
                        progressDialog.dismiss()
                        if(AppUtil.isApkCanInstall(downloadInfo!!.path, downloadInfo.name)){
                            AppUtil.installApk(downloadInfo.path, downloadInfo.name)
                        }else{
                            if(FileUtil.isExists(downloadInfo.path, downloadInfo.name)){
                                FileUtil.delete(downloadInfo.path, downloadInfo.name)
                            }
                            EmojiToast.show("download error", EmojiToast.EMOJI_SAD)
                        }
                    }

                    override fun onCancel(downloadInfo: DownloadInfo?) {
                    }

                    override fun onError(downloadInfo: DownloadInfo?) {
                    }
                })
    }

    private fun checkAuth(){
        val token = SPUtil.get(KEY_AUTH_TOKEN, "") as String
        if(TextUtils.isEmpty(token)){
            jumpToAuth()
        }else{
            presenter!!.validateToken(token)
        }
    }

    override fun validateToken(execute: Boolean, resultInfo: ResultInfo<TokenInfo>?) {
        if(execute && resultInfo != null){
            if(resultInfo.code != ResultInfo.CODE_OK){
                jumpToAuth()
                return
            }
        }
        presenter!!.getPush()
    }

    override fun getPush(execute: Boolean, pushInfo: PushInfo?) {
        if(execute && pushInfo != null) {
            SPUtil.put(KEY_AUTH_PUSH_URL, pushInfo.data.push_full_url)
            val userId = SPUtil.get(KEY_AUTH_USER_ID, 0) as Int
            if(userId == 0){
                EmojiToast.show("signin server error", EmojiToast.EMOJI_SAD)
            }else {
                presenter!!.updateChannel(ChannelInfo(pushInfo.data.push_full_url,
                        pushInfo.data.play_url, userId))
            }
        }else{
            EmojiToast.show("live server error", EmojiToast.EMOJI_SAD)
            jumpToMain()
        }
    }

    override fun updateChannel(execute: Boolean, resultInfo: ResultInfo<ChannelInfo>?) {
        if(execute && resultInfo != null) {
            if(resultInfo.code != ResultInfo.CODE_OK){
                EmojiToast.show(resultInfo.message, EmojiToast.EMOJI_SAD)
            }
        }else{
            EmojiToast.show("signin server error", EmojiToast.EMOJI_SAD)
        }
        jumpToMain()
    }

    private fun jumpToAuth(){
        startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
        finish()
    }

    private fun jumpToMain(){
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }
}
