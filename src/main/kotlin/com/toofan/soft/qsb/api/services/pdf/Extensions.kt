package com.toofan.soft.qsb.api.services.pdf

import com.itextpdf.text.pdf.languages.ArabicLigaturizer
import java.net.MalformedURLException
import java.net.URL

internal fun Int.toString(value: String): String {
    return this.toString(value, false)
}

internal fun Int.toString(value: String, space: Boolean = false): String {
    val str = StringBuilder()
    for (i in (1..this)) {
        str.append(value)
        if (space) {
            str.append("  ")
        }
    }
    if (str.toString().isNotEmpty()) {
        return str.removeSuffix(" ").toString()
    }
    return ""
}


internal fun Int.minutesToDuration(language: Data.Language): String {
    return (this.toDouble() / 60.0).let {
        val h = it.toInt().let { if (it != 0) "$it ${when (language) {
            Data.Language.ARABIC -> "س"
            Data.Language.ENGLISH -> "H"
        }}" else "" }

        val m = ((it - it.toInt().toDouble()) * 60.0).let { if (it != 0.0) "${it.toInt()} ${
            when (language) {
                Data.Language.ARABIC -> "د"
                Data.Language.ENGLISH -> "M"
            }
        }" else "" }

        StringBuilder().apply {
            if (h.isNotEmpty()) {
                append(h)
            }

            append(" ")

            if (m.isNotEmpty()) {
                append(m)
            }
        }.toString().trim()
    }
}

internal fun Int.toString(value: String, spaces: Int = 0): String {
    val str = StringBuilder()
    for (i in (1..this)) {
        str.append(value)
        str.append(spaces.toSpaces)
    }
    if (str.toString().isNotEmpty()) {
        return str.removeSuffix(spaces.toSpaces).toString()
    }
    return ""
}

internal val Int.toSpaces: String
    get() = if (this@toSpaces > 0) {
        java.lang.StringBuilder().apply {

            repeat((1..this@toSpaces).count()) {
//        for(i in 1..this@toSpaces) {
                append(" ")
            }
        }.toString()
    } else ""

internal fun Int.toOrder(language: Data.Language): Char {
    return when (language) {
        Data.Language.ENGLISH -> {
            Order.values()[this].enValue
        }
        Data.Language.ARABIC -> {
            Order.values()[this].arValue
//            val arabicLigaturizer = ArabicLigaturizer()
//            arabicLigaturizer.process(Order.values()[this].arValue.toString()).toCharArray().first()
        }
    }
}

internal val String.url
    get() = try {
        val parsedUrl = URL(this)
        if ((parsedUrl.protocol == "http" || parsedUrl.protocol == "https") && parsedUrl.host.isNotEmpty()) {
            parsedUrl
        } else {
            null
        }
    } catch (e: MalformedURLException) {
        null
    }

private val al = ArabicLigaturizer()

internal val String.value get() = if (this.isArabic()) al.process(this) else this

//internal fun String.isArabic(): Boolean {
//    val arabicPattern = "[\\u0600-\\u06FF\\u0750-\\u077F\\u08A0-\\u08FF\\uFB50-\\uFDFF\\uFE70-\\uFEFF]+".toRegex()
//    return this.matches(arabicPattern)
//}

internal fun String.isArabic(): Boolean {
    val arabicPattern = "[\\u0600-\\u06FF\\u0750-\\u077F\\u08A0-\\u08FF\\uFB50-\\uFDFF\\uFE70-\\uFEFF]".toRegex()
    return this.contains(arabicPattern)
}

