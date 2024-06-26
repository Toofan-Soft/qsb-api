//package com.toofan.soft.qsb.api.services.pdf
//
//import com.itextpdf.io.font.FontProgramFactory
//import com.itextpdf.io.font.PdfEncodings
//import com.itextpdf.kernel.font.PdfFont
//import com.itextpdf.kernel.font.PdfFontFactory
//
//internal object Font {
//    private val TIMES_NEW_ROMAN =
//        "E:\\f\\ToofanSoft\\QsB\\coding\\helper\\pdf-helper\\src\\main\\kotlin/res/times new roman.ttf"
//    private val TIMES_NEW_ROMAN_BOLD =
//        "E:\\f\\ToofanSoft\\QsB\\coding\\helper\\pdf-helper\\src\\main\\kotlin/res/times new roman bold.ttf"
//    private val TIMES_NEW_ROMAN_BOLD_ITALIC =
//        "E:\\f\\ToofanSoft\\QsB\\coding\\helper\\pdf-helper\\src\\main\\kotlin/res/times new roman bold italic.ttf"
//    private val TIMES_NEW_ROMAN_ITALIC =
//        "E:\\f\\ToofanSoft\\QsB\\coding\\helper\\pdf-helper\\src\\main\\kotlin/res/times new roman italic.ttf"
//    //    private val ARIAL = createFont("C:/Windows/Fonts/Arial.ttf")
//    private val ARIAL = "E:\\f\\ToofanSoft\\QsB\\coding\\helper\\pdf-helper\\src\\main\\kotlin/res/arial.ttf"
//    private val ARIAL_BOLD = "E:\\f\\ToofanSoft\\QsB\\coding\\helper\\pdf-helper\\src\\main\\kotlin/res/arial bold.ttf"
//
//    private fun createFont(path: String): PdfFont {
//        return PdfFontFactory.createFont(
//            FontProgramFactory.createFont(path),
//            PdfEncodings.IDENTITY_H
//        )!!
//    }
//
//    fun getFont(language: Data.Language, bold: Boolean = false): PdfFont {
//        return when (language) {
//            Data.Language.ENGLISH -> if (bold) TIMES_NEW_ROMAN_BOLD else TIMES_NEW_ROMAN
//            Data.Language.ARABIC -> if (bold) ARIAL_BOLD else ARIAL
//        }.let {
//            createFont(it)
//        }
//    }
//
//    fun getFont(content: String, bold: Boolean = false): PdfFont {
//        return if (content.isArabic()) {
//            if (bold) ARIAL_BOLD else ARIAL
//        } else {
//            if (bold) TIMES_NEW_ROMAN_BOLD else TIMES_NEW_ROMAN
//        }.let {
//            createFont(it)
//        }
//    }
//
//}