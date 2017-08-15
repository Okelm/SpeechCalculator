package com.bwidlarz.speechcalculator

import android.Manifest
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
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
        }

        viewBinding.progressBar.apply {
            setSpeechRecognizer(speechRecognizer)
            setRecognitionListener(this@MainActivity)
            setColors(intArrayOf(
                    ContextCompat.getColor(this@MainActivity, R.color.color1),
                    ContextCompat.getColor(this@MainActivity, R.color.color2),
                    ContextCompat.getColor(this@MainActivity, R.color.color3),
                    ContextCompat.getColor(this@MainActivity, R.color.color4),
                    ContextCompat.getColor(this@MainActivity, R.color.color5)))
            play()
        }
    }

    override fun onResults(results: Bundle) {
        val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        presenter.loadSpeech(matches)
    }

    override fun onError(error: Int) {
        val errorMessage = getErrorText(error)
        toast(errorMessage)
    }

    override fun onRecognitionFinished(stringExpression: String) {
        presenter.evaluateExpression(stringExpression)
        viewBinding.expression.setText(stringExpression, TextView.BufferType.EDITABLE)
    }

    override fun onRecognitionError(string: String) {
        toast(string)
    }

    override fun onEvaluationFinished(evaluation: Double) {
        viewBinding.evaluation.text = evaluation.toString()
    }

    override fun onEvaluationError(error: EvaluatorError): Double {
        toast(error.errorResId)
        return 0.0
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
        viewBinding.apply {
            expression.text.clear()
            evaluation.clear()
        }
    }

    private fun getErrorText(errorCode: Int): String = when (errorCode) {
        SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
        SpeechRecognizer.ERROR_CLIENT -> "Client side error"
        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
        SpeechRecognizer.ERROR_NETWORK -> "Network error"
        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
        SpeechRecognizer.ERROR_NO_MATCH -> "No match"
        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
        SpeechRecognizer.ERROR_SERVER -> "error from server"
        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
        else -> "Didn't understand, please try again."
    }
}