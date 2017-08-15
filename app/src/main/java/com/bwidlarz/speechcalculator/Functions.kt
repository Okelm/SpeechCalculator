package com.bwidlarz.speechcalculator

fun isNumberOrSymbol(string: String): Boolean {
    for (char in string) {
        if (!(char in '0'..'9' || char == '.' || char == '+' || char == '-' || char == '*' || char == '/' || char == ' ' || char == 'x')) return false
    }
    return true
}

fun evaluate(string: String): Double {

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
                java.lang.Double.parseDouble(string.substring(startPos, position))
            }
            else -> 0.0
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
        if (position < string.length) return 0.0
        return x
    }

    return parse()
}