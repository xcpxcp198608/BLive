package com.px.kotlin.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * extension function
 */

/**
 * activity extension function -- showAlertDialog
 */
fun Activity.showAlertDialog(title: String, message: String) {
    val dialog: Dialog = Dialog(this)
    val window = dialog.window
}

fun ImageView.loadUrl(url: String, placeHolder: Int, error: Int){

}