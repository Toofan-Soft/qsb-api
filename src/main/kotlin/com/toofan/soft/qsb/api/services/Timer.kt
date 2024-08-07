package com.toofan.soft.qsb.api.services

import java.util.*
import java.util.Timer

interface TimerListener {
//    fun onUpdate(value: String)
    fun onUpdate(value: Long)
//    fun onUpdate(hours: Int, minutes: Int, seconds: Int)
    fun onFinish()
}

internal data class Timer(
    private val totalTime: Long
) {
    fun schedule(
//        onUpdate: (Long) -> Unit,
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
            0,  // Initial delay
            1000   // Interval
        )
    }
}

//internal fun formatSeconds(seconds: Long): String {
//    val hours = seconds / 3600
//    val minutes = (seconds % 3600) / 60
//    val secs = seconds % 60
//    return String.format("%d:%02d:%02d", hours, minutes, secs)
//}

fun main() {
//    Timer(10000).schedule(
//        onUpdate = {
////            println("Remaining time: ${it / 1000} seconds")
//            println("Remaining time: $it seconds")
//        },
//        onFinish = {
//            println("Time's up!")
//        }
//    )

//    val startDateTime = LocalDateTime.of(2023, 7, 18, 10, 0, 10)
//    val endDateTime = LocalDateTime.of(2023, 7, 18, 10, 0, 5)
//
//    val duration = Duration.between(startDateTime, endDateTime)
//    val milliseconds = duration.toMillis()
//
//    println("Difference in milliseconds: $milliseconds")

//    val seconds = 3661
//    println(formatSeconds(seconds)) // Output: 1:01:01
}