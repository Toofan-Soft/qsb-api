package com.toofan.soft.qsb.api.repos.department_course

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import kotlinx.coroutines.runBlocking

object RetrieveDepartmentCourseRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke { id ->
            request = Request(id)
        }

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.DepartmentCourse.Retrieve
                ) {
                    val response = Response.map(it)
                    onComplete(response)
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
        @Field("id")
        private val _id: Int
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
            @Field("college_name")
            val collegeName: String,
            @Field("department_name")
            val departmentName: String,
            @Field("course_name")
            val courseName: String,
            @Field("level_name")
            val levelName: String,
            @Field("semester_Name")
            val semesterName: String,
            @Field("course_parts")
            val courseParts: List<CoursePart>
        ) {
            data class CoursePart(
                @Field("id")
                val Id: Int,
                @Field("name")
                val name: String,
                @Field("score")
                val score: Int? = null,
                @Field("lectures_count")
                val lecturesCount: Int? = null,
                @Field("lecture_duration")
                val lectureDuration: Int? = null,
                @Field("note")
                val note: String? = null
            )
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
