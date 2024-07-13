package com.toofan.soft.qsb.api.helper

class ApiException1(
    private val _message: String,
    private val _code: Code
): Exception(_message) {
    val code: Code
        get() = _code

    enum class Code {

    }
}