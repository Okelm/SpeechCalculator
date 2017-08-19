package com.bwidlarz.speechcalculator.common

import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun <T> applyComputingShedulers(): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
        upstream.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun Disposable.addToDisposables(compositeDisposable: CompositeDisposable) = compositeDisposable.add(this)