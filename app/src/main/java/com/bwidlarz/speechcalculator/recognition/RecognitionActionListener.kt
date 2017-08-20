package com.bwidlarz.speechcalculator.recognition

interface RecognitionActionListener {
    fun onNewEvaluationClicked()
    fun onContinueClicked()
    fun onLoopClicked()
    fun onResetClicked()
    fun onStopClicked()
}