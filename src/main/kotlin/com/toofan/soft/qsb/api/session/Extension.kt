package com.toofan.soft.qsb.api.session

import com.google.gson.JsonObject

private val token: String
    get() = "token"

private val userTypeId: String
    get() = "user_type_id"

//internal fun JsonObject.checkToken() {
internal fun JsonObject.checkSession() {
    if (hasToken) {
        removeToken()?.let {
            it.replace("\"", "").let {
                Session.updateToken(it)
            }
        }
    }
    if (hasUserTypeId) {
        removeUserTypeId()?.let {
            it.replace("\"", "").toIntOrNull()?.let { UserType.of(it) }?.let {
                Session.updateUserType(it)
            }
        }
    }
}

private fun JsonObject.removeToken(): String? {
    return this.remove(token)?.toString()
}

private fun JsonObject.removeUserTypeId(): String? {
    return this.remove(userTypeId)?.toString()
}

private val JsonObject.hasToken : Boolean
    get() = this.has(token)

private val JsonObject.hasUserTypeId : Boolean
    get() = this.has(userTypeId)

fun String.unescapeAll(): String {
    return this.replace("\"", "")
}

fun String.unescapeBookends(): String {
    return this.replace(Regex("^\"|\"$"), "")
}
