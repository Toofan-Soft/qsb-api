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
            var error: String? = null

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

                    val _choices: ArrayList<Request.Data> = arrayListOf()
                    var _isCorrect: Boolean? = null
                    var _isIncorrect: Boolean? = null

                    choice.invoke { id, content, isTrue, attachment ->
                        when (QuestionHelper.Type.values().find { it.ordinal == typeId }) {
                            QuestionHelper.Type.TRUE_FALSE -> {
                                if (id != null) {
                                    when (QuestionHelper.Data.Data.Type.of(id)) {
                                        QuestionHelper.Data.Data.Type.CORRECT -> {
                                            _isCorrect = isTrue
                                        }
                                        QuestionHelper.Data.Data.Type.INCORRECT -> {
                                            _isIncorrect = isTrue
                                        }
                                        null -> {
                                            error = "Choices Error!"
                                        }
                                    }
                                } else {
                                    error = "Choices Error!"
                                }
                            }
                            QuestionHelper.Type.MULTI_CHOICE -> {
                                if (id == null) {
                                    Request.Data(content, isTrue).let {
                                        attachment?.let { attachment ->
                                            it.attachment(attachment)
                                        }
                                        _choices.add(it)
                                    }
                                } else {
                                    error = "Choices Error!"
                                }
                            }
                            else -> {
                                error = "Choices Error!"
                            }
                        }

                    }
                    when (QuestionHelper.Type.values().find { it.ordinal == typeId }) {
                        QuestionHelper.Type.TRUE_FALSE -> {
                            if (_choices.isEmpty() &&
                                _isCorrect != null && _isIncorrect != null &&
                                _isCorrect!! == !_isIncorrect!!) {
                                request!!.isTrue(_isCorrect!!)
                            } else {
                                // handle error
                                error = "Choices Error!"
                            }
                        }
                        QuestionHelper.Type.MULTI_CHOICE -> {
                            if (_choices.isNotEmpty()) {
                                request!!.choices(_choices)
                            } else {
                                // choices can be empty...
                            }
                        }
                        else -> {
                            error = "Choices Error!"
                        }
                    }
                },
                { request!!.optional(it) }
            )

            if (error == null) {
                request?.let {
                    ApiExecutor.execute(
                        route = Route.Question.Add,
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
            topicId: Int,
            typeId: Int,
            difficultyLevelId: Int,
            accessibilityStatusId: Int,
            languageId: Int,
            estimatedAnswerTime: Int,
            content: String,

            choice: (
                choice: Data
            ) -> Unit
        )

        fun interface Data {
            operator fun invoke(
                id: Int?,
                content: String,
                isTrue: Boolean,
                attachment: ByteArray?
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
            internal val attachment = loggableProperty(_attachment)

            fun optional(block: Request.() -> Unit): Request {
                return build(block)
            }
        }

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}
