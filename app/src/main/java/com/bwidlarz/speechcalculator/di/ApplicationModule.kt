package com.bwidlarz.speechcalculator.di

import android.content.Context
import com.bwidlarz.speechcalculator.SpeechCalculator
import dagger.Binds
import dagger.Module

@Module
abstract class ApplicationModule {

    @Binds
    abstract fun application(app: SpeechCalculator): Context
}