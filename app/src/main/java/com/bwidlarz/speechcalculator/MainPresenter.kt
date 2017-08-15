package com.bwidlarz.speechcalculator

import java.util.*

class MainPresenter : BasePresenter<SpeechView>() {

    fun loadSpeech(results: ArrayList<String>) {
        val result = results
                .filter { isNumberOrSymbol(it) }
                .asReversed()
                .firstOrNull()

       withView { if (!result.isNullOrEmpty()) onRecognitionFinished(result!!) else onRecognitionError("Please try again!")}
    }

    fun evaluateExpression(stringExpression: String) {
        withView {
            val evaluation = evaluate(stringExpression, this::onEvaluationError)
            onEvaluationFinished(evaluation)
        }
    }
}

