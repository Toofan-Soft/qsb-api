package com.toofan.soft.qsb.api.services.pdf

import com.itextpdf.io.font.FontProgramFactory
import com.itextpdf.io.font.PdfEncodings
import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.kernel.font.PdfFontFactory

internal object Font {
    private val TIMES_NEW_ROMAN = "fonts/times_new_roman.ttf"
    private val TIMES_NEW_ROMAN_BOLD = "fonts/times_new_roman_bold.ttf"
    private val ARIAL = "fonts/arial.ttf"
    private val ARIAL_BOLD = "fonts/arial_bold.ttf"

    private fun createFont(path: String): PdfFont {
//        val classLoader = this::class.java.classLoader
//        val ttfFile = File(classLoader.getResource("fonts/arial.ttf")!!.file)
//        val ttfFile = File(classLoader.getResource(path)!!.file)
        println("Font..........")
        return PdfFontFactory.createFont(
            FontProgramFactory.createFont(path),
//            FontProgramFactory.createFont(ttfFile.readBytes()),
            PdfEncodings.IDENTITY_H
        )!!
    }

    @JvmStatic
    fun main(args: Array<String>) {
        createFont(ARIAL)
        println("done")
    }

    fun getFont(language: Data.Language, bold: Boolean = false): PdfFont {
        return when (language) {
            Data.Language.ENGLISH -> if (bold) TIMES_NEW_ROMAN_BOLD else TIMES_NEW_ROMAN
            Data.Language.ARABIC -> if (bold) ARIAL_BOLD else ARIAL
        }.let {
            createFont(it)
        }
    }

    fun getFont(content: String, bold: Boolean = false): PdfFont {
        return if (content.isArabic()) {
            if (bold) ARIAL_BOLD else ARIAL
        } else {
            if (bold) TIMES_NEW_ROMAN_BOLD else TIMES_NEW_ROMAN
        }.let {
            createFont(it)
        }
    }
}