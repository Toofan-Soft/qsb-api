package com.toofan.soft.qsb.api

data class OptionalVariable<T>(
    private var _value: T? = null,
    private var _isUpdated: Boolean = false
) {
    val value get() = _value
    val isUpdated get() = _isUpdated

    internal fun update(value: T?) {
        _value = value
        _isUpdated = true
    }
}

internal fun <T> loggableProperty(variable: OptionalVariable<T>): (T?) -> Unit {
    return { newValue ->
        println("The value of the variable (?) has changed from ${variable.value} to $newValue")
        variable.update(newValue)
    }
}
