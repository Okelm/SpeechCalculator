package com.bwidlarz.speechcalculator.di

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.bwidlarz.speechcalculator.data.Settings
import com.bwidlarz.speechcalculator.data.SharedPrefSettings
import com.bwidlarz.speechcalculator.recognition.MainPresenter
import com.bwidlarz.speechcalculator.utils.AnimationFactory
import com.bwidlarz.speechcalculator.utils.DialogFactory
import dagger.Module
import dagger.Provides
import java.util.Locale

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