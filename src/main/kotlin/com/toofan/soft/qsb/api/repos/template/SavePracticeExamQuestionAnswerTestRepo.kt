package com.toofan.soft.qsb.api.repos.template

import com.toofan.soft.qsb.api.Field
import com.toofan.soft.qsb.api.IRequest
import com.toofan.soft.qsb.api.Response

object SavePracticeExamQuestionAnswerTestRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke { examId, questionId, answer ->
            var _answer: Request.Data? = null

            answer.invoke(
                {
                    println("isTrue: $it")
                    _answer = Request.Data.TrueFalse(it)
                },
                {
                    println("choiceId: $it")
                    _answer = Request.Data.MultiChoice(it)
                }
            )

            _answer?.let {
                request = Request(examId, questionId, it)
            }
        }

        request?.let {

//            runBlocking {
//                ApiExecutor.execute(
//                    route = Route.Topic.RetrieveList
//                ) {
//                    val response = Response.map(it)
//                    onComplete(response)
//                }
//            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            examId: Int,
            questionId: Int,
            answer: (
                mandatory: TrueFalse,
                multiChoice: MultiChoice
            ) -> Unit
        )

//        fun interface Data {
//            operator fun invoke(
//                isTrue: Boolean
//            )
//
//            operator fun invoke(
//                choiceId: Int
//            )
//        }

        // Additional methods must be default implementations or extensions, not abstract

        fun interface TrueFalse {
            operator fun invoke(
                isTrue: Boolean
            )
        }

        fun interface MultiChoice {
            operator fun invoke(
                choiceId: Int
            )
        }

//        sealed interface Data {
//            data class TrueFalse(
//                val isTrue: Boolean
//            ) : Data
//
//            data class MultiChoice(
//                val choiceId: Int
//            ) : Data
//        }
    }

    data class Request(
        @Field("exam_id")
        private val _examId: Int,
        @Field("question_id")
        private val _questionId: Int,
        @Field("answer")
        private val _answer: Data,
    ) : IRequest {
        sealed interface Data {
            data class TrueFalse(
                @Field("is_true")
                private val isTrue: Boolean
            ) : Data

            data class MultiChoice(
                @Field("choice_id")
                private val _choiceId: Int
            ) : Data
        }
    }
}
