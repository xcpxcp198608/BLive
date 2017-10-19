package com.wiatec.blive.manager

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import java.util.ArrayList

/**
 * Created by patrick on 19/10/2017.
 * create time : 10:12 AM
 */

const val REQUEST_CODE_CAMERA = 0x00001
const val REQUEST_CODE_AUDIO = 0x00002

class PermissionManager(private var necessaryPermissions: Array<String>){

    private val deniedPermissionList = ArrayList<String>()

    fun checkPermission(context: Context): Boolean {
        deniedPermissionList.clear()
        for(permission in necessaryPermissions){
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissionList.add(permission)
            }
        }
        return deniedPermissionList.isEmpty()
    }

    fun applyPermission(context: Context){
        if(Build.VERSION.SDK_INT <= 22) return
        if(checkPermission(context)) return
        var resultCode: Int
        for(permission in deniedPermissionList) {
            resultCode = when(permission){
                Manifest.permission.CAMERA -> REQUEST_CODE_CAMERA
                Manifest.permission.RECORD_AUDIO -> REQUEST_CODE_AUDIO
                else -> -1
            }
            ActivityCompat.requestPermissions(context as Activity, arrayOf(permission), resultCode)
        }
    }
}
