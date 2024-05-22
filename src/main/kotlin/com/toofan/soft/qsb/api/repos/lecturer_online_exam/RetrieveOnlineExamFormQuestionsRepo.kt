package com.toofan.soft.qsb.api.repos.lecturer_online_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.helper.QuestionHelper

object RetrieveOnlineExamFormQuestionsRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { formId ->
                request = Request(formId)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.LecturerOnlineExam.RetrieveFormQuestionList,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<List<Response.Data>>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            formId: Int
        )
    }

    data class Request(
        @Field("form_id")
        private val _formId: Int
    ) : IRequest

    data class Response(
        @Field("is_success")
        val isSuccess: Boolean = false,
        @Field("error_message")
        val errorMessage: String? = null,
        @Field("data")
        val data: List<Data> = emptyList()
    ) : IResponse {

        data class Data(
            @Field("type_name")
            val typeName: String = "",
            @Field("questions")
            val questions: List<Data> = emptyList()
        ) : IResponse {
            data class Data(
                @Field("chapter_name")
                val chapterName: String = "",
                @Field("topic_name")
                val topicName: String = "",
                @Field("id")
                val id: Int = 0,
                @Field("content")
                val content: String = "",
                @Field("attachment_url")
                val attachmentUrl: String? = null,
                @Field("is_true")
                private val isTrue: Boolean? = null,
                @Field("choices")
                private val choices: List<Data>? = null
            ) : IResponse {
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

//            data class Data(
//                @Field("chapter_name")
//                val chapterName: String = "",
//                @Field("topic_name")
//                val topicName: String = "",
//                @Field("question")
//                val question: Data
//            ) : IResponse {
//                sealed interface Data {
//                    data class TrueFalse(
//                        @Field("id")
//                        val id: Int = 0,
//                        @Field("content")
//                        val content: String = "",
//                        @Field("is_true")
//                        val isTrue: Boolean = false,
//                        @Field("attachment_url")
//                        val attachmentUrl: String? = null
//                    ) : Data
//
//                    data class MultiChoice(
//                        @Field("id")
//                        val id: Int = 0,
//                        @Field("content")
//                        val content: String = "",
//                        @Field("attachment_url")
//                        val attachmentUrl: String? = null,
//                        @Field("choices")
//                        val choices: List<Data> = emptyList()
//                    ) : Data {
//                        data class Data(
//                            @Field("id")
//                            val id: Int = 0,
//                            @Field("content")
//                            val content: String = "",
//                            @Field("is_true")
//                            val isTrue: Boolean = false,
//                            @Field("attachment_url")
//                            val attachmentUrl: String? = null
//                        )
//                    }
//                }
//            }
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
