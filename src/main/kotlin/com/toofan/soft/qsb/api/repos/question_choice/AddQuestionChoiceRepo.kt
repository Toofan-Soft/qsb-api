package com.toofan.soft.qsb.api.repos.question_choice

import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AddQuestionChoiceRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<Boolean>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            var request: Request? = null

            data.invoke(
                { questionId, content, isTrue ->
                    request = Request(questionId, content, isTrue)
                },
                { request!!.optional(it) }
            )

            request?.let {
                ApiExecutor.execute(
                    route = Route.QuestionChoice.Add,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Boolean>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            questionId: Int,
            content: String,
            isTrue: Boolean
        )
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("question_id")
        private val _questionId: Int,
        @Field("content")
        private val _content: String,
        @Field("is_true")
        private val _isTrue: Boolean,
        @Field("attachment")
        private val _attachment: OptionalVariable<ByteArray> = OptionalVariable()
    ) : IRequest {
        val attachment = loggableProperty(_attachment)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}
