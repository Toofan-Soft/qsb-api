package com.toofan.soft.qsb.api.repos.course_lecturer

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*

object RetrieveCourseLecturersRepo {
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
                { coursePartId ->
                    request = Request(coursePartId)
                },
                { request!!.optional(it) }
            )

            request?.let {
                ApiExecutor.execute(
                    route = Route.CourseLecturer.RetrieveList,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<List<Response.Data>>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            coursePartId: Int
        )
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }


    data class Request(
        @Field("course_part_id")
        private val _coursePartId: Int,
        @Field("academic_year")
        private val _academicYear: OptionalVariable<Int> = OptionalVariable()
    ) : IRequest {
        val academicYear = loggableProperty(_academicYear)

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
            @Field("course_lecturer_id")
            val courseLecturerId: Int = 0,
            @Field("college_name")
            val collegeName: String = "",
            @Field("department_name")
            val departmentName: String = "",
            @Field("lecturer_name")
            val lecturerName: String = "",
            @Field("academic_year")
            val academicYear: Int? = null
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
