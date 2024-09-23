package com.toofan.soft.qsb.api.services

import java.util.*
import java.util.Timer

interface TimerListener {
    fun onUpdate(value: Long)
    fun onFinish()
}

internal data class Timer(
    private val totalTime: Long
) {
    fun schedule(
        onUpdate: (Long) -> Boolean,
        onFinish: () -> Unit
    ) {
        val timer = Timer()
        val startTime = System.currentTimeMillis()

        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    val currentTime = System.currentTimeMillis()
                    val elapsedTime = currentTime - startTime
                    val remainingTime = totalTime - elapsedTime

                    if (remainingTime <= 0) {
                        onFinish()
                        timer.cancel()
                    } else {
                        if (!onUpdate(remainingTime)) {
                            timer.cancel()
                        }
                    }
                }
            },
            0, // Initial delay
            1000 // Interval
        )
    }
}