package com.bwidlarz.speechcalculator

import android.os.Bundle
import android.speech.RecognitionListener

//TODO
interface RecognitionListenerAdapted: RecognitionListener {
    override fun onReadyForSpeech(p0: Bundle?) {}

    override fun onRmsChanged(p0: Float) {}

    override fun onBufferReceived(p0: ByteArray?) {}

    override fun onPartialResults(p0: Bundle?) {}

    override fun onEvent(p0: Int, p1: Bundle?) {}

    override fun onBeginningOfSpeech() {}

    override fun onEndOfSpeech() {}

    override fun onError(p0: Int) {}

    override fun onResults(p0: Bundle?) {}
}