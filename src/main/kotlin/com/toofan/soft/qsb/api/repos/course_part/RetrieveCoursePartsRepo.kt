package com.toofan.soft.qsb.api.repos.course_part

import com.google.gson.JsonObject
import com.toofan.soft.qsb.api.*

object RetrieveCoursePartsRepo {
    @JvmStatic
    suspend fun execute(
        data: (
            mandatory: Mandatory
        ) -> Unit,
        onComplete: (Resource<List<Response.Data>>) -> Unit
    ) {
        Coroutine.launch {
            var request: Request? = null

            data.invoke { courseId ->
                request = Request(courseId)
            }

            request?.let {
                ApiExecutor.execute(
                    route = Route.CoursePart.RetrieveList,
                    request = it
                ) {
                    onComplete(Response.map(it).getResource() as Resource<List<Response.Data>>)
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
        val data: List<Data> = emptyList()
    ) : IResponse {

        data class Data(
            @Field("id")
            val id: Int = 0,
            @Field("name")
            val name: String = "",
            @Field("status_name")
            val statusName: String = "",
            @Field("description")
            val description: String? = null,

            @Field("is_deletable")
            val isDeletable: Boolean = false
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
