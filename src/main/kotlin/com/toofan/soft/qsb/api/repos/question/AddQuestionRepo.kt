package com.toofan.soft.qsb.api.repos.question

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import com.toofan.soft.qsb.api.repos.department.AddDepartmentRepo
import kotlinx.coroutines.runBlocking

object AddQuestionRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke(
            { topicId, typeId, difficultyLevelId, accessibilityStatusId, languageId, estimatedAnswerTime, content ->
                request = Request(topicId, typeId, difficultyLevelId, accessibilityStatusId, languageId, estimatedAnswerTime, content)
            },
            { request!!.optional(it) }
        )

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.Question.Add,
                    request = it
                ) {
                    val response = Response.map(it)
                    onComplete(response)
                }
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
            content: String
        )
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
        private val _isTrue: OptionalVariable<Boolean> = OptionalVariable()
    ) : IRequest {
        val attachment = loggableProperty(_attachment)
        val title = loggableProperty(_title)
        val isTrue = loggableProperty(_isTrue)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}