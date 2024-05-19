package com.toofan.soft.qsb.api.repos.practice_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*

object RetrievePracticeExamResultRepo {
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
                    route = Route.PracticeOnlineExam.RetrieveResult,
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
        @Field("exam_id")
        private val _examId: Int
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
            @Field("time_spent")
            val timeSpent: Long = 0,
            @Field("question_average_answer_time")
            val questionAverageAnswerTime: Long = 0,
            @Field("correct_answer_count")
            val correctAnswerCount: Int = 0,
            @Field("incorrect_answer_count")
            val incorrectAnswerCount: Int = 0,
            @Field("appreciation")
            val appreciation: String = "",
            @Field("score_rate")
            val scoreRate: Int = 0
        ) : IResponse

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
