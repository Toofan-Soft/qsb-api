package com.toofan.soft.qsb.api.extensions

import java.time.*
import java.time.format.DateTimeFormatter

internal val LocalDate.string get() = this.toString()

//internal val LocalTime.string get() = this.toString()
//
//internal val LocalDateTime.string get() = this.toString()
internal val LocalTime.string: String
    get() = this.format(DateTimeFormatter.ofPattern("hh:mm a"))

internal val LocalDateTime.string: String
    get() = this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"))

internal val Long.datetime get() = LocalDateTime.ofInstant(Instant.ofEpochSecond(this), ZoneId.systemDefault())
internal val Long.date get() = LocalDate.ofEpochDay(this)
internal val Long.time get() = LocalTime.ofSecondOfDay(this)



internal val LocalDateTime.long: Long get() = this.atZone(ZoneId.systemDefault()).toInstant().epochSecond
internal val LocalDate.long: Long get() = this.toEpochDay()
internal val LocalTime.long get() = this.toNanoOfDay() / 1_000_000_000


//internal val LocalDateTime.milliseconds: Long get() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
//
//internal val LocalDate.milliseconds: Long get() = this.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
//
//internal val LocalTime.milliseconds: Long get() = this.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()



fun main() {
//    LocalTime.now().milliseconds.let {
//        println(it)
//    }
//
//    LocalTime.now().let {
//        println(it)
//        println(it.milliseconds1)
//        println(it.milliseconds1.time)
//        println(it.milliseconds1.time1)
//    }
//
//    LocalDate.now().milliseconds.let {
//        println(it)
//    }
//
//    LocalDateTime.now().minusYears(50).milliseconds.let {
//        println(it)
//    }
//
//    48151.toLong().time.let {
//        println(it)
//        println(it.milliseconds)
//    }


//    LocalDateTime.now().plusDays(2).let {
//        println(it)
//        println(it.long)
//        println(it.long.datetime)
//    }
//
//    println()
//
////    LocalDate.now().let {
//    LocalDate.now().minusYears(20).let {
//        println(it)
//        println(it.long)
//        println(it.long.date)
//    }
//
//    println()
//
//    LocalTime.now().let {
//        println(it)
//        println(it.long)
//        println(it.long.time)
//    }

    val time = LocalTime.now()
    val dateTime = LocalDateTime.now()

    println("Formatted LocalTime: ${time.string}")
    println("Formatted LocalDateTime: ${dateTime.string}")
    println("Formatted LocalDateTime: ${dateTime.minusDays(1).long}")
    println("Formatted LocalDateTime: ${dateTime.plusDays(1).long}")

}
