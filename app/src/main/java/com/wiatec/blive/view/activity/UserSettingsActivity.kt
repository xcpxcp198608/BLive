package com.wiatec.blive.view.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.px.common.image.ImageMaster
import com.px.common.utils.EmojiToast
import com.px.common.utils.Logger
import com.px.common.utils.SPUtil

import com.wiatec.blive.R
import com.wiatec.blive.instance.KEY_AUTH_PREVIEW_PATH
import com.wiatec.blive.instance.KEY_AUTH_PREVIEW_URL
import com.wiatec.blive.pojo.ChannelInfo
import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.presenter.UserSettingsPresenter
import com.wiatec.blive.utils.AuthUtils
import com.wiatec.blive.utils.WindowUtil
import kotlinx.android.synthetic.main.activity_user_setting.*
import kotlinx.android.synthetic.main.tool_bar_main.*
import org.apache.commons.io.FileUtils
import java.io.File

class UserSettingsActivity : BaseActivity<UserSettings, UserSettingsPresenter>(),
        UserSettings, View.OnClickListener {

    private var currentFileName = ""

    override fun createPresenter(): UserSettingsPresenter = UserSettingsPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_setting)
        initToolBar()
        initPreview()
    }

    private fun initToolBar() {
        val paddingTop = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT )
            WindowUtil.getStatusBarHeight(this) else 0
        toolBarMain.setPadding(0, paddingTop, 0, 0)
        toolBarMain.title = getString(R.string.settings)
        toolBarMain.setTitleTextColor(Color.WHITE)
        toolBarMain.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolBarMain.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initPreview(){
        if(!AuthUtils.isSignin()) return
        val previewUrl = SPUtil.get(KEY_AUTH_PREVIEW_URL, "") as String
        ImageMaster.load(previewUrl, ivPreview, R.drawable.img_holder_preview,
                R.drawable.img_error_preview)
        ivPreview.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.ivPreview -> {
                pickPreviewImage()
            }
        }
    }

    private fun pickPreviewImage(){
        PictureSelector.create(this@UserSettingsActivity)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.PickerStyle)
                .maxSelectNum(1)
                .imageSpanCount(4)
                .previewImage(true)
                .isZoomAnim(true)
                .enableCrop(true)
                .compress(true)
                .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)
                .withAspectRatio(16, 9)
                .freeStyleCropEnabled(true)
                .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    val selectList = PictureSelector.obtainMultipleResult(data)
                    val localMedia = selectList[0]
                    val file = File(localMedia.compressPath)
                    currentFileName = file.name
                    val dir = getExternalFilesDir("icon")
                    FileUtils.copyFileToDirectory(file, dir)
                    presenter!!.uploadPreviewImage(file)
                    progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onUploadPreviewImage(execute: Boolean, resultInfo: ResultInfo<ChannelInfo>?) {
        progressBar.visibility = View.GONE
        if(execute && resultInfo != null){
            val previewFullPath = getExternalFilesDir("icon").absolutePath + "/" + currentFileName
            SPUtil.put(KEY_AUTH_PREVIEW_PATH, previewFullPath)
            SPUtil.put(KEY_AUTH_PREVIEW_URL, resultInfo.t!!.preview)
            ImageMaster.load(this@UserSettingsActivity, previewFullPath, ivPreview, R.drawable.img_holder_preview,
                    R.drawable.img_error_preview)
        }else{
            EmojiToast.show("user info error, try signin again", EmojiToast.EMOJI_SAD)
        }
    }
}
