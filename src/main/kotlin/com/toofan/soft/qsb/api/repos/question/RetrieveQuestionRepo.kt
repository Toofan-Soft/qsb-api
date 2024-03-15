package com.toofan.soft.qsb.api.repos.question

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import kotlinx.coroutines.runBlocking

object RetrieveQuestionRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke { id ->
            request = Request(id)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.Topic.Retrieve
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

    data class Request(
        @Field("id")
        private val _id: Int
    ) : IRequest


    data class Response(
        @Field("is_success")
        val isSuccess: Boolean = false,
        @Field("error_message")
        val errorMessage: String? = null,
        @Field("data")
        val data: Data? = null
    ) : IResponse {

        data class Data(
            @Field("difficulty_level_id")
            val difficultyLevelId: Int,
            @Field("accessibility_status_id")
            val accessibilityStatusId: Int,
            @Field("language_id")
            val languageId: Int,
            @Field("estimated_answer_time")
            val estimatedAnswerTime: Int,
            @Field("content")
            val content: String,
            @Field("attachment_url")
            val attachmentUrl: String? = null,
            @Field("title")
            val title: String? = null,
            @Field("status")
            val status: Status,
            @Field("answer")
            val answer: Answer
        ) {
            data class Status(
                @Field("is_requested")
                val isRequested: Boolean,
                @Field("is_accepted")
                val isAccepted: Boolean,
            )

            sealed interface Answer {
                data class TrueFalse(
                    @Field("is_true")
                    val isTrue: Boolean
                ) : Answer

                data class Choices(
                    @Field("choices")
                    val choices: List<Choice>
                ) : Answer

                data class Choice(
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
