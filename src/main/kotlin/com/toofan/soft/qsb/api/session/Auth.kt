package com.toofan.soft.qsb.api.session

object Auth {
    val user get() = Session.userType
    val roles get() = Session.roles
    val language get() = Session.language

    internal var listener: LanguageListener? = null
//    internal var rListener: RestrictionListener? = null

    fun setOnLanguageChangeListener(listener: LanguageListener) {
        this.listener = listener
    }

//    fun setOnRestrictionChangeListener(listener: RestrictionListener) {
//        this.rListener = listener
//    }

    fun interface LanguageListener {
        fun onUpdate(language: Language)
    }

//    fun interface RestrictionListener {
//        fun onUpdate(isRestricted: Boolean)
//    }
}