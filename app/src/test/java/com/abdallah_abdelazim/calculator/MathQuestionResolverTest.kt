package com.abdallah_abdelazim.calculator

import com.abdallah_abdelazim.calculator.data.service.mathengine.MathOperator
import com.abdallah_abdelazim.calculator.data.service.mathengine.MathQuestionResolver
import org.junit.Assert.assertEquals
import org.junit.Test

class MathQuestionResolverTest {

    @Test
    fun resolveMathQuestion_add() {
        assertEquals(
            6.0,
            MathQuestionResolver.resolveMathQuestion(
                MathOperator.ADD,
                listOf(1.0, 2.0, 3.0)
            ),
            0.0
        )
    }

    @Test
    fun resolveMathQuestion_subtract() {
        assertEquals(
            -4.0,
            MathQuestionResolver.resolveMathQuestion(
                MathOperator.SUBTRACT,
                listOf(1.0, 2.0, 3.0)
            ),
            0.0
        )
    }

    @Test
    fun resolveMathQuestion_multiply() {
        assertEquals(
            6.0,
            MathQuestionResolver.resolveMathQuestion(
                MathOperator.MULTIPLY,
                listOf(1.0, 2.0, 3.0)
            ),
            0.0
        )
    }

    @Test
    fun resolveMathQuestion_divide() {
        assertEquals(
            0.25,
            MathQuestionResolver.resolveMathQuestion(
                MathOperator.DIVIDE,
                listOf(1.0, 2.0, 2.0)
            ),
            0.0
        )
    }
}