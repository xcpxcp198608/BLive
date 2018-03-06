package com.wiatec.blive.instance

import com.px.common.constant.CommonApplication
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