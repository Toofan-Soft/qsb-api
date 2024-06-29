package com.toofan.soft.qsb.api.repos.question

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.helper.QuestionHelper
import java.net.URLEncoder

object ModifyQuestionRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        Coroutine.launch {
            val request = Request()
            var hasError = false

            data.invoke { optional, choice ->
                request.optional(optional)

                val choices: ArrayList<Request.Data> = arrayListOf()

                choice.invoke {
                    Request.Data().also {
                        choices.add(it)
                    }.let(it)
                }

                val isTrueFalse = (choices.size == 2) && (choices.filter {
                    it._id.value != null && QuestionHelper.Data.Data.Type.of(it._id.value!!) == QuestionHelper.Data.Data.Type.CORRECT
                }.size == 1) && (choices.filter {
                    it._id.value != null && QuestionHelper.Data.Data.Type.of(it._id.value!!) == QuestionHelper.Data.Data.Type.INCORRECT
                }.size == 1)

                val isMultiChoice = choices.all { it._id.value == null || QuestionHelper.Data.Data.Type.of(it._id.value!!) == null }

                if (isTrueFalse && !isMultiChoice) {
                    val isCorrect = choices.find {
                        it._id.value != null && QuestionHelper.Data.Data.Type.of(it._id.value!!) == QuestionHelper.Data.Data.Type.CORRECT
                    }?._isTrue?.value
                    val isIncorrect = choices.find {
                        it._id.value != null && QuestionHelper.Data.Data.Type.of(it._id.value!!) == QuestionHelper.Data.Data.Type.INCORRECT
                    }?._isTrue?.value

                    if (isCorrect != null && isIncorrect != null && isCorrect != isIncorrect) {
                        request.isTrue(isCorrect)
                    } else {
                        hasError = true
                    }
                } else if (!isTrueFalse && isMultiChoice) {
                    for (item in choices) {
                        if ((item._id.value == null && (item._content.value == null || item._isTrue.value == null)) ||
                            (item._id.value != null && (item._content.value == null && item._isTrue.value == null && item._attachment.value != null))) {
                            hasError = true
                        }
                    }
                    if (!hasError) {
                        request.choices(
                            choices
                                .map {
                                    if (it._content.isUpdated) {
                                        it.copy(
                                            _content = it._content.copy(
                                                _value = URLEncoder.encode(
                                                    it._content.value!!.trimIndent(),
                                                    "UTF-8"
                                                )
                                            )
                                        )
                                    } else {
                                        it
                                    }
                                }
                        )
                    }
                } else {
                    hasError = true
                }
            }

            if (!hasError) {
                request.let {
                    it.copy(
                        _content = it._content.copy(
                            _value = URLEncoder.encode(
                                it._content.value!!.trimIndent(),
                                "UTF-8"
                            )
                        )
                    ).let {
                        ApiExecutor.execute(
                            route = Route.Question.Modify,
                            request = it
                        ) {
                            onComplete(Response.map(it).getResource() as Resource<Boolean>)
                        }
                    }
                }
            } else {
                onComplete(Resource.Error("Choices Error!"))
            }
        }
    }

    fun interface Optional {
        operator fun invoke(
            block: Request.() -> Unit,
            choice: (
                optional: com.toofan.soft.qsb.api.Optional<Request.Data>
            ) -> Unit
        )
    }

    data class Request(
        @Field("id")
        private val _id: OptionalVariable<Int> = OptionalVariable(),
        @Field("difficulty_level_id")
        private val _difficultyLevelId: OptionalVariable<Int> = OptionalVariable(),
        @Field("accessibility_status_id")
        private val _accessibilityStatusId: OptionalVariable<Int> = OptionalVariable(),
        @Field("language_id")
        private val _languageId: OptionalVariable<Int> = OptionalVariable(),
        @Field("estimated_answer_time")
        private val _estimatedAnswerTime: OptionalVariable<Int> = OptionalVariable(),
        @Field("content")
        internal val _content: OptionalVariable<String> = OptionalVariable(),
        @Field("attachment")
        private val _attachment: OptionalVariable<ByteArray> = OptionalVariable(),
        @Field("title")
        private val _title: OptionalVariable<String> = OptionalVariable(),
        @Field("is_true")
        private val _isTrue: OptionalVariable<Boolean> = OptionalVariable(),
        @Field("choices")
        private val _choices: OptionalVariable<List<Data>> = OptionalVariable()
    ) : IRequest {
        val id = loggableProperty(_id)
        val difficultyLevelId = loggableProperty(_difficultyLevelId)
        val accessibilityStatusId = loggableProperty(_accessibilityStatusId)
        val languageId = loggableProperty(_languageId)
        val estimatedAnswerTime = loggableProperty(_estimatedAnswerTime)
        val content = loggableProperty(_content)
        val attachment = loggableProperty(_attachment)
        val title = loggableProperty(_title)
        internal val isTrue = loggableProperty(_isTrue)
        internal val choices = loggableProperty(_choices)

        data class Data(
            @Field("id")
            internal val _id: OptionalVariable<Int> = OptionalVariable(),
            @Field("content")
            internal val _content: OptionalVariable<String> = OptionalVariable(),
            @Field("is_true")
            internal val _isTrue: OptionalVariable<Boolean> = OptionalVariable(),
            @Field("attachment")
            internal val _attachment: OptionalVariable<ByteArray> = OptionalVariable()
        ) : IRequest {
            val id = loggableProperty(_id)
            val content = loggableProperty(_content)
            val isTrue = loggableProperty(_isTrue)
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
