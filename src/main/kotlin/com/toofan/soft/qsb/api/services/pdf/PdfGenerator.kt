package com.toofan.soft.qsb.api.services.pdf

import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.RoundDotsBorder
import com.itextpdf.layout.element.*
import com.itextpdf.layout.properties.HorizontalAlignment
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.VerticalAlignment
import java.io.ByteArrayOutputStream

internal object PdfGenerator {
    private const val MARGIN = 12f
    private const val PADDING = 12f
    private const val PADDING_VERTICAL = -16f
    private const val PADDING_HORIZONTAL = -16f

    private val PAGE_SIZE = PageSize.A4

    internal data class File(
        val form: String?,
        val bytes: ByteArray
    )

    fun build(
        data: Data,
        paper: Boolean,
        mirror: Boolean,
        answerMirror: Boolean,
        onFinish: (
            papers: List<File>?,
            mirrors: List<File>?,
            answerMirrors: List<File>?
        ) -> Unit
    ) {
        val papers = if (paper) createPaper(data).map { it.copy(bytes = createBorder(it.bytes)) } else null
        val mirrors = if (mirror) createMirror(data).map { it.copy(bytes = createBorder(it.bytes)) } else null
        val answerMirrors = if (answerMirror) createAnswerMirror(data).map { it.copy(bytes = createBorder(it.bytes)) } else null

        onFinish(papers, mirrors, answerMirrors)
    }

    private fun createBorder(bytes: ByteArray): ByteArray {
        val pdfReader = PdfReader(bytes.inputStream())
        val outputStream = ByteArrayOutputStream()
        val pdfWriter = PdfWriter(outputStream)
        val pdfDocument = PdfDocument(pdfReader, pdfWriter)

        PdfHelper.createPageBorder(pdfDocument)

        pdfDocument.close()
        return outputStream.toByteArray()
    }

    private fun createPaper(data: Data): List<File> {
        val files = ArrayList<File>()

        if (data.multiForms) {
            for (form in data.forms) {
                val outputStream = ByteArrayOutputStream()

                val pdfWriter = PdfWriter(outputStream)
                val pdfDocument = PdfDocument(pdfWriter)
                val document = Document(pdfDocument)

                document.setFont(Font.getFont(data.language))

                val contents = listOf(
                    createHeader(
                        data.universityName,
                        data.universityLogoUrl,
                        data.collegeName,
                        data.departmentName,
                        data.level,
                        data.semester,
                        data.courseName,
                        data.coursePartName,
                        data.lecturerName,
                        data.typeName,
                        data.date,
                        data.duration,
                        form.name,
                        data.notes,
                        data.language
                    ),
                    createQuestions(
                        form.trueFalseQuestions,
                        form.multiChoicesQuestions,
                        data.language
                    )
                )

                document
                    .add(
                        Div()
                            .setPaddingLeft(-16f)
                            .setPaddingRight(-16f)
                            .apply {
                                for (content in contents) {
                                    add(content)
                                }
                            }
                    )

                pdfDocument.close()

                files.add(
                    File(
                        form.name,
                        outputStream.toByteArray()
                    )
                )
            }
        } else if (data.singleForm) {
            val outputStream = ByteArrayOutputStream()

            val pdfWriter = PdfWriter(outputStream)
            val pdfDocument = PdfDocument(pdfWriter)
            val document = Document(pdfDocument)

            document.setFont(Font.getFont(data.language))

            val contents = listOf(
                createHeader(
                    data.universityName,
                    data.universityLogoUrl,
                    data.collegeName,
                    data.departmentName,
                    data.level,
                    data.semester,
                    data.courseName,
                    data.coursePartName,
                    data.lecturerName,
                    data.typeName,
                    data.date,
                    data.duration,
                    null,
                    data.notes,
                    data.language
                ),
                createQuestions(
                    data.trueFalseQuestions,
                    data.multiChoicesQuestions,
                    data.language
                )
            )

            document
                .add(
                    Div()
                        .setPaddingLeft(-16f)
                        .setPaddingRight(-16f)
                        .apply {
                            for (content in contents) {
                                add(content)
                            }
                        }
                )

            pdfDocument.close()

            files.add(
                File(
                    null,
                    outputStream.toByteArray()
                )
            )
        }

        return files
    }

    private fun createMirror(data: Data): List<File> {
        val files = ArrayList<File>()

        if (data.multiForms) {
            for (form in data.forms) {
                val outputStream = ByteArrayOutputStream()

                val pdfWriter = PdfWriter(outputStream)
                val pdfDocument = PdfDocument(pdfWriter)

                val document = Document(pdfDocument)

                document.setFont(Font.getFont(data.language))

                val contents = listOf(
                    createHeader(
                        data.universityName,
                        data.universityLogoUrl,
                        data.collegeName,
                        data.departmentName,
                        data.level,
                        data.semester,
                        data.courseName,
                        data.coursePartName,
                        data.lecturerName,
                        data.typeName,
                        data.date,
                        data.duration,
                        form.name,
                        data.notes,
                        data.language
                    ),
                    createQuestionsMirror(
                        form.trueFalseQuestions,
                        form.multiChoicesQuestions,
                        false,
                        data.language,
                        pdfDocument
                    )
                )

                document.apply {
                    add(
                        Div()
                            .setPaddingLeft(-16f)
                            .setPaddingRight(-16f)
                            .apply {
                                for (content in contents) {
                                    add(content)
                                }
                            }
                    )
                }

                pdfDocument.close()

                files.add(
                    File(
                        form.name,
                        outputStream.toByteArray()
                    )
                )
            }
        } else if (data.singleForm) {
            val outputStream = ByteArrayOutputStream()

            val pdfWriter = PdfWriter(outputStream)
            val pdfDocument = PdfDocument(pdfWriter)

            val document = Document(pdfDocument)

            document.setFont(Font.getFont(data.language))

            val contents = listOf(
                createHeader(
                    data.universityName,
                    data.universityLogoUrl,
                    data.collegeName,
                    data.departmentName,
                    data.level,
                    data.semester,
                    data.courseName,
                    data.coursePartName,
                    data.lecturerName,
                    data.typeName,
                    data.date,
                    data.duration,
                    null,
                    data.notes,
                    data.language
                ),
                createQuestionsMirror(
                    data.trueFalseQuestions,
                    data.multiChoicesQuestions,
                    false,
                    data.language,
                    pdfDocument
                )
            )

            document.apply {
                add(
                    Div()
                        .setPaddingLeft(-16f)
                        .setPaddingRight(-16f)
                        .apply {
                            for (content in contents) {
                                add(content)
                            }
                        }
                )
            }

            pdfDocument.close()

            files.add(
                File(
                    null,
                    outputStream.toByteArray()
                )
            )
        }

        return files
    }

    private fun createAnswerMirror(data: Data): ArrayList<File> {
        val files = ArrayList<File>()

        if (data.multiForms) {
            for (form in data.forms) {
                val outputStream = ByteArrayOutputStream()

                val pdfWriter = PdfWriter(outputStream)
                val pdfDocument = PdfDocument(pdfWriter).apply {
                    defaultPageSize = PageSize.A4
                }

                val document = Document(pdfDocument)

                document.setFont(Font.getFont(data.language))

                val contents = listOf(
                    createHeader(
                        data.universityName,
                        data.universityLogoUrl,
                        data.collegeName,
                        data.departmentName,
                        data.level,
                        data.semester,
                        data.courseName,
                        data.coursePartName,
                        data.lecturerName,
                        data.typeName,
                        data.date,
                        data.duration,
                        form.name,
                        data.notes,
                        data.language
                    ),
                    createQuestionsMirror(
                        form.trueFalseQuestions,
                        form.multiChoicesQuestions,
                        true,
                        data.language,
                        pdfDocument
                    )
                )

                document.apply {
                    add(
                        Div()
                            .setPaddingLeft(-16f)
                            .setPaddingRight(-16f)
                            .apply {
                                for (content in contents) {
                                    add(content)
                                }
                            }
                    )
                }

                pdfDocument.close()

                files.add(
                    File(
                        form.name,
                        outputStream.toByteArray()
                    )
                )
            }
        } else if (data.singleForm) {
            val outputStream = ByteArrayOutputStream()

            val pdfWriter = PdfWriter(outputStream)
            val pdfDocument = PdfDocument(pdfWriter).apply {
                defaultPageSize = PageSize.A4
            }

            val document = Document(pdfDocument)

            document.setFont(Font.getFont(data.language))

            val contents = listOf(
                createHeader(
                    data.universityName,
                    data.universityLogoUrl,
                    data.collegeName,
                    data.departmentName,
                    data.level,
                    data.semester,
                    data.courseName,
                    data.coursePartName,
                    data.lecturerName,
                    data.typeName,
                    data.date,
                    data.duration,
                    null,
                    data.notes,
                    data.language
                ),
                createQuestionsMirror(
                    data.trueFalseQuestions,
                    data.multiChoicesQuestions,
                    true,
                    data.language,
                    pdfDocument
                )
            )

            document.apply {
                add(
                    Div()
                        .setPaddingLeft(-16f)
                        .setPaddingRight(-16f)
                        .apply {
                            for (content in contents) {
                                add(content)
                            }
                        }
                )
            }

            pdfDocument.close()

            files.add(
                File(
                    null,
                    outputStream.toByteArray()
                )
            )
        }

        return files
    }

    private fun createHeader(
        universityName: String,
        universityLogoUrl: String,
        collegeName: String,
        departmentName: String,
        level: String,
        semester: String,
        courseName: String,
        coursePartName: String,
        lecturerName: String,
        examTypeName: String,
        date: String,
        duration: Int,
        form: String?,
        notes: String,
        language: Data.Language
    ): IBlockElement {
        val firstStartHeaders = when (language) {
            Data.Language.ENGLISH -> listOf("Republic of Yemen", "$universityName University", "Faculty of $collegeName")
            Data.Language.ARABIC -> listOf("الجمهورية اليمنية", "جامعة $universityName", "كلية $collegeName")
        }

        val firstEndHeaders = when (language) {
            Data.Language.ENGLISH -> listOf("Department: $departmentName (L$level S$semester)", "Subject: $courseName ($coursePartName)", "Lecturer: $lecturerName")
            Data.Language.ARABIC -> listOf("التخصص: $departmentName (م$level ف$semester)", "المقرر: $courseName ($coursePartName)", "المحاضر: $lecturerName")
        }

        val secondHeaders = when (language) {
            Data.Language.ENGLISH -> listOf("Date: $date", examTypeName, "Duration: ${duration.minutesToDuration(language)}")
            Data.Language.ARABIC -> listOf("التاريخ: $date", examTypeName, "الزمن: ${duration.minutesToDuration(language)}")
        }

        val thirdHeaders = when (language) {
            Data.Language.ENGLISH -> listOf("Name:", if (form != null) "Form: $form" else "")
            Data.Language.ARABIC -> listOf("الاسم:", if (form != null) "نموذج: $form" else "")
        }

        val forthHeaders = when (language) {
            Data.Language.ENGLISH -> arrayListOf("* Notes:")
            Data.Language.ARABIC -> arrayListOf("* ملاحظات:")
        }.apply { addAll(notes.split("\n").map { "- $it" }) }.toList()

        val width = PAGE_SIZE.width / 3f - 12f
        val height = 16f

        val firstTable = listOf(
            PdfHelper.createCell(width)
                .add(
                    Table(1)
                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                        .apply {
                            for (line in firstStartHeaders) {
                                addCell(
                                    PdfHelper.createCell(height = height)
                                        .add(
                                            PdfHelper.createParagraph(line, bold = true)
                                        )
                                )
                            }
                        }
                ),
            PdfHelper.createCell(width)
                .apply {
                    PdfHelper.createImage(universityLogoUrl, 64f, 64f)?.let {
                        add(it)
                    }
                },
            PdfHelper.createCell(width)
                .add(
                    Table(1)
                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                        .apply {
                            for (line in firstEndHeaders) {
                                addCell(
                                    PdfHelper.createCell(height = height)
                                        .add(
                                            PdfHelper.createParagraph(line, bold = true)
                                        )
                                )
                            }
                        }
                )
        ).let {
            PdfHelper.createTable(
                it,
                language
            )
        }

        val secondTable = secondHeaders.map {
            PdfHelper.createCell(width)
                .add(
                    PdfHelper.createParagraph(it, bold = true)
                )
        }.let {
            PdfHelper.createTable(
                it,
                language
            )
        }

        val thirdTable = listOf(
            PdfHelper.createCell(width * 2.5f)
                .add(
                    PdfHelper.createParagraph(thirdHeaders.first(), bold = true)
                        .apply {
                            Text(160.toString(".", 1))
                                .setFontSize(4f).also {
                                    children.add(
                                        when (language) {
                                            Data.Language.ARABIC -> 0
                                            Data.Language.ENGLISH -> 1
                                        },
                                        it
                                    )

                                    children.add(
                                        1,
                                        Text(" ")
                                    )
                                }
                        }
                ),
            PdfHelper.createCell(width / 2f)
                .add(
                    PdfHelper.createParagraph(thirdHeaders.last(), bold = true)
                )
        ).let {
            PdfHelper.createTable(
                it,
                language
            )
        }

        val forthTable = Table(1)
            .setMarginLeft(20f)
            .setMarginRight(20f)
            .apply {

                for (index in forthHeaders.indices) {
                    val line = forthHeaders[index]
                    addCell(
                        PdfHelper.createCell(PAGE_SIZE.width - 20f, height)
                            .add(
                                PdfHelper.createParagraph(line, bold = index == 0)
                                    .setTextAlignment(
                                        when (language) {
                                            Data.Language.ARABIC -> TextAlignment.RIGHT
                                            Data.Language.ENGLISH -> TextAlignment.LEFT
                                        }
                                    ).apply {
                                        if (index != 0) {
                                            when (language) {
                                                Data.Language.ARABIC -> setPaddingRight(12f)
                                                Data.Language.ENGLISH -> setPaddingLeft(12f)
                                            }
                                        }
                                    }
                            )
                    )
                }
            }

        return Div()
            .setPaddingTop(PADDING_VERTICAL)
            .setPaddingLeft(PADDING_HORIZONTAL)
            .setPaddingRight(PADDING_HORIZONTAL)
            .apply {
                add(firstTable)
                add(secondTable)
                add(PdfHelper.createDashedLine())
                add(thirdTable)
                add(PdfHelper.createDoubleSolidLine())
                add(forthTable)
                add(PdfHelper.createDoubleSolidLine())
            }
    }

    private fun createQuestions(
        trueFalseQuestions: List<Data.Form.TrueFalse>,
        multiChoicesQuestions: List<Data.Form.MultiChoices>,
        language: Data.Language
    ): IBlockElement {
        return Div().apply {
            for (index in trueFalseQuestions.indices) {
                add(createTrueFalseQuestion(index+1, trueFalseQuestions[index], language))
            }

            if (trueFalseQuestions.isNotEmpty() && multiChoicesQuestions.isNotEmpty()) {
                add(
                    PdfHelper.createDoubleSolidLine()
                        .setMarginLeft(0f)
                        .setMarginRight(0f)

                )
            }

            for (index in multiChoicesQuestions.indices) {
                add(createMultiChoicesQuestion(index+1, multiChoicesQuestions[index], language))
                add(
                    PdfHelper.createSolidLine(0.7f)
                        .setPaddingTop(4f)
                )
            }
        }
    }

    private fun createTrueFalseQuestion(
        number: Int,
        question: Data.Form.TrueFalse,
        language: Data.Language
    ): IBlockElement {
        return listOf(
            PdfHelper.createCell(12f)
                .add(
                    PdfHelper.createParagraph("$number")
                        .setTextAlignment(
                            when (language) {
                                Data.Language.ARABIC -> TextAlignment.LEFT
                                Data.Language.ENGLISH -> TextAlignment.RIGHT
                            }
                        )
                ),
            PdfHelper.createCell(4f)
                .add(
                    PdfHelper.createParagraph(".")
                        .setTextAlignment(
                            when (language) {
                                Data.Language.ARABIC -> TextAlignment.RIGHT
                                Data.Language.ENGLISH -> TextAlignment.LEFT
                            }
                        )
                        .apply {
                            when (language) {
                                Data.Language.ARABIC -> {
                                    setPaddingRight(-4f)
                                }
                                Data.Language.ENGLISH ->  {
                                    setPaddingLeft(-4f)
                                }
                            }
                        }
                ),
            PdfHelper.createCell(48f)
                .add(
                    PdfHelper.createParagraph("(          )")
                ),
            PdfHelper.createCell(PAGE_SIZE.width - 84f)
                .add(
                    PdfHelper.createParagraph(question.content)
                        .setTextAlignment(
                            when (language) {
                                Data.Language.ARABIC -> TextAlignment.RIGHT
                                Data.Language.ENGLISH -> TextAlignment.LEFT
                            }
                        )
                )
        ).let {
            PdfHelper.createTable(
                it,
                language
            )
                .setMarginLeft(0f)
                .setMarginRight(0f)
        }
    }

    private fun createMultiChoicesQuestion(
        number: Int,
        question: Data.Form.MultiChoices,
        language: Data.Language
    ): IBlockElement {
        return Table(1).apply {
            addCell(
                listOf(
                    PdfHelper.createCell(12f)
                        .add(
                            PdfHelper.createParagraph("$number")
                                .setTextAlignment(
                                    when (language) {
                                        Data.Language.ARABIC -> TextAlignment.LEFT
                                        Data.Language.ENGLISH -> TextAlignment.RIGHT
                                    }
                                )
                        ),
                    PdfHelper.createCell(4f)
                        .add(
                            PdfHelper.createParagraph(".")
                                .setTextAlignment(
                                    when (language) {
                                        Data.Language.ARABIC -> TextAlignment.RIGHT
                                        Data.Language.ENGLISH -> TextAlignment.LEFT
                                    }
                                )
                                .apply {
                                    when (language) {
                                        Data.Language.ARABIC -> {
                                            setPaddingRight(-4f)
                                        }
                                        Data.Language.ENGLISH ->  {
                                            setPaddingLeft(-4f)
                                        }
                                    }
                                }
                        ),
                    PdfHelper.createCell(PAGE_SIZE.width - 36f)
                        .add(
                            PdfHelper.createParagraph(question.content)
                                .setTextAlignment(
                                    when (language) {
                                        Data.Language.ARABIC -> TextAlignment.RIGHT
                                        Data.Language.ENGLISH -> TextAlignment.LEFT
                                    }
                                )
                        )
                ).let {
                    PdfHelper.createCell()
                        .add(
                            PdfHelper.createTable(
                                it,
                                language
                            )
                                .setMarginLeft(0f)
                                .setMarginRight(0f)
                        )
                }
            )

            addCell(
                PdfHelper.createCell(PAGE_SIZE.width - 20f)
                    .add(
                        PdfHelper.createDashedLine()
                            .setPaddingTop(-8f)
                            .setPaddingBottom(-4f)
                            .setMarginLeft(-2f)
                            .setMarginRight(-2f)
                    )
            )

            val width = PAGE_SIZE.width / question.choices.size.toFloat() - 20f

            val choicesTable = question.choices.mapIndexed { index, choice ->
                PdfHelper.createCell(width)
                    .add(
                        PdfHelper.createParagraph("${(index).toOrder(language)})   ${choice.content}")
                    )
            }.let {
                PdfHelper.createTable(
                    it,
                    language
                )
            }

            addCell(
                PdfHelper.createCell(PAGE_SIZE.width - 20f)
                    .add(choicesTable)
            )
        }
    }

    private fun createQuestionsMirror(
        trueFalseQuestions: List<Data.Form.TrueFalse>,
        multiChoicesQuestions: List<Data.Form.MultiChoices>,
        answer: Boolean,
        language: Data.Language,
        pdfDocument: PdfDocument,
    ): IBlockElement {
        val columnQuestions = 20

        val trueFalseTables = trueFalseQuestions
            .chunked(columnQuestions)
            .mapIndexed { groupIndex, questions ->
                listOf(
                    PdfHelper.createCell(16f)
                        .setPadding(4f)
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                        .add(
                            PdfHelper.createParagraph(
                                when (language) {
                                    Data.Language.ARABIC -> "م"
                                    Data.Language.ENGLISH -> "n"
                                }
                            )
                        ),
                    PdfHelper.createCell(4f)
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY),
                    PdfHelper.createCell(14f)
                        .setPadding(4f)
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                        .add(
                            PdfHelper.createParagraph(
                                when (language) {
                                    Data.Language.ARABIC -> "ص"
                                    Data.Language.ENGLISH -> "T"
                                }
                            )
                        ),
                    PdfHelper.createCell(14f)
                        .setPadding(4f)
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                        .add(
                            PdfHelper.createParagraph(
                                when (language) {
                                    Data.Language.ARABIC -> "خ"
                                    Data.Language.ENGLISH -> "F"
                                }
                            )
                        )
                ).let {
                    ArrayList<List<Cell>>().apply {
                        add(it)

                        questions.mapIndexed { index, question ->
                            add(
                                createTrueFalseQuestionMirror(
                                    (groupIndex * columnQuestions) + (index + 1),
                                    question,
                                    answer,
                                    language,
                                    pdfDocument
                                )
                            )
                        }
                    }.toList().let {
                        Table(it.first().size)
                            .setMarginLeft(6f)
                            .setMarginRight(6f)
                            .apply {
                                for (row in it) {
                                    when (language) {
                                        Data.Language.ARABIC -> row.reversed()
                                        Data.Language.ENGLISH -> row
                                    }.let {
                                        for (cell in it) {
                                            addCell(cell)
                                        }
                                    }
                                }
                            }
                    }
                }
            }

        val multiChoicesTables = multiChoicesQuestions
            .chunked(columnQuestions)
            .mapIndexed { groupIndex, questions ->
                arrayListOf(
                    PdfHelper.createCell(16f)
                        .setPadding(4f)
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                        .add(
                            PdfHelper.createParagraph(
                                when (language) {
                                    Data.Language.ARABIC -> "م"
                                    Data.Language.ENGLISH -> "n"
                                }
                            )
                        ),
                    PdfHelper.createCell(4f)
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                ).apply {
                    addAll(
                        List(questions.first().choices.size) { index ->
                            PdfHelper.createCell(14f)
                                .setPadding(4f)
                                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                .add(
                                    PdfHelper.createParagraph(index.toOrder(language).toString())
                                )
                        }
                    )
                }.toList().let {
                    ArrayList<List<Cell>>().apply {
                        add(it)

                        questions.mapIndexed { index, question ->
                            add(
                                createMultiChoicesQuestionMirror(
                                    (groupIndex * columnQuestions) + (index + 1),
                                    question,
                                    answer,
                                    language,
                                    pdfDocument
                                )
                            )
                        }
                    }.toList().let {
                        Table(it.first().size)
                            .setMarginLeft(6f)
                            .setMarginRight(6f)
                            .apply {
                            for (row in it) {
                                when (language) {
                                    Data.Language.ARABIC -> row.reversed()
                                    Data.Language.ENGLISH -> row
                                }.let {
                                    for (cell in it) {
                                        addCell(cell)
                                    }
                                }
                            }
                        }
                    }
                }
            }

        val trueFalseColumns = trueFalseTables.map {
            PdfHelper.createCell()
                .add(it)
                .setVerticalAlignment(VerticalAlignment.TOP)
        }

        val multiChoicesColumns = multiChoicesTables.map {
            PdfHelper.createCell()
                .add(it)
                .setVerticalAlignment(VerticalAlignment.TOP)
        }

        val trueFalseCell = if (trueFalseColumns.isNotEmpty()) {
            PdfHelper.createCell()
                .setPaddingRight(-8f)
                .setPaddingLeft(-8f)
                .setVerticalAlignment(VerticalAlignment.TOP)
                .add(
                    Table(1).apply {
                        addCell(
                            PdfHelper.createCell()
                                .setPaddingRight(-8f)
                                .setPaddingLeft(-8f)
                                .add(
                                    PdfHelper.createParagraph(
                                        when (language) {
                                            Data.Language.ARABIC -> "صح وخطأ"
                                            Data.Language.ENGLISH -> "True-False"
                                        },
                                        bold = true
                                    )
                                )
                        )

                        addCell(
                            PdfHelper.createCell()
                                .setPaddingRight(-8f)
                                .setPaddingLeft(-8f)
                                .add(
                                    PdfHelper.createTable(
                                        trueFalseColumns,
                                        language
                                    )
                                )
                        )
                    }
                )
        } else null

        val multiChoicesCell = if (multiChoicesColumns.isNotEmpty()) {
            PdfHelper.createCell()
                .setPaddingRight(-8f)
                .setPaddingLeft(-8f)
                .setVerticalAlignment(VerticalAlignment.TOP)
                .add(
                    Table(1).apply {
                        addCell(
                            PdfHelper.createCell()
                                .setPaddingRight(-8f)
                                .setPaddingLeft(-8f)
                                .add(
                                    PdfHelper.createParagraph(
                                        when (language) {
                                            Data.Language.ARABIC -> "متعدد الخيارات"
                                            Data.Language.ENGLISH -> "Multi-Choices"
                                        },
                                        bold = true
                                    )
                                )
                        )

                        addCell(
                            PdfHelper.createCell()
                                .setPaddingRight(-8f)
                                .setPaddingLeft(-8f)
                                .add(
                                    PdfHelper.createTable(
                                        multiChoicesColumns,
                                        language
                                    )
                                )
                        )
                    }
                )
        } else null

        return arrayListOf<Cell>()
            .apply {
                trueFalseCell?.let {
                    add(it)
                }

                if (trueFalseCell != null && multiChoicesCell != null) {
                    add(
                        PdfHelper.createCell(8f)
                            .apply {
                                when (language) {
                                    Data.Language.ARABIC -> {
                                        setPaddingRight(0f)
                                        setBorderLeft(RoundDotsBorder(1f))
                                    }
                                    Data.Language.ENGLISH -> {
                                        setPaddingLeft(0f)
                                        setBorderRight(RoundDotsBorder(1f))
                                    }
                                }
                            }
                    )

                    add(
                        PdfHelper.createCell(8f)
                            .apply {
                                when (language) {
                                    Data.Language.ARABIC -> {
                                        setPaddingLeft(0f)
                                    }
                                    Data.Language.ENGLISH -> {
                                        setPaddingRight(0f)
                                    }
                                }
                            }
                    )
                }

                multiChoicesCell?.let{
                    add(it)
                }
            }.toList().let {
                PdfHelper.createTable(
                    it,
                    language
                ).setMarginTop(4f)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
            }
    }

    private fun createTrueFalseQuestionMirror(
        number: Int,
        question: Data.Form.TrueFalse,
        answer: Boolean,
        language: Data.Language,
        pdfDocument: PdfDocument
    ): List<Cell> {
        return arrayListOf(
            PdfHelper.createCell(16f)
                .setPadding(4f)
                .setPaddingTop(8f)
                .setPaddingBottom(0f)
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .add(
                    PdfHelper.createParagraph(number.toString(), bold = true)
                        .setUnderline()
                ),
            PdfHelper.createCell(4f),
            PdfHelper.createCell()
                .setPadding(4f)
                .setPaddingTop(8f)
                .setPaddingBottom(0f)
                .add(
                    PdfHelper.createCircleImage(
                        pdfDocument,
                        answer && question.isTrue == true,
                        when (language) {
                            Data.Language.ARABIC -> 'ص'
                            Data.Language.ENGLISH -> 'T'
                        },
                        language
                    )
                ),
            PdfHelper.createCell()
                .setPadding(4f)
                .setPaddingTop(8f)
                .setPaddingBottom(0f)
                .add(
                    PdfHelper.createCircleImage(
                        pdfDocument,
                        answer && question.isTrue == false,
                        when (language) {
                            Data.Language.ARABIC -> 'خ'
                            Data.Language.ENGLISH -> 'F'
                        },
                        language
                    )
                )
        ).toList()
    }

    private fun createMultiChoicesQuestionMirror(
        number: Int,
        question: Data.Form.MultiChoices,
        answer: Boolean,
        language: Data.Language,
        pdfDocument: PdfDocument
    ): List<Cell> {
        return arrayListOf(
            PdfHelper.createCell(16f)
                .setPadding(4f)
                .setPaddingTop(8f)
                .setPaddingBottom(0f)
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .add(
                    PdfHelper.createParagraph(number.toString(), bold = true)
                        .setUnderline()
                ),
            PdfHelper.createCell(4f)
        ).apply {
            addAll(
                question.choices.mapIndexed { index, choice ->
                    PdfHelper.createCell()
                        .setPadding(4f)
                        .setPaddingTop(8f)
                        .setPaddingBottom(0f)
                        .add(
                            PdfHelper.createCircleImage(
                                pdfDocument,
                                answer && choice.isTrue == true,
                                index.toOrder(language),
                                language
                            )
                        )
                }
            )
        }.toList()
    }
}