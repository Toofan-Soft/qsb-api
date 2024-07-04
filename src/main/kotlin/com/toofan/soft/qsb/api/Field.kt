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
    val _value: String
)

internal val Field.value get() = _value.lowercase()
