package com.wiatec.blive.instance

import android.app.Application
import android.content.Context
import android.support.v4.content.FileProvider
import com.px.common.utils.CommonApplication
import com.px.common.utils.Logger
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * application
 */
class Application: CommonApplication() {

    var executorService: ExecutorService? = null

    override fun onCreate() {
        super.onCreate()
        executorService = Executors.newCachedThreadPool()
    }

}