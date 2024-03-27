package com.toofan.soft.qsb.api.repos.course_lecturer

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*
import com.toofan.soft.qsb.api.Field
import com.toofan.soft.qsb.api.repos.question.RetrieveQuestionsRepo
import kotlinx.coroutines.runBlocking

object RetrieveCourseLecturersRepo {
    @JvmStatic
    fun execute(
        data: (
            mandatory: Mandatory,
            optional: Optional
        ) -> Unit,
        onComplete: (response: Response) -> Unit
    ) {
        var request: Request? = null

        data.invoke(
            { coursePartId ->
                request = Request(coursePartId)
            },
            { request!!.optional(it) }
        )

        request?.let {
            runBlocking {
                ApiExecutor.execute(
                    route = Route.CourseLecture.RetrieveList
                ) {
                    val response = Response.map(it)
                    onComplete(response)
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
        val data: List<Data>? = null
    ) : IResponse {

        data class Data(
            @Field("course_lecturer_id")
            val courseLecturerId: Int,
            @Field("college_name")
            private val _collegeName: String,
            @Field("department_name")
            private val _departmentName: String,
            @Field("lecturer_name")
            private val _lecturerName: String,
            @Field("academic_year")
            private val _academicYear: Int? = null
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
