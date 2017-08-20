package com.bwidlarz.speechcalculator.recognition

import android.support.annotation.StringRes
import com.bwidlarz.speechcalculator.R

enum class EvaluatorError(@StringRes val errorResId: Int) {
    UNEXPECTED_CHAR(R.string.unexpected_char_error),
    UNKNOWN(R.string.unknown_error),
    PARSING(R.string.parsing_error);
}