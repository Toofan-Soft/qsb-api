package com.toofan.soft.qsb.api.repos.favorite_question

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import kotlinx.coroutines.runBlocking

object RetrieveFavoriteQuestionRepo {
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
            { questionId ->
            request = Request(questionId)
        },
            { request!!.optional(it) }
        )

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.FavoriteQuestion.Retrieve
                ) {
                    val response = Response.map(it)
                    onComplete(response)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            questionId: Int
        )
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("question_id")
        private val _questionId: Int,
        @Field("combination_id")
        private val _combinationId: OptionalVariable<Int> = OptionalVariable()
    ) : IRequest {
        val combinationId = loggableProperty(_combinationId)

        fun optional(block: Request.() -> Unit): Request {
            return build(block)
        }
    }

    data class Response(
        @Field("is_success")
        val isSuccess: Boolean = false,
        @Field("error_message")
        val errorMessage: String? = null,
        @Field("data")
        val data: Data? = null
    ) : IResponse {

        data class Data(
            @Field("chapter_name")
            val chapterName: String,
            @Field("topic_name")
            val topicName: String,
            @Field("type_name")
            val typeName: String,
            @Field("question")
            val question: Data? = null,
        ) {
            sealed interface Data {
                data class TrueFalse(
                    @Field("id")
                    val id: Int,
                    @Field("content")
                    val content: String,
                    @Field("is_true")
                    val isTrue: Boolean,
                    @Field("attachment_url")
                    val attachmentUrl: String? = null
                ) : Data

                data class MultiChoice(
                    @Field("id")
                    val id: Int,
                    @Field("content")
                    val content: String,
                    @Field("attachment_url")
                    val attachmentUrl: String? = null,
                    @Field("choices")
                    val choices: List<Data>
                ) : Data {
                    data class Data(
                        @Field("id")
                        val id: Int,
                        @Field("content")
                        val content: String,
                        @Field("is_true")
                        val isTrue: Boolean,
                        @Field("attachment_url")
                        val attachmentUrl: String? = null
                    )
                }
            }
        }

        companion object {
            private fun getInstance(): Response {
                return Response()
            }

            fun map(data: JsonObject): Response {
                return getInstance().getResponse(data) as Response
            }
        }
    }
}
