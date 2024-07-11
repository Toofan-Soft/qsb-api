package com.toofan.soft.qsb.api.session

import com.google.gson.JsonObject

private val token: String
    get() = "token"

private val userTypeId: String
    get() = "user_type_id"

private val rolesIds: String
    get() = "roles_ids"

private val languageId: String
    get() = "language_id"

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

    if (hasRolesIds) {
        removeRolesIds()?.let {
            it
                .replace("[", "")
                .replace("]", "")
                .let {
                    it.replace("\"", "")
                        .split(",")
                        .mapNotNull {
                            it.toIntOrNull()?.let {
                                Role.of(it)
                            }
                        }.let {
                            Session.updateRoles(it)
                        }
                }
        }
    }

    if (hasLanguageId) {
        removeLanguageId()?.let {
            it.replace("\"", "").toIntOrNull()?.let { Language.of(it) }?.let {
                Session.updateLanguage(it)
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

private fun JsonObject.removeRolesIds(): String? {
    return this.remove(rolesIds)?.toString()
}

private fun JsonObject.removeLanguageId(): String? {
    return this.remove(languageId)?.toString()
}

private val JsonObject.hasToken : Boolean
    get() = this.has(token)

private val JsonObject.hasUserTypeId : Boolean
    get() = this.has(userTypeId)

private val JsonObject.hasRolesIds : Boolean
    get() = this.has(rolesIds)

private val JsonObject.hasLanguageId : Boolean
    get() = this.has(languageId)

fun String.unescapeAll(): String {
    return this.replace("\"", "")
}

fun String.unescapeBookends(): String {
    return this.replace(Regex("^\"|\"$"), "")
}

fun main() {
    println("Asdadas")
}