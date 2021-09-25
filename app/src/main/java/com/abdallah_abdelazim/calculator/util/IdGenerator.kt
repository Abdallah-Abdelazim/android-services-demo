package com.abdallah_abdelazim.calculator.util

object IdGenerator {

    private var counter = 0

    fun generateUniqueId(): Int {
        return counter++
    }
}