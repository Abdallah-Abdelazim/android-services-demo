package com.abdallah_abdelazim.calculator.data.model

import com.abdallah_abdelazim.calculator.data.service.mathengine.MathOperator

data class MathQuestion(
    val id: Int,
    val operator: MathOperator,
    val operands: List<Double>,
    val delay: Int
)