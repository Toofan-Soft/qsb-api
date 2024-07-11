package com.toofan.soft.qsb.api.extensions

import java.time.*

internal val LocalDate.string get() = this.toString()

internal val LocalTime.string get() = this.toString()

internal val LocalDateTime.string get() = this.toString()

//internal val Long.datetime get() = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.of("Asia/Aden"))
internal val Long.datetime get() = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
internal val Long.date get() = LocalDate.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
internal val Long.time get() = LocalTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())

internal val Long.time1: LocalTime
    get() {
        val seconds = this / 1000
        val nanos = (this % 1000) * 1_000_000
        return LocalTime.ofSecondOfDay(seconds.toLong()).plusNanos(nanos)
    }


internal val LocalDateTime.milliseconds: Long get() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

internal val LocalDate.milliseconds: Long get() = this.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

internal val LocalTime.milliseconds: Long get() = this.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

internal val LocalTime.milliseconds1: Long get() = this.toNanoOfDay() / 1_000_000


fun main() {
    LocalTime.now().milliseconds.let {
        println(it)
    }

    LocalTime.now().let {
        println(it)
        println(it.milliseconds1)
        println(it.milliseconds1.time)
        println(it.milliseconds1.time1)
    }

    LocalDate.now().milliseconds.let {
        println(it)
    }

    LocalDateTime.now().minusYears(50).milliseconds.let {
        println(it)
    }

    48151.toLong().time.let {
        println(it)
        println(it.milliseconds)
    }
}
