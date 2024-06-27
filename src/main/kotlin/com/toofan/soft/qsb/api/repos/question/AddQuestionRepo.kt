package com.toofan.soft.qsb.api.repos.question

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.helper.QuestionHelper

object AddQuestionRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null
            var errorLine: Int? = null
            var hasError = false

            data.invoke(
                { topicId, typeId, difficultyLevelId, accessibilityStatusId, languageId, estimatedAnswerTime, content, choice ->
                    request = Request(
                        topicId,
                        typeId,
                        difficultyLevelId,
                        accessibilityStatusId,
                        languageId,
                        estimatedAnswerTime,
                        content
                    )

                    val choices: ArrayList<Request.Data> = arrayListOf()
                    var choiceRequest: Request.Data? = null

                    var isCorrect: Boolean? = null
                    var isIncorrect: Boolean? = null

                    choice.invoke(
                        { id, content, isTrue ->
                            when (QuestionHelper.Type.values().find { it.ordinal == typeId }) {
                                QuestionHelper.Type.TRUE_FALSE -> {
                                    if (id != null) {
                                        when (QuestionHelper.Data.Data.Type.of(id)) {
                                            QuestionHelper.Data.Data.Type.CORRECT -> {
                                                isCorrect = isTrue
                                            }
                                            QuestionHelper.Data.Data.Type.INCORRECT -> {
                                                isIncorrect = isTrue
                                            }
                                            null -> {
                                                errorLine = 51
                                                hasError = true
                                            }
                                        }
                                    } else {
                                        errorLine = 56
                                        hasError = true
                                    }
                                }
                                QuestionHelper.Type.MULTI_CHOICE -> {
                                    if (id == null) {
                                        choiceRequest = Request.Data(content, isTrue)
                                        choices.add(choiceRequest!!)
                                    } else {
                                        errorLine = 65
                                        hasError = true
                                    }
                                }
                                else -> {
                                    errorLine = 70
                                    hasError = true
                                }
                            }

                        }, { choiceRequest!!.optional(it) }
                    )

                    when (QuestionHelper.Type.values().find { it.ordinal == typeId }) {
                        QuestionHelper.Type.TRUE_FALSE -> {
                            if (choices.isEmpty() &&
                                isCorrect != null && isIncorrect != null &&
                                isCorrect!! == !isIncorrect!!) {
                                request!!.isTrue(isCorrect!!)
                            } else {
                                // handle error
                                errorLine = 86
                                hasError = true
                            }
                        }
                        QuestionHelper.Type.MULTI_CHOICE -> {
                            if (choices.isNotEmpty() &&
                                isCorrect == null && isIncorrect == null) {
                                if (!hasError) {
                                    request!!.choices(choices)
                                }
                            } else {
                                errorLine = 97
                                
                                if (choices.isEmpty()) errorLine = errorLine!! + 100
                                if (isCorrect != null) errorLine = errorLine!! + 200
                                if (isIncorrect != null) errorLine = errorLine!! + 300

                                hasError = true
                            }
                        }
                        else -> {
                            errorLine = 102
                            hasError = true
                        }
                    }
                },
                { request!!.optional(it) }
            )

            if (!hasError) {
                request?.let {
                    ApiExecutor.execute(
                        route = Route.Question.Add,
                        request = it
                    ) {
                        onComplete(Response.map(it).getResource() as Resource<Boolean>)
                    }
                }
            } else {
                onComplete(Resource.Error("Choices Error! L$errorLine"))
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            topicId: Int,
            typeId: Int,
            difficultyLevelId: Int,
            accessibilityStatusId: Int,
            languageId: Int,
            estimatedAnswerTime: Int,
            content: String,

            choice: (
                mandatory: Data,
                optional: com.toofan.soft.qsb.api.Optional<Request.Data>
            ) -> Unit
        )

        fun interface Data {
            operator fun invoke(
                id: Int?,
                content: String,
                isTrue: Boolean,
            )
        }
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("topic_id")
        private val _topicId: Int,
        @Field("type_id")
        private val _typeId: Int,
        @Field("difficulty_level_id")
        private val _difficultyLevelId: Int,
        @Field("accessibility_status_id")
        private val _accessibilityStatusId: Int,
        @Field("language_id")
        private val _languageId: Int,
        @Field("estimated_answer_time")
        private val _estimatedAnswerTime: Int,
        @Field("content")
        private val _content: String,
        @Field("attachment")
        private val _attachment: OptionalVariable<ByteArray> = OptionalVariable(),
        @Field("title")
        private val _title: OptionalVariable<String> = OptionalVariable(),
        @Field("is_true")
        private val _isTrue: OptionalVariable<Boolean> = OptionalVariable(),
        @Field("choices")
        private val _choices: OptionalVariable<List<Data>> = OptionalVariable()
    ) : IRequest {
        val attachment = loggableProperty(_attachment)
        val title = loggableProperty(_title)
        internal val isTrue = loggableProperty(_isTrue)
        internal val choices = loggableProperty(_choices)

        data class Data(
            @Field("content")
            private val content: String,
            @Field("is_true")
            private val isTrue: Boolean,
            @Field("attachment")
            private val _attachment: OptionalVariable<ByteArray> = OptionalVariable()
        ) : IRequest {
            val attachment = loggableProperty(_attachment)

            fun optional(block: Data.() -> Unit): Data {
                return build(block)
            }
        }

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}
