package com.bwidlarz.speechcalculator

import com.bwidlarz.speechcalculator.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class SpeechCalculator : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<SpeechCalculator> {
        return DaggerApplicationComponent.builder().create(this)
    }
}