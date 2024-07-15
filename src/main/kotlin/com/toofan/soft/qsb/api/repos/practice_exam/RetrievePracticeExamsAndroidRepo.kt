package com.toofan.soft.qsb.api.repos.practice_exam

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.extensions.string
import java.time.LocalDateTime

object RetrievePracticeExamsAndroidRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = Request()

            data.invoke {
                request = Request()
                request!!.optional(it)
            }

            ApiExecutor.execute(
                route = Route.PracticeOnlineExam.RetrieveAndroidList,
                request = request
            ) {
                onComplete(Response.map(it).getResource() as Resource<List<Response.Data>>)
            }
        }
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }

    data class Request(
        @Field("department_course_part_id")
        private val _departmentCoursePartId: OptionalVariable<Int> = OptionalVariable(),
        @Field("status_id")
        private val _statusId: OptionalVariable<Int> = OptionalVariable()
    ) : IRequest {
        val departmentCoursePartId = loggableProperty(_departmentCoursePartId)
        val statusId = loggableProperty(_statusId)

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
        val data: List<Data> = emptyList()
    ) : IResponse {

        data class Data(
            @Field("id")
            val id: Int = 0,
            @Field("course_name")
            val courseName: String = "",
            @Field("course_part_name")
            val coursePartName: String = "",
            @Field("datetime")
            private val _datetime: LocalDateTime = LocalDateTime.now(),
            @Field("language_name")
            val languageName: String = "",
            @Field("status_name")
            val statusName: String = "",
            @Field("title")
            val title: String? = null,
            @Field("appreciation")
            val appreciation: String? = null,
            @Field("score_rate")
            val scoreRate: Float? = null
        ) : IResponse {
            val datetime get() = _datetime.string
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
