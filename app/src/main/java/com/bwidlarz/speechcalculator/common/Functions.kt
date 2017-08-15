package com.bwidlarz.speechcalculator.common

import java.lang.Double.parseDouble

fun isNumberOrSymbol(string: String): Boolean {
    for (char in string) {
        if (!(char in '0'..'9' || char == '.' || char == '+' || char == '-' || char == '*' || char == '/' || char == ' ' || char == 'x')) return false
    }
    return true
}

fun mockErrorHandler(error: EvaluatorError): Double = 0.0

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