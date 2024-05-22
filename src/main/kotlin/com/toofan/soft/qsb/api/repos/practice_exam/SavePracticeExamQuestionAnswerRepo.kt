package com.toofan.soft.qsb.api.repos.practice_exam

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.helper.QuestionHelper

object SavePracticeExamQuestionAnswerRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null
            var error: String? = null

            var _choiceId: Int? =  null
            var _isCorrect: Boolean? = null

            data.invoke { examId, questionId, choiceId ->
                request = Request(examId, questionId)

                when (QuestionHelper.Data.Data.Type.of(choiceId)) {
                    QuestionHelper.Data.Data.Type.CORRECT -> {
                        _isCorrect = true
                    }
                    QuestionHelper.Data.Data.Type.INCORRECT -> {
                        _isCorrect = false
                    }
                    null -> {
                        _choiceId = choiceId
                    }
                }

                if (_choiceId == null && _isCorrect != null) {
                    request!!.isTrue(_isCorrect)
                } else if (_choiceId != null && _isCorrect == null) {
                    request!!.choiceId(_choiceId)
                } else {
                    // handle error
                    error = "Choices Error!"
                }
            }

            if (error == null) {
                request?.let {
                    ApiExecutor.execute(
                        route = Route.PracticeOnlineExam.SaveQuestionAnswer,
                        request = it
                    ) {
                        onComplete(Response.map(it).getResource() as Resource<Boolean>)
                    }
                }
            } else {
                onComplete(Resource.Error(error))
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            examId: Int,
            questionId: Int,
            choiceId: Int
        )
    }

    data class Request(
        @Field("exam_id")
        private val _examId: Int,
        @Field("question_id")
        private val _questionId: Int,
        @Field("is_true")
        private val _isTrue: OptionalVariable<Boolean> = OptionalVariable(),
        @Field("choice_id")
        private val _choiceId: OptionalVariable<Int> = OptionalVariable()
    ) : IRequest {
        internal val isTrue = loggableProperty(_isTrue)
        internal val choiceId = loggableProperty(_choiceId)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}
