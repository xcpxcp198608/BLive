package com.wiatec.blive.view.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import com.afollestad.materialdialogs.MaterialDialog
import com.px.common.utils.EmojiToast
import com.px.common.utils.SPUtil
import com.readystatesoftware.systembartint.SystemBarTintManager
import com.wiatec.blive.instance.DEFAULT_RTMP_URL
import com.wiatec.blive.instance.INPUT_URL
import com.wiatec.blive.instance.KEY_URL
import com.wiatec.blive.R
import com.wiatec.blive.adapter.FragmentAdapter
import com.wiatec.blive.instance.KEY_AUTH_TOKEN
import com.wiatec.blive.manager.PermissionManager
import com.wiatec.blive.manager.REQUEST_CODE_AUDIO
import com.wiatec.blive.manager.REQUEST_CODE_CAMERA
import com.wiatec.blive.presenter.MainPresenter
import com.wiatec.blive.utils.WindowUtil
import com.wiatec.blive.view.fragment.FragmentLiveChannel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_layout.*
import kotlinx.android.synthetic.main.slide_navigation.*
import kotlinx.android.synthetic.main.tool_bar_main.*

/**
 * http://128.1.68.58:88/get.php?username=ZHbSkeb6u1&password=X8wSsqgi1J&type=m3u&output=mpegts
 */
class MainActivity : BaseActivity<Main, MainPresenter>(), Main, View.OnClickListener{

    private var exitTime = 0L
    private val necessaryPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    private val permissionManager = PermissionManager(necessaryPermissions)

    override fun createPresenter(): MainPresenter = MainPresenter(this@MainActivity)

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val systemBarTintManager = SystemBarTintManager(this)
            systemBarTintManager.isStatusBarTintEnabled = true
            systemBarTintManager.setStatusBarTintResource(R.color.colorAccent)
        }
        initToolBar()
        initSlideNavigation()
        initFragment()
        initEvent()
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
    }

    private fun initFragment() {
        val fragmentLive = FragmentLiveChannel()
        val fragmentList: MutableList<Fragment> = MutableList(0, {
            return
        })
        fragmentList.add(fragmentLive)
        val fragmentAdapter = FragmentAdapter(supportFragmentManager ,fragmentList)
        viewPager.adapter = fragmentAdapter
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

    private fun showSettingRtmpUrlDialog (){
        val builder = MaterialDialog.Builder(this)
        builder.title(INPUT_URL)
        val currentUrl:String = SPUtil.get(KEY_URL, DEFAULT_RTMP_URL) as String
        builder.input(KEY_URL, currentUrl) { _, input ->
            SPUtil.put(KEY_URL, input.toString())
        }
        builder.show()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btFloatingAction -> {
                applyPermission()
            }
            R.id.ivPerson -> {

            }
            R.id.tvSetting -> {
//                showSettingRtmpUrlDialog()
            }
            R.id.tvSignOut -> {
                SPUtil.put(KEY_AUTH_TOKEN, "")
                jumpToAuth()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(event!!.keyCode == KeyEvent.KEYCODE_BACK){
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                EmojiToast.show("Press back key down again exit", EmojiToast.EMOJI_SMILE)
                exitTime = System.currentTimeMillis()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun applyPermission() {
        if (Build.VERSION.SDK_INT > 22) {
            permissionManager.applyPermission(this@MainActivity)
        }else{
            jumpToRecorder()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            REQUEST_CODE_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(permissionManager.checkPermission(this@MainActivity)){
                        jumpToRecorder()
                    }else{
                        permissionManager.applyPermission(this@MainActivity)
                    }
                }
            }
            REQUEST_CODE_AUDIO -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(permissionManager.checkPermission(this@MainActivity)){
                        jumpToRecorder()
                    }else{
                        permissionManager.applyPermission(this@MainActivity)
                    }
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun jumpToRecorder(){
        val intent = Intent(this , RecorderActivity::class.java)
        intent.putExtra(KEY_URL, DEFAULT_RTMP_URL)
        startActivity(intent)
    }

    private fun jumpToAuth(){
        val intent = Intent(this , AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

}