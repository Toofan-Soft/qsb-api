package com.toofan.soft.qsb.api.session

internal object Session {
    private var _token: String? = null
    private var _userType: UserType? = null

    internal val token: String?
        get() = _token

    internal val userType: UserType?
        get() = _userType

    internal fun updateToken(token: String) {
        _token = token
    }

    internal fun updateUserType(type: UserType) {
        _userType = type
    }
}