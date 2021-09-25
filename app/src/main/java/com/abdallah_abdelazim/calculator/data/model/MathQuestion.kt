package com.abdallah_abdelazim.calculator.data.model

import com.abdallah_abdelazim.calculator.data.service.mathengine.MathOperator

data class MathQuestion(
    val id: Int,
    val operator: MathOperator,
    val operands: List<Double>,
    val delaySecs: Long,
    var status: Status = Status.PENDING,
    var result: Double? = null
) {
    enum class Status {
        PENDING,
        COMPLETED
    }
}