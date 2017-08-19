package com.bwidlarz.speechcalculator.di

import com.bwidlarz.speechcalculator.SpeechCalculator
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        ApplicationModule::class,
        AndroidBindingModule::class,
        AndroidSupportInjectionModule::class))
interface ApplicationComponent : AndroidInjector<SpeechCalculator> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<SpeechCalculator>()
}
