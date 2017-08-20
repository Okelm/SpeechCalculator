package com.bwidlarz.speechcalculator.recognition

import com.bwidlarz.speechcalculator.common.BaseView

interface SpeechView : BaseView {
    fun onRecognitionFinished(stringExpression: String)
    fun onRecognitionError()
    fun onEvaluationFinished(evaluation: Double)
    fun onEvaluationError(throwable: Throwable): Double
}