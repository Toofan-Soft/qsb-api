package com.toofan.soft.qsb.api.services.pdf

import com.toofan.soft.qsb.api.Coroutine

data class File(
    val form: String,
    val bytes: ByteArray
)


suspend fun testArPdfGenerator(
    paper: Boolean,
    mirror: Boolean,
    answerMirror: Boolean,
    onFinish: (
        papers: List<File>?,
        mirrors: List<File>?,
        answerMirrors: List<File>?
    ) -> Unit
) {
    Coroutine.launch {
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
}

suspend fun testEnPdfGenerator(
    paper: Boolean,
    mirror: Boolean,
    answerMirror: Boolean,
    onFinish: (
        papers: List<File>?,
        mirrors: List<File>?,
        answerMirrors: List<File>?
    ) -> Unit
) {
    Coroutine.launch {
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
}