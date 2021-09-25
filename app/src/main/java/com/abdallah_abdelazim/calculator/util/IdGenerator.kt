package com.abdallah_abdelazim.calculator.util

object IdGenerator {

    @Volatile
    private var counter = 0

    @Synchronized
    fun generateUniqueId(): Int {
        return counter++
    }
}