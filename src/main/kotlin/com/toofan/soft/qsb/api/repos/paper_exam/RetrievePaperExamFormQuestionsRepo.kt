package com.toofan.soft.qsb.api.repos.paper_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object RetrievePaperExamFormQuestionsRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        var request: Request? = null

        data.invoke { formId ->
            request = Request(formId)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.PaperExam.RetrieveFormQuestionList
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
        val data: List<Data>? = null
    ) : IResponse {

        data class Data(
            @Field("type_name")
            val typeName: String,
            @Field("questions")
            val questions: List<Data>
        ) {
            data class Data(
                @Field("chapter_name")
                val chapterName: String,
                @Field("topic_name")
                val topicName: String,
                @Field("question")
                val question: Data
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
