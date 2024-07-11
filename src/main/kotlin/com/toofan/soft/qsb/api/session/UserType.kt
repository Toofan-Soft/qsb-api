package com.toofan.soft.qsb.api.session

enum class UserType {
    GUEST,
    STUDENT,
    EMPLOYEE;

    companion object {
        fun of(id: Int): UserType? {
            return values().find { it.ordinal == id }
        }
    }
}