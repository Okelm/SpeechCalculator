package com.bwidlarz.speechcalculator.common

import android.support.annotation.VisibleForTesting

abstract class BasePresenter<ViewInterface : BaseView> {

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
        view = null
    }

    open fun onPreDetach() {}
}

interface BaseView

