package com.toofan.soft.qsb.api.session

internal object Session {
    private var _token: String? = null
    private var _role: Role? = null

    internal val token: String?
        get() = _token

    internal val role: Role?
        get() = _role

    internal fun updateToken(token: String) {
        _token = token
    }

    internal fun updateRole(type: Role) {
        _role = type
    }
}