package com.toofan.soft.qsb.api.repos.question_choice

import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import com.toofan.soft.qsb.api.loggableProperty
import kotlinx.coroutines.runBlocking

object ModifyQuestionChoiceRepo {
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
        @Field("content")
        private val _content: OptionalVariable<String> = OptionalVariable(),
        @Field("is_true")
        private val _isTrue: OptionalVariable<Boolean> = OptionalVariable(),
        @Field("attachment")
        private val _attachment: OptionalVariable<ByteArray> = OptionalVariable()
    ) : IRequest {
        val content = loggableProperty(_content)
        val isTrue = loggableProperty(_isTrue)
        val attachment = loggableProperty(_attachment)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }
}
