package com.wiatec.blive.instance

import android.app.Application
import android.content.Context
import com.px.kotlin.utils.Logger

/**
 * application
 */
class Application: Application() {

    lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        Logger.init("----px----")
    }

    companion object{
        fun context(){
            
        }
    }

}