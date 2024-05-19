package com.toofan.soft.qsb.api.repos.course_student

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*

object RetrieveEditableCourseStudentRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Response.Data>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { departmentCourseId, studentId ->
                request = Request(departmentCourseId, studentId)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.CourseStudent.RetrieveEditable,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Response.Data>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            departmentCourseId: Int,
            studentId: Int
        )
    }

    data class Request(
        @Field("department_course_id")
        private val _departmentCourseId: Int,
        @Field("student_id")
        private val _studentId: Int
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
            @Field("academic_year")
            val academicYear: Int = 0
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
