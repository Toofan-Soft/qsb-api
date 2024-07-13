package com.toofan.soft.qsb.api.services.pdf

data class File(
    val form: String,
    val bytes: ByteArray
)


fun testArPdfGenerator(
    paper: Boolean,
    mirror: Boolean,
    answerMirror: Boolean,
    onFinish: (
        papers: List<File>?,
        mirrors: List<File>?,
        answerMirrors: List<File>?
    ) -> Unit
) {
    PdfGenerator.build(
        dataAr,
        paper,
        mirror,
        answerMirror
    ) { papers, mirrors, answerMirrors ->
        onFinish(
            papers?.map { File(it.form, it.bytes) },
            mirrors?.map { File(it.form, it.bytes) },
            answerMirrors?.map { File(it.form, it.bytes) }
        )
    }
}

fun testEnPdfGenerator(
    paper: Boolean,
    mirror: Boolean,
    answerMirror: Boolean,
    onFinish: (
        papers: List<File>?,
        mirrors: List<File>?,
        answerMirrors: List<File>?
    ) -> Unit
) {
    PdfGenerator.build(
        dataEn,
        paper,
        mirror,
        answerMirror
    ) { papers, mirrors, answerMirrors ->
        onFinish(
            papers?.map { File(it.form, it.bytes) },
            mirrors?.map { File(it.form, it.bytes) },
            answerMirrors?.map { File(it.form, it.bytes) }
        )
    }
}