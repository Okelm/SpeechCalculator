package com.bwidlarz.speechcalculator

import org.junit.Assert.assertTrue
import org.junit.Test

class EvaluatorTest {

    //region Addition and Subtraction

    //region Addition and Subtraction without spaces
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
        assertTrue(result == 70.0)
    }
    // endregion

    //region Addition and Subtraction with empty spaces
    @Test
    fun should_calculateSimpleAddition_withSpace(){
        val result = evaluate("2 + 2")
        assertTrue(result == 4.0)
    }

    @Test
    fun should_calculateSimpleAdditions_withSpace(){
        val result = evaluate("2 + 8 + 10")
        assertTrue(result == 20.0)
    }

    @Test
    fun should_calculateSimpleSubtraction_withSpace(){
        val result = evaluate("10 - 5")
        assertTrue(result == 5.0)
    }

    @Test
    fun should_calculateSimpleSubtractions_withSpace(){
        val result = evaluate("10 - 5 - 2")
        assertTrue(result == 3.0)
    }

    @Test
    fun should_calculateSimpleSubtractionAndAddition_withSpace(){
        val result = evaluate("22 - 2 + 5")
        assertTrue(result == 25.0)
    }

    @Test
    fun should_calculateSimpleAdditionAndSubstration_withSpace(){
        val result = evaluate("100 + 55 - 85")
        assertTrue(result == 70.0)
    }
    // endregion

    // region Addition and Subtraction on doubles with spaces and without
    @Test
    fun should_calculateSimpleAddition_withSpace_double(){
        val result = evaluate("2.5 + 2.5")
        assertTrue(result == 5.0)
    }

    @Test
    fun should_calculateSimpleAdditions_double(){
        val result = evaluate("2.1+8.1+10.0")
        assertTrue(result == 20.2)
    }

    @Test
    fun should_calculateSimpleSubtraction_withSpace_double(){
        val result = evaluate("10.5 - 5")
        assertTrue(result == 5.5)
    }

    @Test
    fun should_calculateSimpleSubtractions_withSpace_double(){
        val result = evaluate("10.0 - 5.0 - 2.9")
        assertTrue(result == 2.1)
    }

    @Test
    fun should_calculateSimpleSubtractionAndAddition_double(){
        val result = evaluate("22.2-2.8+5.5")
        assertTrue(result == 24.9)
    }

    @Test
    fun should_calculateSimpleAdditionAndSubstration_mixed(){
        val result = evaluate("100.0 + 55- 85.5")
        assertTrue(result == 69.5)
    }
    //endregion

    //region Combinations
    @Test
    fun should_calculateSimpleAddition_withSpace_double_twoAddsSigns(){
        val result = evaluate("2.5 + + 2.5")
        assertTrue(result == 5.0)
    }

    @Test
    fun should_calculateSimpleAdditions_double_multipleZeros(){
        val result = evaluate("2.1000+8.100+10.000000")
        assertTrue(result == 20.2)
    }

    @Test
    fun should_calculateSimpleSubtraction_withSpace_double_tripleMinuses(){
        val result = evaluate("10.5 - - - 5")
        assertTrue(result == 5.5)
    }

    @Test
    fun should_calculateSimpleSubtractions_withSpace_double_minusAndPlus(){
        val result = evaluate("10.0 - + 5.0 - + 2.9")
        assertTrue(result == 2.1)
    }

    @Test
    fun should_calculateSimpleSubtractionAndAddition_double_combo(){
        val result = evaluate("22.2+ -2.8+ +5.5")
        assertTrue(result == 24.9)
    }

    @Test
    fun should_calculateSimpleAdditionAndSubstration_mixedSignes(){
        val result = evaluate("100.0 + 55- + - + -85.5")
        assertTrue(result == 69.5)
    }
    //endregion
    //endregion Addition and Subtraction

    //region Addition, Subtraction, Multiplication, Division
    @Test
    fun should_calculate_withSpace_double_1(){
        val result = evaluate("2.5 * 2.5")
        assertTrue(result == 6.25)
    }

    @Test
    fun should_calculate_withSpace_double_2(){
        val result = evaluate("1 + 1 * 2.0")
        assertTrue(result == 3.0)
    }

    @Test
    fun should_calculate_withSpace_double_3(){
        val result = evaluate("10/5")
        assertTrue(result == 2.0)
    }

    @Test
    fun should_calculate_withSpace_double_4(){
        val result = evaluate("5*4/2")
        assertTrue(result == 10.0)
    }

    @Test
    fun should_calculate_withSpace_double_5(){
        val result = evaluate("100*100*0")
        assertTrue(result == 0.0)
    }
    //endregion

}