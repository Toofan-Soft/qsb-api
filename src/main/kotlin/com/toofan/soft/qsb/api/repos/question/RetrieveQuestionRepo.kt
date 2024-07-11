package com.toofan.soft.qsb.api.repos.question

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.helper.QuestionHelper

object RetrieveQuestionRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Response.Data>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { id ->
                request = Request(id)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.Question.Retrieve,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Response.Data>)
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
            @Field("difficulty_level_name")
            val difficultyLevelName: String = "",
            @Field("accessibility_status_name")
            val accessibilityStatusName: String = "",
            @Field("language_name")
            val languageName: String = "",
            @Field("estimated_answer_time")
            val estimatedAnswerTime: Int = 0,
            @Field("content")
            val content: String = "",
            @Field("attachment_url")
            val attachmentUrl: String? = null,
            @Field("title")
            val title: String? = null,
            @Field("status")
            val status: Status = Status(),
            @Field("is_true")
            private val isTrue: Boolean? = null,
            @Field("choices")
            private val choices: List<Data>? = null,

            @Field("is_deletable")
            val isDeletable: Boolean = false
        ) : IResponse {
            data class Status(
                @Field("is_requested")
                val isRequested: Boolean = false,
                @Field("is_accepted")
                val isAccepted: Boolean = false
            ) : IResponse

            data class Data(
                @Field("id")
                val id: Int = 0,
                @Field("content")
                val content: String = "",
                @Field("is_true")
                val isTrue: Boolean = false,
                @Field("attachment_url")
                val attachmentUrl: String? = null
            ) : IResponse

            fun getChoices(): List<QuestionHelper.Data.Data> {
                return if (isTrue != null && choices == null) {
                    listOf(
                        QuestionHelper.Data.Data.Type.CORRECT.toData().copy(isTrue = isTrue),
                        QuestionHelper.Data.Data.Type.INCORRECT.toData().copy(isTrue = !isTrue)
                    )
                } else if (isTrue == null && choices != null) {
                    choices.map {
                        QuestionHelper.Data.Data(
                            id = it.id,
                            content = it.content,
                            isTrue = it.isTrue,
                            attachmentUrl = it.attachmentUrl
                        )
                    }
                } else {
                    emptyList()
                }
            }
        }

//        data class Data(
//            @Field("difficulty_level_name")
//            val difficultyLevelName: String = "",
//            @Field("accessibility_status_Name")
//            val accessibilityStatusName: String = "",
//            @Field("language_Name")
//            val languageName: String = "",
//            @Field("estimated_answer_time")
//            val estimatedAnswerTime: Int = 0,
//            @Field("content")
//            val content: String = "",
//            @Field("attachment_url")
//            val attachmentUrl: String? = null,
//            @Field("title")
//            val title: String? = null,
//            @Field("status")
//            val status: Status = Status(),
//            @Field("answer")
//            val answer: Answer
//        ) : IResponse {
//            data class Status(
//                @Field("is_requested")
//                val isRequested: Boolean = false,
//                @Field("is_accepted")
//                val isAccepted: Boolean = false
//            ) : IResponse
//
//            sealed interface Answer {
//                data class TrueFalse(
//                    @Field("is_true")
//                    val isTrue: Boolean = false
//                ) : Answer
//
//                data class Choices(
//                    @Field("choices")
//                    val choices: List<Choice> = emptyList()
//                ) : Answer
//
//                data class Choice(
//                    @Field("id")
//                    val id: Int = 0,
//                    @Field("content")
//                    val content: String = "",
//                    @Field("is_true")
//                    val isTrue: Boolean = false,
//                    @Field("attachment_url")
//                    val attachmentUrl: String? = null
//                )
//            }
//        }

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
