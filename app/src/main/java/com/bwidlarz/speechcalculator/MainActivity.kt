package com.bwidlarz.speechcalculator

import android.Manifest
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
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
    private var textSoFar: String = EMPTY_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewBinding.listener = this

        presenter = MainPresenter()

        requestPermissions()
    }

    override fun onPause() {
//        speechRecognizer.stopListening()
//        speechRecognizer.cancel()
        presenter.detach()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        //speechRecognizer.destroy()
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
            WorkingState.CONTINUE -> presenter.loadSpeech(matches, textSoFar)
            WorkingState.LOOP -> {
                presenter.loadSpeech(matches, textSoFar)
                doOnLoopType()
            }
            WorkingState.NONE -> toast("None") //todo
        }
    }

    override fun onError(error: Int) {
        val errorMessage = getErrorText(error)
        toast(errorMessage)
        animateVisibility(viewBinding.progressBar, false)
    }

    override fun onEndOfSpeech() = if (workingState != WorkingState.LOOP) showProgress(false) else Unit //todo

    override fun onRecognitionFinished(stringExpression: String) {
        //this.log(stringExpression)
        presenter.evaluateExpression(stringExpression)
        viewBinding.expression.setText(stringExpression, TextView.BufferType.EDITABLE)
        viewBinding.expression.setSelection(stringExpression.length)
    }

    override fun onRecognitionError() {
        //todo
    }

    override fun onEvaluationFinished(evaluation: Double) {
        viewBinding.evaluation.text = evaluation.toString()
    }

    override fun onEvaluationError(error: EvaluatorError): Double {
//        toast(error.errorResId) //todo
        return 0.0
    }

    override fun onNewEvaluationClicked() {
        clearFields()
        workingState = WorkingState.NEW
        speechRecognizer.startListening(recognizerIntent)
        showProgress(true)
    }

    override fun onContinueClicked() {
        workingState = WorkingState.CONTINUE
        speechRecognizer.startListening(recognizerIntent)
        showProgress(true)
    }

    override fun onLoopClicked() {
        workingState = WorkingState.LOOP
        speechRecognizer.startListening(recognizerIntent)
        showProgress(true)
    }

    override fun onResetClicked() {
        clearFields()
    }

    override fun onStopClicked() {
        speechRecognizer.cancel()
        showProgress(false)
    }

    private fun showProgress(showLoading: Boolean) = animateVisibility(viewBinding.progressBar, showLoading)

    private fun animateVisibility(view: View, shouldBeVisible: Boolean) {
        view.clearAnimation()

        val finalScale = if (shouldBeVisible) 1f else 0f

        view.animate().scaleX(finalScale).scaleY(finalScale)
                .setDuration(200)
                .withEndAction { if (!shouldBeVisible) view.gone() }
                .withStartAction { if (shouldBeVisible) view.visible() }
                .start()
    }

    private fun clearFields() {
        viewBinding.apply {
            expression.text.clear()
            evaluation.clear()
        }
    }

    private fun onPartialResultDelivered() {}//todo
}