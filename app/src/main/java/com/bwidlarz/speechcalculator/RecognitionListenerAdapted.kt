package com.bwidlarz.speechcalculator

import android.os.Bundle
import android.speech.RecognitionListener

//TODO
interface RecognitionListenerAdapted: RecognitionListener {
    override fun onReadyForSpeech(data: Bundle) {}

    override fun onRmsChanged(data: Float) {}

    override fun onBufferReceived(buffer: ByteArray) {}

    override fun onPartialResults(partialResults: Bundle) {}

    override fun onEvent(event: Int, data: Bundle) {}

    override fun onBeginningOfSpeech() {}

    override fun onEndOfSpeech() {}

    override fun onError(error: Int) {}

    override fun onResults(results: Bundle) {}
}