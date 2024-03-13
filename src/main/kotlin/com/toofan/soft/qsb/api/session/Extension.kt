package com.toofan.soft.qsb.api.session

import com.google.gson.JsonObject

private val token: String
    get() = "token"

internal fun JsonObject.checkToken() {
    if (hasToken) {
        removeToken()?.let { Memory.updateToken(it) }
    }
}

private fun JsonObject.removeToken(): String? {
    return this.remove(token)?.toString()
}

private val JsonObject.hasToken : Boolean
    get() = this.has(token)

fun String.unescapeAll(): String {
    return this.replace("\"", "")
}

fun String.unescapeBookends(): String {
    return this.replace(Regex("^\"|\"$"), "")
}
