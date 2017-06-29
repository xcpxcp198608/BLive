package com.px.kotlin.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import java.security.PrivateKey

/**
 * Created by patrick on 22/05/2017.
 * create time : 3:25 PM
 */
object SPUtil{

    fun put(context:Context, key:String , any:Any){
        val sharedPreferences:SharedPreferences = context.getSharedPreferences("sp" , Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor = sharedPreferences.edit()
        when(any){
            is String -> editor.putString(key, any)
            is Int -> editor.putInt(key, any)
            is Float -> editor.putFloat(key, any)
            is Long -> editor.putLong(key, any)
            is Boolean -> editor.putBoolean(key, any)
            else -> editor.putString(key, any as String)
        }
        editor.apply()
    }

    fun get(context: Context, key:String, defaultValue:Any): Any{
        val sharedPreferences:SharedPreferences = context.getSharedPreferences("sp" , Context.MODE_PRIVATE)
        when(defaultValue){
            is String -> return sharedPreferences.getString(key, defaultValue)
            is Int -> return sharedPreferences.getInt(key, defaultValue)
            is Float -> return sharedPreferences.getFloat(key, defaultValue)
            is Long -> return sharedPreferences.getLong(key, defaultValue)
            is Boolean -> return sharedPreferences.getBoolean(key, defaultValue)
            else -> return ""
        }
    }
}