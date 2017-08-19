package com.bwidlarz.speechcalculator.di

import com.bwidlarz.speechcalculator.MainPresenter
import dagger.Module
import dagger.Provides

@Module
class MainAcitivyModule {

    @Provides
    fun providesMainPresenter() = MainPresenter()
}

//@Provides
//fun providesHomePagePresenter(dataSource: DataSource, navigator: Navigator) = HomePagePresenter(dataSource, navigator)
//
//@Provides
//fun providesNavigator(activity: MainActivity) = Navigator(activity)