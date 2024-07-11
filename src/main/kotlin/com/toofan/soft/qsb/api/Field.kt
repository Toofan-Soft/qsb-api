package com.toofan.soft.qsb.api

@Target(AnnotationTarget.FIELD)
@Retention
internal annotation class Field(
    /**
     * Returns the name.
     *
     * @return the name
     */
//    val value: String,
    val _value: String,
    val isDate: Boolean = false,
    val isTime: Boolean = false,
    val isDatetime: Boolean = false

)

internal val Field.value get() = _value.lowercase()
