package com.toofan.soft.qsb.api.session

enum class Language {
    ARABIC,
    ENGLISH;

    companion object {
        fun of(id: Int): Language? {
            return values().find { it.ordinal == id }
        }
    }
}