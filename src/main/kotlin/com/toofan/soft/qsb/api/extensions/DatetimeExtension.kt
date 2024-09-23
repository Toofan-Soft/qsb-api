package com.toofan.soft.qsb.api.extensions

import java.time.*
import java.time.format.DateTimeFormatter

internal val LocalDate.string get() = this.toString()

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
