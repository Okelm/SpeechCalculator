package com.bwidlarz.speechcalculator

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.support.v7.app.AlertDialog

class DialogFactory(private val context: Context) {

    fun createRedirectToSettingsDialog(activityContext: Context): AlertDialog {
        return AlertDialog.Builder(activityContext, R.style.Base_Theme_AppCompat_Light_Dialog)
                .setTitle(getString(R.string.no_internet))
                .setMessage(getString(R.string.no_internet_message))
                .setPositiveButton(getString(R.string.settings_label), { _ , _ -> context.startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS)) })
                .setNegativeButton(getString(R.string.ok), { dialog, _ -> dialog.dismiss()})
                .create()
    }

    private fun getString(resId: Int): String = context.getString(resId)
}