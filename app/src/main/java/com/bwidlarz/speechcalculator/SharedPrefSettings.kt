package com.bwidlarz.speechcalculator

import android.content.Context
import android.content.SharedPreferences
import com.bwidlarz.speechcalculator.common.Settings

class SharedPrefSettings(context: Context) : Settings {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE)

    override var tutorialShownId: Int
        get() = sharedPreferences.getInt(TUTORIAL_SHOWN, 0)
        set(value) {
            sharedPreferences.edit().putInt(TUTORIAL_SHOWN, value).apply()
        }

    override var lastExpression: String
        get() = sharedPreferences.getString(LAST_KNOWN_EXPRESSION, "")
        set(value) {
            sharedPreferences.edit().putString(LAST_KNOWN_EXPRESSION, value).apply()
        }

    override var lastEvaluation: String
        get() = sharedPreferences.getString(LAST_KNOWN_EVALUATION, "")
        set(value) {
            sharedPreferences.edit().putString(LAST_KNOWN_EVALUATION, value).apply()
        }

    companion object {
        const val TUTORIAL_SHOWN = "tutorial_shown"
        const val LAST_KNOWN_EXPRESSION = "last_known_expression"
        const val LAST_KNOWN_EVALUATION = "last_known_evaluation"
    }
}
