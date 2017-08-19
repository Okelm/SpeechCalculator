package com.bwidlarz.speechcalculator.di

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.bwidlarz.speechcalculator.MainPresenter
import com.bwidlarz.speechcalculator.SharedPrefSettings
import dagger.Module
import dagger.Provides
import java.util.*

@Module
class MainAcitivyModule {

    @Provides
    fun providesMainPresenter() = MainPresenter()

    @Provides
    fun providesSharedPrefSettings(context: Context) = SharedPrefSettings(context)

    @Provides
    fun providesSpeechRecognition(context: Context) = SpeechRecognizer.createSpeechRecognizer(context)

    @Provides
    fun providesSpeechIntent(): Intent {
        val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent.apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH.toString())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }
        return recognizerIntent
    }
}