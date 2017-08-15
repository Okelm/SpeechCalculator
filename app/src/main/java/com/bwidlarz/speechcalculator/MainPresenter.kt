package com.bwidlarz.speechcalculator

import com.bwidlarz.speechcalculator.common.BasePresenter
import com.bwidlarz.speechcalculator.common.EMPTY_STRING
import com.bwidlarz.speechcalculator.common.evaluate
import com.bwidlarz.speechcalculator.common.isNumberOrSymbol
import java.util.*

class MainPresenter : BasePresenter<SpeechView>() {

    fun loadSpeech(results: ArrayList<String>, previousResult: String = EMPTY_STRING) {
        val result = results
                .filter { isNumberOrSymbol(it) }
                .asReversed()
                .firstOrNull()

        withView {
            if (!result.isNullOrEmpty()){
                val stringToShow = if (previousResult.isNotEmpty()) previousResult + EMPTY_STRING + result else result!!
                onRecognitionFinished(stringToShow)
            }
            else onRecognitionError()
        }
    }

    fun evaluateExpression(stringExpression: String) {
        withView {
            val evaluation = evaluate(stringExpression, this::onEvaluationError)
            onEvaluationFinished(evaluation)
        }
    }
}

