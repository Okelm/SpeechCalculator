package com.bwidlarz.speechcalculator

import android.Manifest
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.bwidlarz.speechcalculator.common.BaseActivity
import com.bwidlarz.speechcalculator.common.EMPTY_STRING
import com.bwidlarz.speechcalculator.common.DEFAULT_RESULT
import com.bwidlarz.speechcalculator.common.addToDisposables
import com.bwidlarz.speechcalculator.common.toast
import com.bwidlarz.speechcalculator.common.clear
import com.bwidlarz.speechcalculator.data.Settings
import com.bwidlarz.speechcalculator.databinding.ActivityMainBinding
import com.bwidlarz.speechcalculator.recognition.MainPresenter
import com.bwidlarz.speechcalculator.recognition.RecognitionActionListener
import com.bwidlarz.speechcalculator.recognition.RecognitionListenerAdapted
import com.bwidlarz.speechcalculator.recognition.SpeechView
import com.bwidlarz.speechcalculator.recognition.WorkingState
import com.bwidlarz.speechcalculator.recognition.getErrorText
import com.bwidlarz.speechcalculator.utils.AnimationFactory
import com.bwidlarz.speechcalculator.utils.DialogFactory
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : BaseActivity(), SpeechView, RecognitionActionListener, RecognitionListenerAdapted {

    @Inject lateinit var presenter: MainPresenter
    @Inject lateinit var sharedPrefSettings: Settings
    @Inject lateinit var speechRecognizer: SpeechRecognizer
    @Inject lateinit var recognizerIntent: Intent
    @Inject lateinit var dialogFactory: DialogFactory
    @Inject lateinit var animationFactory: AnimationFactory

    private lateinit var viewBinding: ActivityMainBinding
    private var workingState: WorkingState = WorkingState.NONE
    private var textSoFar: String = EMPTY_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewBinding.listener = this

        restoreStateIfNeeded(savedInstanceState)
        animationFactory.setMainShowcase(this, viewBinding).start()
        requestPermissions()
    }

    private fun restoreStateIfNeeded(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            extractFieldsStateFromBundle(savedInstanceState)
        } else {
            extractFieldsStateFromPrefs()
        }
    }

    private fun extractFieldsStateFromPrefs() {
        viewBinding.expression.setText(sharedPrefSettings.lastExpression, TextView.BufferType.EDITABLE)
        viewBinding.evaluation.text = sharedPrefSettings.lastEvaluation
    }

    private fun extractFieldsStateFromBundle(savedInstanceState: Bundle) {
        savedInstanceState.apply {
            viewBinding.evaluation.text = getString(EVALUATION)
            viewBinding.expression.setText(getString(SO_FAR_TEXT_EXPRESSION), TextView.BufferType.EDITABLE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.replay_tutorial) {
            sharedPrefSettings.tutorialShownId = ++sharedPrefSettings.tutorialShownId
            animationFactory.setMainShowcase(this, viewBinding).start()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        presenter.detach()
        super.onPause()
    }

    override fun onStop() {
        super.onStop()

        speechRecognizer.destroy()
        saveStateToPrefs()
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

    private fun saveStateToPrefs() {
        sharedPrefSettings.lastExpression = viewBinding.expression.text.toString()
        sharedPrefSettings.lastEvaluation = viewBinding.evaluation.text.toString()
    }

    private fun requestPermissions() {
        RxPermissions(this)
                .request(Manifest.permission.RECORD_AUDIO)
                .subscribe()
                .addToDisposables(disposables)
    }

    private fun setupRecognizer() {
        speechRecognizer.setRecognitionListener(this)

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

    override fun onPartialResults(partialResults: Bundle) = proceedResults(partialResults)

    override fun onResults(results: Bundle) = proceedResults(results, this::onLoopClicked)

    private fun proceedResults(partialResults: Bundle, doOnLoopType: () -> Unit = {}) {
        val matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

        when (workingState) {
            WorkingState.NEW -> presenter.loadSpeech(matches)
            WorkingState.CONTINUE -> presenter.loadSpeech(matches, textSoFar)
            WorkingState.LOOP -> {
                presenter.loadSpeech(matches, textSoFar)
                doOnLoopType()
            }
            WorkingState.NONE -> RuntimeException("WorkingState should have been opted before running the recognition")
        }
    }

    override fun onError(error: Int) {
        when (error) {
            SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_SERVER ->
                dialogFactory.createRedirectToSettingsDialog(this).show()
            else -> toast(getErrorText(error))
        }
        showProgress(false)
    }

    override fun onEndOfSpeech() = if (workingState != WorkingState.LOOP) showProgress(false) else Unit //todo

    override fun onRecognitionFinished(stringExpression: String) {
        presenter.evaluateExpression(stringExpression)
        viewBinding.expression.setText(stringExpression, TextView.BufferType.EDITABLE)
        viewBinding.expression.setSelection(stringExpression.length)
    }

    override fun onEvaluationFinished(evaluation: Double) {
        viewBinding.evaluation.text = evaluation.toString()
    }

    // the empty result is so common that it is not worth handling. It might be implemented in the future though
    override fun onRecognitionError() {}

    // it might be use to somehow visualize the errors, right now its dummy implementation
    override fun onEvaluationError(throwable: Throwable): Double = DEFAULT_RESULT

    override fun onNewEvaluationClicked() {
        clearFields()
        startListeningWithState(WorkingState.NEW)
    }

    override fun onContinueClicked() {
        startListeningWithState(WorkingState.CONTINUE)
    }

    override fun onLoopClicked() {
        startListeningWithState(WorkingState.LOOP)
    }

    private fun startListeningWithState(state: WorkingState) {
        workingState = state
        speechRecognizer.startListening(recognizerIntent)
    }

    override fun onResetClicked() = clearFields()

    override fun onStopClicked() {
        speechRecognizer.cancel()
        showProgress(false)
    }

    private fun showProgress(showLoading: Boolean) = animationFactory.animateVisibility(viewBinding.progressBar, showLoading)

    private fun clearFields() {
        viewBinding.apply {
            expression.text.clear()
            evaluation.clear()
        }
    }

    companion object {
        const val SO_FAR_TEXT_EXPRESSION = "so_far_text_expression"
        const val EVALUATION = "evaluation"
    }
}