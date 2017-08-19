package com.bwidlarz.speechcalculator

import android.Manifest
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.TextView
import com.bwidlarz.speechcalculator.common.*
import com.bwidlarz.speechcalculator.databinding.ActivityMainBinding
import com.tbruyelle.rxpermissions2.RxPermissions
import java.util.*


class MainActivity : BaseActivity(), SpeechView, RecognitionActionListener, RecognitionListenerAdapted {

    private val SO_FAR_TEXT_EXPRESSION = "so_far_text_expression"
    private val EVALUATION = "evaluation"

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
        restoreStateIfNeeded(savedInstanceState)
    }

    private fun restoreStateIfNeeded(savedInstanceState: Bundle?) {
            savedInstanceState?.apply {
                viewBinding.evaluation.text = getString(EVALUATION)
                viewBinding.expression.setText(getString(SO_FAR_TEXT_EXPRESSION), TextView.BufferType.EDITABLE)
            }
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

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(EVALUATION, viewBinding.evaluation.text.toString())
        outState?.putString(SO_FAR_TEXT_EXPRESSION, viewBinding.expression.text.toString())
    }

    private fun requestPermissions() {
        RxPermissions(this)
                .request(Manifest.permission.RECORD_AUDIO)
                .subscribe()
                .addToDisposables(disposables)
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

    override fun onReadyForSpeech(data: Bundle) = showProgress(true)

    override fun onBeginningOfSpeech() {
        textSoFar = viewBinding.expression.text.toString()
    }

    override fun onPartialResults(partialResults: Bundle) =proceedResults(partialResults, this::onPartialResultDelivered )

    override fun onResults(results: Bundle) = proceedResults(results, this::onLoopClicked)

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

    private fun createDisableLocationMockingDialog(): AlertDialog {
        return AlertDialog.Builder(this, R.style.Base_Theme_AppCompat_Light_Dialog)
                .setTitle(getString(R.string.no_internet))
                .setMessage(getString(R.string.no_internet_message))
                .setPositiveButton(getString(R.string.settings_label), { dialog, _ -> startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS)) })
                .setNegativeButton(getString(R.string.ok), { dialog, _ -> dialog.dismiss()})
                .create()
    }

    override fun onError(error: Int) {
        when(error){
            SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_SERVER -> createDisableLocationMockingDialog().show()
            else -> toast(getErrorText(error))
        }
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

    override fun onEvaluationError(throwable: Throwable): Double {
       // toast(throwable.message.toString())
        return DEFAULT_RESULT
    }

    override fun onNewEvaluationClicked() {
        clearFields()
        workingState = WorkingState.NEW
        speechRecognizer.startListening(recognizerIntent)
    }

    override fun onContinueClicked() {
        workingState = WorkingState.CONTINUE
        speechRecognizer.startListening(recognizerIntent)
    }

    override fun onLoopClicked() {
        workingState = WorkingState.LOOP
        speechRecognizer.startListening(recognizerIntent)
    }

    override fun onResetClicked() = clearFields()

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