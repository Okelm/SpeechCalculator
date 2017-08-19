package com.bwidlarz.speechcalculator.common

import android.support.annotation.VisibleForTesting
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<ViewInterface : BaseView> {

    protected val disposables: CompositeDisposable = CompositeDisposable()

    var view: ViewInterface? = null
        @VisibleForTesting(otherwise = VisibleForTesting.NONE) get
        private set

    fun <T : BaseView> BasePresenter<T>.withView(block: T.() -> Unit) {
        if (this.view != null) block(this.view as T)
    }

    fun attach(viewImpl: ViewInterface) {
        view = requireNotNull(viewImpl) { "View implementation must not be null!" }
        onAttached()
    }

    open fun onAttached() {}

    open fun detach() {
        onPreDetach()
        disposables.clear()
        view = null
    }

    open fun onPreDetach() {}
}

interface BaseView

