package com.wiatec.blive.view.component

import com.wiatec.blive.module.DataModule
import com.wiatec.blive.view.activity.MainActivity
import dagger.Component

/**
 * Created by patrick on 24/05/2017.
 * create time : 1:59 PM
 */
@Component (modules = arrayOf(DataModule::class))
interface MainActivityComponent {
    fun inject(activity: MainActivity)
}