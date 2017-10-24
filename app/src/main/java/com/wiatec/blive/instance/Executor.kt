package com.wiatec.blive.instance

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by patrick on 24/10/2017.
 * create time : 9:43 AM
 */

object Executor {

    val executorService: ExecutorService = Executors.newCachedThreadPool()

}
