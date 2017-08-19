package com.bwidlarz.speechcalculator.common

import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {

    protected val disposables = CompositeDisposable()

    override fun onStop() {
        disposables.clear()
        super.onStop()
    }
}