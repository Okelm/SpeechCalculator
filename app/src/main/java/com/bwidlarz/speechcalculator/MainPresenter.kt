package com.bwidlarz.speechcalculator

import android.util.Log
import com.bwidlarz.speechcalculator.common.BasePresenter
import com.bwidlarz.speechcalculator.common.evaluate
import com.bwidlarz.speechcalculator.common.isNumberOrSymbol
import java.util.*

class MainPresenter : BasePresenter<SpeechView>() {

    fun loadSpeech(results: ArrayList<String>) {

        for (result in results){
            Log.d("asdf", result)
        }
        val result = results
                .filter { isNumberOrSymbol(it) }
                .asReversed()
                .firstOrNull()

       withView {
           if (!result.isNullOrEmpty()) onRecognitionFinished(result!!)
           else onRecognitionError("Please try again!")
       }
    }

    fun loadSpeechWithPrevious(results: ArrayList<String>, previousResult: String) {
        for (result in results){
            Log.d("asdf", result)
        }
        val result = results
                .filter { isNumberOrSymbol(it) }
                .asReversed()
                .firstOrNull()

        withView {
            if (!result.isNullOrEmpty()) onRecognitionFinished(previousResult + " " + result!!)
            else onRecognitionError("Please try again!")
        }
    }

    fun evaluateExpression(stringExpression: String) {
        withView {
            val evaluation = evaluate(stringExpression, this::onEvaluationError)
            onEvaluationFinished(evaluation)
        }
    }
}

