package com.toofan.soft.qsb.api.repos.course_student

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*

object RetrieveUnlinkedCourseStudentsRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { departmentCourseId ->
                request = Request(departmentCourseId)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.CourseStudent.RetrieveUnlinkList
                ) {
                    onComplete(Response.map(it).getResource() as Resource<List<Response.Data>>)
                }
            }
        }
    }

    fun interface Mandatory {
        operator fun invoke(
            departmentCourseId: Int
        )
    }

    fun interface Optional {
        operator fun invoke(block: Request.() -> Unit)
    }


    data class Request(
        @Field("department_course_id")
        private val _departmentCourseId: Int
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
            @Field("id")
            val id: Int = 0,
            @Field("academic_id")
            val academicId: Int = 0,
            @Field("name")
            val name: String = "",
            @Field("image_url")
            val imageUrl: String? = null
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
