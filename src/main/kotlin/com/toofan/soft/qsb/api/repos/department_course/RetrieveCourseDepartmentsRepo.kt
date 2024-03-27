package com.toofan.soft.qsb.api.repos.department_course

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import kotlinx.coroutines.runBlocking

object RetrieveCourseDepartmentsRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke { courseId ->
            request = Request(courseId)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.DepartmentCourse.RetrieveCourseDepartmentList
                ) {
                    val response = Response.map(it)
                    onComplete(response)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            courseId: Int
        )
    }

    data class Request(
        @Field("course_id")
        private val _courseId: Int
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
            @Field("college_name")
            val collegeName: String,
            @Field("department_name")
            val departmentName: String,
            @Field("department_course_id")
            val departmentCourseId: Int,
            @Field("level_name")
            val levelName: String,
            @Field("semester_name")
            val semesterName: String
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
