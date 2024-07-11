package com.toofan.soft.qsb.api.session

internal object Session {
    private var _token: String? = null
    private var _userType: UserType? = null
    private var _roles: List<Role>? = null
    private var _language: Language? = null

    internal val token: String?
        get() = _token

    internal val userType: UserType?
        get() = _userType

    internal val roles: List<Role>?
        get() = _roles

    internal val language: Language?
        get() = _language

    internal fun updateToken(token: String) {
        _token = token
    }

    internal fun updateUserType(type: UserType) {
        _userType = type
    }

    internal fun updateRoles(roles: List<Role>) {
        _roles = roles
    }

    internal fun updateLanguage(language: Language) {
        _language = language
    }
}