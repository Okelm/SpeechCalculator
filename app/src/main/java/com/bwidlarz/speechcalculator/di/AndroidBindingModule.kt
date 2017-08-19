package com.bwidlarz.speechcalculator.di

import com.bwidlarz.speechcalculator.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AndroidBindingModule {

    @ContributesAndroidInjector(modules = arrayOf(MainAcitivyModule::class))
    abstract fun mainActivity(): MainActivity
}