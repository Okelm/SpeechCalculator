package com.bwidlarz.speechcalculator

import android.support.annotation.StringRes

enum class EvaluatorError(@StringRes val errorResId: Int) {
    UNEXPECTED_CHAR(R.string.unexpected_char_error),
    UNKNOWN(R.string.unknown_error);
}