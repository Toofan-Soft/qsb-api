package com.toofan.soft.qsb.api.repos.proctor_online_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import kotlinx.coroutines.runBlocking

object RefreshOnlineExamStudentsRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke { examId ->
            request = Request(examId)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.ProctorOnlineExam.RefreshStudentList
                ) {
                    val response = Response.map(it)
                    onComplete(response)
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
        val data: List<Data>? = null
    ) : IResponse {

        data class Data(
            @Field("id")
            val id: Int,
            @Field("status_name")
            val statusName: String,
            @Field("start_datetime")
            val startDatetime: Long? = null,
            @Field("end_datetime")
            val endDatetime: Long? = null,
            @Field("answered_questions_count")
            val answeredQuestionsCount: Int? = null,
            @Field("is_started")
            val isStarted: Boolean? = false,
            @Field("is_finished")
            val isFinished: Boolean? = null,
            @Field("is_suspended")
            val isSuspended: Boolean? = null
        )

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
