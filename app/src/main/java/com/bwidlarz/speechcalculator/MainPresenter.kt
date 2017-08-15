package com.bwidlarz.speechcalculator

import java.util.*

class MainPresenter : BasePresenter<SpeechView>() {

    fun loadSpeech(results: ArrayList<String>) {
        val result = results
                .filter { isNumberOrSymbol(it) }
                .asReversed()
                .first()

       withView { if (result.isNotEmpty()) onRecognitionFinished(result) else onRecognitionError("empty string")}
    }

    fun evaluateExpression(stringExpression: String) {
        val evaluation = evaluate(stringExpression)
        withView { onEvaluationFinished(evaluation) }}
}

