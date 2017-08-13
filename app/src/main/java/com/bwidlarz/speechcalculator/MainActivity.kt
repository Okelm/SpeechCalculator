package com.bwidlarz.speechcalculator

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bwidlarz.speechcalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SpeechView, RecognitionActionListener {

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        presenter = MainPresenter()
        presenter.attach(this)
    }

    override fun onRecognitionFinished(stringExpression: String) {
        TODO("not implemented")
    }

    override fun onRecognitionError() {
        TODO("not implemented")
    }

    override fun onEvaluationFinished(evaluation: Double) {
        TODO("not implemented")
    }

    override fun onEvaluationError(error: String) {
        TODO("not implemented")
    }

    override fun onNewEvaluationClicked() {
        TODO("not implemented")
    }

    override fun onContinueClicked() {
        TODO("not implemented")
    }

    override fun onLoopClicked() {
        TODO("not implemented")
    }

    override fun onResetClicked() {
        TODO("not implemented")
    }
}
