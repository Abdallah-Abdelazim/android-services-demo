package com.abdallah_abdelazim.calculator.ui.calculator

import androidx.lifecycle.ViewModel
import com.abdallah_abdelazim.calculator.util.SingleLiveEvent

class CalculatorViewModel : ViewModel() {

    companion object {
        private val TAG = CalculatorViewModel::class.simpleName
    }

    val messageEvent by lazy {
        SingleLiveEvent<Int>()
    }

    fun calculate() {
        TODO("Not yet implemented")
    }

}