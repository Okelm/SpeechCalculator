package com.bwidlarz.speechcalculator

import android.content.Context
import android.support.annotation.StringRes
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast

fun Context.log(message: String) = Log.d(this.packageName, message)

fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, message, length).show()

fun Context.toast(messageResId: Int, length: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, getString(messageResId), length).show()

fun Context.hideKeyboard(v: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(v.windowToken, 0)
}

fun Context.string(@StringRes stringResId: Int): String = getString(stringResId)

fun TextView.clear() {
    text = ""
}
