package com.toofan.soft.qsb.api.services.pdf

internal data class Data(
    val universityName: String,
    val universityLogoUrl: String,
    val collegeName: String,
    val departmentName: String,
    val level: String,
    val semester: String,
    val courseName: String,
    val coursePartName: String,
    val typeName: String,
    val date: String,
    val duration: Int,
    val lecturerName: String,
    val score: Int,
    val languageId: Int,
    val notes: String,
    val forms: List<Form> = emptyList(),
    val trueFalseQuestions: List<Form.TrueFalse> = emptyList(),
    val multiChoicesQuestions: List<Form.MultiChoices> = emptyList()
) {
    val language = Language.values()[languageId]

    val multiForms get() = forms.isNotEmpty() && (trueFalseQuestions.isEmpty() && multiChoicesQuestions.isEmpty())
    val singleForm get() = forms.isEmpty() && (trueFalseQuestions.isNotEmpty() || multiChoicesQuestions.isNotEmpty())


    data class Form(
        val name: String,
        val trueFalseQuestions: List<TrueFalse>,
        val multiChoicesQuestions: List<MultiChoices>
    ) {
        data class TrueFalse(
            val content: String,
            val attachmentUrl: String?,
            val isTrue: Boolean?
        )

        data class MultiChoices(
            val content: String,
            val attachmentUrl: String?,
            val choices: List<Choice>
        ) {
            data class Choice(
                val content: String,
                val attachmentUrl: String?,
                val isTrue: Boolean?
            )
        }
    }

    enum class Language {
        ARABIC,
        ENGLISH;

        companion object {
            fun of(id: Int): Language? {
                return values().find { it.ordinal == id }
            }
        }
    }
}


// Test Data..
internal val dataAr get() = arData
internal val dataEn get() = enData

private val enData = Data(
    "Taiz",
    "https://th.bing.com/th/id/R.ec35979634cc67d591ad003e9c825dfe?rik=QxlIviIeK8cLYg&pid=ImgRaw&r=0",
    "Engineering & IT",
    "IT",
    "1",
    "1",
    "Math III",
    "Practical",
    "Midterm",
    "10/10/2024",
    120,
    "Mahmood Al-Hakimi",
    40,
    1,
    "Be careful...\nBe calm...",
    listOf(
        Data.Form(
            "A",
            (1..20).map {
                Data.Form.TrueFalse(
                    "Content of question $it...",
                    null,
                    listOf(true, false).random()
                )
            },
            (1..60).map {
                Data.Form.MultiChoices(
                    "Content of question $it...",
                    null,
                    (1..4).random().let { trueNumber ->
                        (1..4).map {
                            Data.Form.MultiChoices.Choice(
                                "Content of choice $it...",
                                null,
                                it == trueNumber
                            )
                        }
                    }
                )
            },
        ),
        Data.Form(
            "B",
            (1..20).map {
                Data.Form.TrueFalse(
                    "Content of question $it...",
                    null,
                    listOf(true, false).random()
                )
            },
            (1..60).map {
                Data.Form.MultiChoices(
                    "Content of question $it...",
                    null,
                    (1..4).random().let { trueNumber ->
                        (1..4).map {
                            Data.Form.MultiChoices.Choice(
                                "Content of choice $it...",
                                null,
                                it == trueNumber
                            )
                        }
                    }
                )
            },
        )
    )
)

private val arData = Data(
    "تعز",
    "https://th.bing.com/th/id/R.ec35979634cc67d591ad003e9c825dfe?rik=QxlIviIeK8cLYg&pid=ImgRaw&r=0",
    "الهندسة وتقنية المعلومات",
    "تقنية المعلومات",
    "1",
    "1",
    "رياضيات 3",
    "تمارين",
    "نصفي",
    "10/10/2024",
    150,
    "محمود الحكيمي",
    40,
    0,
    "كن حذراً...\nكن هادئاً...",
    listOf(
        Data.Form(
            "أ",
            (1..20).map {
                Data.Form.TrueFalse(
                    "محتوى السؤال رقم $it...",
                    null,
                    listOf(true, false).random()
                )
            },
            (1..60).map {
                Data.Form.MultiChoices(
                    "محتوى السؤال رقم $it...",
                    null,
                    (1..4).random().let { trueNumber ->
                        (1..4).map {
                            Data.Form.MultiChoices.Choice(
                                "محتوى الاختيار رقم $it...",
                                null,
                                it == trueNumber
                            )
                        }
                    },
                )
            },
        ),
        Data.Form(
            "ب",
            (1..20).map {
                Data.Form.TrueFalse(
                    "محتوى السؤال رقم $it...",
                    null,
                    listOf(true, false).random()
                )
            },
            (1..60).map {
                Data.Form.MultiChoices(
                    "محتوى السؤال رقم $it...",
                    null,
                    (1..4).random().let { trueNumber ->
                        (1..4).map {
                            Data.Form.MultiChoices.Choice(
                                "محتوى الاختيار رقم $it...",
                                null,
                                it == trueNumber
                            )
                        }
                    },
                )
            },
        )
    )
)