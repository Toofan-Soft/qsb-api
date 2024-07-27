package com.toofan.soft.qsb.api.helper

import com.toofan.soft.qsb.api.session.Auth
import com.toofan.soft.qsb.api.session.Language

object QuestionHelper {
    @JvmStatic
    fun getQuestionTypeProperties(typeId: Int): Data {
        Type.values().find { it.ordinal == typeId }?.let {
            return it.getData()
        }
        return Type.TRUE_FALSE.getData()
    }

    @JvmStatic
    fun getQuestionTypeProperties(choices: List<Data.Data>): Data {
        if (choices.all { it.id in Data.Data.Type.values().map { it.toData().id } }) {
            return Type.TRUE_FALSE.getData().copy(defaultChoices = emptyList())
        }
        return Type.MULTI_CHOICE.getData()
    }

    internal enum class Type(private val data: Data) {
        TRUE_FALSE(
            Data(
                defaultChoices = Data.Data.Type.values().map { it.toData() },
                addable = false,
                editable = false,
                deletable = false,
                singleCorrectChoice = true
            )
        ),
        MULTI_CHOICE(
            Data(
                defaultChoices = emptyList(),
                addable = true,
                editable = true,
                deletable = true,
                singleCorrectChoice = false
            )
        );

        fun getData(): Data {
            return this.data
        }
    }

    data class Data(
        val defaultChoices: List<Data> = emptyList(),
        val addable: Boolean,
        val editable: Boolean,
        val deletable: Boolean,
        val singleCorrectChoice: Boolean
    ) {
        data class Data(
            val id: Int,
            val content: String,
            val isTrue: Boolean? = null,
            val isSelected: Boolean? = null,
            val attachmentUrl: String? = null
        ) {
            companion object {
                val MIX_CHOICE = Data(
                    -3,
                    when (Auth.language) {
                        Language.ARABIC -> "أ و ب"
                        Language.ENGLISH -> "A & B"
                        null -> "أ و ب"
                    }
                )
            }
            internal enum class Type(
                private val id: Int,
                private val arabicTitle: String,
                private val englishTitle: String
            ) {
                CORRECT(-999, "إجابة صحيحة", "Correct Answer"),
                INCORRECT(-998, "إجابة خاطئة", "Incorrect Answer");

                fun toData(): Data {
//                    return Data(this.id, this.title)
                    return Data(
                        this.id,
                        when (Auth.language) {
                            Language.ARABIC -> this.arabicTitle
                            Language.ENGLISH -> this.englishTitle
                            null -> this.arabicTitle
                        }
                    )
                }

                companion object {
                    fun of(id: Int): Type? {
                        return values().find { it.id == id }
                    }
                }
            }
        }
    }
}