package com.abdallah_abdelazim.calculator.ui.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abdallah_abdelazim.calculator.R
import com.abdallah_abdelazim.calculator.data.model.MathQuestion
import com.abdallah_abdelazim.calculator.data.service.mathengine.MathOperator
import com.abdallah_abdelazim.calculator.util.IdGenerator
import com.abdallah_abdelazim.calculator.util.SingleLiveEvent

class CalculatorViewModel : ViewModel() {

    companion object {
        private val TAG = CalculatorViewModel::class.simpleName
    }

    private val pendingMathQuestionsList = mutableListOf<MathQuestion>()
    private val completedMathQuestionsList = mutableListOf<MathQuestion>()

    private val _pendingMathQuestions = MutableLiveData<List<MathQuestion>>()
    val pendingMathQuestions: LiveData<List<MathQuestion>> get() = _pendingMathQuestions

    private val _completedMathQuestions = MutableLiveData<List<MathQuestion>>()
    val completedMathQuestions: LiveData<List<MathQuestion>> get() = _completedMathQuestions

    val calculateMathQuestionEvent = SingleLiveEvent<MathQuestion>()

    val messageEvent by lazy {
        SingleLiveEvent<Int>()
    }

    fun calculate(mathOperator: MathOperator?, operands: List<Double>?, delaySecs: Long?) {
        if (mathOperator != null && !operands.isNullOrEmpty() && delaySecs != null) {
            val mathQuestion = MathQuestion(
                IdGenerator.generateUniqueId(),
                mathOperator, operands, delaySecs
            )
            calculateMathQuestionEvent.value = mathQuestion

            pendingMathQuestionsList.run {
                add(mathQuestion)
                _pendingMathQuestions.value = this
            }
        } else {
            messageEvent.value = R.string.error_calculator_input
        }
    }

    fun notifyMathQuestionResult(mathQuestion: MathQuestion) {
        pendingMathQuestionsList.run {
            remove(mathQuestion)
            _pendingMathQuestions.value = this
        }

        completedMathQuestionsList.run {
            add(mathQuestion)
            _completedMathQuestions.value = this
        }
    }
}