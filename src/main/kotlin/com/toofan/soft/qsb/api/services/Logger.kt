package com.toofan.soft.qsb.api.services

object Logger {
    private const val TAG = "m7devoo-logger"

    fun log(title: String, message: Any?) {
        println("$TAG\t\t\t$title: $message")
    }
}