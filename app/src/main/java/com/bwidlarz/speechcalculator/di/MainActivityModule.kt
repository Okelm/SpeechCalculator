package com.bwidlarz.speechcalculator.di

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.bwidlarz.speechcalculator.AnimationFactory
import com.bwidlarz.speechcalculator.DialogFactory
import com.bwidlarz.speechcalculator.MainPresenter
import com.bwidlarz.speechcalculator.SharedPrefSettings
import com.bwidlarz.speechcalculator.common.Settings
import dagger.Module
import dagger.Provides
import java.util.*

@Module
class MainActivityModule {

    @Provides
    fun providesMainPresenter(): MainPresenter = MainPresenter()

    @Provides
    fun providesSharedPrefSettings(context: Context): Settings = SharedPrefSettings(context)

    @Provides
    fun providesSpeechRecognition(context: Context): SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

    @Provides
    fun providesSpeechIntent(): Intent {
        val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent.apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH.toString())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }
        return recognizerIntent
    }

    @Provides
    fun providesDialogFactory(context: Context): DialogFactory = DialogFactory(context)

    @Provides
    fun providesAnimationFactory(context: Context, settings: Settings): AnimationFactory = AnimationFactory(context, settings)
}