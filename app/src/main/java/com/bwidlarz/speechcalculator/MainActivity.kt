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
import com.bwidlarz.speechcalculator.common.*
import com.bwidlarz.speechcalculator.databinding.ActivityMainBinding
import com.tbruyelle.rxpermissions2.RxPermissions
import java.util.*


class MainActivity : AppCompatActivity(), SpeechView, RecognitionActionListener, RecognitionListenerAdapted {

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var presenter: MainPresenter
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognizerIntent: Intent

    private var workingState: WorkingState = WorkingState.NONE
    private var textSoFar: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewBinding.listener = this

        presenter = MainPresenter()

        requestPermissions()
    }

    override fun onPause() {
        speechRecognizer.stopListening()
        speechRecognizer.cancel()
        presenter.detach()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
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
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
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

    override fun onBeginningOfSpeech() {
        textSoFar = viewBinding.expression.text.toString()
    }

    override fun onPartialResults(partialResults: Bundle) {
        proceedResults(partialResults, this::onPartialResultDelivered )
    }

    override fun onResults(results: Bundle) {
        proceedResults(results, this::onLoopClicked)
    }

    private fun proceedResults(partialResults: Bundle, doOnLoopType: () -> Unit) {
        val matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

        when (workingState) {
            WorkingState.NEW -> presenter.loadSpeech(matches)
            WorkingState.CONTINUE -> presenter.loadSpeechWithPrevious(matches, textSoFar)
            WorkingState.LOOP -> {
                presenter.loadSpeechWithPrevious(matches, textSoFar)
                doOnLoopType()
            }
            WorkingState.NONE -> toast("None") //todo
        }
    }

    override fun onError(error: Int) {
        val errorMessage = getErrorText(error)
        toast(errorMessage)
    }

    override fun onRecognitionFinished(stringExpression: String) {
        this.log(stringExpression)
        presenter.evaluateExpression(stringExpression)
        viewBinding.expression.setText(stringExpression, TextView.BufferType.EDITABLE)
        viewBinding.expression.setSelection(stringExpression.length)
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
        workingState = WorkingState.NEW
        speechRecognizer.startListening(recognizerIntent)
    }

    override fun onContinueClicked() {
        workingState = WorkingState.CONTINUE
        speechRecognizer.startListening(recognizerIntent)
    }

    override fun onLoopClicked() {
        workingState = WorkingState.LOOP
        speechRecognizer.stopListening()
        speechRecognizer.startListening(recognizerIntent)
    }

    override fun onResetClicked() {
        viewBinding.apply {
            expression.text.clear()
            evaluation.clear()
        }
    }

    private fun onPartialResultDelivered() {}//todo
}