package com.toofan.soft.qsb.api.helper

object QuestionHelper {

    @JvmStatic
    fun getQuestionType(id: Int): Type {
        return Type.values().find { it.ordinal == id } ?: Type.TRUE_FALSE
    }

    enum class Type {
        TRUE_FALSE,
        MULTI_CHOICE;
    }


//    @JvmStatic
//    fun getQuestionTypeProperties(typeId: Int): Data {
//        Type.values().find { it.ordinal == typeId }?.let {
//            return it.getData()
//        }
//        return Type.TRUE_FALSE.getData()
//    }
//
//    internal enum class Type(private val data: Data) {
//        TRUE_FALSE(
//            Data(
//                defaultChoices = Data.Data.Type.values().map { it.toData() },
//                addable = false,
//                editable = false,
//                deletable = false,
//                singleCorrectChoice = true
//            )
//        ),
//        MULTI_CHOICE(
//            Data(
//                defaultChoices = emptyList(),
//                addable = true,
//                editable = true,
//                deletable = true,
//                singleCorrectChoice = false
//            )
//        );
//
//        fun getData(): Data {
//            return this.data
//        }
//    }
//
//    data class Data(
//        val defaultChoices: List<Data> = emptyList(),
//        val addable: Boolean,
//        val editable: Boolean,
//        val deletable: Boolean,
//        val singleCorrectChoice: Boolean
//    ) {
//        data class Data(
//            val id: Int,
//            val content: String,
//            val isTrue: Boolean? = null,
//            val isSelected: Boolean? = null,
//            val attachmentUrl: String? = null
//        ) {
//            internal enum class Type(
//                private val id: Int,
//                private val title: String
//            ) {
//                CORRECT(-1, "Correct Answer"),
//                INCORRECT(-2, "Incorrect Answer");
//
//                fun toData(): Data {
////                    return Data(this.ordinal, this.title)
//                    return Data(this.id, this.title)
//                }
//
//                companion object {
//                    fun of(id: Int): Type? {
//                        return values().find { it.id == id }
//                    }
//                }
//            }
//        }
//    }
}