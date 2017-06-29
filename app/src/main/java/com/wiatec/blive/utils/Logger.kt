package com.px.kotlin.utils

import android.util.Log

/**
 * Created by patrick on 22/05/2017.
 * create time : 1:46 PM
 */
object Logger{
    var tag:String = ""
    val V:Int = 1
    val D:Int = 2
    val I:Int = 3
    val W:Int = 4
    val E:Int = 5
    val N:Int = 6
    val L:Int = D

    fun init(tag:String){this.tag = tag}

    fun v (message:Any){
        if (L <= V){
            Log.v(tag ,"---->" + message + getInfo(Thread.currentThread().stackTrace[3]))
        }
    }

    fun d (message:Any){
        if (L <= D){
            Log.d(tag ,"---->" + message + getInfo(Thread.currentThread().stackTrace[3]))
        }
    }

    fun i (message:Any){
        if (L <= I){
            Log.i(tag ,"---->" + message + getInfo(Thread.currentThread().stackTrace[3]))
        }
    }

    fun w (message:Any){
        if (L <= W){
            Log.w(tag ,"---->" + message + getInfo(Thread.currentThread().stackTrace[3]))
        }
    }

    fun e (message:Any){
        if (L <= E){
            Log.e(tag ,"---->" + message + getInfo(Thread.currentThread().stackTrace[3]))
        }
    }

    fun getInfo(stackTraceElement: StackTraceElement):String{
        var s:StringBuilder = StringBuilder()
        val threadName:String = Thread.currentThread().name
        val threadId:Long = Thread.currentThread().id
        val fileName:String = stackTraceElement.fileName
        val className:String = stackTraceElement.className
        val methodName:String = stackTraceElement.methodName
        val lineNumber:Int = stackTraceElement.lineNumber
        s.append("     ◆◆◆◆◆【")
        s.append("ThreadName: " + threadName)
        s.append("  ThreadId: " + threadId)
        s.append("  FileName: " + fileName)
        s.append("  Line: " + lineNumber)
        s.append("】◆◆◆◆◆")
        return s.toString()
    }
}