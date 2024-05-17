package com.toofan.soft.qsb.api.repos.course_student

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*

object RetrieveCourseStudentsRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke(
                { departmentCourseId, academicYear ->
                    request = Request(departmentCourseId, academicYear)
                },
                { request!!.optional(it) }
            )

            request?.let {
                ApiExecutor.execute(
                    route = Route.CourseStudent.RetrieveList
                ) {
                    onComplete(Response.map(it).getResource() as Resource<List<Response.Data>>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            departmentCourseId: Int,
            academicYear: Int
        )
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }


    data class Request(
        @Field("department_course_id")
        private val _departmentCourseId: Int,
        @Field("academic_year")
        private val _academicYear: Int,
        @Field("status_id")
        private val _statusId: OptionalVariable<Int> = OptionalVariable()
    ) : IRequest {
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
            @Field("academic_id")
            val academicId: Int = 0,
            @Field("name")
            val name: String = "",
            @Field("status_name")
            val statusName: String? = null,
            @Field("image_url")
            val imageUrl: String? = null
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
