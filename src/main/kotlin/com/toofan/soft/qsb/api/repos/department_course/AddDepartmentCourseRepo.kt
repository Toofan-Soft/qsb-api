package com.toofan.soft.qsb.api.repos.department_course

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*

object AddDepartmentCourseRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<Response.Data>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { departmentId, levelId, semesterId, courseId ->
                request = Request(departmentId, levelId, semesterId, courseId)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.DepartmentCourse.Add,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<Response.Data>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            departmentId: Int,
            levelId: Int,
            semesterId: Int,
            courseId: Int
        )
    }

    data class Request(
        @Field("department_id")
        private val _departmentId: Int,
        @Field("level_id")
        private val _levelId: Int,
        @Field("semester_id")
        private val _semesterId: Int,
        @Field("course_id")
        private val _courseId: Int
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
            @Field("id")
            val id: Int = 0
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
