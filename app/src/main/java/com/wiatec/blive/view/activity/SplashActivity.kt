package com.wiatec.blive.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import com.afollestad.materialdialogs.MaterialDialog
import com.px.common.http.Bean.DownloadInfo
import com.px.common.http.HttpMaster
import com.px.common.http.Listener.DownloadListener
import com.px.common.utils.*

import com.wiatec.blive.R
import com.wiatec.blive.instance.KEY_AUTH_TOKEN
import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.pojo.TokenInfo
import com.wiatec.blive.pojo.UpgradeInfo
import com.wiatec.blive.presenter.SplashPresenter


class SplashActivity : BaseActivity<Splash, SplashPresenter>(), Splash {

    override fun createPresenter(): SplashPresenter = SplashPresenter(this@SplashActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            if(resultInfo.code == ResultInfo.CODE_OK){
                jumpToMain()
            }else{
                jumpToAuth()
            }
        }else{
            Thread(Runnable {
                Thread.sleep(2000)
                jumpToMain()
            }).start()
        }
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
