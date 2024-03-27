package com.toofan.soft.qsb.api.repos.course_lecturer

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import com.toofan.soft.qsb.api.repos.question.RetrieveQuestionsRepo
import kotlinx.coroutines.runBlocking

object RetrieveCourseLecturerRepo {
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
                    route = Route.CourseLecture.Retrieve
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
            private val _collegeName: String,
            @Field("department_name")
            private val _departmentName: String,
            @Field("level_name")
            private val _levelName: String,
            @Field("semester_name")
            private val _semesterName: String,
            @Field("course_name")
            private val _courseName: String,
            @Field("course_part_name")
            private val _coursePartName: String,
            @Field("academic_year")
            private val _academicYear: Int,
            @Field("lectures_count")
            private val _lecturesCount: Int? = null,
            @Field("lecture_duration")
            private val _lectureDuration: Int? = null,
            @Field("score")
            private val _score: Int? = null,

            @Field("lecturer_name")
            private val _lecturerName: String,
            @Field("qualification_name")
            private val _qualificationName: String,
            @Field("email")
            private val _email: Int? = null,
            @Field("phone")
            private val _phone: Int? = null,
            @Field("specialization")
            private val _specialization: Int? = null,
            @Field("image_url")
            private val _imageUrl: Int? = null,
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
