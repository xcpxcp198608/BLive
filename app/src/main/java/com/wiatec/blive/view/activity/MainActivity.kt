package com.wiatec.blive.view.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.px.common.image.ImageMaster
import com.px.common.utils.EmojiToast
import com.px.common.utils.Logger
import com.px.common.utils.SPUtil
import com.wiatec.blive.R
import com.wiatec.blive.adapter.FragmentAdapter
import com.wiatec.blive.instance.*
import com.wiatec.blive.manager.PermissionManager
import com.wiatec.blive.manager.REQUEST_CODE_AUDIO
import com.wiatec.blive.manager.REQUEST_CODE_CAMERA
import com.wiatec.blive.pojo.ResultInfo
import com.wiatec.blive.pojo.UserInfo
import com.wiatec.blive.presenter.MainPresenter
import com.wiatec.blive.task.DownloadUserIcon
import com.wiatec.blive.utils.AuthUtils
import com.wiatec.blive.utils.WindowUtil
import com.wiatec.blive.view.fragment.FragmentLiveChannel
import com.wiatec.blive.view.fragment.FragmentLiveRecords
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_layout.*
import kotlinx.android.synthetic.main.slide_navigation.*
import kotlinx.android.synthetic.main.tool_bar_main.*
import org.apache.commons.io.FileUtils
import java.io.File


class MainActivity : BaseActivity<Main, MainPresenter>(), Main, View.OnClickListener{

    private var exitTime = 0L
    private val necessaryPermissions = arrayOf(Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO)
    private val permissionManager = PermissionManager(necessaryPermissions)
    private var currentFileName = ""

    override fun createPresenter(): MainPresenter = MainPresenter(this@MainActivity)

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolBar()
        initSlideNavigation()
        initFragment()
        initEvent()
        Executor.executorService.execute(DownloadUserIcon())
    }

    override fun onStart() {
        super.onStart()
        presenter!!.updateChannelStatus(DEACTIVATE)
    }

    private fun initToolBar() {
        val paddingTop = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT )
            WindowUtil.getStatusBarHeight(this) else 0
        toolBarMain.setPadding(0, paddingTop, 0, 0)
        toolBarMain.title = getString(R.string.app_name)
        toolBarMain.setTitleTextColor(Color.WHITE)
        toolBarMain.setNavigationIcon(R.drawable.ic_list_white_36dp)
        toolBarMain.inflateMenu(R.menu.menu_tool_bar)
        toolBarMain.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.item_about -> showConsentDialog()
            }
            true
        }
    }

    private fun initSlideNavigation() {
        val drawerToggle = ActionBarDrawerToggle(this , drawer_layout_main ,
                toolBarMain , R.string.app_name, R.string.app_name)
        drawer_layout_main.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        fl1.setPadding(0,WindowUtil.getStatusBarHeight(this), 0, 0)
        if(!AuthUtils.isSignin()) return
        val iconPath = SPUtil.get(KEY_AUTH_ICON_PATH, "") as String
        if(!TextUtils.isEmpty(iconPath)) {
            ImageMaster.load(this@MainActivity, iconPath, ivPerson, R.drawable.img_holder_icon,
                    R.drawable.img_error_icon)
        }else{
            val iconUrl = SPUtil.get(KEY_AUTH_ICON_URL, "") as String
            ImageMaster.load(this@MainActivity, iconUrl, ivPerson, R.drawable.img_holder_icon,
                    R.drawable.img_error_icon)
        }
        val username = SPUtil.get(KEY_AUTH_USERNAME, "") as String
        if(!TextUtils.isEmpty(username)) tvNoSignin.text = username
    }

    private fun initFragment() {

        val fragmentList = ArrayList<Fragment>()
        val fragmentLive = FragmentLiveChannel()
        val fragmentRecords = FragmentLiveRecords()
        fragmentList.add(fragmentLive)
        fragmentList.add(fragmentRecords)
        val fragmentAdapter = FragmentAdapter(supportFragmentManager, fragmentList)
        viewPager.adapter = fragmentAdapter
        viewPager.offscreenPageLimit = fragmentList.size
        tabLayout.addTab(tabLayout.newTab().setText("Live"))
        tabLayout.addTab(tabLayout.newTab().setText("Record"))
    }

    private fun initEvent(){
        btFloatingAction.setOnClickListener(this)
        ivPerson.setOnClickListener(this)
        tvSetting.setOnClickListener(this)
        tvSignOut.setOnClickListener(this)
    }

    private fun showConsentDialog() {
        val dialog = AlertDialog.Builder(this).create()
        dialog.show()
        val window = dialog.window
        window.setContentView(R.layout.dialog_consent)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btFloatingAction -> {
                applyPermission()
            }
            R.id.ivPerson -> {
                if(AuthUtils.isSignin()){
                    pickImage()
                }else{
                    jumpToAuth()
                }
            }
            R.id.tvSetting -> {
                if(AuthUtils.isSignin()) {
                    jumpToSettings()
                }else{
                    jumpToAuth()
                }
            }
            R.id.tvSignOut -> {
                if(AuthUtils.isSignin()) {
                    SPUtil.put(KEY_AUTH_TOKEN, "")
                    jumpToAuth()
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(event!!.keyCode == KeyEvent.KEYCODE_BACK){
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                EmojiToast.show(getString(R.string.exit_notice), EmojiToast.EMOJI_SMILE)
                exitTime = System.currentTimeMillis()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun applyPermission() {
        if (Build.VERSION.SDK_INT > 22) {
            if(!permissionManager.checkPermission(this@MainActivity)) {
                permissionManager.applyPermission(this@MainActivity)
                return
            }
        }
        jumpToPush()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        when(requestCode){
            REQUEST_CODE_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    if(permissionManager.checkPermission(this@MainActivity)){
                        jumpToPush()
                    }else{
                        permissionManager.applyPermission(this@MainActivity)
                    }
                }
            }
            REQUEST_CODE_AUDIO -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    if(permissionManager.checkPermission(this@MainActivity)){
                        jumpToPush()
                    }else{
                        permissionManager.applyPermission(this@MainActivity)
                    }
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun pickImage(){
        PictureSelector.create(this@MainActivity)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.PickerStyle)
                .maxSelectNum(1)
                .imageSpanCount(4)
                .previewImage(true)
                .isZoomAnim(true)
                .enableCrop(true)
                .compress(true)
                .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)
                .withAspectRatio(4, 4)
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
                    presenter!!.uploadIcon(file)
                    progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun uploadIconCallBack(execute: Boolean, resultInfo: ResultInfo<UserInfo>?) {
        if(execute && resultInfo != null){
            val fileFullPath = getExternalFilesDir("icon").absolutePath + "/" + currentFileName
            SPUtil.put(KEY_AUTH_ICON_PATH, fileFullPath)
            Logger.d(fileFullPath)
            ImageMaster.load(this@MainActivity, fileFullPath, ivPerson, R.drawable.img_holder_icon,
                    R.drawable.img_error_icon)
            progressBar.visibility = View.GONE
        }else{
            EmojiToast.show("upload server error",  EmojiToast.EMOJI_SAD)
        }
    }

    private fun validateSignIn(): Boolean{
        val token = SPUtil.get(KEY_AUTH_TOKEN, "") as String
        if(TextUtils.isEmpty(token)){
            return false
        }
        return true
    }

    private fun jumpToPush(){
        if(!validateSignIn()){
            jumpToAuth()
            return
        }
        val intent = Intent(this , PushActivity::class.java)
        startActivity(intent)
    }

    private fun jumpToAuth(){
        val intent = Intent(this , AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun jumpToSettings(){
        val intent = Intent(this , UserSettingsActivity::class.java)
        startActivity(intent)
    }

}