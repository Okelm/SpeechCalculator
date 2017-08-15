package com.bwidlarz.speechcalculator

class MainPresenter : BasePresenter<SpeechView>() {

    fun loadSpeech(result: String) {withView { onRecognitionFinished(result) }}

    fun evaluateExpression(stringExpression: String) {
        val evaluation = evaluate(stringExpression)
        withView { onEvaluationFinished(evaluation) } }
}

