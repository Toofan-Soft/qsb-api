package com.toofan.soft.qsb.api.repos.student_online_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*

object RetrieveOnlineExamQuestionsRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { examId ->
                request = Request(examId)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.StudentOnlineExam.RetrieveQuestionList,
                    request = it
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
        ) : IResponse {
            sealed interface Data {
                data class TrueFalse(
                    @Field("id")
                    val id: Int = 0,
                    @Field("content")
                    val content: String = "",
                    @Field("attachment_url")
                    val attachmentUrl: String? = null
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
