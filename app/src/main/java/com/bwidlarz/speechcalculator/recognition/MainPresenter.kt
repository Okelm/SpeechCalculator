package com.bwidlarz.speechcalculator.recognition

import com.bwidlarz.speechcalculator.common.BasePresenter
import com.bwidlarz.speechcalculator.common.EMPTY_STRING
import com.bwidlarz.speechcalculator.common.addToDisposables
import com.bwidlarz.speechcalculator.common.applyComputingShedulers
import io.reactivex.Observable

class MainPresenter : BasePresenter<SpeechView>() {

    fun loadSpeech(results: ArrayList<String>, previousResult: String = EMPTY_STRING) {
        val result = results
                .filter { isNumberOrSymbol(it) }
                .asReversed()
                .firstOrNull()

        withView {
            if (!result.isNullOrEmpty()) {
                val stringToShow = if (previousResult.isNotEmpty()) previousResult + EMPTY_STRING + result else result!!
                onRecognitionFinished(stringToShow)
            }
            else onRecognitionError()
        }
    }

    fun evaluateExpression(stringExpression: String) {
        withView {
            Observable.just(stringExpression)
                    .compose(applyComputingShedulers())
                    .map { evaluate(stringExpression, this::onEvaluationError) }
                    .subscribe( { onEvaluationFinished(it) }, { onEvaluationError(it) })
                    .addToDisposables(disposables)
        }
    }
}

