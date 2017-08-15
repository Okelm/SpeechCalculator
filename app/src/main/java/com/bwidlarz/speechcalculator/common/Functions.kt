package com.bwidlarz.speechcalculator.common

import android.speech.SpeechRecognizer
import java.lang.Double.parseDouble

fun evaluate(string: String, errorHandler: (EvaluatorError) -> Double = ::mockErrorHandler): Double {

    var position = 0
    var char = '0'
    val endingChar = '£'

    fun moveToNextChar() {
        char = if (++position < string.length) string[position] else endingChar
    }

    fun moveForwardIfCharOrEmpty(vararg charWeSeek: Char): Boolean {
        while (char == ' ') moveToNextChar()
        if (charWeSeek.contains(char)) {
            moveToNextChar()
            return true
        }
        return false
    }

    fun isItStillNumber() = char in '0'..'9' || char == '.'

    fun parseTheWholeNumber(): Double {
        val startPos = position
        return when {
            isItStillNumber() -> {
                while (isItStillNumber()) {
                    moveToNextChar()
                }
                try {
                    parseDouble(string.substring(startPos, position))
                } catch (e: NumberFormatException){
                    errorHandler(EvaluatorError.UNEXPECTED_CHAR)
                }
            }
            else -> errorHandler(EvaluatorError.UNEXPECTED_CHAR)
        }
    }

    fun parseFactor(): Double {
        if (moveForwardIfCharOrEmpty('+')) return parseFactor()
        if (moveForwardIfCharOrEmpty('-')) return -parseFactor()

        return parseTheWholeNumber()
    }

    fun parseTerm(): Double {
        var x = parseFactor()
        while (true) when {
            moveForwardIfCharOrEmpty('*','x') -> x *= parseFactor()
            moveForwardIfCharOrEmpty('/') -> x /= parseFactor()
            else -> return x
        }
    }

    fun parseExpression(): Double {
        var x = parseTerm()
        while (true) when {
            moveForwardIfCharOrEmpty('+') -> x += parseTerm()
            moveForwardIfCharOrEmpty('-') -> x -= parseTerm()
            else -> return x
        }
    }

    fun parse(): Double {
        val x = parseExpression()
        if (position < string.length) errorHandler(EvaluatorError.UNEXPECTED_CHAR)
        return x
    }

    return parse()
}

fun isNumberOrSymbol(string: String): Boolean {
    for (char in string) {
        if (!(char in '0'..'9' || char == '.' || char == '+' || char == '-' || char == '*' || char == '/' || char == ' ' || char == 'x')) return false
    }
    return true
}

fun mockErrorHandler(error: EvaluatorError): Double = 0.0 //default function for unit


fun getErrorText(errorCode: Int): String = when (errorCode) {
    SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
    SpeechRecognizer.ERROR_CLIENT -> "Client side error"
    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
    SpeechRecognizer.ERROR_NETWORK -> "Network error"
    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
    SpeechRecognizer.ERROR_NO_MATCH -> "No match"
    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
    SpeechRecognizer.ERROR_SERVER -> "error from server"
    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
    else -> "Didn't understand, please try again."
}