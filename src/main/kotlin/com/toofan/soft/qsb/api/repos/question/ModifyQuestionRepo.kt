package com.toofan.soft.qsb.api.repos.question

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import com.toofan.soft.qsb.api.loggableProperty
import kotlinx.coroutines.runBlocking

object ModifyQuestionRepo {
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
            { id ->
                request = Request(id)
            },
            { request!!.optional(it) }
        )

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.Topic.Modify,
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
            id: Int
        )
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("id")
        private val _id: Int,
        @Field("difficulty_level_id")
        private val _difficultyLevelId: OptionalVariable<Int> = OptionalVariable(),
        @Field("accessibility_status_id")
        private val _accessibilityStatusId: OptionalVariable<Int> = OptionalVariable(),
        @Field("language_id")
        private val _languageId: OptionalVariable<Int> = OptionalVariable(),
        @Field("estimated_answer_time")
        private val _estimatedAnswerTime: OptionalVariable<Int> = OptionalVariable(),
        @Field("content")
        private val _content: OptionalVariable<String> = OptionalVariable(),
        @Field("attachment")
        private val _attachment: OptionalVariable<ByteArray> = OptionalVariable(),
        @Field("title")
        private val _title: OptionalVariable<String> = OptionalVariable(),
        @Field("is_true")
        private val _isTrue: OptionalVariable<Boolean> = OptionalVariable()
    ) : IRequest {
        val difficultyLevelId = loggableProperty(_difficultyLevelId)
        val accessibilityStatusId = loggableProperty(_accessibilityStatusId)
        val languageId = loggableProperty(_languageId)
        val estimatedAnswerTime = loggableProperty(_estimatedAnswerTime)
        val content = loggableProperty(_content)
        val attachment = loggableProperty(_attachment)
        val title = loggableProperty(_title)
        val isTrue = loggableProperty(_isTrue)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}