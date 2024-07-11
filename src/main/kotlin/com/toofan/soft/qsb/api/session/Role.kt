package com.toofan.soft.qsb.api.session

enum class Role {
    GUEST,
    STUDENT,
    LECTURER,
    QUESTION_ENTRY,
    QUESTION_REVIEWER,
    SYSTEM_ADMINISTRATOR,
    DATA_ENTRY,
    PROCTOR;

    companion object {
        fun of(id: Int): Role? {
            return values().find { it.ordinal == id }
        }
    }
}