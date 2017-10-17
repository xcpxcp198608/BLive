package com.wiatec.blive.view.activity

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.view.KeyEvent
import android.view.WindowManager
import com.afollestad.materialdialogs.MaterialDialog
import com.px.common.utils.Logger
import com.px.common.utils.SPUtil
import com.readystatesoftware.systembartint.SystemBarTintManager
import com.wiatec.blive.instance.DEFAULT_RTMP_URL
import com.wiatec.blive.instance.INPUT_URL
import com.wiatec.blive.instance.KEY_URL
import com.wiatec.blive.R
import com.wiatec.blive.adapter.FragmentAdapter
import com.wiatec.blive.presenter.MainPresenter
import com.wiatec.blive.utils.WindowUtil
import com.wiatec.blive.view.fragment.FragmentLive
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_layout.*
import kotlinx.android.synthetic.main.slide_navigation.*
import kotlinx.android.synthetic.main.tool_bar_main.*

/**
 * http://128.1.68.58:88/get.php?username=ZHbSkeb6u1&password=X8wSsqgi1J&type=m3u&output=mpegts
 */
class MainActivity : BaseActivity<Main, MainPresenter>(), Main {

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
        initFloatActionButton()
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
        tvSetting.setOnClickListener { showSettingRtmpUrlDialog() }
        tvExit.setOnClickListener { Logger.d("exit") }
        ivPerson.setOnClickListener { Logger.d("person")}
    }

    private fun initFragment() {
        val fragmentLive = FragmentLive()
        val fragmentList: MutableList<Fragment> = MutableList(0, {
            return
        })
        fragmentList.add(fragmentLive)
        val fragmentAdapter = FragmentAdapter(supportFragmentManager ,fragmentList)
        viewPager.adapter = fragmentAdapter
    }

    private fun initFloatActionButton() {
        btFloatingAction.setOnClickListener {
            val intent = Intent(this , RecorderActivity::class.java)
            intent.putExtra(KEY_URL, DEFAULT_RTMP_URL)
            startActivity(intent)
        }
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(event!!.keyCode == KeyEvent.KEYCODE_BACK){

        }

        return super.onKeyDown(keyCode, event)
    }

}