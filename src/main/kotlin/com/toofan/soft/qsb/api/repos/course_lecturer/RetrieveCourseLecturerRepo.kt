package com.toofan.soft.qsb.api.repos.course_lecturer

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*

object RetrieveCourseLecturerRepo {
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
                    route = Route.CourseLecturer.Retrieve,
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
            val collegeName: String = "",
            @Field("department_name")
            val departmentName: String = "",
            @Field("level_name")
            val levelName: String = "",
            @Field("semester_name")
            val semesterName: String = "",
            @Field("course_name")
            val courseName: String = "",
            @Field("course_part_name")
            val coursePartName: String = "",
            @Field("academic_year")
            val academicYear: Int = 0,
            @Field("lectures_count")
            val lecturesCount: Int? = null,
            @Field("lecture_duration")
            val lectureDuration: Int? = null,
            @Field("score")
            val score: Int? = null,

            @Field("lecturer_name")
            val lecturerName: String = "",
            @Field("qualification_name")
            val qualificationName: String = "",
            @Field("email")
            val email: String? = null,
            @Field("phone")
            val phone: Long? = null,
            @Field("specialization")
            val specialization: Int? = null,
            @Field("image_url")
            val imageUrl: Int? = null
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
