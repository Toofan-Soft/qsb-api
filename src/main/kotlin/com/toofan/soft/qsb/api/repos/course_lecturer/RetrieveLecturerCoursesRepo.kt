package com.toofan.soft.qsb.api.repos.course_lecturer

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import kotlinx.coroutines.runBlocking

object RetrieveLecturerCoursesRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        var request: Request? = null

        data.invoke { employeeId ->
            request = Request(employeeId)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.CourseLecture.RetrieveLecturerCourseList
                ) {
                    onComplete(Response.map(it).getResource() as Resource<List<Response.Data>>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            employeeId: Int
        )
    }

    data class Request(
        @Field("employee_id")
        private val _employeeId: Int
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
            @Field("course_lecturer_id")
            val courseLecturerId: Int = 0,
            @Field("course_name")
            private val _courseName: String = "",
            @Field("course_part_name")
            private val _coursePartName: String = "",
            @Field("academic_year")
            private val _academicYear: Int = 0,
            @Field("college_name")
            private val _collegeName: String = "",
            @Field("department_name")
            private val _departmentName: String = "",
            @Field("level_name")
            private val _levelName: String = "",
            @Field("semester_name")
            private val _semesterName: String = "",
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
