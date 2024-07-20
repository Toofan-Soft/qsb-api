package com.toofan.soft.qsb.api.session

enum class Language(
    private val _symbol: String
) {
    ARABIC("ar"),
    ENGLISH("en");

    companion object {
        fun of(id: Int): Language? {
            return values().find { it.ordinal == id }
        }
    }

    val id get() = this.ordinal
    val symbol get() = this._symbol
}