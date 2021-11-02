package com.ma.ikea

import android.app.Activity
import android.app.AlertDialog
import android.text.Editable
import android.util.Log

fun Any.logd(message: Any?="no message") {
    Log.d(this.javaClass.simpleName, message.toString())
}

fun showAlert(activity: Activity, title: String, message: String) {
    activity.let {
        val builder = AlertDialog.Builder(it)
        builder.apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("ok", null)
        }
        builder.create()
        builder.show()
    }
}

fun validateInteger(value: String): Boolean {
    return value.matches("^-?\\d+$".toRegex())
}
