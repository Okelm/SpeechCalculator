package com.bwidlarz.speechcalculator

interface SpeechView : BaseView {
    fun onRecognitionFinished(stringExpression: String)
    fun onRecognitionError(string: String)
    fun onEvaluationFinished(evaluation: Double)
    fun onEvaluationError(error: EvaluatorError): Double
}