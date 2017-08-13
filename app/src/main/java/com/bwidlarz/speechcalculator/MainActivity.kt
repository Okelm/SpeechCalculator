package com.bwidlarz.speechcalculator

import android.Manifest
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.support.v7.app.AppCompatActivity
import com.bwidlarz.speechcalculator.databinding.ActivityMainBinding
import com.tbruyelle.rxpermissions2.RxPermissions
import java.util.*

class MainActivity : AppCompatActivity(), SpeechView, RecognitionActionListener, RecognitionListenerAdapted {

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var presenter: MainPresenter
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognizerIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewBinding.listener = this

        presenter = MainPresenter()
        presenter.attach(this)

        requestPermissions()
        setupRecognizer()
    }

    private fun requestPermissions() {
        RxPermissions(this)
                .request(Manifest.permission.RECORD_AUDIO)
                .subscribe()
    }

    private fun setupRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(this)

        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent.apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH.toString())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Enter the expression")
        }
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
        speechRecognizer.startListening(recognizerIntent) //todo
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
