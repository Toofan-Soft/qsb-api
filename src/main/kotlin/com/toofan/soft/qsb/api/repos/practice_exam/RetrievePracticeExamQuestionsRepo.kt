package com.toofan.soft.qsb.api.repos.practice_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object RetrievePracticeExamQuestionsRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        var request: Request? = null

        data.invoke { examId ->
            request = Request(examId)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.PracticeOnlineExam.RetrieveQuestionList
                ) {
                    onComplete(Response.map(it).getResource() as Resource<List<Response.Data>>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            examId: Int
        )
    }

    data class Request(
        @Field("exam_id")
        private val _examId: Int
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
        ) {
            sealed interface Data {
                data class TrueFalse(
                    @Field("id")
                    val id: Int = 0,
                    @Field("content")
                    val content: String = "",
                    @Field("attachment_url")
                    val attachmentUrl: String? = null,
                    @Field("user_answer")
                    val userAnswer: Boolean? = null,
                    @Field("is_true")
                    val isTrue: Boolean? = null
                ) : Data

                data class MultiChoice(
                    @Field("id")
                    val id: Int = 0,
                    @Field("content")
                    val content: String = "",
                    @Field("attachment_url")
                    val attachmentUrl: String? = null,
                    @Field("choices")
                    val choices: List<Data> = emptyList()
                ) : Data {
                    data class Data(
                        @Field("id")
                        val id: Int = 0,
                        @Field("content")
                        val content: String = "",
                        @Field("attachment_url")
                        val attachmentUrl: String? = null,
                        @Field("is_selected")
                        val isSelected: Boolean? = null,
                        @Field("is_true")
                        val isTrue: Boolean? = null
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
