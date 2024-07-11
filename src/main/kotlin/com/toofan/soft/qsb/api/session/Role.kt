package com.toofan.soft.qsb.api.session

enum class Role {
    GUEST,
    STUDENT,
    LECTURER,
    EMPLOYEE;

    companion object {
        fun of(id: Int): Role? {
            return values().find { it.ordinal == id }
        }
    }
}