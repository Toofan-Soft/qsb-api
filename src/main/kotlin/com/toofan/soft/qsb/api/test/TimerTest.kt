package com.toofan.soft.qsb.api.test

import com.toofan.soft.qsb.api.services.Timer
import com.toofan.soft.qsb.api.services.TimerListener

data class TimerTest(
    val time: Long
) {
    companion object {
        private var listener: TimerListener? = null
        private var isRun: Boolean = false

        internal fun stop() {
            isRun = false
        }
    }

    internal fun run() {
        isRun = true
        startTimer()
    }

    private fun startTimer() {
        Timer(time)
            .schedule(
                onUpdate = {
                    if (isRun) {
                        listener?.onUpdate(it / 1000)
                    }
                    isRun
                },
                onFinish = {
                    listener?.onFinish()
                }
            )
    }

    fun setOnRemainingTimerListener(listener: TimerListener) {
        TimerTest.listener = listener
    }
}

fun main() {
    val timer = TimerTest(10000)
        .apply {
            setOnRemainingTimerListener(object : TimerListener {
                override fun onUpdate(value: Long) {
                    println("remaining time: $value")
                }

                override fun onFinish() {
                    println("Finished...")
                }
            })

            run()
        }

    val thread = Thread {
        // First process
        println("\nstart\n")
        Thread.sleep(3000) // Simulating work with sleep

        TimerTest.stop()
        println("\nstop\n")

        Thread.sleep(3000) // Simulating work with sleep

        println("\nrun\n")
        timer.run()

        Thread.sleep(3000) // Simulating work with sleep

        TimerTest.stop()
        println("\nstop\n")

        println("\nend\n")
    }

    thread.start()
    thread.join()

    println("\nDone#\n")
}

