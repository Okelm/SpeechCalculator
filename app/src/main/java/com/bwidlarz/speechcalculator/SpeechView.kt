package com.bwidlarz.speechcalculator

import com.bwidlarz.speechcalculator.common.BaseView
import com.bwidlarz.speechcalculator.common.EvaluatorError

interface SpeechView : BaseView {
    fun onRecognitionFinished(stringExpression: String)
    fun onRecognitionError(string: String)
    fun onEvaluationFinished(evaluation: Double)
    fun onEvaluationError(error: EvaluatorError): Double
}