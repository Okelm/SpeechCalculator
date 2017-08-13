package com.bwidlarz.speechcalculator

interface SpeechView : BaseView {
    fun onRecognitionFinished(stringExpression: String)
    fun onRecognitionError()
    fun onEvaluationFinished(evaluation: Double)
    fun onEvaluationError(error: String)
}