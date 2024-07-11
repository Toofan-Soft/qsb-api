package com.toofan.soft.qsb.api.session

object Auth {
    val user get() = Session.userType
    val roles get() = Session.roles
    val language get() = Session.language
}