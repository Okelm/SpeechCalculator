package com.bwidlarz.speechcalculator

class MainPresenter : BasePresenter<SpeechView>() {

    fun loadSpeech(result: String) {}

    fun evaluateExpression(stringExpression: String) { withView { onEvaluationFinished(evaluate(stringExpression)) } }
}

