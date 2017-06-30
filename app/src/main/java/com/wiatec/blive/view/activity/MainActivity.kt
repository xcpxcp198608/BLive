package com.wiatec.blive.view.activity

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.view.WindowManager
import com.afollestad.materialdialogs.MaterialDialog
import com.px.kotlin.utils.Logger
import com.px.kotlin.utils.SPUtil
import com.readystatesoftware.systembartint.SystemBarTintManager
import com.wiatec.blive.DEFAULT_RTMP_URL
import com.wiatec.blive.KEY_URL
import com.wiatec.blive.R
import com.wiatec.blive.adapter.FragmentAdapter
import com.wiatec.blive.model.Constant
import com.wiatec.blive.utils.WindowUtil
import com.wiatec.blive.view.fragment.FragmentLive
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_layout.*
import kotlinx.android.synthetic.main.slide_navigation.*
import kotlinx.android.synthetic.main.tool_bar_main.*

/**
 * http://128.1.68.58:88/get.php?username=ZHbSkeb6u1&password=X8wSsqgi1J&type=m3u&output=mpegts
 */
class MainActivity : BaseActivity (){

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val systemBarTintManager:SystemBarTintManager = SystemBarTintManager(this)
            systemBarTintManager.isStatusBarTintEnabled = true
            systemBarTintManager.setStatusBarTintResource(R.color.colorAccent)
        }
        initToolBar()
        initSlideNavigation()
        initFragment()
        initFloatActionButton()
    }

    private fun initToolBar() {
        toolBarMain.setPadding(0, WindowUtil.getStatusBarHeight(this), 0, 0)
        toolBarMain.title = getString(R.string.app_name)
        toolBarMain.setTitleTextColor(Color.WHITE)
        toolBarMain.setNavigationIcon(R.drawable.ic_list_white_36dp)
        toolBarMain.inflateMenu(R.menu.menu_tool_bar)
        toolBarMain.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.item_about -> showAboutDialog()
                else -> Logger.d("sf")
            }
            true
        }
    }

    private fun initSlideNavigation() {
        val drawerToggle:ActionBarDrawerToggle = ActionBarDrawerToggle(this , drawer_layout_main ,
                toolBarMain , R.string.app_name, R.string.app_name)
        drawer_layout_main.setDrawerListener(drawerToggle)
        drawerToggle.syncState()
        fl1.setPadding(0,WindowUtil.getStatusBarHeight(this), 0, 0)
        tvSetting.setOnClickListener { showSettingRtmpUrlDialog() }
        tvExit.setOnClickListener { Logger.d("exit") }
        ivPerson.setOnClickListener { Logger.d("person")}
    }

    private fun initFragment() {
        val fragmentLive: FragmentLive = FragmentLive()
        val fragmentList: MutableList<Fragment> = MutableList(0, {
            return
        })
        fragmentList.add(fragmentLive)
        val fragmentAdapter: FragmentAdapter = FragmentAdapter(supportFragmentManager ,fragmentList)
        viewPager.adapter = fragmentAdapter
    }

    private fun initFloatActionButton() {
        btFloatingAction.setOnClickListener {
            val intent = Intent(this , RecorderActivity::class.java)
            intent.putExtra(KEY_URL, DEFAULT_RTMP_URL)
            startActivity(intent)
        }
    }

    private fun showAboutDialog() {
        val dialog = AlertDialog.Builder(this ,R.style.DialogStyleFullScreen).create()
        dialog.show()
        val window = dialog.window
        window.setContentView(R.layout.dialog_about)
    }

    private fun showSettingRtmpUrlDialog (){
        val builder = MaterialDialog.Builder(this)
        builder.title(Constant.INPUT_URL)
        val currentUrl:String = SPUtil.get(applicationContext, Constant.KEY_URL, Constant.DEFAULT_URL) as String
        builder.input(Constant.KEY_URL , currentUrl) { _ , input ->
            SPUtil.put(applicationContext , Constant.KEY_URL , input.toString())
        }
        builder.show()
    }

}