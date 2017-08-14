package com.bwidlarz.speechcalculator

import org.junit.Assert.assertTrue
import org.junit.Test

class EvaluatorTest {

    @Test
    fun should_calculateSimpleAddition(){
        val result = evaluate("2+2")
        assertTrue(result == 4.0)
    }

    @Test
    fun should_calculateSimpleAdditions(){
        val result = evaluate("2+8+10")
        assertTrue(result == 20.0)
    }

    @Test
    fun should_calculateSimpleSubtraction(){
        val result = evaluate("10-5")
        assertTrue(result == 5.0)
    }

    @Test
    fun should_calculateSimpleSubtractions(){
        val result = evaluate("10-5-2")
        assertTrue(result == 3.0)
    }

    @Test
    fun should_calculateSimpleSubtractionAndAddition(){
        val result = evaluate("22-2+5")
        assertTrue(result == 25.0)
    }

    @Test
    fun should_calculateSimpleAdditionAndSubstration(){
        val result = evaluate("100+55-85")
        assertTrue(result == 130.0)
    }
}