package com.toofan.soft.qsb.api.session

object Auth {
    val user get() = Session.userType
    val roles get() = Session.roles
    val language get() = Session.language

//    internal lateinit var listener: LanguageListener
    internal var listener: LanguageListener? = null

    fun setOnLanguageChangeListener(listener: LanguageListener) {
        this.listener = listener
    }

    fun interface LanguageListener {
        fun onUpdate(language: Language)
    }
}