package com.abdallah_abdelazim.calculator.data.service.mathengine

import com.abdallah_abdelazim.calculator.data.service.mathengine.MathOperator.*

object MathQuestionResolver {

    fun resolveMathQuestion(operator: MathOperator, operands: List<Double>): Double {
        return if (operands.isEmpty()) 0.0 else {
            when (operator) {
                ADD -> {
                    // Using the equation: result = operands[0] + operands[1] + operands[2] + ...
                    var result = operands.first()
                    for (i in 1 until operands.size) {
                        result += operands[i]
                    }
                    return result
                }
                SUBTRACT -> {
                    // Using the equation: result = operands[0] - operands[1] - operands[2] - ...
                    var result = operands.first()
                    for (i in 1 until operands.size) {
                        result -= operands[i]
                    }
                    return result
                }
                MULTIPLY -> {
                    // Using the equation: result = operands[0] * operands[1] * operands[2] * ...
                    var result = operands.first()
                    for (i in 1 until operands.size) {
                        result *= operands[i]
                    }
                    return result
                }
                DIVIDE -> {
                    // Using the equation: result = operands[0] / operands[1] / operands[2] / ...
                    var result = operands.first()
                    for (i in 1 until operands.size) {
                        result /= operands[i]
                    }
                    return result
                }
            }
        }
    }
}