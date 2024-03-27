package com.toofan.soft.qsb.api.repos.course_lecturer

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import com.toofan.soft.qsb.api.repos.question.RetrieveQuestionsRepo
import kotlinx.coroutines.runBlocking

object RetrieveLecturerCoursesRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
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
                    val response = Response.map(it)
                    onComplete(response)
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
        val data: List<Data>? = null
    ) : IResponse {

        data class Data(
            @Field("course_lecturer_id")
            val courseLecturerId: Int,
            @Field("course_name")
            private val _courseName: String,
            @Field("course_part_name")
            private val _coursePartName: String,
            @Field("academic_year")
            private val _academicYear: Int,
            @Field("college_name")
            private val _collegeName: String,
            @Field("department_name")
            private val _departmentName: String,
            @Field("level_name")
            private val _levelName: String,
            @Field("semester_name")
            private val _semesterName: String,
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
