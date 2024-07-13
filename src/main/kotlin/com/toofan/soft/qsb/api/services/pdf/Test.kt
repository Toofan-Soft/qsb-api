package com.toofan.soft.qsb.api.services.pdf

data class File(
    val form: String,
    val bytes: ByteArray
)


fun testArPdfGenerator(
    onFinish: (
        papers: List<File>?,
        mirrors: List<File>?,
        answerMirrors: List<File>?
    ) -> Unit
) {
    PdfGenerator.build(
        dataAr,
        true,
        false,
        false
    ) { papers, mirrors, answerMirrors ->
        onFinish(
            papers?.map { File(it.form, it.bytes) },
            mirrors?.map { File(it.form, it.bytes) },
            answerMirrors?.map { File(it.form, it.bytes) }
        )
    }
}

fun testEnPdfGenerator(
    onFinish: (
        papers: List<File>?,
        mirrors: List<File>?,
        answerMirrors: List<File>?
    ) -> Unit
) {
    PdfGenerator.build(
        dataEn,
        true,
        false,
        false
    ) { papers, mirrors, answerMirrors ->
        onFinish(
            papers?.map { File(it.form, it.bytes) },
            mirrors?.map { File(it.form, it.bytes) },
            answerMirrors?.map { File(it.form, it.bytes) }
        )
    }
}