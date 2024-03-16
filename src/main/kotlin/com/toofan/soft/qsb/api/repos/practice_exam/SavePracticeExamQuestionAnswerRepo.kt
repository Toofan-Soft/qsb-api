package com.toofan.soft.qsb.api.repos.practice_exam

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object SavePracticeExamQuestionAnswerRepo {
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
                    _answer = Request.Data.TrueFalse(it)
                },
                {
                    _answer = Request.Data.MultiChoice(it)
                }
            )

            _answer?.let {
                request = Request(examId, questionId, it)
            }
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.Topic.RetrieveList
                ) {
                    val response = Response.map(it)
                    onComplete(response)
                }
            }
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
