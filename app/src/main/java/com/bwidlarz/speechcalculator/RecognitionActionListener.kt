package com.bwidlarz.speechcalculator

interface RecognitionActionListener {
    fun onNewEvaluationClicked()
    fun onContinueClicked()
    fun onLoopClicked()
    fun onResetClicked()
}