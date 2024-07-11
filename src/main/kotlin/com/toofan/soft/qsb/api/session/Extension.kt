package com.toofan.soft.qsb.api.session

import com.google.gson.JsonObject

private val token: String
    get() = "token"

private val roleId: String
    get() = "role_id"

internal fun JsonObject.checkSession() {
    if (hasToken) {
        removeToken()?.let {
            it.replace("\"", "").let {
                Session.updateToken(it)
            }
        }
    }
    if (hasRoleId) {
        removeRoleId()?.let {
            it.replace("\"", "").toIntOrNull()?.let { Role.of(it) }?.let {
                Session.updateRole(it)
            }
        }
    }
}

private fun JsonObject.removeToken(): String? {
    return this.remove(token)?.toString()
}

private fun JsonObject.removeRoleId(): String? {
    return this.remove(roleId)?.toString()
}

private val JsonObject.hasToken : Boolean
    get() = this.has(token)

private val JsonObject.hasRoleId : Boolean
    get() = this.has(roleId)

fun String.unescapeAll(): String {
    return this.replace("\"", "")
}

fun String.unescapeBookends(): String {
    return this.replace(Regex("^\"|\"$"), "")
}
