package com.toofan.soft.qsb.api.session

internal object Memory {
    private var _token: String? = null
    val token: String?
        get() = _token

    internal fun updateToken(token: String) {
        _token = token.replace("\"", "")
    }
}